package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.ProjectDAO;
import com.devflow.dao.UserDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.dao.impl.ProjectDAOImpl;
import com.devflow.dao.impl.UserDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Notification;
import com.devflow.model.Project;
import com.devflow.model.ProjectMember;
import com.devflow.model.User;
import com.devflow.service.ProjectService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;

    public ProjectServiceImpl() {
        this.projectDAO = new ProjectDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public ProjectServiceImpl(ProjectDAO projectDAO, UserDAO userDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO) {
        this.projectDAO = projectDAO;
        this.userDAO = userDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public Project getProjectById(int id) {
        return projectDAO.findById(id);
    }

    @Override
    public Project getProjectByKey(String key) {
        return projectDAO.findByKey(key);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectDAO.findAll();
    }

    @Override
    public List<Project> getUserProjects(int userId, String roleName) {
        if (Constants.ROLE_ADMIN.equalsIgnoreCase(roleName) || Constants.ROLE_FACULTY.equalsIgnoreCase(roleName)) {
            return projectDAO.findAll(); // Admins and Faculty can oversee all projects
        }
        return projectDAO.findByUserId(userId);
    }

    @Override
    public List<Project> searchProjects(String keyword, String status, String priority) {
        return projectDAO.search(keyword, status, priority);
    }

    @Override
    public boolean createProject(Project project, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(project.getName())) {
            throw new ValidationException("Project name is required.");
        }
        if (!ValidationUtil.isNotEmpty(project.getProjectKey()) || project.getProjectKey().trim().length() < 2) {
            throw new ValidationException("Project key (e.g. DEV, PROJ) of at least 2 characters is required.");
        }
        project.setProjectKey(project.getProjectKey().trim().toUpperCase());
        if (projectDAO.findByKey(project.getProjectKey()) != null) {
            throw new ValidationException("Project key '" + project.getProjectKey() + "' is already in use.");
        }
        if (project.getManagerId() <= 0) {
            project.setManagerId(userId);
        }

        boolean created = projectDAO.create(project);
        if (created) {
            // Automatically add the creator/manager as LEAD member
            projectDAO.addMember(project.getId(), project.getManagerId(), "LEAD");
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_CREATE_PROJECT, "PROJECT", project.getId(), "Created project " + project.getName() + " (" + project.getProjectKey() + ")", ipAddress));
        }
        return created;
    }

    @Override
    public boolean updateProject(Project project, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(project.getName())) {
            throw new ValidationException("Project name cannot be empty.");
        }
        boolean updated = projectDAO.update(project);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_UPDATE_PROJECT, "PROJECT", project.getId(), "Updated project metadata", ipAddress));
        }
        return updated;
    }

    @Override
    public boolean deleteProject(int projectId, int userId, String username, String ipAddress) {
        Project p = projectDAO.findById(projectId);
        if (p == null) return false;
        boolean deleted = projectDAO.delete(projectId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, "DELETE_PROJECT", "PROJECT", projectId, "Deleted project " + p.getName(), ipAddress));
        }
        return deleted;
    }

    @Override
    public boolean updateStatus(int projectId, String status, int userId, String username, String ipAddress) {
        boolean updated = projectDAO.updateStatus(projectId, status);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, "PROJECT_STATUS_CHANGE", "PROJECT", projectId, "Updated project status to " + status, ipAddress));
        }
        return updated;
    }

    @Override
    public boolean addMember(int projectId, int targetUserId, String projectRole, int currentUserId) {
        User targetUser = userDAO.findById(targetUserId);
        if (targetUser == null) {
            throw new ValidationException("Selected user does not exist.");
        }
        boolean added = projectDAO.addMember(projectId, targetUserId, projectRole);
        if (added) {
            Project p = projectDAO.findById(projectId);
            String pName = p != null ? p.getName() : "a project";
            Notification notif = new Notification();
            notif.setUserId(targetUserId);
            notif.setProjectId(projectId);
            notif.setTitle("Added to Project");
            notif.setMessage("You have been added to project '" + pName + "' as " + projectRole);
            notif.setLinkUrl("/projects?action=view&id=" + projectId);
            notif.setNotificationType("PROJECT");
            notificationDAO.create(notif);
        }
        return added;
    }

    @Override
    public boolean removeMember(int projectId, int targetUserId, int currentUserId) {
        return projectDAO.removeMember(projectId, targetUserId);
    }

    @Override
    public boolean updateMemberRole(int projectId, int targetUserId, String projectRole) {
        return projectDAO.updateMemberRole(projectId, targetUserId, projectRole);
    }

    @Override
    public List<ProjectMember> getProjectMembers(int projectId) {
        return projectDAO.findMembersByProjectId(projectId);
    }

    @Override
    public boolean isUserAuthorizedForProject(int projectId, int userId, String roleName) {
        if (Constants.ROLE_ADMIN.equalsIgnoreCase(roleName) || Constants.ROLE_FACULTY.equalsIgnoreCase(roleName)) {
            return true;
        }
        return projectDAO.isUserInProject(projectId, userId);
    }
}
