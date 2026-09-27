package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Bug;
import com.devflow.model.User;
import com.devflow.service.BugService;
import com.devflow.service.ProjectService;
import com.devflow.service.TaskService;
import com.devflow.service.UserService;
import com.devflow.service.impl.BugServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.service.impl.TaskServiceImpl;
import com.devflow.service.impl.UserServiceImpl;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "BugController", urlPatterns = {"/bugs"})
public class BugController extends HttpServlet {
    private BugService bugService;
    private ProjectService projectService;
    private UserService userService;
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        this.bugService = new BugServiceImpl();
        this.projectService = new ProjectServiceImpl();
        this.userService = new UserServiceImpl();
        this.taskService = new TaskServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "view":
                viewBug(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "export":
                exportBugsCsv(request, response);
                break;
            case "list":
            default:
                listBugs(request, response);
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
            case "status-update":
            case "updateStatus":
                handleStatusUpdate(request, response);
                break;
            case "comment":
            case "addComment":
                handleAddComment(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/bugs");
                break;
        }
    }

    private void listBugs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String assignedToStr = request.getParameter("assignedTo");
        String severity = request.getParameter("severity");
        String status = request.getParameter("status");
        String keyword = request.getParameter("keyword");

        Integer projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : null;
        Integer assignedTo = (assignedToStr != null && !assignedToStr.isEmpty()) ? Integer.parseInt(assignedToStr) : null;

        List<Bug> bugs = bugService.searchBugs(projectId, assignedTo, severity, status, keyword);

        request.setAttribute("bugs", bugs);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("users", userService.getAllUsers());
        request.setAttribute("projectId", projectId);
        request.setAttribute("assignedTo", assignedTo);
        request.setAttribute("severity", severity);
        request.setAttribute("status", status);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/bug/list.jsp").forward(request, response);
    }

    private void viewBug(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Bug bug = bugService.getBugById(id);
        if (bug == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bug not found.");
            return;
        }

        request.setAttribute("bug", bug);
        request.setAttribute("comments", bugService.getComments(id));
        request.setAttribute("projectUsers", userService.getProjectUsers(bug.getProjectId()));
        request.getRequestDispatcher("/WEB-INF/views/bug/view.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("tasks", taskService.getTasksByProjectId(projectId));
        request.setAttribute("projectUsers", userService.getProjectUsers(projectId));

        request.getRequestDispatcher("/WEB-INF/views/bug/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        Bug bug = bugService.getBugById(id);
        if (bug == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bug not found.");
            return;
        }

        request.setAttribute("bug", bug);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("tasks", taskService.getTasksByProjectId(bug.getProjectId()));
        request.setAttribute("projectUsers", userService.getProjectUsers(bug.getProjectId()));

        request.getRequestDispatcher("/WEB-INF/views/bug/edit.jsp").forward(request, response);
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String taskIdStr = request.getParameter("taskId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String stepsToReproduce = request.getParameter("stepsToReproduce");
        String expectedResult = request.getParameter("expectedResult");
        String actualResult = request.getParameter("actualResult");
        String severity = request.getParameter("severity");
        String priority = request.getParameter("priority");
        String assignedToStr = request.getParameter("assignedTo");

        Bug b = new Bug();
        b.setProjectId(Integer.parseInt(projectIdStr));
        if (taskIdStr != null && !taskIdStr.isEmpty()) b.setTaskId(Integer.parseInt(taskIdStr));
        b.setTitle(title);
        b.setDescription(description);
        b.setStepsToReproduce(stepsToReproduce);
        b.setExpectedResult(expectedResult);
        b.setActualResult(actualResult);
        b.setSeverity(severity != null ? severity : "MEDIUM");
        b.setPriority(priority != null ? priority : "MEDIUM");
        b.setStatus("OPEN");
        if (assignedToStr != null && !assignedToStr.isEmpty()) b.setAssignedTo(Integer.parseInt(assignedToStr));

        try {
            boolean created = bugService.reportBug(b, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (created) {
                response.sendRedirect(request.getContextPath() + "/bugs?action=view&id=" + b.getId());
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
        String taskIdStr = request.getParameter("taskId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String stepsToReproduce = request.getParameter("stepsToReproduce");
        String expectedResult = request.getParameter("expectedResult");
        String actualResult = request.getParameter("actualResult");
        String severity = request.getParameter("severity");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String assignedToStr = request.getParameter("assignedTo");
        String resolutionNotes = request.getParameter("resolutionNotes");

        Bug b = bugService.getBugById(id);
        if (taskIdStr != null && !taskIdStr.isEmpty()) b.setTaskId(Integer.parseInt(taskIdStr)); else b.setTaskId(null);
        b.setTitle(title);
        b.setDescription(description);
        b.setStepsToReproduce(stepsToReproduce);
        b.setExpectedResult(expectedResult);
        b.setActualResult(actualResult);
        b.setSeverity(severity);
        b.setPriority(priority);
        b.setStatus(status);
        if (assignedToStr != null && !assignedToStr.isEmpty()) b.setAssignedTo(Integer.parseInt(assignedToStr)); else b.setAssignedTo(null);
        b.setResolutionNotes(resolutionNotes);

        try {
            bugService.updateBug(b, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            response.sendRedirect(request.getContextPath() + "/bugs?action=view&id=" + id);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int bugId = Integer.parseInt(request.getParameter("bugId"));
        String status = request.getParameter("status");
        String resolutionNotes = request.getParameter("resolutionNotes");

        bugService.updateBugStatus(bugId, status, resolutionNotes, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With")) || "true".equals(request.getParameter("ajax"))) {
            java.util.Map<String, Object> resp = new java.util.HashMap<>();
            resp.put("success", true);
            resp.put("status", status);
            resp.put("resolutionNotes", resolutionNotes != null ? resolutionNotes : "");
            com.devflow.util.JsonUtil.sendJsonResponse(response, resp);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/bugs?action=view&id=" + bugId);
    }

    private void handleAddComment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int bugId = Integer.parseInt(request.getParameter("bugId"));
        String comment = request.getParameter("comment");

        bugService.addComment(bugId, currentUser.getId(), comment);
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
        response.sendRedirect(request.getContextPath() + "/bugs?action=view&id=" + bugId);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int bugId = Integer.parseInt(request.getParameter("id"));

        bugService.deleteBug(bugId, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/bugs");
    }

    private void exportBugsCsv(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String projectIdStr = request.getParameter("projectId");
        Integer projectId = (projectIdStr != null && !projectIdStr.trim().isEmpty()) ? Integer.parseInt(projectIdStr.trim()) : null;
        List<com.devflow.model.Bug> bugs;
        if (projectId != null) {
            bugs = bugService.getBugsByProjectId(projectId);
        } else {
            bugs = bugService.searchBugs(null, null, null, null, null);
        }

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"devflow_defects_" + System.currentTimeMillis() + ".csv\"");

        try (java.io.PrintWriter writer = response.getWriter()) {
            writer.write('\ufeff'); // UTF-8 BOM for Excel
            writer.println("Bug ID,Bug Key,Title,Severity,Priority,Status,Project ID,Assigned To,Reported By,Created At");
            for (com.devflow.model.Bug b : bugs) {
                writer.printf("\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%d\",\"%s\",\"%s\",\"%s\"%n",
                        b.getId(),
                        escapeCsv(b.getBugKey()),
                        escapeCsv(b.getTitle()),
                        escapeCsv(b.getSeverity()),
                        escapeCsv(b.getPriority()),
                        escapeCsv(b.getStatus()),
                        b.getProjectId(),
                        escapeCsv(b.getAssigneeName()),
                        escapeCsv(b.getReporterName()),
                        b.getCreatedAt() != null ? b.getCreatedAt().toString() : "N/A"
                );
            }
        }
    }

    private String escapeCsv(String val) {
        if (val == null) return "";
        return val.replace("\"", "\"\"");
    }
}
