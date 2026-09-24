<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Report Bug" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Report a Defect / Bug</h1>
                <p class="page-subtitle">Submit bug findings with replication steps, logs, and environment metadata</p>
            </div>
            <a href="${pageContext.request.contextPath}/bugs?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Bugs
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/bugs" method="POST">
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
                                <label for="title" class="form-label fw-bold">Bug Summary / Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="e.g., NullPointerException during user registration" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description & Expected vs Actual Result</label>
                                <textarea class="form-control" id="description" name="description" rows="4" placeholder="Describe the unexpected behavior..."></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="stepsToReproduce" class="form-label fw-bold">Steps to Reproduce</label>
                                <textarea class="form-control" id="stepsToReproduce" name="stepsToReproduce" rows="4" placeholder="1. Go to '/register'&#10;2. Submit blank form&#10;3. Observe 500 error page..."></textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="severity" class="form-label fw-bold">Severity</label>
                                    <select class="form-select" id="severity" name="severity">
                                        <option value="MINOR">Minor</option>
                                        <option value="MAJOR" selected>Major</option>
                                        <option value="CRITICAL">Critical</option>
                                        <option value="BLOCKER">Blocker</option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="priority" class="form-label fw-bold">Priority</label>
                                    <select class="form-select" id="priority" name="priority">
                                        <option value="LOW">Low</option>
                                        <option value="MEDIUM" selected>Medium</option>
                                        <option value="HIGH">High</option>
                                        <option value="URGENT">Urgent</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="assigneeId" class="form-label fw-bold">Assignee (Developer)</label>
                                    <select class="form-select" id="assigneeId" name="assigneeId">
                                        <option value="">-- Unassigned --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.userId}">${u.fullName} (${u.roleName != null ? u.roleName : u.role.name})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="environment" class="form-label fw-bold">Environment</label>
                                    <input type="text" class="form-control" id="environment" name="environment" placeholder="e.g., Chrome 120, Windows 11 / Staging">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-danger px-4">
                                    <i class="bi bi-bug me-1"></i> File Bug Report
                                </button>
                                <a href="${pageContext.request.contextPath}/bugs?action=list" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
