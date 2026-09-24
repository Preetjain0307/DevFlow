package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private Integer projectId;
    private String projectKey;
    private String title;
    private String message;
    private String linkUrl;
    private String notificationType; // TASK, BUG, IDEA, FACULTY, MEETING, GENERAL
    private boolean read;
    private Timestamp createdAt;

    public Notification() {
        this.notificationType = "GENERAL";
        this.read = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNotificationId() { return id; }
    public void setNotificationId(int nid) { this.id = nid; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
