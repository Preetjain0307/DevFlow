package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class GitHubRepo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private String repoOwner;
    private String repoName;
    private String repoUrl;
    private String defaultBranch;
    private boolean active;
    private Timestamp lastSyncedAt;
    private Timestamp createdAt;

    // Live GitHub API statistics
    private String description;
    private int starsCount;
    private int forksCount;
    private int openIssuesCount;
    private int watchersCount;

    public GitHubRepo() {
        this.defaultBranch = "main";
        this.active = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }

    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }

    public String getDefaultBranch() { return defaultBranch; }
    public void setDefaultBranch(String defaultBranch) { this.defaultBranch = defaultBranch; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Timestamp getLastSyncedAt() { return lastSyncedAt; }
    public void setLastSyncedAt(Timestamp lastSyncedAt) { this.lastSyncedAt = lastSyncedAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getFullName() {
        return repoOwner + "/" + repoName;
    }

    public String getHtmlUrl() {
        return repoUrl != null ? repoUrl : ("https://github.com/" + repoOwner + "/" + repoName);
    }

    public void setHtmlUrl(String htmlUrl) {
        this.repoUrl = htmlUrl;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStarsCount() { return starsCount; }
    public void setStarsCount(int starsCount) { this.starsCount = starsCount; }

    public int getForksCount() { return forksCount; }
    public void setForksCount(int forksCount) { this.forksCount = forksCount; }

    public int getOpenIssuesCount() { return openIssuesCount; }
    public void setOpenIssuesCount(int openIssuesCount) { this.openIssuesCount = openIssuesCount; }

    public int getWatchersCount() { return watchersCount; }
    public void setWatchersCount(int watchersCount) { this.watchersCount = watchersCount; }
}
