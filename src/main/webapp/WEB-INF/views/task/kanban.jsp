<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Kanban Board - ${project.name}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Interactive Kanban Board</h1>
                <p class="page-subtitle">Project: <strong>${project.name}</strong> (${project.projectKey})</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <form action="${pageContext.request.contextPath}/tasks" method="GET" class="d-inline-flex gap-2">
                    <input type="hidden" name="action" value="kanban">
                    <select class="form-select form-select-sm" name="projectId" onchange="this.form.submit()">
                        <c:forEach var="proj" items="${projects}">
                            <option value="${proj.projectId}" ${project.projectId == proj.projectId ? 'selected' : ''}>${proj.name}</option>
                        </c:forEach>
                    </select>
                </form>
                <a href="${pageContext.request.contextPath}/tasks?action=create&projectId=${project.projectId}" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> Add Task
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Kanban Board Grid -->
        <div class="kanban-board d-flex gap-3 overflow-auto pb-4" style="min-height: 70vh;">
            <!-- TODO Column -->
            <div class="kanban-column flex-fill" style="min-width: 280px; max-width: 340px;">
                <div class="kanban-header d-flex justify-content-between align-items-center mb-3 p-2 bg-light rounded border-start border-4 border-warning">
                    <span class="fw-bold">To Do</span>
                    <span class="badge bg-secondary rounded-pill" id="count-TODO">${todoTasks.size()}</span>
                </div>
                <div class="kanban-droppable p-2 bg-light rounded shadow-sm" data-status="TODO" style="min-height: 500px; background-color: #f8fafc;">
                    <c:forEach var="task" items="${todoTasks}">
                        <div class="kanban-card card mb-2 shadow-sm border-0" draggable="true" data-task-id="${task.taskId}">
                            <div class="card-body p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-secondary" style="font-size: 11px;">${task.taskKey}</span>
                                    <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}" style="font-size: 10px;">${task.priority}</span>
                                </div>
                                <h6 class="card-title mb-2 fs-6">
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="text-dark text-decoration-none">${task.title}</a>
                                </h6>
                                <div class="d-flex justify-content-between align-items-center mt-3 pt-2 border-top text-muted small">
                                    <span><i class="bi bi-person me-1"></i>${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</span>
                                    <c:if test="${task.storyPoints > 0}">
                                        <span class="badge bg-light text-dark border">${task.storyPoints} pts</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- IN_PROGRESS Column -->
            <div class="kanban-column flex-fill" style="min-width: 280px; max-width: 340px;">
                <div class="kanban-header d-flex justify-content-between align-items-center mb-3 p-2 bg-light rounded border-start border-4 border-primary">
                    <span class="fw-bold">In Progress</span>
                    <span class="badge bg-primary rounded-pill" id="count-IN_PROGRESS">${inProgressTasks.size()}</span>
                </div>
                <div class="kanban-droppable p-2 bg-light rounded shadow-sm" data-status="IN_PROGRESS" style="min-height: 500px; background-color: #f8fafc;">
                    <c:forEach var="task" items="${inProgressTasks}">
                        <div class="kanban-card card mb-2 shadow-sm border-0" draggable="true" data-task-id="${task.taskId}">
                            <div class="card-body p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-secondary" style="font-size: 11px;">${task.taskKey}</span>
                                    <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}" style="font-size: 10px;">${task.priority}</span>
                                </div>
                                <h6 class="card-title mb-2 fs-6">
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="text-dark text-decoration-none">${task.title}</a>
                                </h6>
                                <div class="d-flex justify-content-between align-items-center mt-3 pt-2 border-top text-muted small">
                                    <span><i class="bi bi-person me-1"></i>${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</span>
                                    <c:if test="${task.storyPoints > 0}">
                                        <span class="badge bg-light text-dark border">${task.storyPoints} pts</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- IN_REVIEW Column -->
            <div class="kanban-column flex-fill" style="min-width: 280px; max-width: 340px;">
                <div class="kanban-header d-flex justify-content-between align-items-center mb-3 p-2 bg-light rounded border-start border-4 border-info">
                    <span class="fw-bold">In Review</span>
                    <span class="badge bg-info text-dark rounded-pill" id="count-IN_REVIEW">${inReviewTasks.size()}</span>
                </div>
                <div class="kanban-droppable p-2 bg-light rounded shadow-sm" data-status="IN_REVIEW" style="min-height: 500px; background-color: #f8fafc;">
                    <c:forEach var="task" items="${inReviewTasks}">
                        <div class="kanban-card card mb-2 shadow-sm border-0" draggable="true" data-task-id="${task.taskId}">
                            <div class="card-body p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-secondary" style="font-size: 11px;">${task.taskKey}</span>
                                    <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}" style="font-size: 10px;">${task.priority}</span>
                                </div>
                                <h6 class="card-title mb-2 fs-6">
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="text-dark text-decoration-none">${task.title}</a>
                                </h6>
                                <div class="d-flex justify-content-between align-items-center mt-3 pt-2 border-top text-muted small">
                                    <span><i class="bi bi-person me-1"></i>${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</span>
                                    <c:if test="${task.storyPoints > 0}">
                                        <span class="badge bg-light text-dark border">${task.storyPoints} pts</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- DONE Column -->
            <div class="kanban-column flex-fill" style="min-width: 280px; max-width: 340px;">
                <div class="kanban-header d-flex justify-content-between align-items-center mb-3 p-2 bg-light rounded border-start border-4 border-success">
                    <span class="fw-bold">Done</span>
                    <span class="badge bg-success rounded-pill" id="count-DONE">${doneTasks.size()}</span>
                </div>
                <div class="kanban-droppable p-2 bg-light rounded shadow-sm" data-status="DONE" style="min-height: 500px; background-color: #f8fafc;">
                    <c:forEach var="task" items="${doneTasks}">
                        <div class="kanban-card card mb-2 shadow-sm border-0 opacity-75" draggable="true" data-task-id="${task.taskId}">
                            <div class="card-body p-3">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="badge bg-secondary" style="font-size: 11px;">${task.taskKey}</span>
                                    <span class="badge bg-success" style="font-size: 10px;">DONE</span>
                                </div>
                                <h6 class="card-title mb-2 fs-6 text-decoration-line-through">
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="text-muted text-decoration-line-through">${task.title}</a>
                                </h6>
                                <div class="d-flex justify-content-between align-items-center mt-3 pt-2 border-top text-muted small">
                                    <span><i class="bi bi-person me-1"></i>${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</span>
                                    <c:if test="${task.storyPoints > 0}">
                                        <span class="badge bg-light text-dark border">${task.storyPoints} pts</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/kanban.js"></script>
