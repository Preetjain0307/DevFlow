<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard" class="sidebar-brand">
        <i class="bi bi-cpu-fill"></i>
        <span>DEVFLOW</span>
    </a>

    <div class="sidebar-nav">
        <div class="nav-category">Workspace</div>
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-link ${pageContext.request.servletPath == '/dashboard' ? 'active' : ''}">
            <i class="bi bi-grid-1x2-fill"></i>
            <span>Dashboard</span>
        </a>
        <a href="${pageContext.request.contextPath}/projects" class="nav-link ${pageContext.request.servletPath == '/projects' ? 'active' : ''}">
            <i class="bi bi-folder-fill"></i>
            <span>Projects</span>
        </a>

        <div class="nav-category">Development</div>
        <a href="${pageContext.request.contextPath}/task/kanban" class="nav-link ${pageContext.request.servletPath == '/task/kanban' ? 'active' : ''}">
            <i class="bi bi-kanban-fill"></i>
            <span>Kanban Board</span>
        </a>
        <a href="${pageContext.request.contextPath}/tasks" class="nav-link ${pageContext.request.servletPath == '/tasks' ? 'active' : ''}">
            <i class="bi bi-check2-square"></i>
            <span>All Tasks</span>
        </a>
        <a href="${pageContext.request.contextPath}/sprints" class="nav-link ${pageContext.request.servletPath == '/sprints' ? 'active' : ''}">
            <i class="bi bi-arrow-repeat"></i>
            <span>Sprints</span>
        </a>
        <a href="${pageContext.request.contextPath}/bugs" class="nav-link ${pageContext.request.servletPath == '/bugs' ? 'active' : ''}">
            <i class="bi bi-bug-fill"></i>
            <span>Bug Tracker</span>
        </a>

        <div class="nav-category">Collaboration</div>
        <a href="${pageContext.request.contextPath}/ideas" class="nav-link ${pageContext.request.servletPath == '/ideas' ? 'active' : ''}">
            <i class="bi bi-lightbulb-fill"></i>
            <span>Idea Proposals</span>
        </a>
        <c:if test="${sessionScope.currentUser.faculty || sessionScope.currentUser.admin}">
            <a href="${pageContext.request.contextPath}/ideas?action=review" class="nav-link ${param.action == 'review' ? 'active' : ''}">
                <i class="bi bi-award-fill"></i>
                <span>Faculty Review</span>
            </a>
        </c:if>
        <a href="${pageContext.request.contextPath}/meetings" class="nav-link ${pageContext.request.servletPath == '/meetings' ? 'active' : ''}">
            <i class="bi bi-camera-video-fill"></i>
            <span>Meetings & AI</span>
        </a>
        <a href="${pageContext.request.contextPath}/documents" class="nav-link ${pageContext.request.servletPath == '/documents' ? 'active' : ''}">
            <i class="bi bi-file-earmark-text-fill"></i>
            <span>Documents</span>
        </a>
        <a href="${pageContext.request.contextPath}/github" class="nav-link ${pageContext.request.servletPath == '/github' ? 'active' : ''}">
            <i class="bi bi-github"></i>
            <span>GitHub Monitoring</span>
        </a>
        <a href="${pageContext.request.contextPath}/reports" class="nav-link ${pageContext.request.servletPath == '/reports' ? 'active' : ''}">
            <i class="bi bi-bar-chart-fill"></i>
            <span>Reports & Analytics</span>
        </a>

        <c:if test="${sessionScope.currentUser.admin}">
            <div class="nav-category">Administration</div>
            <a href="${pageContext.request.contextPath}/admin/users" class="nav-link ${pageContext.request.servletPath == '/admin/users' ? 'active' : ''}">
                <i class="bi bi-people-fill"></i>
                <span>User Management</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/audit-logs" class="nav-link ${pageContext.request.servletPath == '/admin/audit-logs' ? 'active' : ''}">
                <i class="bi bi-shield-lock-fill"></i>
                <span>Audit Logs</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/settings" class="nav-link ${pageContext.request.servletPath == '/admin/settings' ? 'active' : ''}">
                <i class="bi bi-gear-fill"></i>
                <span>System Settings</span>
            </a>
        </c:if>
    </div>

    <div class="sidebar-footer">
        <div class="d-flex align-items-center justify-content-between text-muted small">
            <div>
                <span class="d-block text-white fw-semibold">${sessionScope.currentUser.fullName}</span>
                <span class="badge badge-soft-primary" style="font-size: 0.65rem;">${sessionScope.currentUser.roleName}</span>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="text-secondary hover-text-white" title="Logout" data-bs-toggle="tooltip">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>
</aside>
