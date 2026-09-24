package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Project;
import com.devflow.model.User;
import com.devflow.service.MilestoneService;
import com.devflow.service.ProjectService;
import com.devflow.service.SprintService;
import com.devflow.service.TaskService;
import com.devflow.service.UserService;
import com.devflow.service.impl.MilestoneServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.service.impl.SprintServiceImpl;
import com.devflow.service.impl.TaskServiceImpl;
import com.devflow.service.impl.UserServiceImpl;
import com.devflow.util.DateUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ProjectController", urlPatterns = {"/projects"})
public class ProjectController extends HttpServlet {
    private ProjectService projectService;
    private UserService userService;
    private SprintService sprintService;
    private MilestoneService milestoneService;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        this.projectService = new ProjectServiceImpl();
        this.userService = new UserServiceImpl();
        this.sprintService = new SprintServiceImpl();
        this.milestoneService = new MilestoneServiceImpl();
        this.taskService = new TaskServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "view":
                viewProject(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "members":
                showMembers(request, response);
                break;
            case "list":
            default:
                listProjects(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                handleCreate(request, response);
                break;
            case "edit":
                handleEdit(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            case "status-update":
                handleStatusUpdate(request, response);
                break;
            case "add-member":
                handleAddMember(request, response);
                break;
            case "remove-member":
                handleRemoveMember(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/projects");
                break;
        }
    }

    private void listProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");

        List<Project> projects;
        if ((keyword != null && !keyword.trim().isEmpty()) || (status != null && !status.isEmpty()) || (priority != null && !priority.isEmpty())) {
            projects = projectService.searchProjects(keyword, status, priority);
        } else {
            projects = projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName());
        }

        request.setAttribute("projects", projects);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("priority", priority);
        request.getRequestDispatcher("/WEB-INF/views/project/list.jsp").forward(request, response);
    }

    private void viewProject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Project project = projectService.getProjectById(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
            return;
        }

        request.setAttribute("project", project);
        request.setAttribute("members", projectService.getProjectMembers(id));
        request.setAttribute("sprints", sprintService.getSprintsByProjectId(id));
        request.setAttribute("milestones", milestoneService.getMilestonesByProjectId(id));
        request.setAttribute("tasks", taskService.getTasksByProjectId(id));
        request.setAttribute("allUsers", userService.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/project/view.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("managers", userService.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/project/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Project project = projectService.getProjectById(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found.");
            return;
        }
        request.setAttribute("project", project);
        request.setAttribute("managers", userService.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/project/edit.jsp").forward(request, response);
    }

    private void showMembers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Project project = projectService.getProjectById(id);
        request.setAttribute("project", project);
        request.setAttribute("members", projectService.getProjectMembers(id));
        request.setAttribute("allUsers", userService.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/project/members.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String name = request.getParameter("name");
        String projectKey = request.getParameter("projectKey");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String managerIdStr = request.getParameter("managerId");

        Project p = new Project();
        p.setName(name);
        p.setProjectKey(projectKey);
        p.setDescription(description);
        p.setPriority(priority != null ? priority : "MEDIUM");
        p.setStatus("PLANNING");
        p.setStartDate(DateUtil.parseDate(startDateStr));
        p.setEndDate(DateUtil.parseDate(endDateStr));

        int managerId = currentUser.getId();
        if (managerIdStr != null && !managerIdStr.trim().isEmpty()) {
            try { managerId = Integer.parseInt(managerIdStr); } catch (Exception ignored) {}
        }
        p.setManagerId(managerId);

        try {
            boolean created = projectService.createProject(p, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (created) {
                response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + p.getId());
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        request.setAttribute("project", p);
        request.setAttribute("managers", userService.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/project/create.jsp").forward(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String managerIdStr = request.getParameter("managerId");

        Project p = projectService.getProjectById(id);
        p.setName(name);
        p.setDescription(description);
        p.setStatus(status);
        p.setPriority(priority);
        p.setStartDate(DateUtil.parseDate(startDateStr));
        p.setEndDate(DateUtil.parseDate(endDateStr));
        if (managerIdStr != null && !managerIdStr.trim().isEmpty()) {
            try { p.setManagerId(Integer.parseInt(managerIdStr)); } catch (Exception ignored) {}
        }

        try {
            projectService.updateProject(p, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + p.getId());
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("project", p);
            request.setAttribute("managers", userService.getAllUsers());
            request.getRequestDispatcher("/WEB-INF/views/project/edit.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int id = Integer.parseInt(request.getParameter("id"));

        projectService.deleteProject(id, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/projects");
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        projectService.updateStatus(id, status, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + id);
    }

    private void handleAddMember(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String role = request.getParameter("projectRole");

        projectService.addMember(projectId, userId, role, currentUser.getId());
        response.sendRedirect(request.getContextPath() + "/projects?action=members&id=" + projectId);
    }

    private void handleRemoveMember(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        projectService.removeMember(projectId, userId, currentUser.getId());
        response.sendRedirect(request.getContextPath() + "/projects?action=members&id=" + projectId);
    }
}
