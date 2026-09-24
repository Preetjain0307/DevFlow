package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Idea;
import com.devflow.model.User;
import com.devflow.service.IdeaService;
import com.devflow.service.ProjectService;
import com.devflow.service.impl.IdeaServiceImpl;
import com.devflow.service.impl.ProjectServiceImpl;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "IdeaController", urlPatterns = {"/ideas", "/idea/vote", "/idea/faculty-decision", "/idea/comment"})
public class IdeaController extends HttpServlet {
    private IdeaService ideaService;
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        this.ideaService = new IdeaServiceImpl();
        this.projectService = new ProjectServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "view":
                viewIdea(request, response);
                break;
            case "submit":
                showSubmitForm(request, response);
                break;
            case "review":
                showFacultyReview(request, response);
                break;
            case "list":
            default:
                listIdeas(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/idea/vote".equals(servletPath)) {
            handleVote(request, response);
            return;
        }

        if ("/idea/faculty-decision".equals(servletPath)) {
            handleFacultyDecision(request, response);
            return;
        }

        if ("/idea/comment".equals(servletPath)) {
            handleAddComment(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "submit";

        switch (action) {
            case "submit":
                handleSubmit(request, response);
                break;
            case "edit":
                handleEdit(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ideas");
                break;
        }
    }

    private void listIdeas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String status = request.getParameter("status");
        String facultyStatus = request.getParameter("facultyStatus");
        String keyword = request.getParameter("keyword");

        Integer projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : null;

        List<Idea> ideas = ideaService.searchIdeas(projectId, status, facultyStatus, keyword);

        request.setAttribute("ideas", ideas);
        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);
        request.setAttribute("status", status);
        request.setAttribute("facultyStatus", facultyStatus);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/idea/list.jsp").forward(request, response);
    }

    private void viewIdea(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int id = Integer.parseInt(request.getParameter("id"));
        Idea idea = ideaService.getIdeaById(id, currentUser.getId());
        if (idea == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Proposal not found.");
            return;
        }

        request.setAttribute("idea", idea);
        request.getRequestDispatcher("/WEB-INF/views/idea/view.jsp").forward(request, response);
    }

    private void showSubmitForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        int projectId = (projectIdStr != null && !projectIdStr.isEmpty()) ? Integer.parseInt(projectIdStr) : 1;

        request.setAttribute("projects", projectService.getUserProjects(currentUser.getId(), currentUser.getRoleName()));
        request.setAttribute("selectedProjectId", projectId);

        request.getRequestDispatcher("/WEB-INF/views/idea/submit.jsp").forward(request, response);
    }

    private void showFacultyReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        if (!currentUser.isFaculty() && !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Faculty members can perform proposal reviews.");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            request.setAttribute("selectedIdea", ideaService.getIdeaById(id, currentUser.getId()));
        }

        request.setAttribute("pendingProposals", ideaService.getPendingFacultyReviewIdeas());
        request.getRequestDispatcher("/WEB-INF/views/idea/faculty_review.jsp").forward(request, response);
    }

    private void handleSubmit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        String projectIdStr = request.getParameter("projectId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String problemStatement = request.getParameter("problemStatement");
        String proposedSolution = request.getParameter("proposedSolution");
        String expectedBenefit = request.getParameter("expectedBenefit");
        String priority = request.getParameter("priority");
        String estEffortStr = request.getParameter("estimatedEffortDays");

        Idea idea = new Idea();
        idea.setProjectId(Integer.parseInt(projectIdStr));
        idea.setTitle(title);
        idea.setDescription(description);
        idea.setProblemStatement(problemStatement);
        idea.setProposedSolution(proposedSolution);
        idea.setExpectedBenefit(expectedBenefit);
        idea.setPriority(priority != null ? priority : "MEDIUM");
        int effort = 1;
        if (estEffortStr != null && !estEffortStr.isEmpty()) {
            try { effort = Integer.parseInt(estEffortStr); } catch (Exception ignored) {}
        }
        idea.setEstimatedEffortDays(effort);

        try {
            boolean submitted = ideaService.submitIdea(idea, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
            if (submitted) {
                response.sendRedirect(request.getContextPath() + "/ideas?action=view&id=" + idea.getId());
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        showSubmitForm(request, response);
    }

    private void handleVote(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int ideaId = Integer.parseInt(request.getParameter("ideaId"));
        String vote = request.getParameter("vote");
        String comment = request.getParameter("comment");

        try {
            ideaService.castVote(ideaId, currentUser.getId(), currentUser.getUsername(), vote, comment, request.getRemoteAddr());
        } catch (Exception e) {
            // error logged
        }

        response.sendRedirect(request.getContextPath() + "/ideas?action=view&id=" + ideaId);
    }

    private void handleFacultyDecision(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        if (!currentUser.isFaculty() && !currentUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Faculty members can perform proposal reviews.");
            return;
        }

        int ideaId = Integer.parseInt(request.getParameter("ideaId"));
        String decision = request.getParameter("decision"); // APPROVED, REJECTED, CHANGES_REQUESTED
        String rejectionReason = request.getParameter("rejectionReason");

        try {
            ideaService.processFacultyDecision(ideaId, decision, rejectionReason, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        } catch (Exception e) {
            // error logged
        }

        response.sendRedirect(request.getContextPath() + "/ideas?action=view&id=" + ideaId);
    }

    private void handleAddComment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int ideaId = Integer.parseInt(request.getParameter("ideaId"));
        String comment = request.getParameter("comment");

        ideaService.addComment(ideaId, currentUser.getId(), comment);
        response.sendRedirect(request.getContextPath() + "/ideas?action=view&id=" + ideaId);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        int ideaId = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String problemStatement = request.getParameter("problemStatement");
        String proposedSolution = request.getParameter("proposedSolution");
        String expectedBenefit = request.getParameter("expectedBenefit");
        String priority = request.getParameter("priority");
        String estEffortStr = request.getParameter("estimatedEffortDays");

        Idea idea = ideaService.getIdeaById(ideaId, currentUser.getId());
        idea.setTitle(title);
        idea.setDescription(description);
        idea.setProblemStatement(problemStatement);
        idea.setProposedSolution(proposedSolution);
        idea.setExpectedBenefit(expectedBenefit);
        idea.setPriority(priority);
        if (estEffortStr != null && !estEffortStr.isEmpty()) {
            try { idea.setEstimatedEffortDays(Integer.parseInt(estEffortStr)); } catch (Exception ignored) {}
        }

        ideaService.updateIdea(idea, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/ideas?action=view&id=" + ideaId);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);
        int ideaId = Integer.parseInt(request.getParameter("id"));

        ideaService.deleteIdea(ideaId, currentUser.getId(), currentUser.getUsername(), request.getRemoteAddr());
        response.sendRedirect(request.getContextPath() + "/ideas");
    }
}
