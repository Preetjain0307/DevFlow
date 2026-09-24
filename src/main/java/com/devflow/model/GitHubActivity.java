package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class GitHubActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String eventType; // PUSH, PULL_REQUEST, ISSUE, RELEASE
    private String authorName;
    private String authorAvatar;
    private String message;
    private String eventUrl;
    private Timestamp eventTimestamp;
    private Timestamp createdAt;

    public GitHubActivity() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getEventUrl() { return eventUrl; }
    public void setEventUrl(String eventUrl) { this.eventUrl = eventUrl; }

    public String getHtmlUrl() { return eventUrl != null ? eventUrl : ""; }
    public void setHtmlUrl(String htmlUrl) { this.eventUrl = htmlUrl; }

    public Timestamp getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(Timestamp eventTimestamp) { this.eventTimestamp = eventTimestamp; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
