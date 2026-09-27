<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${bug.bugKey}: ${bug.title}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <div class="d-flex align-items-center gap-2">
                    <span class="badge bg-danger">${bug.bugKey}</span>
                    <h1 class="page-title mb-0">${bug.title}</h1>
                    <span class="badge bg-warning text-dark" id="headerStatusBadge">${bug.status}</span>
                </div>
                <p class="page-subtitle mt-1">Project: <a href="${pageContext.request.contextPath}/projects?action=view&id=${bug.projectId}" class="text-decoration-none fw-semibold">${bug.projectName}</a></p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a href="${pageContext.request.contextPath}/bugs?action=edit&id=${bug.bugId}" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-pencil me-1"></i> Edit Bug
                </a>
                <a href="${pageContext.request.contextPath}/bugs?action=list&projectId=${bug.projectId}" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Back to Bugs
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row g-4">
            <!-- Left Column -->
            <div class="col-lg-8">
                <!-- Description -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-file-earmark-text me-2 text-danger"></i>Bug Details</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <h6>Description</h6>
                        <div class="p-3 bg-light rounded mb-3" style="white-space: pre-wrap;">${bug.description}</div>

                        <c:if test="${not empty bug.stepsToReproduce}">
                            <h6>Steps to Reproduce</h6>
                            <div class="p-3 bg-light rounded mb-3" style="white-space: pre-wrap;">${bug.stepsToReproduce}</div>
                        </c:if>

                        <c:if test="${not empty bug.resolutionNotes}">
                            <h6>Resolution Notes</h6>
                            <div class="p-3 bg-success-subtle text-success-emphasis rounded" style="white-space: pre-wrap;">${bug.resolutionNotes}</div>
                        </c:if>
                    </div>
                </div>

                <!-- Comments -->
                <div class="df-card">
                    <div class="df-card-header py-3 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-chat-left-text me-2 text-info"></i>Comments & Triage</h5>
                        <span class="badge bg-light text-dark border" id="bugCommentCount">${comments.size()} Comments</span>
                    </div>
                    <div class="df-card-body p-3">
                        <form id="bugCommentForm" action="${pageContext.request.contextPath}/bugs" method="POST" class="mb-4">
                            <input type="hidden" name="action" value="addComment">
                            <input type="hidden" name="bugId" value="${bug.bugId}">
                            <div class="mb-2">
                                <textarea class="form-control" name="comment" rows="3" placeholder="Add investigation details, workarounds or fix status..." required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="submit" class="btn btn-danger btn-sm px-3">
                                    <i class="bi bi-send me-1"></i> Post Comment
                                </button>
                            </div>
                        </form>

                        <hr>

                        <div class="comments-list" id="bugCommentsList">
                            <c:forEach var="c" items="${comments}">
                                <div class="d-flex mb-3">
                                    <div class="avatar bg-danger text-white rounded-circle me-3 d-flex align-items-center justify-content-center flex-shrink-0" style="width:36px;height:36px;">
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
                                <p class="text-muted small text-center my-3">No comments posted on this bug yet.</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column -->
            <div class="col-lg-4">
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-sliders me-2 text-secondary"></i>Bug Status</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <form id="bugStatusForm" action="${pageContext.request.contextPath}/bugs" method="POST" class="mb-3">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="bugId" value="${bug.bugId}">
                            <label for="bugStatus" class="form-label small fw-bold">Update Status</label>
                            <div class="input-group mb-2">
                                <select class="form-select" id="bugStatus" name="status">
                                    <option value="OPEN" ${bug.status == 'OPEN' ? 'selected' : ''}>Open</option>
                                    <option value="TRIAGED" ${bug.status == 'TRIAGED' ? 'selected' : ''}>Triaged</option>
                                    <option value="IN_PROGRESS" ${bug.status == 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                                    <option value="RESOLVED" ${bug.status == 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                                    <option value="CLOSED" ${bug.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                                    <option value="REOPENED" ${bug.status == 'REOPENED' ? 'selected' : ''}>Reopened</option>
                                </select>
                                <button class="btn btn-danger" type="submit">Update</button>
                            </div>
                            <div class="mt-2">
                                <input type="text" class="form-control form-control-sm" name="resolutionNotes" placeholder="Resolution notes (optional)">
                            </div>
                        </form>

                        <ul class="list-group list-group-flush small">
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Severity:</span>
                                <span class="badge bg-${bug.severity == 'BLOCKER' || bug.severity == 'CRITICAL' ? 'danger' : 'warning text-dark'}">${bug.severity}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Priority:</span>
                                <span class="badge bg-secondary">${bug.priority}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Reporter:</span>
                                <span class="fw-semibold">${bug.reporterName}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Assignee:</span>
                                <span class="fw-semibold">${bug.assigneeName != null ? bug.assigneeName : 'Unassigned'}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Environment:</span>
                                <span class="fw-semibold">${bug.environment != null ? bug.environment : 'N/A'}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Reported Date:</span>
                                <span class="fw-semibold">${bug.createdAt}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
