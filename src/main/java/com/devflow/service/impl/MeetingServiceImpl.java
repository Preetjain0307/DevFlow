package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.MeetingDAO;
import com.devflow.dao.NotificationDAO;
import com.devflow.dao.ProjectDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.MeetingDAOImpl;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.dao.impl.ProjectDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.integration.AIService;
import com.devflow.integration.AIServiceImpl;
import com.devflow.integration.JitsiService;
import com.devflow.model.AuditLog;
import com.devflow.model.Meeting;
import com.devflow.model.MeetingActionItem;
import com.devflow.model.MeetingNote;
import com.devflow.model.MeetingParticipant;
import com.devflow.model.Notification;
import com.devflow.model.Project;
import com.devflow.service.MeetingService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class MeetingServiceImpl implements MeetingService {
    private final MeetingDAO meetingDAO;
    private final ProjectDAO projectDAO;
    private final NotificationDAO notificationDAO;
    private final AuditLogDAO auditLogDAO;
    private final AIService aiService;

    public MeetingServiceImpl() {
        this.meetingDAO = new MeetingDAOImpl();
        this.projectDAO = new ProjectDAOImpl();
        this.notificationDAO = new NotificationDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
        this.aiService = new AIServiceImpl();
    }

    public MeetingServiceImpl(MeetingDAO meetingDAO, ProjectDAO projectDAO, NotificationDAO notificationDAO, AuditLogDAO auditLogDAO, AIService aiService) {
        this.meetingDAO = meetingDAO;
        this.projectDAO = projectDAO;
        this.notificationDAO = notificationDAO;
        this.auditLogDAO = auditLogDAO;
        this.aiService = aiService;
    }

    @Override
    public Meeting getMeetingById(int id) {
        return meetingDAO.findById(id);
    }

    @Override
    public List<Meeting> getMeetingsByProjectId(int projectId) {
        return meetingDAO.findByProjectId(projectId);
    }

    @Override
    public List<Meeting> getUserMeetings(int userId) {
        return meetingDAO.findByUserId(userId);
    }

    @Override
    public List<Meeting> getUpcomingMeetings(int limit) {
        return meetingDAO.findUpcomingMeetings(limit);
    }

    @Override
    public boolean scheduleMeeting(Meeting meeting, List<Integer> participantUserIds, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(meeting.getTitle())) {
            throw new ValidationException("Meeting title is required.");
        }
        if (meeting.getMeetingDate() == null || meeting.getStartTime() == null || meeting.getEndTime() == null) {
            throw new ValidationException("Meeting date, start time, and end time are required.");
        }
        if (meeting.getProjectId() <= 0) {
            throw new ValidationException("Project is required.");
        }

        Project project = projectDAO.findById(meeting.getProjectId());
        String projectKey = project != null ? project.getProjectKey() : "PROJ";

        // Generate Jitsi room code and URL
        String roomCode = JitsiService.generateRoomCode(projectKey, meeting.getTitle());
        meeting.setRoomCode(roomCode);
        meeting.setMeetingUrl(JitsiService.buildMeetingUrl(roomCode));
        meeting.setCreatedBy(userId);
        meeting.setStatus("SCHEDULED");

        boolean created = meetingDAO.create(meeting);
        if (created) {
            // Add creator as participant (ACCEPTED)
            meetingDAO.addParticipant(meeting.getId(), userId, "ACCEPTED");

            // Add invited participants
            if (participantUserIds != null) {
                for (Integer pUserId : participantUserIds) {
                    if (pUserId != null && pUserId != userId) {
                        meetingDAO.addParticipant(meeting.getId(), pUserId, "INVITED");

                        // Send notification
                        Notification notif = new Notification();
                        notif.setUserId(pUserId);
                        notif.setProjectId(meeting.getProjectId());
                        notif.setTitle("Meeting Invitation: " + meeting.getTitle());
                        notif.setMessage("You have been invited to meeting '" + meeting.getTitle() + "' on " + meeting.getMeetingDate());
                        notif.setLinkUrl("/meetings?action=view&id=" + meeting.getId());
                        notif.setNotificationType("MEETING");
                        notificationDAO.create(notif);
                    }
                }
            }

            auditLogDAO.log(new AuditLog(userId, username, Constants.AUDIT_SCHEDULE_MEETING, "MEETING", meeting.getId(), "Scheduled meeting: " + meeting.getTitle(), ipAddress));
        }
        return created;
    }

    @Override
    public boolean updateMeeting(Meeting meeting, int userId, String username, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(meeting.getTitle())) {
            throw new ValidationException("Meeting title cannot be empty.");
        }
        boolean updated = meetingDAO.update(meeting);
        if (updated) {
            auditLogDAO.log(new AuditLog(userId, username, "UPDATE_MEETING", "MEETING", meeting.getId(), "Updated meeting: " + meeting.getTitle(), ipAddress));
        }
        return updated;
    }

    @Override
    public boolean updateMeetingStatus(int meetingId, String status) {
        return meetingDAO.updateStatus(meetingId, status);
    }

    @Override
    public boolean deleteMeeting(int meetingId, int userId, String username, String ipAddress) {
        Meeting m = meetingDAO.findById(meetingId);
        if (m == null) return false;
        boolean deleted = meetingDAO.delete(meetingId);
        if (deleted) {
            auditLogDAO.log(new AuditLog(userId, username, "DELETE_MEETING", "MEETING", meetingId, "Deleted meeting: " + m.getTitle(), ipAddress));
        }
        return deleted;
    }

    @Override
    public boolean updateParticipantStatus(int meetingId, int userId, String status) {
        return meetingDAO.updateParticipantStatus(meetingId, userId, status);
    }

    @Override
    public List<MeetingParticipant> getParticipants(int meetingId) {
        return meetingDAO.findParticipantsByMeetingId(meetingId);
    }

    @Override
    public MeetingNote generateAndSaveAiSummary(int meetingId, String rawNotes, int userId) {
        if (!ValidationUtil.isNotEmpty(rawNotes)) {
            throw new ValidationException("Raw meeting notes are required to generate AI summary.");
        }
        MeetingNote note = aiService.generateMeetingSummary(rawNotes, meetingId, userId);
        meetingDAO.saveNotes(note);
        return note;
    }

    @Override
    public MeetingNote getMeetingNotes(int meetingId) {
        return meetingDAO.findNotesByMeetingId(meetingId);
    }

    @Override
    public boolean addActionItem(MeetingActionItem item) {
        if (!ValidationUtil.isNotEmpty(item.getDescription())) {
            throw new ValidationException("Action item description is required.");
        }
        return meetingDAO.addActionItem(item);
    }

    @Override
    public boolean toggleActionItem(int itemId, boolean completed) {
        return meetingDAO.toggleActionItem(itemId, completed);
    }

    @Override
    public List<MeetingActionItem> getActionItems(int meetingId) {
        return meetingDAO.findActionItemsByMeetingId(meetingId);
    }
}
