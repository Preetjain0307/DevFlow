package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Bug implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private Integer taskId;
    private String taskTitle;
    private String title;
    private String description;
    private String stepsToReproduce;
    private String expectedResult;
    private String actualResult;
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status; // OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, REOPENED, CLOSED
    private int reportedBy;
    private String reporterName;
    private Integer assignedTo;
    private String assigneeName;
    private String resolutionNotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Bug() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBugId() { return id; }
    public void setBugId(int bugId) { this.id = bugId; }

    public String getBugKey() {
        if (projectKey != null && !projectKey.isEmpty()) {
            return projectKey + "-BUG-" + id;
        }
        return "BUG-" + id;
    }

    public Integer getAssigneeId() {
        return assignedTo;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assignedTo = assigneeId;
    }

    public int getReporterId() {
        return reportedBy;
    }

    public void setReporterId(int reporterId) {
        this.reportedBy = reporterId;
    }

    public String getEnvironment() {
        return actualResult != null ? actualResult : "";
    }

    public void setEnvironment(String env) {
        this.actualResult = env;
    }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public String getTaskTitle() { return taskTitle; }
    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStepsToReproduce() { return stepsToReproduce; }
    public void setStepsToReproduce(String stepsToReproduce) { this.stepsToReproduce = stepsToReproduce; }

    public String getExpectedResult() { return expectedResult; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }

    public String getActualResult() { return actualResult; }
    public void setActualResult(String actualResult) { this.actualResult = actualResult; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getReportedBy() { return reportedBy; }
    public void setReportedBy(int reportedBy) { this.reportedBy = reportedBy; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
