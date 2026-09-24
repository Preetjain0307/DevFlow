package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MeetingNote implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int meetingId;
    private String rawNotes;
    private String aiSummary;
    private String aiDecisions;
    private String aiActionItems;
    private String aiResponsibilities;
    private int updatedBy;
    private String updaterName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MeetingNote() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public String getRawNotes() { return rawNotes; }
    public void setRawNotes(String rawNotes) { this.rawNotes = rawNotes; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public String getAiDecisions() { return aiDecisions; }
    public void setAiDecisions(String aiDecisions) { this.aiDecisions = aiDecisions; }

    public String getAiActionItems() { return aiActionItems; }
    public void setAiActionItems(String aiActionItems) { this.aiActionItems = aiActionItems; }

    public String getAiResponsibilities() { return aiResponsibilities; }
    public void setAiResponsibilities(String aiResponsibilities) { this.aiResponsibilities = aiResponsibilities; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }

    public String getUpdaterName() { return updaterName; }
    public void setUpdaterName(String updaterName) { this.updaterName = updaterName; }

    public String getAuthorName() { return updaterName != null ? updaterName : "Author"; }
    public void setAuthorName(String an) { this.updaterName = an; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
