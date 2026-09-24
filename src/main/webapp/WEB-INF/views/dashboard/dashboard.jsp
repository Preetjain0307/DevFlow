<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Dashboard" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <!-- Dashboard Greeting -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Welcome back, ${sessionScope.currentUser.fullName}!</h1>
                <p class="page-subtitle">Here is the active progress across your projects and workspace.</p>
            </div>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/task/kanban" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-kanban me-1"></i> Kanban Board
                </a>
                <a href="${pageContext.request.contextPath}/tasks?action=create" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> New Task
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp"/>

        <!-- 4 Primary Metric Stat Cards -->
        <div class="row g-3 mb-4">
            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="stat-icon primary">
                        <i class="bi bi-folder-fill"></i>
                    </div>
                    <div>
                        <div class="stat-value">${stats.activeProjects}</div>
                        <div class="stat-label">Active Projects (${stats.totalProjects} total)</div>
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="stat-icon success">
                        <i class="bi bi-check2-all"></i>
                    </div>
                    <div>
                        <div class="stat-value">${stats.completedTasks} <span class="fs-6 text-muted font-monospace">/ ${stats.totalTasks}</span></div>
                        <div class="stat-label">Tasks Completed (${stats.inProgressTasks} in progress)</div>
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="stat-icon ${stats.criticalBugs > 0 ? 'danger' : 'warning'}">
                        <i class="bi bi-bug-fill"></i>
                    </div>
                    <div>
                        <div class="stat-value">${stats.openBugs}</div>
                        <div class="stat-label">Open Bugs (${stats.criticalBugs} Critical)</div>
                    </div>
                </div>
            </div>

            <div class="col-sm-6 col-xl-3">
                <div class="stat-card">
                    <div class="stat-icon info">
                        <i class="bi bi-lightbulb-fill"></i>
                    </div>
                    <div>
                        <div class="stat-value">${stats.pendingIdeas}</div>
                        <div class="stat-label">Pending Idea Proposals (${stats.approvedIdeas} approved)</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-4">
                <div class="df-card h-100 mb-0">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-pie-chart-fill text-primary"></i> Task Breakdown</h6>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center" style="height: 260px;">
                        <canvas id="taskStatusChart"></canvas>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="df-card h-100 mb-0">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-bar-chart-fill text-danger"></i> Bug Severity</h6>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center" style="height: 260px;">
                        <canvas id="bugSeverityChart"></canvas>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="df-card h-100 mb-0">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-lightbulb text-warning"></i> Idea Proposals</h6>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center" style="height: 260px;">
                        <canvas id="ideaStatusChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Role-Specific Custom Section -->
        <div class="row g-4">
            <!-- Left Column: Tasks / Projects / Audits -->
            <div class="col-lg-8">
                <!-- Developer View: My Assigned Tasks -->
                <c:if test="${sessionScope.currentUser.developer}">
                    <div class="df-card mb-4">
                        <div class="df-card-header">
                            <h6 class="df-card-title"><i class="bi bi-person-check-fill text-primary"></i> My Assigned Tasks</h6>
                            <a href="${pageContext.request.contextPath}/tasks?assignedTo=${sessionScope.currentUser.id}" class="small text-primary text-decoration-none">View All</a>
                        </div>
                        <div class="df-card-body p-0">
                            <div class="table-responsive">
                                <table class="table df-table">
                                    <thead>
                                        <tr>
                                            <th>Task</th>
                                            <th>Project</th>
                                            <th>Priority</th>
                                            <th>Status</th>
                                            <th>Due Date</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="task" items="${myAssignedTasks}">
                                            <tr>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.id}" class="fw-semibold text-dark text-decoration-none">
                                                        <c:out value="${task.title}"/>
                                                    </a>
                                                </td>
                                                <td><span class="badge bg-light text-dark">${task.projectKey}</span></td>
                                                <td>
                                                    <span class="badge ${task.priority == 'CRITICAL' ? 'bg-danger' : (task.priority == 'HIGH' ? 'bg-warning text-dark' : 'bg-secondary')}">
                                                        ${task.priority}
                                                    </span>
                                                </td>
                                                <td><span class="badge badge-soft-primary">${task.status}</span></td>
                                                <td class="small text-muted">${task.dueDate != null ? task.dueDate : '-'}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.id}" class="btn btn-sm btn-outline-secondary py-0">View</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty myAssignedTasks}">
                                            <tr><td colspan="6" class="text-center text-muted py-4">No tasks currently assigned to you.</td></tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Faculty / PM View: Proposals Ready for Review -->
                <c:if test="${sessionScope.currentUser.faculty || sessionScope.currentUser.projectManager}">
                    <div class="df-card mb-4">
                        <div class="df-card-header">
                            <h6 class="df-card-title"><i class="bi bi-award-fill text-warning"></i> Proposals Awaiting Review</h6>
                            <a href="${pageContext.request.contextPath}/ideas?action=review" class="small text-primary text-decoration-none">Review Center</a>
                        </div>
                        <div class="df-card-body p-0">
                            <div class="table-responsive">
                                <table class="table df-table">
                                    <thead>
                                        <tr>
                                            <th>Title</th>
                                            <th>Submitted By</th>
                                            <th>Project</th>
                                            <th>Votes</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="idea" items="${proposalsForReview != null ? proposalsForReview : pendingProposals}">
                                            <tr>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.id}" class="fw-semibold text-dark text-decoration-none">
                                                        <c:out value="${idea.title}"/>
                                                    </a>
                                                </td>
                                                <td><c:out value="${idea.submitterName}"/></td>
                                                <td><span class="badge bg-light text-dark">${idea.projectKey}</span></td>
                                                <td><span class="text-success fw-bold">${idea.yesVotes} YES</span> / <span class="text-danger">${idea.noVotes} NO</span></td>
                                                <td><span class="badge badge-soft-warning">${idea.status}</span></td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.id}" class="btn btn-sm btn-df-primary py-0">Review</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty proposalsForReview && empty pendingProposals}">
                                            <tr><td colspan="6" class="text-center text-muted py-4">No pending proposals awaiting review.</td></tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Admin View: Security Audit Trail -->
                <c:if test="${sessionScope.currentUser.admin}">
                    <div class="df-card mb-4">
                        <div class="df-card-header">
                            <h6 class="df-card-title"><i class="bi bi-shield-check text-primary"></i> Live Security Audit Trail</h6>
                            <a href="${pageContext.request.contextPath}/admin/audit-logs" class="small text-primary text-decoration-none">All Logs</a>
                        </div>
                        <div class="df-card-body p-0">
                            <div class="table-responsive">
                                <table class="table df-table">
                                    <thead>
                                        <tr>
                                            <th>Timestamp</th>
                                            <th>User</th>
                                            <th>Action</th>
                                            <th>Details</th>
                                            <th>IP</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="log" items="${recentAuditLogs}">
                                            <tr>
                                                <td class="small text-muted"><fmt:formatDate value="${log.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td class="fw-semibold">${log.username}</td>
                                                <td><span class="badge bg-light text-dark font-monospace">${log.action}</span></td>
                                                <td class="small text-truncate" style="max-width: 250px;"><c:out value="${log.details}"/></td>
                                                <td class="small text-muted">${log.ipAddress}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Projects Overview -->
                <div class="df-card">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-folder text-primary"></i> Active Projects</h6>
                        <a href="${pageContext.request.contextPath}/projects" class="small text-primary text-decoration-none">View All</a>
                    </div>
                    <div class="df-card-body p-0">
                        <div class="table-responsive">
                            <table class="table df-table">
                                <thead>
                                    <tr>
                                        <th>Project</th>
                                        <th>Status</th>
                                        <th>Manager</th>
                                        <th>Progress</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="proj" items="${recentProjects}">
                                        <tr>
                                            <td>
                                                <div class="fw-semibold text-dark"><c:out value="${proj.name}"/></div>
                                                <span class="badge bg-light text-muted font-monospace">${proj.projectKey}</span>
                                            </td>
                                            <td><span class="badge badge-soft-success">${proj.status}</span></td>
                                            <td class="small">${proj.managerName}</td>
                                            <td style="width: 150px;">
                                                <div class="d-flex align-items-center gap-2">
                                                    <div class="progress flex-grow-1" style="height: 6px;">
                                                        <div class="progress-bar bg-primary" role="progressbar" style="width: ${proj.progressPercentage}%"></div>
                                                    </div>
                                                    <span class="small text-muted">${proj.progressPercentage}%</span>
                                                </div>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/projects?action=view&id=${proj.id}" class="btn btn-sm btn-outline-primary py-0">Open</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column: Meetings & GitHub Activity -->
            <div class="col-lg-4">
                <!-- Upcoming Meetings Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-camera-video text-info"></i> Upcoming Meetings</h6>
                        <a href="${pageContext.request.contextPath}/meetings" class="small text-primary text-decoration-none">Schedule</a>
                    </div>
                    <div class="df-card-body p-0">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="m" items="${upcomingMeetings}">
                                <li class="list-group-item p-3">
                                    <div class="d-flex justify-content-between align-items-start mb-1">
                                        <h6 class="fw-semibold text-dark mb-0 small"><c:out value="${m.title}"/></h6>
                                        <span class="badge badge-soft-info">${m.startTime}</span>
                                    </div>
                                    <div class="small text-muted mb-2">
                                        <i class="bi bi-calendar3 me-1"></i> ${m.meetingDate} | <span class="badge bg-light text-dark">${m.projectKey}</span>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/meetings?action=join&id=${m.id}" target="_blank" class="btn btn-sm btn-success py-0 px-2" style="font-size: 0.75rem;">
                                            <i class="bi bi-camera-video me-1"></i> Join Jitsi
                                        </a>
                                        <a href="${pageContext.request.contextPath}/meetings?action=view&id=${m.id}" class="btn btn-sm btn-outline-secondary py-0 px-2" style="font-size: 0.75rem;">
                                            Notes & AI
                                        </a>
                                    </div>
                                </li>
                            </c:forEach>
                            <c:if test="${empty upcomingMeetings}">
                                <li class="list-group-item text-center text-muted py-4 small">No meetings currently scheduled.</li>
                            </c:if>
                        </ul>
                    </div>
                </div>

                <!-- GitHub Repository Stream -->
                <div class="df-card">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-github text-dark"></i> GitHub Activity</h6>
                        <a href="${pageContext.request.contextPath}/github" class="small text-primary text-decoration-none">Repo Hub</a>
                    </div>
                    <div class="df-card-body p-0">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="act" items="${recentActivity}">
                                <li class="list-group-item p-3">
                                    <div class="d-flex align-items-center gap-2 mb-1">
                                        <span class="badge bg-dark font-monospace">${act.eventType}</span>
                                        <span class="fw-semibold small text-dark">${act.authorName}</span>
                                    </div>
                                    <p class="small text-muted mb-1 text-truncate"><c:out value="${act.message}"/></p>
                                    <a href="${act.eventUrl}" target="_blank" class="small text-primary text-decoration-none">
                                        View on GitHub <i class="bi bi-box-arrow-up-right" style="font-size: 0.7rem;"></i>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- Chart initialization -->
<script src="${pageContext.request.contextPath}/js/charts.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const taskData = {
            TODO: ${stats.todoTasks},
            IN_PROGRESS: ${stats.inProgressTasks},
            IN_REVIEW: ${stats.inReviewTasks},
            COMPLETED: ${stats.completedTasks}
        };
        initTaskStatusChart('taskStatusChart', taskData);

        const bugData = {
            LOW: ${stats.bugSeverityDistribution.LOW != null ? stats.bugSeverityDistribution.LOW : 0},
            MEDIUM: ${stats.bugSeverityDistribution.MEDIUM != null ? stats.bugSeverityDistribution.MEDIUM : 0},
            HIGH: ${stats.bugSeverityDistribution.HIGH != null ? stats.bugSeverityDistribution.HIGH : 0},
            CRITICAL: ${stats.criticalBugs}
        };
        initBugSeverityChart('bugSeverityChart', bugData);

        const ideaData = {
            IN_VOTING: ${stats.ideaStatusDistribution.IN_VOTING != null ? stats.ideaStatusDistribution.IN_VOTING : 0},
            PENDING_FACULTY: ${stats.ideaStatusDistribution.PENDING_FACULTY != null ? stats.ideaStatusDistribution.PENDING_FACULTY : 0},
            APPROVED: ${stats.approvedIdeas},
            REJECTED: ${stats.ideaStatusDistribution.REJECTED != null ? stats.ideaStatusDistribution.REJECTED : 0},
            CHANGES_REQUESTED: ${stats.ideaStatusDistribution.CHANGES_REQUESTED != null ? stats.ideaStatusDistribution.CHANGES_REQUESTED : 0}
        };
        initIdeaStatusChart('ideaStatusChart', ideaData);
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
