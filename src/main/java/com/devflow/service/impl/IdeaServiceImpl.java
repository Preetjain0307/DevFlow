package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.IdeaDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.ProjectDAO;
import com.devflow.dao.TaskDAO;
import com.devflow.dao.UserDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.IdeaDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.dao.impl.ProjectDAOImpl;
import com.devflow.dao.impl.TaskDAOImpl;
import com.devflow.dao.impl.UserDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Idea;
import com.devflow.model.IdeaComment;
import com.devflow.model.IdeaHistory;
import com.devflow.model.IdeaVote;
import com.devflow.model.Notification;
import com.devflow.model.Project;
import com.devflow.model.Task;
import com.devflow.model.User;
import com.devflow.service.IdeaService;
import com.devflow.util.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class IdeaServiceImpl implements IdeaService {
    private final IdeaDAO ideaDAO;
    private final ProjectDAO projectDAO;
    private final TaskDAO taskDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;
    private final UserDAO userDAO;

    public IdeaServiceImpl() {
        this.ideaDAO = new IdeaDAOImpl();
        this.projectDAO = new ProjectDAOImpl();
        this.taskDAO = new TaskDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    public IdeaServiceImpl(IdeaDAO ideaDAO, ProjectDAO projectDAO, TaskDAO taskDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO, UserDAO userDAO) {
        this.ideaDAO = ideaDAO;
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Idea getIdeaById(int id, Integer currentUserId) {
        Idea idea = ideaDAO.findById(id);
        if (idea != null && currentUserId != null) {
            IdeaVote vote = ideaDAO.findUserVote(id, currentUserId);
            if (vote != null) {
                idea.setUserVote(vote.getVote());
            }
        }
        return idea;
    }

    @Override
    public List<Idea> getIdeasByProjectId(int projectId) {
        return ideaDAO.findByProjectId(projectId);
    }

    @Override
    public List<Idea> getIdeasBySubmitter(int userId) {
        return ideaDAO.findBySubmitterId(userId);
    }

    @Override
    public List<Idea> getPendingFacultyReviewIdeas() {
        return ideaDAO.findPendingFacultyReview();
    }

    @Override
    public List<Idea> searchIdeas(Integer projectId, String status, String facultyStatus, String keyword) {
        return ideaDAO.search(projectId, status, facultyStatus, keyword);
    }

    @Override
    public boolean submitIdea(Idea idea, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(idea.getTitle())) {
            throw new ValidationException("Idea / Proposal title is required.");
        }
        if (!ValidationUtil.isNotEmpty(idea.getProblemStatement())) {
            throw new ValidationException("Problem statement is required.");
        }
        if (!ValidationUtil.isNotEmpty(idea.getProposedSolution())) {
            throw new ValidationException("Proposed solution is required.");
        }
        if (idea.getProjectId() <= 0) {
            throw new ValidationException("Project is required.");
        }

        idea.setSubmittedBy(userId);
        idea.setStatus(Constants.IDEA_STATUS_IN_VOTING);
        idea.setFacultyStatus("PENDING");

        boolean created = ideaDAO.create(idea);
        if (created) {
            // Automatically log author's YES vote
            IdeaVote authorVote = new IdeaVote();
            authorVote.setIdeaId(idea.getId());
            authorVote.setUserId(userId);
            authorVote.setVote("YES");
            authorVote.setComments("Author initial submission");
            ideaDAO.castVote(authorVote);

            // Add history
            IdeaHistory hist = new IdeaHistory();
            hist.setIdeaId(idea.getId());
            hist.setUserId(userId);
            hist.setAction("SUBMITTED");
            hist.setNotes("Proposal submitted for team voting");
            ideaDAO.addHistory(hist);

            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_SUBMIT_IDEA, "IDEA", idea.getId(), "Submitted proposal: " + idea.getTitle(), ipAddress));

            // Notify team members
            notificationDAO.notifyProjectMembers(idea.getProjectId(), userId, "New Change Proposal", username + " submitted a new proposal: '" + idea.getTitle() + "'. Please cast your vote.", "/ideas?action=view&id=" + idea.getId(), "IDEA");

            // Evaluate if single-member project immediately passes voting
            checkVotingThresholdAndForward(idea.getId(), idea.getProjectId(), username);
        }
        return created;
    }

    @Override
    public boolean updateIdea(Idea idea, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(idea.getTitle())) {
            throw new ValidationException("Proposal title cannot be empty.");
        }
        // If updating an idea that had CHANGES_REQUESTED, reset to IN_VOTING
        Idea existing = ideaDAO.findById(idea.getId());
        if (existing != null && (Constants.IDEA_STATUS_CHANGES_REQUESTED.equals(existing.getStatus()) || "CHANGES_REQUESTED".equals(existing.getFacultyStatus()))) {
            idea.setStatus(Constants.IDEA_STATUS_IN_VOTING);
        }

        boolean updated = ideaDAO.update(idea);
        if (updated) {
            IdeaHistory hist = new IdeaHistory();
            hist.setIdeaId(idea.getId());
            hist.setUserId(userId);
            hist.setAction("UPDATED");
            hist.setNotes("Proposal revised and resubmitted");
            ideaDAO.addHistory(hist);

            auditLogDAO.log(new AuditLog(userId, username, "UPDATE_IDEA", "IDEA", idea.getId(), "Updated proposal: " + idea.getTitle(), ipAddress));
        }
        return updated;
    }

    @Override
    public boolean castVote(int ideaId, int userId, String username, String vote, String comment, String ipAddress) {
        if (vote != null) {
            String v = vote.trim().toUpperCase();
            if ("UPVOTE".equals(v) || "UP".equals(v) || "LIKE".equals(v) || "YES".equals(v)) {
                vote = "YES";
            } else if ("DOWNVOTE".equals(v) || "DOWN".equals(v) || "DISLIKE".equals(v) || "NO".equals(v)) {
                vote = "NO";
            }
        }
        if (!"YES".equalsIgnoreCase(vote) && !"NO".equalsIgnoreCase(vote)) {
            throw new ValidationException("Vote must be either YES (Upvote) or NO (Downvote).");
        }

        Idea idea = ideaDAO.findById(ideaId);
        if (idea == null) {
            throw new ValidationException("Proposal not found.");
        }

        if (!Constants.IDEA_STATUS_IN_VOTING.equals(idea.getStatus())) {
            throw new ValidationException("Voting is closed for this proposal.");
        }

        IdeaVote iv = new IdeaVote();
        iv.setIdeaId(ideaId);
        iv.setUserId(userId);
        iv.setVote(vote.toUpperCase());
        iv.setComments(comment);

        boolean voted = ideaDAO.castVote(iv);
        if (voted) {
            IdeaHistory hist = new IdeaHistory();
            hist.setIdeaId(ideaId);
            hist.setUserId(userId);
            hist.setAction("VOTED_" + vote.toUpperCase());
            hist.setNotes("Voted " + vote.toUpperCase() + (comment != null && !comment.isEmpty() ? ": " + comment : ""));
            ideaDAO.addHistory(hist);

            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_VOTE_IDEA, "IDEA", ideaId, "Voted " + vote.toUpperCase() + " on proposal '" + idea.getTitle() + "'", ipAddress));

            // Check if majority reached to forward to faculty
            checkVotingThresholdAndForward(ideaId, idea.getProjectId(), username);
        }
        return voted;
    }

    private void checkVotingThresholdAndForward(int ideaId, int projectId, String triggeringUser) {
        Idea idea = ideaDAO.findById(ideaId);
        if (idea == null || !Constants.IDEA_STATUS_IN_VOTING.equals(idea.getStatus())) return;

        int yesVotes = ideaDAO.countYesVotes(ideaId);
        int eligibleVoters = idea.getTotalEligibleVoters();
        if (eligibleVoters <= 0) eligibleVoters = 1;

        // Majority condition: YES votes strictly > eligibleVoters / 2
        double threshold = (double) eligibleVoters / 2.0;
        if (yesVotes > threshold) {
            ideaDAO.updateStatus(ideaId, Constants.IDEA_STATUS_PENDING_FACULTY);

            IdeaHistory hist = new IdeaHistory();
            hist.setIdeaId(ideaId);
            hist.setUserId(idea.getSubmittedBy());
            hist.setAction("FORWARDED_FACULTY");
            hist.setNotes("Team voting passed with " + yesVotes + "/" + eligibleVoters + " YES votes. Forwarded to Faculty for review.");
            ideaDAO.addHistory(hist);

            // Notify all faculty members
            List<User> facultyUsers = userDAO.findByRoleId(Constants.ROLE_ID_FACULTY);
            for (User fac : facultyUsers) {
                Notification notif = new Notification();
                notif.setUserId(fac.getId());
                notif.setProjectId(projectId);
                notif.setTitle("Proposal Ready for Faculty Review");
                notif.setMessage("Proposal '" + idea.getTitle() + "' has passed team voting and awaits your review.");
                notif.setLinkUrl("/ideas?action=review&id=" + ideaId);
                notif.setNotificationType("FACULTY");
                notificationDAO.create(notif);
            }
        }
    }

    @Override
    public boolean processFacultyDecision(int ideaId, String decision, String rejectionReason, int facultyUserId, String facultyUsername, String ipAddress) {
        Idea idea = ideaDAO.findById(ideaId);
        if (idea == null) {
            throw new ValidationException("Proposal not found.");
        }

        if ("REJECTED".equalsIgnoreCase(decision) && !ValidationUtil.isNotEmpty(rejectionReason)) {
            throw new ValidationException("A formal rejection reason is mandatory when rejecting a student proposal.");
        }

        boolean processed = ideaDAO.updateFacultyDecision(ideaId, decision.toUpperCase(), rejectionReason, facultyUserId);
        if (processed) {
            String actionName = "FACULTY_" + decision.toUpperCase();
            IdeaHistory hist = new IdeaHistory();
            hist.setIdeaId(ideaId);
            hist.setUserId(facultyUserId);
            hist.setAction(actionName);
            hist.setNotes("Faculty " + decision.toUpperCase() + (rejectionReason != null ? ": " + rejectionReason : ""));
            ideaDAO.addHistory(hist);

            auditLogDAO.log(new AuditLog(facultyUserId, facultyUsername, Constants.AUDIT_FACULTY_DECISION, "IDEA", ideaId, "Faculty decided: " + decision + " on proposal '" + idea.getTitle() + "'", ipAddress));

            // Notify Submitter & Project Members
            String notifMsg = "Faculty has " + decision.toLowerCase() + " proposal: '" + idea.getTitle() + "'";
            if ("REJECTED".equalsIgnoreCase(decision)) {
                notifMsg += " (Reason: " + rejectionReason + ")";
            }
            notificationDAO.notifyProjectMembers(idea.getProjectId(), facultyUserId, "Faculty Decision: " + decision, notifMsg, "/ideas?action=view&id=" + ideaId, "IDEA");

            // IF APPROVED: Automatically create development task on the project board!
            if ("APPROVED".equalsIgnoreCase(decision)) {
                Task autoTask = new Task();
                autoTask.setProjectId(idea.getProjectId());
                autoTask.setTitle("[Proposal] " + idea.getTitle());
                autoTask.setDescription("Approved Proposal Implementation:\n" + idea.getProposedSolution() + "\n\nProblem Solved:\n" + idea.getProblemStatement());
                autoTask.setTaskType("FEATURE");
                autoTask.setPriority(idea.getPriority());
                autoTask.setStatus("TODO");
                autoTask.setCreatedBy(facultyUserId);
                autoTask.setAssignedTo(idea.getSubmittedBy());
                autoTask.setEstimatedHours(BigDecimal.valueOf(idea.getEstimatedEffortDays() * 8.0)); // 8 hours per day

                taskDAO.create(autoTask);

                // Add to history
                IdeaHistory taskHist = new IdeaHistory();
                taskHist.setIdeaId(ideaId);
                taskHist.setUserId(facultyUserId);
                taskHist.setAction("TASK_CREATED");
                taskHist.setNotes("Development task #" + autoTask.getId() + " automatically generated on Kanban board.");
                ideaDAO.addHistory(taskHist);
            }
        }
        return processed;
    }

    @Override
    public boolean addComment(int ideaId, int userId, String comment) {
        if (!ValidationUtil.isNotEmpty(comment)) {
            throw new ValidationException("Comment cannot be empty.");
        }
        IdeaComment ic = new IdeaComment();
        ic.setIdeaId(ideaId);
        ic.setUserId(userId);
        ic.setComment(comment.trim());
        return ideaDAO.addComment(ic);
    }

    @Override
    public List<IdeaComment> getComments(int ideaId) {
        return ideaDAO.findCommentsByIdeaId(ideaId);
    }

    @Override
    public List<IdeaHistory> getHistory(int ideaId) {
        return ideaDAO.findHistoryByIdeaId(ideaId);
    }

    @Override
    public boolean deleteIdea(int ideaId, int userId, String username, String ipAddress) {
        Idea idea = ideaDAO.findById(ideaId);
        if (idea == null) return false;
        boolean deleted = ideaDAO.delete(ideaId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, "DELETE_IDEA", "IDEA", ideaId, "Deleted proposal: " + idea.getTitle(), ipAddress));
        }
        return deleted;
    }
}
