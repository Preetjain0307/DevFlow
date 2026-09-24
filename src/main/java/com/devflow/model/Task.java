package com.devflow.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private Integer sprintId;
    private String sprintName;
    private Integer milestoneId;
    private String milestoneTitle;
    private String title;
    private String description;
    private String taskType; // FEATURE, BUGFIX, REFACTOR, DOCS, TESTING
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status; // TODO, IN_PROGRESS, IN_REVIEW, COMPLETED
    private int createdBy;
    private String creatorName;
    private Integer assignedTo;
    private String assigneeName;
    private BigDecimal estimatedHours;
    private BigDecimal loggedHours;
    private Date dueDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Task() {
        this.estimatedHours = BigDecimal.ZERO;
        this.loggedHours = BigDecimal.ZERO;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTaskId() { return id; }
    public void setTaskId(int taskId) { this.id = taskId; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getTaskKey() {
        if (projectKey != null && !projectKey.isEmpty()) {
            return projectKey + "-" + id;
        }
        return "TASK-" + id;
    }

    public int getStoryPoints() {
        return estimatedHours != null ? estimatedHours.intValue() : 0;
    }

    public void setStoryPoints(int points) {
        this.estimatedHours = new BigDecimal(points);
    }

    public Integer getAssigneeId() {
        return assignedTo;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assignedTo = assigneeId;
    }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getSprintId() { return sprintId; }
    public void setSprintId(Integer sprintId) { this.sprintId = sprintId; }

    public String getSprintName() { return sprintName; }
    public void setSprintName(String sprintName) { this.sprintName = sprintName; }

    public Integer getMilestoneId() { return milestoneId; }
    public void setMilestoneId(Integer milestoneId) { this.milestoneId = milestoneId; }

    public String getMilestoneTitle() { return milestoneTitle; }
    public void setMilestoneTitle(String milestoneTitle) { this.milestoneTitle = milestoneTitle; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public BigDecimal getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(BigDecimal estimatedHours) { this.estimatedHours = estimatedHours; }

    public BigDecimal getLoggedHours() { return loggedHours; }
    public void setLoggedHours(BigDecimal loggedHours) { this.loggedHours = loggedHours; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
