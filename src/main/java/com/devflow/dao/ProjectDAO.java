package com.devflow.dao;

import com.devflow.model.Project;
import com.devflow.model.ProjectMember;
import java.util.List;

public interface ProjectDAO {
    Project findById(int id);
    Project findByKey(String key);
    List<Project> findAll();
    List<Project> findByUserId(int userId);
    List<Project> findByManagerId(int managerId);
    List<Project> search(String keyword, String status, String priority);
    boolean create(Project project);
    boolean update(Project project);
    boolean delete(int id);
    boolean updateStatus(int id, String status);

    // Member operations
    boolean addMember(int projectId, int userId, String projectRole);
    boolean removeMember(int projectId, int userId);
    boolean updateMemberRole(int projectId, int userId, String projectRole);
    List<ProjectMember> findMembersByProjectId(int projectId);
    ProjectMember findMember(int projectId, int userId);
    boolean isUserInProject(int projectId, int userId);

    int countTotalProjects();
    int countActiveProjects();
}
