package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Idea implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private String title;
    private String description;
    private String problemStatement;
    private String proposedSolution;
    private String expectedBenefit;
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private int estimatedEffortDays;
    private String status; // SUBMITTED, IN_VOTING, VOTING_PASSED, VOTING_FAILED, PENDING_FACULTY, APPROVED, REJECTED, CHANGES_REQUESTED
    private String facultyStatus; // PENDING, APPROVED, REJECTED, CHANGES_REQUESTED
    private int submittedBy;
    private String submitterName;
    private Integer facultyReviewedBy;
    private String facultyReviewerName;
    private String facultyRejectionReason;
    private Timestamp facultyReviewDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Voting aggregates & collections
    private int yesVotes;
    private int noVotes;
    private int totalEligibleVoters;
    private String userVote; // The current logged-in user's vote if any (YES, NO, or null)
    private List<IdeaVote> votes;
    private List<IdeaComment> comments;
    private List<IdeaHistory> history;

    public Idea() {
        this.estimatedEffortDays = 1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdeaId() { return id; }
    public void setIdeaId(int ideaId) { this.id = ideaId; }

    public String getCategory() { return priority != null ? priority : "FEATURE"; }
    public void setCategory(String c) { this.priority = c; }

    public String getImpactLevel() { return priority != null ? priority : "MEDIUM"; }
    public void setImpactLevel(String lvl) { this.priority = lvl; }

    public String getAuthorName() { return submitterName != null ? submitterName : "Author"; }
    public void setAuthorName(String name) { this.submitterName = name; }

    public int getUpvotes() { return yesVotes; }
    public void setUpvotes(int u) { this.yesVotes = u; }

    public int getDownvotes() { return noVotes; }
    public void setDownvotes(int d) { this.noVotes = d; }

    public String getFacultyNotes() { return facultyRejectionReason != null ? facultyRejectionReason : ""; }
    public void setFacultyNotes(String fn) { this.facultyRejectionReason = fn; }

    public Integer getConvertedTaskId() { return null; }
    public void setConvertedTaskId(Integer tid) {}

    public String getJustification() { return expectedBenefit != null ? expectedBenefit : ""; }
    public void setJustification(String j) { this.expectedBenefit = j; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProblemStatement() { return problemStatement; }
    public void setProblemStatement(String problemStatement) { this.problemStatement = problemStatement; }

    public String getProposedSolution() { return proposedSolution; }
    public void setProposedSolution(String proposedSolution) { this.proposedSolution = proposedSolution; }

    public String getExpectedBenefit() { return expectedBenefit; }
    public void setExpectedBenefit(String expectedBenefit) { this.expectedBenefit = expectedBenefit; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getEstimatedEffortDays() { return estimatedEffortDays; }
    public void setEstimatedEffortDays(int estimatedEffortDays) { this.estimatedEffortDays = estimatedEffortDays; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFacultyStatus() { return facultyStatus; }
    public void setFacultyStatus(String facultyStatus) { this.facultyStatus = facultyStatus; }

    public int getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(int submittedBy) { this.submittedBy = submittedBy; }

    public String getSubmitterName() { return submitterName; }
    public void setSubmitterName(String submitterName) { this.submitterName = submitterName; }

    public Integer getFacultyReviewedBy() { return facultyReviewedBy; }
    public void setFacultyReviewedBy(Integer facultyReviewedBy) { this.facultyReviewedBy = facultyReviewedBy; }

    public String getFacultyReviewerName() { return facultyReviewerName; }
    public void setFacultyReviewerName(String facultyReviewerName) { this.facultyReviewerName = facultyReviewerName; }

    public String getFacultyRejectionReason() { return facultyRejectionReason; }
    public void setFacultyRejectionReason(String facultyRejectionReason) { this.facultyRejectionReason = facultyRejectionReason; }

    public Timestamp getFacultyReviewDate() { return facultyReviewDate; }
    public void setFacultyReviewDate(Timestamp facultyReviewDate) { this.facultyReviewDate = facultyReviewDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getYesVotes() { return yesVotes; }
    public void setYesVotes(int yesVotes) { this.yesVotes = yesVotes; }

    public int getNoVotes() { return noVotes; }
    public void setNoVotes(int noVotes) { this.noVotes = noVotes; }

    public int getTotalEligibleVoters() { return totalEligibleVoters; }
    public void setTotalEligibleVoters(int totalEligibleVoters) { this.totalEligibleVoters = totalEligibleVoters; }

    public String getUserVote() { return userVote; }
    public void setUserVote(String userVote) { this.userVote = userVote; }

    public List<IdeaVote> getVotes() { return votes; }
    public void setVotes(List<IdeaVote> votes) { this.votes = votes; }

    public List<IdeaComment> getComments() { return comments; }
    public void setComments(List<IdeaComment> comments) { this.comments = comments; }

    public List<IdeaHistory> getHistory() { return history; }
    public void setHistory(List<IdeaHistory> history) { this.history = history; }

    public int getTotalVotes() {
        return yesVotes + noVotes;
    }

    public int getApprovalPercentage() {
        if (getTotalVotes() == 0) return 0;
        return (int) Math.round(((double) yesVotes / getTotalVotes()) * 100);
    }
}
