package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Meeting;
import com.devflow.model.MeetingActionItem;
import com.devflow.model.MeetingNote;
import com.devflow.model.User;
import com.devflow.service.MeetingService;
import com.devflow.service.ProjectService;
import com.devflow.service.UserService;
import com.devflow.service.impl.MeetingServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.service.impl.UserServiceImpl;
import com.devflow.util.DateUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MeetingController", urlPatterns = {"/meetings", "/meeting/ai-summary", "/meeting/action-item"})
public class MeetingController extends HttpServlet {
    private MeetingService meetingService;
    private ProjectService projectService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.meetingService = new MeetingServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "view":
                viewMeeting(request, response);
                break;
            case "schedule":
                showScheduleForm(request, response);
                break;
            case "join":
                joinMeeting(request, response);
                break;
            case "list":
            default:
                listMeetings(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/meeting/ai-summary".equals(servletPath)) {
            handleAiSummary(request, response);
            return;
        }

        if ("/meeting/action-item".equals(servletPath)) {
            handleActionItem(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "schedule";

        switch (action) {
            case "schedule":
                handleSchedule(request, response);
                break;
            case "status-update":
                handleStatusUpdate(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            case "saveNotes":
                handleSaveNotes(request, response);
                break;
            case "generateAISummary":
                handleAiSummary(request, response);
                break;
            case "addActionItem":
                handleAddActionItem(request, response);
                break;
            case "completeActionItem":
                handleCompleteActionItem(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/meetings");
                break;
        }
    }

    private void listMeetings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        List<Meeting> meetings;

        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            meetings = meetingService.getMeetingsByProjectId(Integer.parseInt(projectIdStr));
            request.setAttribute("selectedProjectId", Integer.parseInt(projectIdStr));
        } else {
            meetings = meetingService.getUserMeetings(currentUser.getId());
        }

        request.setAttribute("meetings", meetings);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.getRequestDispatcher("/WEB-INF/views/meeting/list.jsp").forward(request, response);
    }

    private void viewMeeting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Meeting not found.");
            return;
        }

        request.setAttribute("meeting", meeting);
        List<User> pUsers = userService.getProjectUsers(meeting.getProjectId());
        request.setAttribute("projectUsers", pUsers);
        request.setAttribute("users", pUsers);

        MeetingNote note = meetingService.getMeetingNotes(id);
        request.setAttribute("note", note);
        if (note != null) {
            List<MeetingNote> noteList = new ArrayList<>();
            noteList.add(note);
            request.setAttribute("notes", noteList);
        } else {
            request.setAttribute("notes", new ArrayList<MeetingNote>());
        }

        request.setAttribute("actionItems", meetingService.getActionItems(id));
        request.getRequestDispatcher("/WEB-INF/views/meeting/view.jsp").forward(request, response);
    }

    private void showScheduleForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("projectUsers", userService.getProjectUsers(projectId));

        request.getRequestDispatcher("/WEB-INF/views/meeting/schedule.jsp").forward(request, response);
    }

    private void joinMeeting(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int id = Integer.parseInt(request.getParameter("id"));

        Meeting meeting = meetingService.getMeetingById(id);
        if (meeting != null) {
            // Update attendance to ATTENDED
            meetingService.updateParticipantStatus(id, currentUser.getId(), "ATTENDED");
            // Redirect to Jitsi Meet URL
            response.sendRedirect(meeting.getMeetingUrl());
        } else {
            response.sendRedirect(request.getContextPath() + "/meetings");
        }
    }

    private void handleSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String meetingDateStr = request.getParameter("meetingDate");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String[] participantIds = request.getParameterValues("participants");

        Meeting m = new Meeting();
        m.setProjectId(Integer.parseInt(projectIdStr));
        m.setTitle(title);
        m.setDescription(description);
        m.setMeetingDate(DateUtil.parseDate(meetingDateStr));
        m.setStartTime(DateUtil.parseTime(startTimeStr));
        m.setEndTime(DateUtil.parseTime(endTimeStr));

        List<Integer> pList = new ArrayList<>();
        if (participantIds != null) {
            for (String pid : participantIds) {
                try { pList.add(Integer.parseInt(pid)); } catch (Exception ignored) {}
            }
        }

        try {
            boolean scheduled = meetingService.scheduleMeeting(m, pList, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (scheduled) {
                response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + m.getId());
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        showScheduleForm(request, response);
    }

    private void handleAiSummary(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int meetingId = Integer.parseInt(request.getParameter("meetingId"));
        String rawNotes = request.getParameter("rawNotes");

        if (rawNotes == null || rawNotes.trim().isEmpty()) {
            MeetingNote existing = meetingService.getMeetingNotes(meetingId);
            if (existing != null && existing.getRawNotes() != null) {
                rawNotes = existing.getRawNotes();
            }
        }

        try {
            MeetingNote note = meetingService.generateAndSaveAiSummary(meetingId, rawNotes, currentUser.getId());
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
                response.setContentType("application/json;charset=UTF-8");
                String json = "{\"success\":true,\"summary\":\"" + escapeJson(note.getAiSummary()) + "\"," +
                        "\"decisions\":\"" + escapeJson(note.getAiDecisions()) + "\"," +
                        "\"actionItems\":\"" + escapeJson(note.getAiActionItems()) + "\"," +
                        "\"responsibilities\":\"" + escapeJson(note.getAiResponsibilities()) + "\"}";
                response.getWriter().write(json);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId + "&summary=generated");
        } catch (Exception e) {
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId + "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void handleSaveNotes(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int meetingId = Integer.parseInt(request.getParameter("meetingId"));
        String rawNotes = request.getParameter("rawNotes");

        try {
            meetingService.saveMeetingNotes(meetingId, rawNotes, currentUser.getId());
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":true,\"message\":\"Notes saved successfully\"}");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId + "&saved=true");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId + "&error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void handleAddActionItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int meetingId = Integer.parseInt(request.getParameter("meetingId"));
        String description = request.getParameter("description");
        String assignedToStr = request.getParameter("assigneeId");
        if (assignedToStr == null || assignedToStr.isEmpty()) {
            assignedToStr = request.getParameter("assignedTo");
        }
        String dueDateStr = request.getParameter("dueDate");

        MeetingActionItem item = new MeetingActionItem();
        item.setMeetingId(meetingId);
        item.setDescription(description);
        if (assignedToStr != null && !assignedToStr.isEmpty()) {
            item.setAssignedTo(Integer.parseInt(assignedToStr));
        }
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            item.setDueDate(DateUtil.parseDate(dueDateStr));
        }
        item.setCompleted(false);

        meetingService.addActionItem(item);
        response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId);
    }

    private void handleCompleteActionItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int meetingId = Integer.parseInt(request.getParameter("meetingId"));
        String actionItemIdStr = request.getParameter("actionItemId");
        if (actionItemIdStr == null || actionItemIdStr.isEmpty()) {
            actionItemIdStr = request.getParameter("itemId");
        }
        int itemId = Integer.parseInt(actionItemIdStr);
        meetingService.toggleActionItem(itemId, true);
        response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId);
    }

    private void handleActionItem(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String action = request.getParameter("itemAction");
        int meetingId = Integer.parseInt(request.getParameter("meetingId"));

        if ("add".equals(action)) {
            handleAddActionItem(request, response);
            return;
        } else if ("toggle".equals(action)) {
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            boolean completed = Boolean.parseBoolean(request.getParameter("completed"));
            meetingService.toggleActionItem(itemId, completed);
        }

        response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId);
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int meetingId = Integer.parseInt(request.getParameter("meetingId"));
        String status = request.getParameter("status");

        meetingService.updateMeetingStatus(meetingId, status);
        response.sendRedirect(request.getContextPath() + "/meetings?action=view&id=" + meetingId);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int meetingId = Integer.parseInt(request.getParameter("id"));

        meetingService.deleteMeeting(meetingId, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/meetings");
    }
}
