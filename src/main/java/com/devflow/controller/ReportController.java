package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.dao.BugDAO;
import com.devflow.dao.IdeaDAO;
import com.devflow.dao.TaskDAO;
import com.devflow.dao.impl.BugDAOImpl;
import com.devflow.dao.impl.IdeaDAOImpl;
import com.devflow.dao.impl.TaskDAOImpl;
import com.devflow.model.Project;
import com.devflow.model.User;
import com.devflow.service.ProjectService;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.util.JsonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ReportController", urlPatterns = {"/reports", "/reports/data"})
public class ReportController extends HttpServlet {
    private ProjectService projectService;
    private TaskDAO taskDAO;
    private BugDAO bugDAO;
    private IdeaDAO ideaDAO;

    @Override
    public void init() throws ServletException {
        this.projectService = new ProjectServiceImpl();
        this.taskDAO = new TaskDAOImpl();
        this.bugDAO = new BugDAOImpl();
        this.ideaDAO = new IdeaDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        List<Project> userProjects = projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName());
        
        int projectId = 1;
        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            projectId = Integer.parseInt(projectIdStr);
        } else if (!userProjects.isEmpty()) {
            projectId = userProjects.get(0).getId();
        }

        if ("/reports/data".equals(servletPath)) {
            Map<String, Object> data = new HashMap<>();
            data.put("tasks", taskDAO.getStatusDistributionByProject(projectId));
            data.put("bugs", bugDAO.getSeverityDistributionByProject(projectId));
            data.put("ideas", ideaDAO.getStatusDistributionByProject(projectId));
            JsonUtil.sendJsonResponse(response, data);
            return;
        }

        request.setAttribute("selectedProject", projectService.getProjectById(projectId));
        request.setAttribute("projects", userProjects);
        request.setAttribute("projectId", projectId);
        request.setAttribute("taskStats", taskDAO.getStatusDistributionByProject(projectId));
        request.setAttribute("bugStats", bugDAO.getSeverityDistributionByProject(projectId));
        request.setAttribute("ideaStats", ideaDAO.getStatusDistributionByProject(projectId));

        request.getRequestDispatcher("/WEB-INF/views/report/index.jsp").forward(request, response);
    }
}
