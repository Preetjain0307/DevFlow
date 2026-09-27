package com.devflow.service;

import com.devflow.model.Meeting;
import com.devflow.model.MeetingActionItem;
import com.devflow.model.MeetingNote;
import com.devflow.model.MeetingParticipant;
import java.util.List;

public interface MeetingService {
    Meeting getMeetingById(int id);
    List<Meeting> getMeetingsByProjectId(int projectId);
    List<Meeting> getUserMeetings(int userId);
    List<Meeting> getUpcomingMeetings(int limit);
    boolean scheduleMeeting(Meeting meeting, List<Integer> participantUserIds, int userId, String username, String ipAddress);
    boolean updateMeeting(Meeting meeting, int userId, String username, String ipAddress);
    boolean updateMeetingStatus(int meetingId, String status);
    boolean deleteMeeting(int meetingId, int userId, String username, String ipAddress);

    // Participants & Attendance
    boolean updateParticipantStatus(int meetingId, int userId, String status);
    List<MeetingParticipant> getParticipants(int meetingId);

    // Notes & AI Summarization
    boolean saveMeetingNotes(int meetingId, String rawNotes, int userId);
    MeetingNote generateAndSaveAiSummary(int meetingId, String rawNotes, int userId);
    MeetingNote getMeetingNotes(int meetingId);

    // Action Items
    boolean addActionItem(MeetingActionItem item);
    boolean toggleActionItem(int itemId, boolean completed);
    List<MeetingActionItem> getActionItems(int meetingId);
}
