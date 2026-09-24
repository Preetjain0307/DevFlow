package com.devflow.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Sprint implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String sprintName;
    private String goal;
    private Date startDate;
    private Date endDate;
    private String status; // PLANNING, ACTIVE, COMPLETED, CANCELLED
    private Timestamp createdAt;

    // Metrics
    private int totalTasks;
    private int completedTasks;

    public Sprint() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSprintId() { return id; }
    public void setSprintId(int sid) { this.id = sid; }

    public String getName() { return sprintName; }
    public void setName(String name) { this.sprintName = name; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getSprintName() { return sprintName; }
    public void setSprintName(String sprintName) { this.sprintName = sprintName; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(int completedTasks) { this.completedTasks = completedTasks; }

    public int getProgressPercentage() {
        if (totalTasks == 0) return 0;
        return (int) Math.round(((double) completedTasks / totalTasks) * 100);
    }
}
