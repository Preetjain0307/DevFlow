<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="All Tasks" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Tasks & Work Items</h1>
                <p class="page-subtitle">Manage, assign, and monitor tasks across all active projects</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <c:if test="${projectId != null}">
                    <a href="${pageContext.request.contextPath}/tasks?action=kanban&projectId=${projectId}" class="btn btn-outline-secondary btn-sm">
                        <i class="bi bi-kanban me-1"></i> Kanban Board
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/tasks?action=create${projectId != null ? '&projectId='.concat(projectId) : ''}" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Create Task
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Filter Form -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/tasks" method="GET" class="row g-3 align-items-center">
                    <input type="hidden" name="action" value="list">
                    <div class="col-md-4">
                        <label for="filterProjectId" class="form-label small fw-bold mb-1">Filter by Project</label>
                        <select class="form-select form-select-sm" id="filterProjectId" name="projectId" onchange="this.form.submit()">
                            <option value="">-- All Accessible Projects --</option>
                            <c:forEach var="proj" items="${projects}">
                                <option value="${proj.projectId}" ${projectId == proj.projectId ? 'selected' : ''}>${proj.name} (${proj.projectKey})</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <!-- Task List Table -->
        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Key</th>
                            <th>Task Title</th>
                            <th>Type</th>
                            <th>Status</th>
                            <th>Priority</th>
                            <th>Assignee</th>
                            <th>Sprint</th>
                            <th>Due Date</th>
                            <th class="text-end">Actions</th>
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
                                <td><span class="badge badge-soft-primary">${task.taskType}</span></td>
                                <td>
                                    <span class="badge bg-${task.status == 'DONE' ? 'success' : (task.status == 'IN_PROGRESS' ? 'primary' : (task.status == 'IN_REVIEW' ? 'info text-dark' : 'warning text-dark'))}">
                                        ${task.status}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}">
                                        ${task.priority}
                                    </span>
                                </td>
                                <td>${task.assigneeName != null ? task.assigneeName : '<span class="text-muted">Unassigned</span>'}</td>
                                <td><small class="text-muted">${task.sprintName != null ? task.sprintName : 'Backlog'}</small></td>
                                <td><small class="text-muted">${task.dueDate != null ? task.dueDate : '-'}</small></td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="btn btn-sm btn-outline-secondary" title="View Details">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/tasks?action=edit&id=${task.taskId}" class="btn btn-sm btn-outline-primary" title="Edit Task">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty tasks}">
                            <tr>
                                <td colspan="9" class="text-center py-5 text-muted">
                                    <i class="bi bi-inbox fs-1 d-block mb-2 text-secondary"></i>
                                    No tasks found. Create a new task to get started!
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
