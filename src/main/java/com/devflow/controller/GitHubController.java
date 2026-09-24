package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.integration.GitHubService;
import com.devflow.integration.GitHubServiceImpl;
import com.devflow.model.GitHubRepo;
import com.devflow.model.Project;
import com.devflow.model.User;
import com.devflow.service.ProjectService;
import com.devflow.service.impl.ProjectServiceImpl;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GitHubController", urlPatterns = {"/github", "/github/sync", "/github/connect"})
public class GitHubController extends HttpServlet {
    private GitHubService gitHubService;
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        this.gitHubService = new GitHubServiceImpl();
        this.projectService = new ProjectServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/github/sync".equals(servletPath)) {
            int projectId = Integer.parseInt(request.getParameter("projectId"));
            gitHubService.syncRepository(projectId);
            response.sendRedirect(request.getContextPath() + "/github?projectId=" + projectId + "&synced=true");
            return;
        }

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

        String view = request.getParameter("view");
        if (view == null) view = "overview";

        GitHubRepo repo = gitHubService.getRepoDetails(projectId);

        request.setAttribute("currentProject", projectService.getProjectById(projectId));
        request.setAttribute("projects", userProjects);
        request.setAttribute("projectId", projectId);
        request.setAttribute("repo", repo);
        request.setAttribute("currentView", view);

        if (repo != null) {
            switch (view) {
                case "commits":
                    request.setAttribute("commits", gitHubService.getLiveCommits(projectId, 25));
                    request.getRequestDispatcher("/WEB-INF/views/github/commits.jsp").forward(request, response);
                    return;
                case "pulls":
                    request.setAttribute("pulls", gitHubService.getLivePullRequests(projectId, 20));
                    request.getRequestDispatcher("/WEB-INF/views/github/pulls.jsp").forward(request, response);
                    return;
                case "issues":
                    request.setAttribute("issues", gitHubService.getLiveIssues(projectId, 20));
                    request.getRequestDispatcher("/WEB-INF/views/github/issues.jsp").forward(request, response);
                    return;
                case "overview":
                default:
                    request.setAttribute("recentActivity", gitHubService.getRecentActivity(projectId, 10));
                    request.setAttribute("recentCommits", gitHubService.getLiveCommits(projectId, 5));
                    request.setAttribute("recentPulls", gitHubService.getLivePullRequests(projectId, 5));
                    request.getRequestDispatcher("/WEB-INF/views/github/overview.jsp").forward(request, response);
                    return;
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/views/github/connect.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        String repoOwner = request.getParameter("repoOwner");
        String repoName = request.getParameter("repoName");
        String defaultBranch = request.getParameter("defaultBranch");

        gitHubService.connectRepository(projectId, repoOwner, repoName, defaultBranch);
        response.sendRedirect(request.getContextPath() + "/github?projectId=" + projectId);
    }
}
