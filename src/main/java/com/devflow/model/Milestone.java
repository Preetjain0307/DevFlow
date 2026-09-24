package com.devflow.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Milestone implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String title;
    private String description;
    private Date dueDate;
    private String status; // OPEN, IN_PROGRESS, REACHED, CANCELLED
    private Timestamp createdAt;

    public Milestone() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMilestoneId() { return id; }
    public void setMilestoneId(int mid) { this.id = mid; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
