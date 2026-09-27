package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Project;
import com.devflow.model.Task;
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
import com.devflow.util.JsonUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TaskController", urlPatterns = {"/tasks", "/task/kanban", "/task/status-update"})
public class TaskController extends HttpServlet {
    private TaskService taskService;
    private ProjectService projectService;
    private UserService userService;
    private SprintService sprintService;
    private MilestoneService milestoneService;

    @Override
    public void init() throws ServletException {
        this.taskService = new TaskServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.userService = new UserServiceImpl();
        this.sprintService = new SprintServiceImpl();
        this.milestoneService = new MilestoneServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        if (action == null) action = "list";

        if ("/task/kanban".equals(servletPath) || "kanban".equals(action)) {
            showKanban(request, response);
            return;
        }

        switch (action) {
            case "view":
                viewTask(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "export":
                exportTasksCsv(request, response);
                break;
            case "list":
            default:
                listTasks(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/task/status-update".equals(servletPath)) {
            handleAjaxStatusUpdate(request, response);
            return;
        }

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
            case "comment":
            case "addComment":
                handleAddComment(request, response);
                break;
            case "status-update":
            case "updateStatus":
                handleStatusUpdateForm(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/tasks");
                break;
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String sprintIdStr = request.getParameter("sprintId");
        String assignedToStr = request.getParameter("assignedTo");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");
        String keyword = request.getParameter("keyword");

        Integer projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : null;
        Integer sprintId = (sprintIdStr != null && !sprintIdStr.isEmpty()) ? Integer.parseInt(sprintIdStr) : null;
        Integer assignedTo = (assignedToStr != null && !assignedToStr.isEmpty()) ? Integer.parseInt(assignedToStr) : null;

        List<Task> tasks = taskService.searchTasks(projectId, sprintId, assignedTo, status, priority, keyword);

        request.setAttribute("tasks", tasks);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("users", userService.getAllUsers());
        request.setAttribute("projectId", projectId);
        request.setAttribute("sprintId", sprintId);
        request.setAttribute("assignedTo", assignedTo);
        request.setAttribute("status", status);
        request.setAttribute("priority", priority);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/task/list.jsp").forward(request, response);
    }

    private void showKanban(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String projectIdStr = request.getParameter("projectId");
        List<Project> userProjects = projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName());
        
        int projectId = 1;
        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            projectId = Integer.parseInt(projectIdStr);
        } else if (!userProjects.isEmpty()) {
            projectId = userProjects.get(0).getId();
        }

        List<Task> allTasks = taskService.getTasksByProjectId(projectId);

        List<Task> todoTasks = new ArrayList<>();
        List<Task> inProgressTasks = new ArrayList<>();
        List<Task> inReviewTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();

        for (Task t : allTasks) {
            if ("TODO".equalsIgnoreCase(t.getStatus())) todoTasks.add(t);
            else if ("IN_PROGRESS".equalsIgnoreCase(t.getStatus())) inProgressTasks.add(t);
            else if ("IN_REVIEW".equalsIgnoreCase(t.getStatus())) inReviewTasks.add(t);
            else if ("COMPLETED".equalsIgnoreCase(t.getStatus())) completedTasks.add(t);
        }

        request.setAttribute("currentProject", projectService.getProjectById(projectId));
        request.setAttribute("projects", userProjects);
        request.setAttribute("projectId", projectId);
        request.setAttribute("todoTasks", todoTasks);
        request.setAttribute("inProgressTasks", inProgressTasks);
        request.setAttribute("inReviewTasks", inReviewTasks);
        request.setAttribute("completedTasks", completedTasks);
        request.setAttribute("projectUsers", userService.getProjectUsers(projectId));

        request.getRequestDispatcher("/WEB-INF/views/task/kanban.jsp").forward(request, response);
    }

    private void viewTask(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Task task = taskService.getTaskById(id);
        if (task == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
            return;
        }

        request.setAttribute("task", task);
        request.setAttribute("comments", taskService.getComments(id));
        request.setAttribute("history", taskService.getHistory(id));
        request.getRequestDispatcher("/WEB-INF/views/task/view.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("sprints", sprintService.getSprintsByProjectId(projectId));
        request.setAttribute("milestones", milestoneService.getMilestonesByProjectId(projectId));
        request.setAttribute("projectUsers", userService.getProjectUsers(projectId));

        request.getRequestDispatcher("/WEB-INF/views/task/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        Task task = taskService.getTaskById(id);
        if (task == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
            return;
        }

        request.setAttribute("task", task);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("sprints", sprintService.getSprintsByProjectId(task.getProjectId()));
        request.setAttribute("milestones", milestoneService.getMilestonesByProjectId(task.getProjectId()));
        request.setAttribute("projectUsers", userService.getProjectUsers(task.getProjectId()));

        request.getRequestDispatcher("/WEB-INF/views/task/edit.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String sprintIdStr = request.getParameter("sprintId");
        String milestoneIdStr = request.getParameter("milestoneId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String taskType = request.getParameter("taskType");
        String priority = request.getParameter("priority");
        String assignedToStr = request.getParameter("assignedTo");
        String estHoursStr = request.getParameter("estimatedHours");
        String dueDateStr = request.getParameter("dueDate");

        Task t = new Task();
        t.setProjectId(Integer.parseInt(projectIdStr));
        if (sprintIdStr != null && !sprintIdStr.isEmpty()) t.setSprintId(Integer.parseInt(sprintIdStr));
        if (milestoneIdStr != null && !milestoneIdStr.isEmpty()) t.setMilestoneId(Integer.parseInt(milestoneIdStr));
        t.setTitle(title);
        t.setDescription(description);
        t.setTaskType(taskType);
        t.setPriority(priority);
        t.setStatus("TODO");
        if (assignedToStr != null && !assignedToStr.isEmpty()) t.setAssignedTo(Integer.parseInt(assignedToStr));
        if (estHoursStr != null && !estHoursStr.isEmpty()) t.setEstimatedHours(new BigDecimal(estHoursStr));
        t.setDueDate(DateUtil.parseDate(dueDateStr));

        try {
            boolean created = taskService.createTask(t, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (created) {
                response.sendRedirect(request.getContextPath() + "/task/kanban?projectId=" + t.getProjectId());
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        showCreateForm(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        String sprintIdStr = request.getParameter("sprintId");
        String milestoneIdStr = request.getParameter("milestoneId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String taskType = request.getParameter("taskType");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String assignedToStr = request.getParameter("assignedTo");
        String estHoursStr = request.getParameter("estimatedHours");
        String logHoursStr = request.getParameter("loggedHours");
        String dueDateStr = request.getParameter("dueDate");

        Task t = taskService.getTaskById(id);
        if (sprintIdStr != null && !sprintIdStr.isEmpty()) t.setSprintId(Integer.parseInt(sprintIdStr)); else t.setSprintId(null);
        if (milestoneIdStr != null && !milestoneIdStr.isEmpty()) t.setMilestoneId(Integer.parseInt(milestoneIdStr)); else t.setMilestoneId(null);
        t.setTitle(title);
        t.setDescription(description);
        t.setTaskType(taskType);
        t.setPriority(priority);
        t.setStatus(status);
        if (assignedToStr != null && !assignedToStr.isEmpty()) t.setAssignedTo(Integer.parseInt(assignedToStr)); else t.setAssignedTo(null);
        if (estHoursStr != null && !estHoursStr.isEmpty()) t.setEstimatedHours(new BigDecimal(estHoursStr));
        if (logHoursStr != null && !logHoursStr.isEmpty()) t.setLoggedHours(new BigDecimal(logHoursStr));
        t.setDueDate(DateUtil.parseDate(dueDateStr));

        try {
            taskService.updateTask(t, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            response.sendRedirect(request.getContextPath() + "/tasks?action=view&id=" + id);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void handleAjaxStatusUpdate(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String status = request.getParameter("status");

            boolean updated = taskService.updateTaskStatus(taskId, status, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", updated);
            resp.put("taskId", taskId);
            resp.put("status", status);
            JsonUtil.sendJsonResponse(response, resp);
        } catch (Exception e) {
            JsonUtil.sendErrorJson(response, 400, e.getMessage());
        }
    }

    private void handleStatusUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String status = request.getParameter("status");

        taskService.updateTaskStatus(taskId, status, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
            java.util.Map<String, Object> resp = new java.util.HashMap<>();
            resp.put("success", true);
            resp.put("status", status);
            com.devflow.util.JsonUtil.sendJsonResponse(response, resp);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/tasks?action=view&id=" + taskId);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int taskId = Integer.parseInt(request.getParameter("id"));
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        taskService.deleteTask(taskId, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/task/kanban?projectId=" + projectId);
    }

    private void handleAddComment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String comment = request.getParameter("comment");

        taskService.addComment(taskId, currentUser.getId(), comment);
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
            java.util.Map<String, Object> resp = new java.util.HashMap<>();
            resp.put("success", true);
            resp.put("authorName", currentUser.getFullName());
            String initial = currentUser.getFullName() != null && !currentUser.getFullName().isEmpty()
                    ? currentUser.getFullName().substring(0, 1).toUpperCase() : "U";
            resp.put("initial", initial);
            resp.put("createdAt", "Just now");
            resp.put("comment", comment);
            com.devflow.util.JsonUtil.sendJsonResponse(response, resp);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/tasks?action=view&id=" + taskId);
    }

    private void exportTasksCsv(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String projectIdStr = request.getParameter("projectId");
        Integer projectId = (projectIdStr != null && !projectIdStr.trim().isEmpty()) ? Integer.parseInt(projectIdStr.trim()) : null;
        List<com.devflow.model.Task> tasks;
        if (projectId != null) {
            tasks = taskService.getTasksByProjectId(projectId);
        } else {
            tasks = taskService.searchTasks(null, null, null, null, null, null);
        }

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"devflow_tasks_" + System.currentTimeMillis() + ".csv\"");

        try (java.io.PrintWriter writer = response.getWriter()) {
            writer.write('\ufeff'); // UTF-8 BOM for Excel
            writer.println("Task ID,Task Key,Title,Type,Priority,Status,Estimated Hours,Assigned To,Due Date,Created At");
            for (com.devflow.model.Task t : tasks) {
                writer.printf("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        t.getId(),
                        escapeCsv(t.getTaskKey()),
                        escapeCsv(t.getTitle()),
                        escapeCsv(t.getTaskType()),
                        escapeCsv(t.getPriority()),
                        escapeCsv(t.getStatus()),
                        t.getEstimatedHours() != null ? t.getEstimatedHours().toString() : "0",
                        escapeCsv(t.getAssigneeName()),
                        t.getDueDate() != null ? t.getDueDate().toString() : "N/A",
                        t.getCreatedAt() != null ? t.getCreatedAt().toString() : "N/A"
                );
            }
        }
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }
}
