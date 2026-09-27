<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="My Profile & Permissions" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header d-flex justify-content-between align-items-center flex-wrap gap-2">
            <div>
                <h1 class="page-title mb-1">User Profile & RBAC Governance</h1>
                <p class="page-subtitle mb-0">Manage personal credentials, assigned deliverables, and role permission entitlements</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <span class="badge bg-primary px-3 py-2 fs-6">
                    <i class="bi bi-shield-check me-1"></i>Role: ${user.roleName}
                </span>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp"/>

        <div class="row g-4">
            <!-- Left Column: User Card & RBAC Matrix -->
            <div class="col-lg-4">
                <!-- User Overview Card -->
                <div class="df-card text-center p-4 mb-4 border-0 shadow-sm">
                    <div class="avatar-lg bg-primary text-white rounded-circle mx-auto mb-3 d-flex align-items-center justify-content-center shadow" style="width: 80px; height: 80px; font-size: 2.2rem; font-weight: 700;">
                        ${user.fullName.substring(0, 1).toUpperCase()}
                    </div>
                    <h5 class="fw-bold text-dark mb-1">${user.fullName}</h5>
                    <p class="text-muted small mb-2">@${user.username}</p>
                    <div class="d-flex justify-content-center gap-2 mb-3">
                        <span class="badge badge-soft-primary px-3 py-1">${user.roleName}</span>
                        <span class="badge bg-success-subtle text-success px-3 py-1 border border-success-subtle">${user.status}</span>
                    </div>

                    <div class="border-top pt-3 text-start small">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted"><i class="bi bi-envelope me-1"></i>Email:</span>
                            <span class="fw-semibold text-dark text-truncate ms-2" style="max-width: 190px;">${user.email}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted"><i class="bi bi-telephone me-1"></i>Phone:</span>
                            <span class="fw-semibold text-dark">${user.phone != null && !user.phone.isEmpty() ? user.phone : '-'}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted"><i class="bi bi-briefcase me-1"></i>Designation:</span>
                            <span class="fw-semibold text-dark">${user.designation != null && !user.designation.isEmpty() ? user.designation : '-'}</span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted"><i class="bi bi-calendar-check me-1"></i>Member Since:</span>
                            <span class="fw-semibold text-dark">${user.createdAt != null ? user.createdAt.toString().substring(0, 10) : 'Active'}</span>
                        </div>
                    </div>
                </div>

                <!-- Visual RBAC Permission Matrix Card (Feature 3.3) -->
                <div class="df-card border-0 shadow-sm mb-4">
                    <div class="df-card-header bg-light py-3 d-flex justify-content-between align-items-center">
                        <h6 class="df-card-title mb-0 fw-bold">
                            <i class="bi bi-shield-lock-fill text-primary me-2"></i>RBAC Permission Matrix
                        </h6>
                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle">${user.roleName}</span>
                    </div>
                    <div class="df-card-body p-3">
                        <p class="small text-muted mb-3">
                            Platform privileges enforced via <code>AuthenticationFilter</code> and <code>RoleAuthorizationFilter</code>.
                        </p>

                        <ul class="list-group list-group-flush small">
                            <!-- 1. Platform Governance -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Platform & User Governance</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Manage accounts, roles, audit logs</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 2. Database & Pool Telemetry -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Database & HikariCP Telemetry</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Pool metrics, connection monitor</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'FACULTY'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 3. Project Creation & Archival -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Project & Milestone Creation</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Initialize roadmaps and repositories</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'PROJECT_MANAGER'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 4. Sprint & Backlog Allocation -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Sprint Planning & Assignment</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Allocate tasks and manage backlogs</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'PROJECT_MANAGER'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 5. Kanban Drag-and-Drop Execution -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Kanban Board Interaction</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Drag & drop cards across TODO ➔ DONE</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'PROJECT_MANAGER' || user.roleName == 'DEVELOPER'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 6. Defect Logging & Verification -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Defect Logging & Triage</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">File bug tickets and verify fixes</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'TESTER' || user.roleName == 'PROJECT_MANAGER' || user.roleName == 'DEVELOPER'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 7. Video Meetings & AI Minutes -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Jitsi Meet & AI Summaries</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Host calls and generate meeting NLP</span>
                                </div>
                                <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                            </li>

                            <!-- 8. Academic Proposal Review -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Faculty Proposal Review</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Approve, reject, or request changes</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'FACULTY' || user.roleName == 'ADMIN'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>

                            <!-- 9. Executive Reports & CSV Export -->
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2">
                                <div>
                                    <div class="fw-semibold">Executive Reports & CSV Export</div>
                                    <span class="text-muted" style="font-size: 0.75rem;">Printable PDF reports and data dumps</span>
                                </div>
                                <c:choose>
                                    <c:when test="${user.roleName == 'ADMIN' || user.roleName == 'PROJECT_MANAGER' || user.roleName == 'FACULTY'}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle-fill me-1"></i>Granted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-light text-muted border"><i class="bi bi-dash-circle me-1"></i>Restricted</span>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Right Column: Deliverables, Forms, and Audit Trail -->
            <div class="col-lg-8">
                <!-- Deliverables & Active Assigned Work Items (Feature 3.3) -->
                <div class="df-card mb-4 border-0 shadow-sm">
                    <div class="df-card-header bg-light py-2 px-3">
                        <ul class="nav nav-tabs card-header-tabs" id="deliverablesTabs" role="tablist">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link active fw-semibold small" id="tasks-tab" data-bs-toggle="tab" data-bs-target="#tasksTabPane" type="button" role="tab" aria-selected="true">
                                    <i class="bi bi-check2-square me-1 text-primary"></i>My Assigned Tasks
                                    <span class="badge bg-primary text-white ms-1">${userTasks.size()}</span>
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link fw-semibold small" id="bugs-tab" data-bs-toggle="tab" data-bs-target="#bugsTabPane" type="button" role="tab" aria-selected="false">
                                    <i class="bi bi-bug me-1 text-danger"></i>Assigned Defects
                                    <span class="badge bg-danger text-white ms-1">${userBugs.size()}</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                    <div class="df-card-body p-3">
                        <div class="tab-content" id="deliverablesTabContent">
                            <!-- Tasks Pane -->
                            <div class="tab-pane fade show active" id="tasksTabPane" role="tabpanel" aria-labelledby="tasks-tab">
                                <c:choose>
                                    <c:when test="${not empty userTasks}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0 small">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>Key</th>
                                                        <th>Title</th>
                                                        <th>Priority</th>
                                                        <th>Status</th>
                                                        <th class="text-end">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="t" items="${userTasks}">
                                                        <tr>
                                                            <td><span class="badge bg-light text-dark border">${t.taskKey}</span></td>
                                                            <td class="fw-semibold text-truncate" style="max-width: 250px;">
                                                                <a href="${pageContext.request.contextPath}/tasks?action=view&id=${t.id}" class="text-decoration-none text-dark">
                                                                    ${t.title}
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <span class="badge bg-${t.priority == 'CRITICAL' ? 'danger' : (t.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}">
                                                                    ${t.priority}
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="badge bg-${t.status == 'DONE' ? 'success' : (t.status == 'IN_PROGRESS' ? 'primary' : 'secondary')}">
                                                                    ${t.status}
                                                                </span>
                                                            </td>
                                                            <td class="text-end">
                                                                <a href="${pageContext.request.contextPath}/tasks?action=view&id=${t.id}" class="btn btn-sm btn-outline-primary py-0 px-2">
                                                                    View
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-4 text-muted">
                                            <i class="bi bi-clipboard2-check fs-2 d-block mb-2 text-secondary"></i>
                                            <p class="small mb-0">No active tasks assigned to your account.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <!-- Bugs Pane -->
                            <div class="tab-pane fade" id="bugsTabPane" role="tabpanel" aria-labelledby="bugs-tab">
                                <c:choose>
                                    <c:when test="${not empty userBugs}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0 small">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>Key</th>
                                                        <th>Title</th>
                                                        <th>Severity</th>
                                                        <th>Status</th>
                                                        <th class="text-end">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="b" items="${userBugs}">
                                                        <tr>
                                                            <td><span class="badge bg-danger-subtle text-danger border border-danger-subtle">${b.bugKey}</span></td>
                                                            <td class="fw-semibold text-truncate" style="max-width: 250px;">
                                                                <a href="${pageContext.request.contextPath}/bugs?action=view&id=${b.id}" class="text-decoration-none text-dark">
                                                                    ${b.title}
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <span class="badge bg-${b.severity == 'BLOCKER' || b.severity == 'CRITICAL' ? 'danger' : 'warning text-dark'}">
                                                                    ${b.severity}
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="badge bg-secondary">${b.status}</span>
                                                            </td>
                                                            <td class="text-end">
                                                                <a href="${pageContext.request.contextPath}/bugs?action=view&id=${b.id}" class="btn btn-sm btn-outline-danger py-0 px-2">
                                                                    View
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-4 text-muted">
                                            <i class="bi bi-shield-check fs-2 d-block mb-2 text-success"></i>
                                            <p class="small mb-0">Zero assigned defects! System is running cleanly.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activity Audit Trail (Feature 3.3) -->
                <div class="df-card mb-4 border-0 shadow-sm">
                    <div class="df-card-header bg-light py-3 d-flex justify-content-between align-items-center">
                        <h6 class="df-card-title mb-0 fw-bold">
                            <i class="bi bi-clock-history text-secondary me-2"></i>My Recent Activity Audit Trail
                        </h6>
                        <span class="badge bg-light text-dark border">${recentAuditLogs.size()} Events</span>
                    </div>
                    <div class="df-card-body p-3">
                        <c:choose>
                            <c:when test="${not empty recentAuditLogs}">
                                <div class="table-responsive" style="max-height: 280px; overflow-y: auto;">
                                    <table class="table table-sm table-hover align-middle mb-0 small">
                                        <thead class="table-light sticky-top">
                                            <tr>
                                                <th>Action</th>
                                                <th>Details</th>
                                                <th>IP Address</th>
                                                <th>Timestamp</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="log" items="${recentAuditLogs}">
                                                <tr>
                                                    <td>
                                                        <span class="badge bg-${log.action.contains('LOGIN') ? 'info text-dark' : (log.action.contains('CREATE') ? 'success' : (log.action.contains('DELETE') ? 'danger' : 'secondary'))}">
                                                            ${log.action}
                                                        </span>
                                                    </td>
                                                    <td class="text-truncate" style="max-width: 280px;" title="<c:out value='${log.details}'/>">
                                                        <c:out value="${log.details}"/>
                                                    </td>
                                                    <td class="text-muted font-monospace" style="font-size: 0.75rem;">${log.ipAddress}</td>
                                                    <td class="text-muted small">${log.createdAt}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-3 text-muted small">
                                    No recorded activities yet for this user session.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Personal Details & Credentials Settings Card -->
                <div class="row g-4">
                    <!-- Edit Profile Details -->
                    <div class="col-md-6">
                        <div class="df-card h-100 border-0 shadow-sm">
                            <div class="df-card-header bg-light py-3">
                                <h6 class="df-card-title mb-0 fw-bold"><i class="bi bi-person-lines-fill text-primary me-2"></i>Personal Details</h6>
                            </div>
                            <div class="df-card-body p-3">
                                <form action="${pageContext.request.contextPath}/profile" method="POST">
                                    <div class="mb-2">
                                        <label class="form-label small fw-semibold">Full Name *</label>
                                        <input type="text" class="form-control form-control-sm" name="fullName" value="<c:out value='${user.fullName}'/>" required>
                                    </div>
                                    <div class="mb-2">
                                        <label class="form-label small fw-semibold">Phone Number</label>
                                        <input type="text" class="form-control form-control-sm" name="phone" value="<c:out value='${user.phone}'/>">
                                    </div>
                                    <div class="mb-2">
                                        <label class="form-label small fw-semibold">Designation / Role Title</label>
                                        <input type="text" class="form-control form-control-sm" name="designation" value="<c:out value='${user.designation}'/>">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label small fw-semibold">Short Bio</label>
                                        <textarea class="form-control form-control-sm" name="bio" rows="2"><c:out value='${user.bio}'/></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-df-primary btn-sm w-100 fw-semibold">
                                        <i class="bi bi-check-lg me-1"></i> Save Profile Changes
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Change Password Form -->
                    <div class="col-md-6">
                        <div class="df-card h-100 border-0 shadow-sm">
                            <div class="df-card-header bg-light py-3">
                                <h6 class="df-card-title mb-0 fw-bold"><i class="bi bi-shield-lock text-warning me-2"></i>Change Password</h6>
                            </div>
                            <div class="df-card-body p-3">
                                <c:if test="${not empty passwordError}">
                                    <div class="alert alert-danger small py-1 px-2 mb-2"><c:out value="${passwordError}"/></div>
                                </c:if>
                                <c:if test="${not empty passwordSuccess}">
                                    <div class="alert alert-success small py-1 px-2 mb-2"><c:out value="${passwordSuccess}"/></div>
                                </c:if>

                                <form action="${pageContext.request.contextPath}/change-password" method="POST">
                                    <div class="mb-2">
                                        <label class="form-label small fw-semibold">Current Password *</label>
                                        <input type="password" class="form-control form-control-sm" name="currentPassword" required>
                                    </div>
                                    <div class="mb-2">
                                        <label class="form-label small fw-semibold">New Password *</label>
                                        <input type="password" class="form-control form-control-sm" name="newPassword" placeholder="Min. 6 characters" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label small fw-semibold">Confirm Password *</label>
                                        <input type="password" class="form-control form-control-sm" name="confirmNewPassword" placeholder="Repeat new password" required>
                                    </div>
                                    <button type="submit" class="btn btn-outline-warning btn-sm w-100 fw-semibold">
                                        <i class="bi bi-key-fill me-1"></i> Update Password
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
