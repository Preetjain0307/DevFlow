<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${task.taskKey}: ${task.title}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <div class="d-flex align-items-center gap-2">
                    <span class="badge bg-secondary">${task.taskKey}</span>
                    <h1 class="page-title mb-0">${task.title}</h1>
                </div>
                <p class="page-subtitle mt-1">Project: <a href="${pageContext.request.contextPath}/projects?action=view&id=${task.projectId}" class="text-decoration-none fw-semibold">${task.projectName}</a></p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a href="${pageContext.request.contextPath}/tasks?action=edit&id=${task.taskId}" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-pencil me-1"></i> Edit Task
                </a>
                <a href="${pageContext.request.contextPath}/tasks?action=list&projectId=${task.projectId}" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Back to Tasks
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row g-4">
            <!-- Left Column: Details, Description, Comments -->
            <div class="col-lg-8">
                <!-- Description Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-file-text me-2 text-primary"></i>Description</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <c:choose>
                            <c:when test="${not empty task.description}">
                                <div class="p-3 bg-light rounded" style="white-space: pre-wrap;">${task.description}</div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted fst-italic">No description provided for this task.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Comments Section -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-chat-left-dots me-2 text-info"></i>Activity & Discussion</h5>
                        <span class="badge bg-light text-dark border">${comments.size()} Comments</span>
                    </div>
                    <div class="df-card-body p-3">
                        <!-- New Comment Form -->
                        <form action="${pageContext.request.contextPath}/tasks" method="POST" class="mb-4">
                            <input type="hidden" name="action" value="addComment">
                            <input type="hidden" name="taskId" value="${task.taskId}">
                            <div class="mb-2">
                                <textarea class="form-control" name="comment" rows="3" placeholder="Add a comment or progress update..." required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="submit" class="btn btn-df-primary btn-sm px-3">
                                    <i class="bi bi-send me-1"></i> Post Comment
                                </button>
                            </div>
                        </form>

                        <hr>

                        <!-- Comments List -->
                        <div class="comments-list">
                            <c:forEach var="c" items="${comments}">
                                <div class="d-flex mb-3">
                                    <div class="avatar bg-primary text-white rounded-circle me-3 d-flex align-items-center justify-content-center flex-shrink-0" style="width:36px;height:36px;">
                                        ${c.userFullName != null ? c.userFullName.substring(0,1).toUpperCase() : 'U'}
                                    </div>
                                    <div class="flex-grow-1 bg-light p-3 rounded">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold">${c.userFullName}</span>
                                            <small class="text-muted">${c.createdAt}</small>
                                        </div>
                                        <p class="mb-0 small" style="white-space: pre-wrap;">${c.comment}</p>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty comments}">
                                <p class="text-muted small text-center my-3">No comments posted yet.</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column: Status, Assignee, Metadata, Quick Actions -->
            <div class="col-lg-4">
                <!-- Status & Quick Update Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-sliders me-2 text-secondary"></i>Workflow Status</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <form action="${pageContext.request.contextPath}/tasks" method="POST" class="mb-3">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="taskId" value="${task.taskId}">
                            <label for="quickStatus" class="form-label small fw-bold">Current Status</label>
                            <div class="input-group">
                                <select class="form-select" id="quickStatus" name="status">
                                    <option value="TODO" ${task.status == 'TODO' ? 'selected' : ''}>To Do</option>
                                    <option value="IN_PROGRESS" ${task.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                    <option value="IN_REVIEW" ${task.status == 'IN_REVIEW' ? 'selected' : ''}>In Review</option>
                                    <option value="DONE" ${task.status == 'DONE' ? 'selected' : ''}>Done</option>
                                </select>
                                <button class="btn btn-df-primary" type="submit">Update</button>
                            </div>
                        </form>

                        <ul class="list-group list-group-flush small">
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Type:</span>
                                <span class="fw-semibold">${task.taskType}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Priority:</span>
                                <span class="badge bg-${task.priority == 'CRITICAL' ? 'danger' : (task.priority == 'HIGH' ? 'warning text-dark' : 'info text-dark')}">${task.priority}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Assignee:</span>
                                <span class="fw-semibold">${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Creator:</span>
                                <span class="fw-semibold">${task.creatorName}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Sprint:</span>
                                <span class="fw-semibold">${task.sprintName != null ? task.sprintName : 'Product Backlog'}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Story Points:</span>
                                <span class="fw-semibold">${task.storyPoints} pts</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Due Date:</span>
                                <span class="fw-semibold">${task.dueDate != null ? task.dueDate : 'Not set'}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
