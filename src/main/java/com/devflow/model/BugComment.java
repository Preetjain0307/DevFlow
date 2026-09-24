package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class BugComment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int bugId;
    private int userId;
    private String username;
    private String userFullName;
    private String comment;
    private Timestamp createdAt;

    public BugComment() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBugId() { return bugId; }
    public void setBugId(int bugId) { this.bugId = bugId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
