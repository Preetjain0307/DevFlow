package com.devflow.integration;

import com.devflow.model.MeetingNote;

public interface AIService {
    MeetingNote generateMeetingSummary(String rawNotes, int meetingId, int userId);
    boolean isConfigured();
}
