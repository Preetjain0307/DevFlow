<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="top-navbar">
    <div class="d-flex align-items-center gap-3">
        <button class="btn btn-sm btn-outline-secondary d-md-none" id="sidebarToggleBtn" type="button">
            <i class="bi bi-list fs-5"></i>
        </button>

        <span class="fw-semibold text-dark d-none d-sm-inline-block">
            <i class="bi bi-laptop me-1 text-primary"></i> Developer Collaboration Portal
        </span>
    </div>

    <div class="d-flex align-items-center gap-3">
        <!-- Quick Action Shortcuts -->
        <div class="dropdown d-none d-sm-inline-block">
            <button class="btn btn-sm btn-df-primary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                <i class="bi bi-plus-lg me-1"></i> New
            </button>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/tasks?action=create"><i class="bi bi-check2-square me-2 text-primary"></i>New Task</a></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bugs?action=create"><i class="bi bi-bug me-2 text-danger"></i>Report Bug</a></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ideas?action=submit"><i class="bi bi-lightbulb me-2 text-warning"></i>Submit Proposal</a></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/meetings?action=schedule"><i class="bi bi-camera-video me-2 text-info"></i>Schedule Meeting</a></li>
                <c:if test="${sessionScope.currentUser.projectManager || sessionScope.currentUser.admin}">
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/projects?action=create"><i class="bi bi-folder-plus me-2 text-success"></i>Create Project</a></li>
                </c:if>
            </ul>
        </div>

        <!-- Live Role / Persona Switcher for Teacher Evaluation -->
        <div class="dropdown d-none d-md-inline-block">
            <button class="btn btn-sm btn-outline-primary dropdown-toggle d-flex align-items-center gap-1" type="button" data-bs-toggle="dropdown" title="Instant Persona Switcher for Academic Demo">
                <i class="bi bi-person-bounding-box text-primary"></i>
                <span class="d-none d-xl-inline">Persona:</span>
                <span class="badge bg-primary bg-opacity-10 text-primary border border-primary border-opacity-25">${sessionScope.currentUser.roleName}</span>
            </button>
            <ul class="dropdown-menu dropdown-menu-end shadow-lg" style="min-width: 260px;">
                <li><h6 class="dropdown-header d-flex justify-content-between align-items-center">
                    <span>Teacher Demo Switcher</span>
                    <span class="badge bg-light text-dark border">1-Click</span>
                </h6></li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 ${sessionScope.currentUser.admin ? 'active bg-light text-primary fw-semibold' : ''}" href="${pageContext.request.contextPath}/demo-login?role=admin">
                        <i class="bi bi-shield-lock-fill text-danger"></i>
                        <div>
                            <div>System Admin</div>
                            <small class="text-muted d-block">admin (Full Governance)</small>
                        </div>
                    </a>
                </li>
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 ${sessionScope.currentUser.projectManager ? 'active bg-light text-primary fw-semibold' : ''}" href="${pageContext.request.contextPath}/demo-login?role=pm">
                        <i class="bi bi-person-gear text-primary"></i>
                        <div>
                            <div>Project Manager</div>
                            <small class="text-muted d-block">pm_sarah (Sprints & Projects)</small>
                        </div>
                    </a>
                </li>
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 ${sessionScope.currentUser.developer ? 'active bg-light text-primary fw-semibold' : ''}" href="${pageContext.request.contextPath}/demo-login?role=dev">
                        <i class="bi bi-code-slash text-success"></i>
                        <div>
                            <div>Lead Developer</div>
                            <small class="text-muted d-block">dev_alex (Kanban & Code)</small>
                        </div>
                    </a>
                </li>
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 ${sessionScope.currentUser.tester ? 'active bg-light text-primary fw-semibold' : ''}" href="${pageContext.request.contextPath}/demo-login?role=tester">
                        <i class="bi bi-bug-fill text-warning"></i>
                        <div>
                            <div>QA Tester</div>
                            <small class="text-muted d-block">tester_mark (Bugs & Triage)</small>
                        </div>
                    </a>
                </li>
                <li>
                    <a class="dropdown-item d-flex align-items-center gap-2 ${sessionScope.currentUser.faculty ? 'active bg-light text-primary fw-semibold' : ''}" href="${pageContext.request.contextPath}/demo-login?role=faculty">
                        <i class="bi bi-award-fill" style="color: #6366f1;"></i>
                        <div>
                            <div>Faculty Mentor</div>
                            <small class="text-muted d-block">faculty_dr_alan (Proposals & Reviews)</small>
                        </div>
                    </a>
                </li>
            </ul>
        </div>

        <!-- Notifications Dropdown -->
        <a href="${pageContext.request.contextPath}/notifications" class="btn btn-light position-relative rounded-circle p-2 text-secondary" title="Notifications">
            <i class="bi bi-bell fs-5"></i>
            <span id="navNotificationBadge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger d-none" style="font-size: 0.65rem;">
                0
            </span>
        </a>

        <!-- User Profile Menu -->
        <div class="dropdown">
            <div class="nav-user-badge dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                <div class="avatar-sm">
                    ${sessionScope.currentUser.fullName.substring(0, 1).toUpperCase()}
                </div>
                <div class="d-none d-lg-block text-start">
                    <div class="fw-semibold text-dark" style="font-size: 0.875rem;">${sessionScope.currentUser.fullName}</div>
                    <div class="text-muted" style="font-size: 0.75rem;">${sessionScope.currentUser.roleName}</div>
                </div>
            </div>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                <li><h6 class="dropdown-header">Signed in as <strong>${sessionScope.currentUser.username}</strong></h6></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-person me-2"></i>My Profile</a></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/notifications"><i class="bi bi-bell me-2"></i>Notifications</a></li>
                <c:if test="${sessionScope.currentUser.admin}">
                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/settings"><i class="bi bi-sliders me-2"></i>System Settings</a></li>
                </c:if>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i>Sign Out</a></li>
            </ul>
        </div>
    </div>
</header>
