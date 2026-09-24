package com.devflow.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class MeetingActionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int meetingId;
    private String description;
    private Integer assignedTo;
    private String assigneeName;
    private Date dueDate;
    private boolean completed;
    private Timestamp createdAt;

    public MeetingActionItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public int getActionItemId() { return id; }
    public void setActionItemId(int aid) { this.id = aid; }

    public String getStatus() { return completed ? "COMPLETED" : "PENDING"; }
    public void setStatus(String s) { this.completed = "COMPLETED".equalsIgnoreCase(s); }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
