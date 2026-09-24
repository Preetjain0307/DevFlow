package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProjectMember implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectName;
    private int userId;
    private String username;
    private String fullName;
    private String email;
    private String userRoleName;
    private String projectRole; // LEAD, DEVELOPER, TESTER, REVIEWER, OBSERVER
    private Timestamp joinedAt;

    public ProjectMember() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUserFullName() { return fullName; }
    public void setUserFullName(String fn) { this.fullName = fn; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserEmail() { return email; }
    public void setUserEmail(String ue) { this.email = ue; }

    public String getUserRoleName() { return userRoleName; }
    public void setUserRoleName(String userRoleName) { this.userRoleName = userRoleName; }

    public String getProjectRole() { return projectRole; }
    public void setProjectRole(String projectRole) { this.projectRole = projectRole; }

    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }
}
