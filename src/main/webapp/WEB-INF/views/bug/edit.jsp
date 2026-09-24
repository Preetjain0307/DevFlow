<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Edit Bug - ${bug.bugKey}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Edit Bug <span class="badge bg-danger">${bug.bugKey}</span></h1>
                <p class="page-subtitle">Update defect status, severity, assignee, and resolution notes</p>
            </div>
            <a href="${pageContext.request.contextPath}/bugs?action=view&id=${bug.bugId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Details
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/bugs" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="bugId" value="${bug.bugId}">

                            <div class="mb-3">
                                <label for="title" class="form-label fw-bold">Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" value="${bug.title}" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="4">${bug.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label for="stepsToReproduce" class="form-label fw-bold">Steps to Reproduce</label>
                                <textarea class="form-control" id="stepsToReproduce" name="stepsToReproduce" rows="4">${bug.stepsToReproduce}</textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="severity" class="form-label fw-bold">Severity</label>
                                    <select class="form-select" id="severity" name="severity">
                                        <option value="MINOR" ${bug.severity == 'MINOR' ? 'selected' : ''}>Minor</option>
                                        <option value="MAJOR" ${bug.severity == 'MAJOR' ? 'selected' : ''}>Major</option>
                                        <option value="CRITICAL" ${bug.severity == 'CRITICAL' ? 'selected' : ''}>Critical</option>
                                        <option value="BLOCKER" ${bug.severity == 'BLOCKER' ? 'selected' : ''}>Blocker</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="status" class="form-label fw-bold">Status</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="OPEN" ${bug.status == 'OPEN' ? 'selected' : ''}>Open</option>
                                        <option value="TRIAGED" ${bug.status == 'TRIAGED' ? 'selected' : ''}>Triaged</option>
                                        <option value="IN_PROGRESS" ${bug.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                        <option value="RESOLVED" ${bug.status == 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                                        <option value="CLOSED" ${bug.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                                        <option value="REOPENED" ${bug.status == 'REOPENED' ? 'selected' : ''}>Reopened</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="priority" class="form-label fw-bold">Priority</label>
                                    <select class="form-select" id="priority" name="priority">
                                        <option value="LOW" ${bug.priority == 'LOW' ? 'selected' : ''}>Low</option>
                                        <option value="MEDIUM" ${bug.priority == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                                        <option value="HIGH" ${bug.priority == 'HIGH' ? 'selected' : ''}>High</option>
                                        <option value="URGENT" ${bug.priority == 'URGENT' ? 'selected' : ''}>Urgent</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="assigneeId" class="form-label fw-bold">Assignee</label>
                                    <select class="form-select" id="assigneeId" name="assigneeId">
                                        <option value="">-- Unassigned --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.userId}" ${bug.assigneeId == u.userId ? 'selected' : ''}>${u.fullName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="environment" class="form-label fw-bold">Environment</label>
                                    <input type="text" class="form-control" id="environment" name="environment" value="${bug.environment}">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-save me-1"></i> Update Bug
                                </button>
                                <a href="${pageContext.request.contextPath}/bugs?action=view&id=${bug.bugId}" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
