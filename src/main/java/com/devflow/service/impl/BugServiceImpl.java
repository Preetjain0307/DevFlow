package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.BugDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.BugDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Bug;
import com.devflow.model.BugComment;
import com.devflow.model.Notification;
import com.devflow.service.BugService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class BugServiceImpl implements BugService {
    private final BugDAO bugDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;

    public BugServiceImpl() {
        this.bugDAO = new BugDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public BugServiceImpl(BugDAO bugDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO) {
        this.bugDAO = bugDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public Bug getBugById(int id) {
        return bugDAO.findById(id);
    }

    @Override
    public List<Bug> getBugsByProjectId(int projectId) {
        return bugDAO.findByProjectId(projectId);
    }

    @Override
    public List<Bug> getBugsByAssignee(int userId) {
        return bugDAO.findByAssigneeId(userId);
    }

    @Override
    public List<Bug> getBugsByReporter(int userId) {
        return bugDAO.findByReporterId(userId);
    }

    @Override
    public List<Bug> searchBugs(Integer projectId, Integer assignedTo, String severity, String status, String keyword) {
        return bugDAO.search(projectId, assignedTo, severity, status, keyword);
    }

    @Override
    public boolean reportBug(Bug bug, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(bug.getTitle())) {
            throw new ValidationException("Bug title is required.");
        }
        if (!ValidationUtil.isNotEmpty(bug.getDescription())) {
            throw new ValidationException("Bug description is required.");
        }
        if (bug.getProjectId() <= 0) {
            throw new ValidationException("Project is required.");
        }
        bug.setReportedBy(userId);
        if (bug.getStatus() == null) bug.setStatus("OPEN");
        if (bug.getSeverity() == null) bug.setSeverity("MEDIUM");

        boolean created = bugDAO.create(bug);
        if (created) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_REPORT_BUG, "BUG", bug.getId(), "Reported bug: " + bug.getTitle() + " (" + bug.getSeverity() + ")", ipAddress));
            
            // Notify assignee if assigned directly
            if (bug.getAssignedTo() != null && bug.getAssignedTo() != userId) {
                Notification notif = new Notification();
                notif.setUserId(bug.getAssignedTo());
                notif.setProjectId(bug.getProjectId());
                notif.setTitle("Bug Assigned to You");
                notif.setMessage("You have been assigned to fix bug: " + bug.getTitle());
                notif.setLinkUrl("/bugs?action=view&id=" + bug.getId());
                notif.setNotificationType("BUG");
                notificationDAO.create(notif);
            }
        }
        return created;
    }

    @Override
    public boolean updateBug(Bug bug, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(bug.getTitle())) {
            throw new ValidationException("Bug title cannot be empty.");
        }
        Bug old = bugDAO.findById(bug.getId());
        boolean updated = bugDAO.update(bug);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_UPDATE_BUG, "BUG", bug.getId(), "Updated bug: " + bug.getTitle(), ipAddress));
            
            if (bug.getAssignedTo() != null && (old == null || old.getAssignedTo() == null || !old.getAssignedTo().equals(bug.getAssignedTo()))) {
                if (bug.getAssignedTo() != userId) {
                    Notification notif = new Notification();
                    notif.setUserId(bug.getAssignedTo());
                    notif.setProjectId(bug.getProjectId());
                    notif.setTitle("Bug Assigned to You");
                    notif.setMessage("You have been assigned to fix bug: " + bug.getTitle());
                    notif.setLinkUrl("/bugs?action=view&id=" + bug.getId());
                    notif.setNotificationType("BUG");
                    notificationDAO.create(notif);
                }
            }
        }
        return updated;
    }

    @Override
    public boolean updateBugStatus(int bugId, String status, String resolutionNotes, int userId, String username, String ipAddress) {
        Bug b = bugDAO.findById(bugId);
        if (b == null) return false;
        boolean updated = bugDAO.updateStatus(bugId, status, resolutionNotes);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, "BUG_STATUS_CHANGE", "BUG", bugId, "Changed bug '" + b.getTitle() + "' status to " + status, ipAddress));
            
            // Notify reporter if status changed to RESOLVED or CLOSED
            if (b.getReportedBy() != userId) {
                Notification notif = new Notification();
                notif.setUserId(b.getReportedBy());
                notif.setProjectId(b.getProjectId());
                notif.setTitle("Bug Status Updated");
                notif.setMessage("Bug '" + b.getTitle() + "' is now " + status + " by " + username);
                notif.setLinkUrl("/bugs?action=view&id=" + bugId);
                notif.setNotificationType("BUG");
                notificationDAO.create(notif);
            }
        }
        return updated;
    }

    @Override
    public boolean deleteBug(int bugId, int userId, String username, String ipAddress) {
        Bug b = bugDAO.findById(bugId);
        if (b == null) return false;
        boolean deleted = bugDAO.delete(bugId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, "DELETE_BUG", "BUG", bugId, "Deleted bug: " + b.getTitle(), ipAddress));
        }
        return deleted;
    }

    @Override
    public boolean addComment(int bugId, int userId, String comment) {
        if (!ValidationUtil.isNotEmpty(comment)) {
            throw new ValidationException("Comment cannot be empty.");
        }
        BugComment bc = new BugComment();
        bc.setBugId(bugId);
        bc.setUserId(userId);
        bc.setComment(comment.trim());
        return bugDAO.addComment(bc);
    }

    @Override
    public List<BugComment> getComments(int bugId) {
        return bugDAO.findCommentsByBugId(bugId);
    }
}
