<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="${project.name} - Overview" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <div class="d-flex align-items-center gap-2">
                    <h1 class="page-title mb-0">${project.name}</h1>
                    <span class="badge bg-secondary">${project.projectKey}</span>
                    <span class="badge bg-${project.status == 'ACTIVE' ? 'success' : (project.status == 'COMPLETED' ? 'info' : 'warning')}">${project.status}</span>
                </div>
                <p class="page-subtitle mt-1">${project.description}</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a href="${pageContext.request.contextPath}/tasks?action=kanban&projectId=${project.projectId}" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-kanban me-1"></i> Kanban Board
                </a>
                <a href="${pageContext.request.contextPath}/projects?action=members&id=${project.projectId}" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-people me-1"></i> Team (${members.size()})
                </a>
                <c:if test="${sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.roleName == 'PROJECT_MANAGER' || sessionScope.currentUser.role.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'PROJECT_MANAGER'}">
                    <a href="${pageContext.request.contextPath}/projects?action=edit&id=${project.projectId}" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-pencil me-1"></i> Edit
                    </a>
                </c:if>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Stat Summary Row -->
        <div class="row g-3 mb-4">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon primary"><i class="bi bi-check2-square"></i></div>
                    <div>
                        <div class="stat-value">${tasks.size()}</div>
                        <div class="stat-label">Tasks Overview</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon info"><i class="bi bi-arrow-repeat"></i></div>
                    <div>
                        <div class="stat-value">${sprints.size()}</div>
                        <div class="stat-label">Active Sprints</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon danger"><i class="bi bi-bug"></i></div>
                    <div>
                        <div class="stat-value">${bugs.size()}</div>
                        <div class="stat-label">Open Bugs</div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon warning"><i class="bi bi-lightbulb"></i></div>
                    <div>
                        <div class="stat-value">${ideas.size()}</div>
                        <div class="stat-label">Ideas / Proposals</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <!-- Left Column: Tasks & Sprints -->
            <div class="col-lg-8">
                <!-- Tasks Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-list-task me-2 text-primary"></i>Project Tasks</h5>
                        <a href="${pageContext.request.contextPath}/tasks?action=create&projectId=${project.projectId}" class="btn btn-sm btn-df-primary">
                            <i class="bi bi-plus-lg me-1"></i> New Task
                        </a>
                    </div>
                    <div class="table-responsive">
                        <table class="df-table align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Key</th>
                                    <th>Title</th>
                                    <th>Status</th>
                                    <th>Priority</th>
                                    <th>Assignee</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="task" items="${tasks}">
                                    <tr>
                                        <td><span class="badge bg-secondary">${task.taskKey}</span></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="text-decoration-none fw-semibold text-dark">
                                                ${task.title}
                                            </a>
                                        </td>
                                        <td>
                                            <span class="badge bg-${task.status == 'DONE' ? 'success' : (task.status == 'IN_PROGRESS' ? 'primary' : 'warning')}">
                                                ${task.status}
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}">
                                                ${task.priority}
                                            </span>
                                        </td>
                                        <td>${task.assigneeName != null ? task.assigneeName : '<span class="text-muted">Unassigned</span>'}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="btn btn-sm btn-outline-secondary">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty tasks}">
                                    <tr><td colspan="6" class="text-center py-4 text-muted">No tasks created yet.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Milestones & Sprints -->
                <div class="df-card mb-4">
                    <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-flag me-2 text-info"></i>Milestones & Sprints</h5>
                        <a href="${pageContext.request.contextPath}/sprints?projectId=${project.projectId}" class="btn btn-sm btn-outline-info">
                            Manage Sprints
                        </a>
                    </div>
                    <div class="df-card-body p-3">
                        <h6 class="text-muted fw-bold">Active Sprints</h6>
                        <div class="row g-2 mb-3">
                            <c:forEach var="sprint" items="${sprints}">
                                <div class="col-md-6">
                                    <div class="p-3 border rounded bg-light">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold">${sprint.name}</span>
                                            <span class="badge bg-${sprint.status == 'ACTIVE' ? 'success' : 'secondary'}">${sprint.status}</span>
                                        </div>
                                        <p class="small text-muted mb-2">${sprint.goal}</p>
                                        <div class="small text-muted"><i class="bi bi-calendar me-1"></i> ${sprint.startDate} to ${sprint.endDate}</div>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty sprints}">
                                <p class="text-muted small">No sprints configured for this project.</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column: Team Members & Metadata -->
            <div class="col-lg-4">
                <!-- Team Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-people me-2 text-primary"></i>Team Members</h5>
                        <a href="${pageContext.request.contextPath}/projects?action=members&id=${project.projectId}" class="btn btn-sm btn-outline-primary">Manage</a>
                    </div>
                    <div class="df-card-body p-0">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="member" items="${members}">
                                <li class="list-group-item d-flex justify-content-between align-items-center py-3">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar bg-primary text-white rounded-circle me-2 d-flex align-items-center justify-content-center" style="width:36px;height:36px;font-size:14px;">
                                            ${member.userFullName != null ? member.userFullName.substring(0,1).toUpperCase() : 'U'}
                                        </div>
                                        <div>
                                            <div class="fw-semibold">${member.userFullName}</div>
                                            <small class="text-muted">${member.userEmail}</small>
                                        </div>
                                    </div>
                                    <span class="badge badge-soft-info">${member.projectRole}</span>
                                </li>
                            </c:forEach>
                            <c:if test="${empty members}">
                                <li class="list-group-item text-center py-3 text-muted">No members assigned yet.</li>
                            </c:if>
                        </ul>
                    </div>
                </div>

                <!-- Quick Project Details -->
                <div class="df-card">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-info-circle me-2 text-secondary"></i>Details</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <div class="mb-2"><strong class="text-muted">Start Date:</strong> <span>${project.startDate != null ? project.startDate : 'N/A'}</span></div>
                        <div class="mb-2"><strong class="text-muted">End Date:</strong> <span>${project.endDate != null ? project.endDate : 'N/A'}</span></div>
                        <div class="mb-2"><strong class="text-muted">Created At:</strong> <span>${project.createdAt}</span></div>
                        <hr>
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/github?action=overview&projectId=${project.projectId}" class="btn btn-outline-dark btn-sm">
                                <i class="bi bi-github me-1"></i> GitHub Integration
                            </a>
                            <a href="${pageContext.request.contextPath}/meetings?action=list&projectId=${project.projectId}" class="btn btn-outline-info btn-sm">
                                <i class="bi bi-camera-video me-1"></i> Video Meetings
                            </a>
                            <a href="${pageContext.request.contextPath}/documents?action=list&projectId=${project.projectId}" class="btn btn-outline-secondary btn-sm">
                                <i class="bi bi-folder me-1"></i> Documents
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
