package com.devflow.dao;

import com.devflow.model.Meeting;
import com.devflow.model.MeetingActionItem;
import com.devflow.model.MeetingNote;
import com.devflow.model.MeetingParticipant;
import java.util.List;

public interface MeetingDAO {
    Meeting findById(int id);
    List<Meeting> findByProjectId(int projectId);
    List<Meeting> findByUserId(int userId);
    List<Meeting> findUpcomingMeetings(int limit);
    boolean create(Meeting meeting);
    boolean update(Meeting meeting);
    boolean updateStatus(int meetingId, String status);
    boolean delete(int meetingId);

    // Participants
    boolean addParticipant(int meetingId, int userId, String status);
    boolean updateParticipantStatus(int meetingId, int userId, String status);
    List<MeetingParticipant> findParticipantsByMeetingId(int meetingId);

    // Meeting Notes & AI Summary
    boolean saveNotes(MeetingNote note);
    MeetingNote findNotesByMeetingId(int meetingId);

    // Action Items
    boolean addActionItem(MeetingActionItem item);
    boolean toggleActionItem(int itemId, boolean completed);
    List<MeetingActionItem> findActionItemsByMeetingId(int meetingId);

    int countUpcomingMeetings();
}
