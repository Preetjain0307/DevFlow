<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Task" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Create Task</h1>
                <p class="page-subtitle">Add a new work item, story, or task to the project backlog</p>
            </div>
            <a href="${pageContext.request.contextPath}/tasks?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Tasks
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/tasks" method="POST">
                            <input type="hidden" name="action" value="create">

                            <div class="mb-3">
                                <label for="projectId" class="form-label fw-bold">Project <span class="text-danger">*</span></label>
                                <select class="form-select" id="projectId" name="projectId" required>
                                    <option value="">-- Select Project --</option>
                                    <c:forEach var="p" items="${projects}">
                                        <option value="${p.projectId}" ${selectedProjectId == p.projectId ? 'selected' : ''}>${p.name} (${p.projectKey})</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="title" class="form-label fw-bold">Task Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="e.g., Implement JWT Auth Refresh Tokens" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description / Acceptance Criteria</label>
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="Detailed description of the task..."></textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="taskType" class="form-label fw-bold">Task Type</label>
                                    <select class="form-select" id="taskType" name="taskType">
                                        <option value="TASK">Task</option>
                                        <option value="STORY">Story</option>
                                        <option value="BUG">Bug</option>
                                        <option value="IMPROVEMENT">Improvement</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="priority" class="form-label fw-bold">Priority</label>
                                    <select class="form-select" id="priority" name="priority">
                                        <option value="MEDIUM" selected>Medium</option>
                                        <option value="LOW">Low</option>
                                        <option value="HIGH">High</option>
                                        <option value="CRITICAL">Critical</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="storyPoints" class="form-label fw-bold">Story Points</label>
                                    <input type="number" class="form-control" id="storyPoints" name="storyPoints" min="0" max="100" value="0">
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="assigneeId" class="form-label fw-bold">Assignee</label>
                                    <select class="form-select" id="assigneeId" name="assigneeId">
                                        <option value="">-- Unassigned --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.userId}">${u.fullName} (${u.roleName != null ? u.roleName : u.role.name})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="dueDate" class="form-label fw-bold">Due Date</label>
                                    <input type="date" class="form-control" id="dueDate" name="dueDate">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-plus-circle me-1"></i> Create Task
                                </button>
                                <a href="${pageContext.request.contextPath}/tasks?action=list" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
