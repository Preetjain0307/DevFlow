package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class MeetingParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int meetingId;
    private int userId;
    private String username;
    private String fullName;
    private String email;
    private String status; // INVITED, ACCEPTED, DECLINED, ATTENDED
    private Timestamp joinedAt;

    public MeetingParticipant() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getJoinedAt() { return joinedAt; }
    public void setJoinedAt(Timestamp joinedAt) { this.joinedAt = joinedAt; }
}
