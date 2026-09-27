package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Sprint;
import com.devflow.model.User;
import com.devflow.service.ProjectService;
import com.devflow.service.SprintService;
import com.devflow.service.TaskService;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.service.impl.SprintServiceImpl;
import com.devflow.service.impl.TaskServiceImpl;
import com.devflow.util.DateUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SprintController", urlPatterns = {"/sprints"})
public class SprintController extends HttpServlet {
    private SprintService sprintService;
    private ProjectService projectService;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        this.sprintService = new SprintServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.taskService = new TaskServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("project", projectService.getProjectById(projectId));
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("sprints", sprintService.getSprintsByProjectId(projectId));

        if ("view".equals(action)) {
            String sprintIdStr = request.getParameter("id");
            if (sprintIdStr != null && !sprintIdStr.isEmpty()) {
                try {
                    int sprintId = Integer.parseInt(sprintIdStr);
                    request.setAttribute("selectedSprint", sprintService.getSprintById(sprintId));
                    request.setAttribute("sprintTasks", taskService.getTasksBySprintId(sprintId));
                } catch (NumberFormatException ignored) {}
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/task/sprints.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        int projectId = Integer.parseInt(request.getParameter("projectId"));

        if ("create".equals(action)) {
            String name = request.getParameter("sprintName");
            String goal = request.getParameter("goal");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");

            Sprint s = new Sprint();
            s.setProjectId(projectId);
            s.setSprintName(name);
            s.setGoal(goal);
            s.setStartDate(DateUtil.parseDate(startDate));
            s.setEndDate(DateUtil.parseDate(endDate));
            s.setStatus("PLANNING");

            sprintService.createSprint(s);
        } else if ("status-update".equals(action)) {
            int sprintId = Integer.parseInt(request.getParameter("sprintId"));
            String status = request.getParameter("status");
            sprintService.updateStatus(sprintId, status);
        } else if ("delete".equals(action)) {
            int sprintId = Integer.parseInt(request.getParameter("sprintId"));
            sprintService.deleteSprint(sprintId);
        }

        response.sendRedirect(request.getContextPath() + "/sprints?projectId=" + projectId);
    }
}
