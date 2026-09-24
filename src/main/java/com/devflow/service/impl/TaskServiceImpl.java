package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.ProjectDAO;
import com.devflow.dao.TaskDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.dao.impl.ProjectDAOImpl;
import com.devflow.dao.impl.TaskDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Notification;
import com.devflow.model.Task;
import com.devflow.model.TaskComment;
import com.devflow.model.TaskHistory;
import com.devflow.service.TaskService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class TaskServiceImpl implements TaskService {
    private final TaskDAO taskDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;
    private final ProjectDAO projectDAO;

    public TaskServiceImpl() {
        this.taskDAO = new TaskDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
        this.projectDAO = new ProjectDAOImpl();
    }

    public TaskServiceImpl(TaskDAO taskDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO, ProjectDAO projectDAO) {
        this.taskDAO = taskDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public Task getTaskById(int id) {
        return taskDAO.findById(id);
    }

    @Override
    public List<Task> getTasksByProjectId(int projectId) {
        return taskDAO.findByProjectId(projectId);
    }

    @Override
    public List<Task> getTasksBySprintId(int sprintId) {
        return taskDAO.findBySprintId(sprintId);
    }

    @Override
    public List<Task> getTasksByAssignee(int userId) {
        return taskDAO.findByAssigneeId(userId);
    }

    @Override
    public List<Task> searchTasks(Integer projectId, Integer sprintId, Integer assignedTo, String status, String priority, String keyword) {
        return taskDAO.search(projectId, sprintId, assignedTo, status, priority, keyword);
    }

    @Override
    public boolean createTask(Task task, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(task.getTitle())) {
            throw new ValidationException("Task title is required.");
        }
        if (task.getProjectId() <= 0) {
            throw new ValidationException("Project is required.");
        }
        task.setCreatedBy(userId);
        if (task.getStatus() == null) task.setStatus("TODO");

        boolean created = taskDAO.create(task);
        if (created) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_CREATE_TASK, "TASK", task.getId(), "Created task: " + task.getTitle(), ipAddress));
            
            // Notify assignee if assigned
            if (task.getAssignedTo() != null && task.getAssignedTo() != userId) {
                Notification notif = new Notification();
                notif.setUserId(task.getAssignedTo());
                notif.setProjectId(task.getProjectId());
                notif.setTitle("Task Assigned");
                notif.setMessage("You have been assigned to task: " + task.getTitle());
                notif.setLinkUrl("/tasks?action=view&id=" + task.getId());
                notif.setNotificationType("TASK");
                notificationDAO.create(notif);
            }
        }
        return created;
    }

    @Override
    public boolean updateTask(Task task, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(task.getTitle())) {
            throw new ValidationException("Task title cannot be empty.");
        }
        Task old = taskDAO.findById(task.getId());
        boolean updated = taskDAO.update(task);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_UPDATE_TASK, "TASK", task.getId(), "Updated task: " + task.getTitle(), ipAddress));
            
            // If assignee changed, notify new assignee
            if (task.getAssignedTo() != null && (old == null || old.getAssignedTo() == null || !old.getAssignedTo().equals(task.getAssignedTo()))) {
                if (task.getAssignedTo() != userId) {
                    Notification notif = new Notification();
                    notif.setUserId(task.getAssignedTo());
                    notif.setProjectId(task.getProjectId());
                    notif.setTitle("Task Assigned");
                    notif.setMessage("You have been assigned to task: " + task.getTitle());
                    notif.setLinkUrl("/tasks?action=view&id=" + task.getId());
                    notif.setNotificationType("TASK");
                    notificationDAO.create(notif);
                }
            }
        }
        return updated;
    }

    @Override
    public boolean updateTaskStatus(int taskId, String status, int userId, String username, String ipAddress) {
        Task t = taskDAO.findById(taskId);
        if (t == null) return false;
        boolean updated = taskDAO.updateStatus(taskId, status, userId);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, "TASK_STATUS_CHANGE", "TASK", taskId, "Moved task '" + t.getTitle() + "' to " + status, ipAddress));
            
            // Notify task creator or manager if completed
            if ("COMPLETED".equalsIgnoreCase(status) && t.getCreatedBy() != userId) {
                Notification notif = new Notification();
                notif.setUserId(t.getCreatedBy());
                notif.setProjectId(t.getProjectId());
                notif.setTitle("Task Completed");
                notif.setMessage("Task '" + t.getTitle() + "' was marked as completed by " + username);
                notif.setLinkUrl("/tasks?action=view&id=" + taskId);
                notif.setNotificationType("TASK");
                notificationDAO.create(notif);
            }
        }
        return updated;
    }

    @Override
    public boolean deleteTask(int taskId, int userId, String username, String ipAddress) {
        Task t = taskDAO.findById(taskId);
        if (t == null) return false;
        boolean deleted = taskDAO.delete(taskId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, "DELETE_TASK", "TASK", taskId, "Deleted task: " + t.getTitle(), ipAddress));
        }
        return deleted;
    }

    @Override
    public boolean addComment(int taskId, int userId, String comment) {
        if (!ValidationUtil.isNotEmpty(comment)) {
            throw new ValidationException("Comment cannot be empty.");
        }
        TaskComment tc = new TaskComment();
        tc.setTaskId(taskId);
        tc.setUserId(userId);
        tc.setComment(comment.trim());
        return taskDAO.addComment(tc);
    }

    @Override
    public List<TaskComment> getComments(int taskId) {
        return taskDAO.findCommentsByTaskId(taskId);
    }

    @Override
    public List<TaskHistory> getHistory(int taskId) {
        return taskDAO.findHistoryByTaskId(taskId);
    }
}
