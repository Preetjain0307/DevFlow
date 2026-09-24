package com.devflow.service.impl;

import com.devflow.dao.BugDAO;
import com.devflow.dao.IdeaDAO;
import com.devflow.dao.MeetingDAO;
import com.devflow.dao.ProjectDAO;
import com.devflow.dao.TaskDAO;
import com.devflow.dao.UserDAO;
import com.devflow.dao.impl.BugDAOImpl;
import com.devflow.dao.impl.IdeaDAOImpl;
import com.devflow.dao.impl.MeetingDAOImpl;
import com.devflow.dao.impl.ProjectDAOImpl;
import com.devflow.dao.impl.TaskDAOImpl;
import com.devflow.dao.impl.UserDAOImpl;
import com.devflow.model.DashboardStats;
import com.devflow.model.User;
import com.devflow.service.DashboardService;

public class DashboardServiceImpl implements DashboardService {
    private final ProjectDAO projectDAO;
    private final TaskDAO taskDAO;
    private final BugDAO bugDAO;
    private final IdeaDAO ideaDAO;
    private final MeetingDAO meetingDAO;
    private final UserDAO userDAO;

    public DashboardServiceImpl() {
        this.projectDAO = new ProjectDAOImpl();
        this.taskDAO = new TaskDAOImpl();
        this.bugDAO = new BugDAOImpl();
        this.ideaDAO = new IdeaDAOImpl();
        this.meetingDAO = new MeetingDAOImpl();
        this.userDAO = new UserDAOImpl();
    }

    public DashboardServiceImpl(ProjectDAO projectDAO, TaskDAO taskDAO, BugDAO bugDAO, IdeaDAO ideaDAO, MeetingDAO meetingDAO, UserDAO userDAO) {
        this.projectDAO = projectDAO;
        this.taskDAO = taskDAO;
        this.bugDAO = bugDAO;
        this.ideaDAO = ideaDAO;
        this.meetingDAO = meetingDAO;
        this.userDAO = userDAO;
    }

    @Override
    public DashboardStats getDashboardStats(User currentUser) {
        DashboardStats stats = new DashboardStats();

        // Global / Admin metrics
        stats.setTotalProjects(projectDAO.countTotalProjects());
        stats.setActiveProjects(projectDAO.countActiveProjects());
        stats.setTotalTasks(taskDAO.countTotalTasks());
        stats.setTodoTasks(taskDAO.countTasksByStatus("TODO"));
        stats.setInProgressTasks(taskDAO.countTasksByStatus("IN_PROGRESS"));
        stats.setInReviewTasks(taskDAO.countTasksByStatus("IN_REVIEW"));
        stats.setCompletedTasks(taskDAO.countCompletedTasks());

        stats.setTotalBugs(bugDAO.countTotalBugs());
        stats.setOpenBugs(bugDAO.countOpenBugs());
        stats.setCriticalBugs(bugDAO.countCriticalBugs());

        stats.setUpcomingMeetings(meetingDAO.countUpcomingMeetings());
        stats.setPendingIdeas(ideaDAO.countPendingIdeas());
        stats.setApprovedIdeas(ideaDAO.countApprovedIdeas());
        stats.setTotalUsers(userDAO.findAll().size());

        // User personal stats
        if (currentUser != null) {
            stats.setMyTasks(taskDAO.countTasksByAssignee(currentUser.getId()));
            stats.setMyBugs(bugDAO.countBugsByAssignee(currentUser.getId()));
        }

        // Chart datasets
        stats.setTaskStatusDistribution(taskDAO.getStatusDistribution());
        stats.setBugSeverityDistribution(bugDAO.getSeverityDistribution());
        stats.setIdeaStatusDistribution(ideaDAO.getStatusDistribution());

        return stats;
    }
}
