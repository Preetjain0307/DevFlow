package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private int roleId;
    private String roleName;
    private String phone;
    private String designation;
    private String bio;
    private String avatarUrl;
    private String status; // ACTIVE, INACTIVE, SUSPENDED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return id; }
    public void setUserId(int uid) { this.id = uid; }

    public String getDepartment() { return designation != null ? designation : ""; }
    public void setDepartment(String dep) { this.designation = dep; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.roleName);
    }

    public boolean isProjectManager() {
        return "PROJECT_MANAGER".equalsIgnoreCase(this.roleName);
    }

    public boolean isDeveloper() {
        return "DEVELOPER".equalsIgnoreCase(this.roleName);
    }

    public boolean isTester() {
        return "TESTER".equalsIgnoreCase(this.roleName);
    }

    public boolean isFaculty() {
        return "FACULTY".equalsIgnoreCase(this.roleName);
    }

    public Role getRole() {
        Role r = new Role();
        r.setId(roleId);
        r.setName(roleName);
        return r;
    }
}
