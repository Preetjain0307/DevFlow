package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class IdeaHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int ideaId;
    private int userId;
    private String username;
    private String userFullName;
    private String action; // SUBMITTED, VOTED_YES, VOTED_NO, FORWARDED_FACULTY, APPROVED, REJECTED, CHANGES_REQUESTED
    private String notes;
    private Timestamp createdAt;

    public IdeaHistory() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdeaId() { return ideaId; }
    public void setIdeaId(int ideaId) { this.ideaId = ideaId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
