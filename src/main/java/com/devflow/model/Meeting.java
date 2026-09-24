package com.devflow.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class Meeting implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int projectId;
    private String projectKey;
    private String projectName;
    private String title;
    private String description;
    private Date meetingDate;
    private Time startTime;
    private Time endTime;
    private String roomCode;
    private String meetingUrl;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private int createdBy;
    private String creatorName;
    private Timestamp createdAt;

    // Associated details
    private List<MeetingParticipant> participants;
    private MeetingNote meetingNote;
    private List<MeetingActionItem> actionItems;

    public Meeting() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMeetingId() { return id; }
    public void setMeetingId(int meetingId) { this.id = meetingId; }

    public String getMeetingType() { return status != null ? status : "STANDUP"; }
    public void setMeetingType(String type) {}

    public String getHostName() { return creatorName != null ? creatorName : "Host"; }
    public void setHostName(String name) { this.creatorName = name; }

    public String getScheduledAt() {
        return (meetingDate != null ? meetingDate.toString() : "") + (startTime != null ? " " + startTime.toString() : "");
    }
    public void setScheduledAt(String s) {}

    public int getDurationMinutes() { return 30; }
    public void setDurationMinutes(int m) {}

    public String getRoomName() { return roomCode != null ? roomCode : "devflow-room-" + id; }
    public void setRoomName(String r) { this.roomCode = r; }

    public String getAgenda() { return description != null ? description : ""; }
    public void setAgenda(String a) { this.description = a; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectKey() { return projectKey; }
    public void setProjectKey(String projectKey) { this.projectKey = projectKey; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getMeetingDate() { return meetingDate; }
    public void setMeetingDate(Date meetingDate) { this.meetingDate = meetingDate; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getMeetingUrl() { return meetingUrl; }
    public void setMeetingUrl(String meetingUrl) { this.meetingUrl = meetingUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<MeetingParticipant> getParticipants() { return participants; }
    public void setParticipants(List<MeetingParticipant> participants) { this.participants = participants; }

    public MeetingNote getMeetingNote() { return meetingNote; }
    public void setMeetingNote(MeetingNote meetingNote) { this.meetingNote = meetingNote; }

    public List<MeetingActionItem> getActionItems() { return actionItems; }
    public void setActionItems(List<MeetingActionItem> actionItems) { this.actionItems = actionItems; }
}
