package com.devflow.controller;

import com.devflow.model.Milestone;
import com.devflow.service.MilestoneService;
import com.devflow.service.ProjectService;
import com.devflow.service.impl.MilestoneServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import com.devflow.util.DateUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MilestoneController", urlPatterns = {"/milestones"})
public class MilestoneController extends HttpServlet {
    private MilestoneService milestoneService;
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        this.milestoneService = new MilestoneServiceImpl();
        this.projectService = new ProjectServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int projectId = Integer.parseInt(request.getParameter("projectId"));

        if ("create".equals(action)) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");

            Milestone m = new Milestone();
            m.setProjectId(projectId);
            m.setTitle(title);
            m.setDescription(description);
            m.setDueDate(DateUtil.parseDate(dueDate));
            m.setStatus("OPEN");

            milestoneService.createMilestone(m);
        } else if ("status-update".equals(action)) {
            int milestoneId = Integer.parseInt(request.getParameter("milestoneId"));
            String status = request.getParameter("status");
            milestoneService.updateStatus(milestoneId, status);
        } else if ("delete".equals(action)) {
            int milestoneId = Integer.parseInt(request.getParameter("milestoneId"));
            milestoneService.deleteMilestone(milestoneId);
        }

        response.sendRedirect(request.getContextPath() + "/projects?action=view&id=" + projectId);
    }
}
