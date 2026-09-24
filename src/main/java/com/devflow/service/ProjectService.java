package com.devflow.service;

import com.devflow.model.Project;
import com.devflow.model.ProjectMember;
import java.util.List;

public interface ProjectService {
    Project getProjectById(int id);
    Project getProjectByKey(String key);
    List<Project> getAllProjects();
    List<Project> getUserProjects(int userId, String roleName);
    List<Project> searchProjects(String keyword, String status, String priority);
    boolean createProject(Project project, int userId, String username, String ipAddress);
    boolean updateProject(Project project, int userId, String username, String ipAddress);
    boolean deleteProject(int projectId, int userId, String username, String ipAddress);
    boolean updateStatus(int projectId, String status, int userId, String username, String ipAddress);

    // Members
    boolean addMember(int projectId, int targetUserId, String projectRole, int currentUserId);
    boolean removeMember(int projectId, int targetUserId, int currentUserId);
    boolean updateMemberRole(int projectId, int targetUserId, String projectRole);
    List<ProjectMember> getProjectMembers(int projectId);
    boolean isUserAuthorizedForProject(int projectId, int userId, String roleName);
}
