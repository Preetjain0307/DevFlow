<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Idea & Change Proposals" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Idea & Change Proposals</h1>
                <p class="page-subtitle">Democratized innovation: Submit ideas, gather peer votes, and seek faculty approval</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <c:if test="${sessionScope.currentUser.roleName == 'FACULTY' || sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'FACULTY' || sessionScope.currentUser.role.roleName == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/ideas?action=facultyReview" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-mortarboard me-1"></i> Faculty Review Desk
                    </a>
                </c:if>
                <a href="${pageContext.request.contextPath}/ideas?action=submit${projectId != null ? '&projectId='.concat(projectId) : ''}" class="btn btn-warning btn-sm fw-bold">
                    <i class="bi bi-lightbulb me-1"></i> Propose Idea
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Filter -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/ideas" method="GET" class="row g-3 align-items-center">
                    <div class="col-md-4">
                        <label for="projectId" class="form-label small fw-bold mb-1">Filter by Project</label>
                        <select class="form-select form-select-sm" id="projectId" name="projectId" onchange="this.form.submit()">
                            <option value="">-- All Projects --</option>
                            <c:forEach var="p" items="${projects}">
                                <option value="${p.projectId}" ${projectId == p.projectId ? 'selected' : ''}>${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <div class="row g-4">
            <c:forEach var="idea" items="${ideas}">
                <div class="col-md-6 col-lg-4">
                    <div class="df-card h-100 position-relative d-flex flex-column">
                        <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                            <span class="badge badge-soft-primary">${idea.category}</span>
                            <span class="badge bg-${idea.status == 'APPROVED' ? 'success' : (idea.status == 'REJECTED' ? 'danger' : (idea.status == 'UNDER_FACULTY_REVIEW' || idea.status == 'PENDING_FACULTY' ? 'info' : (idea.status == 'VOTING' || idea.status == 'IN_VOTING' ? 'primary' : 'secondary')))}">
                                ${idea.status}
                            </span>
                        </div>
                        <div class="df-card-body flex-grow-1 p-3">
                            <h5 class="fw-bold mb-2">
                                <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.ideaId}" class="text-dark text-decoration-none">
                                    ${idea.title}
                                </a>
                            </h5>
                            <p class="text-muted small mb-3">
                                <c:choose>
                                    <c:when test="${idea.description.length() > 140}">
                                        ${idea.description.substring(0, 140)}...
                                    </c:when>
                                    <c:otherwise>
                                        ${idea.description}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <!-- Voting Progress Mini Bar -->
                            <div class="mb-3">
                                <div class="d-flex justify-content-between small text-muted mb-1">
                                    <span><i class="bi bi-hand-thumbs-up text-success"></i> ${idea.upvotes} Up</span>
                                    <span><i class="bi bi-hand-thumbs-down text-danger"></i> ${idea.downvotes} Down</span>
                                </div>
                                <div class="progress" style="height: 6px;">
                                    <c:set var="totalVotes" value="${idea.upvotes + idea.downvotes}" />
                                    <c:choose>
                                        <c:when test="${totalVotes > 0}">
                                            <div class="progress-bar bg-success" style="width: ${(idea.upvotes / totalVotes) * 100}%"></div>
                                            <div class="progress-bar bg-danger" style="width: ${(idea.downvotes / totalVotes) * 100}%"></div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="progress-bar bg-secondary opacity-25" style="width: 100%"></div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="small text-muted mb-1">
                                <i class="bi bi-folder me-1 text-primary"></i> ${idea.projectName}
                            </div>
                            <div class="small text-muted">
                                <i class="bi bi-person me-1 text-secondary"></i> Proposed by ${idea.authorName}
                            </div>
                        </div>
                        <div class="df-card-footer border-top d-flex justify-content-between align-items-center p-3">
                            <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.ideaId}" class="btn btn-outline-primary btn-sm">
                                <i class="bi bi-chat-left-dots me-1"></i> View & Vote
                            </a>
                            <small class="text-muted">${idea.createdAt}</small>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty ideas}">
                <div class="col-12">
                    <div class="df-card py-5 text-center text-muted">
                        <i class="bi bi-lightbulb fs-1 mb-2 text-warning"></i>
                        <h5>No ideas submitted yet</h5>
                        <p class="mb-0">Be the first to submit a proposal or feature idea for your project team!</p>
                    </div>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
