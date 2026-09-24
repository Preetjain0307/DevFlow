package com.devflow.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String projectKey;
    private String name;
    private String description;
    private String status; // PLANNING, ACTIVE, ON_HOLD, COMPLETED, ARCHIVED
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private int managerId;
    private String managerName;
    private Date startDate;
    private Date endDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Metrics for display
    private int totalTasks;
    private int completedTasks;
    private int openBugs;
    private int memberCount;

    public Project() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProjectId() { return id; }
    public void setProjectId(int projectId) { this.id = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }

    public int getOpenBugs() { return openBugs; }
    public void setOpenBugs(int openBugs) { this.openBugs = openBugs; }

    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    public int getProgressPercentage() {
        if (totalTasks == 0) return 0;
        return (int) Math.round(((double) completedTasks / totalTasks) * 100);
    }
}
