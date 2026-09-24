<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Edit Task - ${task.taskKey}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Edit Task <span class="badge bg-secondary">${task.taskKey}</span></h1>
                <p class="page-subtitle">Project: <strong>${task.projectName}</strong></p>
            </div>
            <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Details
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/tasks" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="taskId" value="${task.taskId}">

                            <div class="mb-3">
                                <label for="title" class="form-label fw-bold">Task Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" value="${task.title}" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="5">${task.description}</textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="taskType" class="form-label fw-bold">Task Type</label>
                                    <select class="form-select" id="taskType" name="taskType">
                                        <option value="TASK" ${task.taskType == 'TASK' ? 'selected' : ''}>Task</option>
                                        <option value="STORY" ${task.taskType == 'STORY' ? 'selected' : ''}>Story</option>
                                        <option value="BUG" ${task.taskType == 'BUG' ? 'selected' : ''}>Bug</option>
                                        <option value="IMPROVEMENT" ${task.taskType == 'IMPROVEMENT' ? 'selected' : ''}>Improvement</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="status" class="form-label fw-bold">Status</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="TODO" ${task.status == 'TODO' ? 'selected' : ''}>To Do</option>
                                        <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                        <option value="IN_REVIEW" ${task.status == 'IN_REVIEW' ? 'selected' : ''}>In Review</option>
                                        <option value="DONE" ${task.status == 'DONE' ? 'selected' : ''}>Done</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="priority" class="form-label fw-bold">Priority</label>
                                    <select class="form-select" id="priority" name="priority">
                                        <option value="LOW" ${task.priority == 'LOW' ? 'selected' : ''}>Low</option>
                                        <option value="MEDIUM" ${task.priority == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                                        <option value="HIGH" ${task.priority == 'HIGH' ? 'selected' : ''}>High</option>
                                        <option value="CRITICAL" ${task.priority == 'CRITICAL' ? 'selected' : ''}>Critical</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="storyPoints" class="form-label fw-bold">Story Points</label>
                                    <input type="number" class="form-control" id="storyPoints" name="storyPoints" min="0" max="100" value="${task.storyPoints}">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="assigneeId" class="form-label fw-bold">Assignee</label>
                                    <select class="form-select" id="assigneeId" name="assigneeId">
                                        <option value="">-- Unassigned --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.userId}" ${task.assigneeId == u.userId ? 'selected' : ''}>${u.fullName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="sprintId" class="form-label fw-bold">Sprint</label>
                                    <select class="form-select" id="sprintId" name="sprintId">
                                        <option value="">-- Product Backlog --</option>
                                        <c:forEach var="s" items="${sprints}">
                                            <option value="${s.sprintId}" ${task.sprintId == s.sprintId ? 'selected' : ''}>${s.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="dueDate" class="form-label fw-bold">Due Date</label>
                                    <input type="date" class="form-control" id="dueDate" name="dueDate" value="${task.dueDate}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="estimatedHours" class="form-label fw-bold">Estimated Hours</label>
                                    <input type="number" step="0.5" class="form-control" id="estimatedHours" name="estimatedHours" value="${task.estimatedHours}">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-save me-1"></i> Update Task
                                </button>
                                <a href="${pageContext.request.contextPath}/tasks?action=view&id=${task.taskId}" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
