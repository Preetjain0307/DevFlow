<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="GitHub Integration" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title"><i class="bi bi-github me-2"></i>GitHub Integration</h1>
                <p class="page-subtitle">Live repository sync, commits, pull requests, issues, and branch monitoring</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <c:choose>
                    <c:when test="${repo != null}">
                        <form action="${pageContext.request.contextPath}/github" method="POST" class="d-inline">
                            <input type="hidden" name="action" value="sync">
                            <input type="hidden" name="projectId" value="${selectedProjectId}">
                            <button type="submit" class="btn btn-outline-dark btn-sm">
                                <i class="bi bi-arrow-clockwise me-1"></i> Sync Repository
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/github?action=connect&projectId=${selectedProjectId}" class="btn btn-dark btn-sm">
                            <i class="bi bi-link-45deg me-1"></i> Connect Repository
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Selector -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/github" method="GET" class="row g-3 align-items-center">
                    <div class="col-md-4">
                        <label for="projectId" class="form-label small fw-bold mb-1">Select Project</label>
                        <select class="form-select form-select-sm" id="projectId" name="projectId" onchange="this.form.submit()">
                            <c:forEach var="p" items="${projects}">
                                <option value="${p.projectId}" ${selectedProjectId == p.projectId ? 'selected' : ''}>${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <c:choose>
            <c:when test="${repo != null}">
                <!-- Repository Details Header Card -->
                <div class="df-card mb-4 bg-light">
                    <div class="df-card-body p-4">
                        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                            <div>
                                <div class="d-flex align-items-center gap-2">
                                    <i class="bi bi-journal-code fs-3 text-dark"></i>
                                    <h4 class="mb-0 fw-bold">${repo.repoOwner} / ${repo.repoName}</h4>
                                    <span class="badge bg-dark">${repo.defaultBranch}</span>
                                </div>
                                <p class="text-muted small mt-1 mb-0">${repo.description != null ? repo.description : 'Connected GitHub Repository'}</p>
                            </div>
                            <div>
                                <a href="${repo.htmlUrl}" target="_blank" class="btn btn-dark btn-sm">
                                    <i class="bi bi-box-arrow-up-right me-1"></i> Open on GitHub
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- GitHub Activity Tabs / Quick Views -->
                <div class="row g-4">
                    <!-- Recent Commits -->
                    <div class="col-lg-7">
                        <div class="df-card h-100">
                            <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                                <h5 class="mb-0 fw-semibold"><i class="bi bi-git me-2 text-primary"></i>Recent Commits</h5>
                                <a href="${pageContext.request.contextPath}/github?action=commits&projectId=${selectedProjectId}" class="btn btn-sm btn-outline-secondary">View All</a>
                            </div>
                            <div class="df-card-body p-0">
                                <ul class="list-group list-group-flush">
                                    <c:forEach var="commit" items="${commits}">
                                        <li class="list-group-item py-3">
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <span class="fw-semibold text-truncate" style="max-width: 70%;">${commit.message}</span>
                                                <span class="badge bg-secondary font-monospace">${commit.commitHash != null && commit.commitHash.length() >= 7 ? commit.commitHash.substring(0, 7) : commit.commitHash}</span>
                                            </div>
                                            <div class="d-flex justify-content-between text-muted small">
                                                <span><i class="bi bi-person me-1"></i>${commit.authorName}</span>
                                                <span>${commit.committedAt}</span>
                                            </div>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${empty commits}">
                                        <li class="list-group-item text-center py-4 text-muted">No commit history available. Click 'Sync Repository' above.</li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!-- Open Pull Requests & Issues -->
                    <div class="col-lg-5">
                        <div class="df-card mb-4">
                            <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                                <h5 class="mb-0 fw-semibold"><i class="bi bi-diagram-2 me-2 text-success"></i>Pull Requests</h5>
                                <a href="${pageContext.request.contextPath}/github?action=pulls&projectId=${selectedProjectId}" class="btn btn-sm btn-outline-secondary">View All</a>
                            </div>
                            <div class="df-card-body p-0">
                                <ul class="list-group list-group-flush">
                                    <c:forEach var="pr" items="${pullRequests}">
                                        <li class="list-group-item p-3">
                                            <div class="fw-semibold text-truncate">#${pr.issueNumber} ${pr.title}</div>
                                            <div class="d-flex justify-content-between text-muted small mt-1">
                                                <span>${pr.authorName}</span>
                                                <span class="badge bg-success">${pr.state}</span>
                                            </div>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${empty pullRequests}">
                                        <li class="list-group-item text-center py-3 text-muted small">No active pull requests.</li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Not Connected Banner -->
                <div class="df-card py-5 text-center text-muted">
                    <i class="bi bi-github fs-1 mb-3 text-dark"></i>
                    <h5>No GitHub Repository Linked</h5>
                    <p class="mb-3">Connect your project to a GitHub repository to track commits, pull requests, and branch activity.</p>
                    <div>
                        <a href="${pageContext.request.contextPath}/github?action=connect&projectId=${selectedProjectId}" class="btn btn-dark">
                            <i class="bi bi-link me-1"></i> Connect GitHub Repo
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
