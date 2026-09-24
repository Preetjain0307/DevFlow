package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.integration.GitHubService;
import com.devflow.integration.GitHubServiceImpl;
import com.devflow.model.DashboardStats;
import com.devflow.model.User;
import com.devflow.service.AuditLogService;
import com.devflow.service.BugService;
import com.devflow.service.DashboardService;
import com.devflow.service.IdeaService;
import com.devflow.service.MeetingService;
import com.devflow.service.ProjectService;
import com.devflow.service.TaskService;
import com.devflow.service.impl.AuditLogServiceImpl;
import com.devflow.service.impl.BugServiceImpl;
import com.devflow.service.impl.DashboardServiceImpl;
import com.devflow.service.impl.IdeaServiceImpl;
import com.devflow.service.impl.MeetingServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.service.impl.TaskServiceImpl;
import com.devflow.util.JsonUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard", "/dashboard/stats"})
public class DashboardController extends HttpServlet {
    private DashboardService dashboardService;
    private ProjectService projectService;
    private TaskService taskService;
    private BugService bugService;
    private IdeaService ideaService;
    private MeetingService meetingService;
    private AuditLogService auditLogService;
    private GitHubService gitHubService;

    @Override
    public void init() throws ServletException {
        this.dashboardService = new DashboardServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.taskService = new TaskServiceImpl();
        this.bugService = new BugServiceImpl();
        this.ideaService = new IdeaServiceImpl();
        this.meetingService = new MeetingServiceImpl();
        this.auditLogService = new AuditLogServiceImpl();
        this.gitHubService = new GitHubServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        if ("/dashboard/stats".equals(servletPath)) {
            DashboardStats stats = dashboardService.getDashboardStats(currentUser);
            JsonUtil.sendJsonResponse(response, stats);
            return;
        }

        // Render main dashboard JSP with rich role-tailored datasets
        DashboardStats stats = dashboardService.getDashboardStats(currentUser);
        request.setAttribute("stats", stats);
        request.setAttribute("recentProjects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("upcomingMeetings", meetingService.getUpcomingMeetings(5));
        request.setAttribute("recentActivity", gitHubService.getRecentActivity(1, 5));

        if (currentUser.isAdmin()) {
            request.setAttribute("recentAuditLogs", auditLogService.getRecentLogs(10));
        } else if (currentUser.isProjectManager()) {
            request.setAttribute("pendingProposals", ideaService.getPendingFacultyReviewIdeas());
        } else if (currentUser.isDeveloper()) {
            request.setAttribute("myAssignedTasks", taskService.getTasksByAssignee(currentUser.getId()));
            request.setAttribute("myAssignedBugs", bugService.getBugsByAssignee(currentUser.getId()));
        } else if (currentUser.isTester()) {
            request.setAttribute("myReportedBugs", bugService.getBugsByReporter(currentUser.getId()));
        } else if (currentUser.isFaculty()) {
            request.setAttribute("proposalsForReview", ideaService.getPendingFacultyReviewIdeas());
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard/dashboard.jsp").forward(request, response);
    }
}
