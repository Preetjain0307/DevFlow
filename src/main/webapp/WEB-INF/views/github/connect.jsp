<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Connect GitHub Repository" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title"><i class="bi bi-github me-2"></i>Link GitHub Repository</h1>
                <p class="page-subtitle">Connect your project repository for automatic commit and pull request tracking</p>
            </div>
            <a href="${pageContext.request.contextPath}/github?action=overview&projectId=${selectedProjectId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to GitHub
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/github" method="POST">
                            <input type="hidden" name="action" value="saveConnection">

                            <div class="mb-3">
                                <label for="projectId" class="form-label fw-bold">Select Project <span class="text-danger">*</span></label>
                                <select class="form-select" id="projectId" name="projectId" required>
                                    <c:forEach var="p" items="${projects}">
                                        <option value="${p.projectId}" ${selectedProjectId == p.projectId ? 'selected' : ''}>${p.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="repoOwner" class="form-label fw-bold">Repository Owner / Org <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="repoOwner" name="repoOwner" placeholder="e.g., torvalds or my-org" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="repoName" class="form-label fw-bold">Repository Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="repoName" name="repoName" placeholder="e.g., linux or devflow-core" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="accessToken" class="form-label fw-bold">Personal Access Token (PAT) (Optional)</label>
                                <input type="password" class="form-control" id="accessToken" name="accessToken" placeholder="ghp_xxxxxxxxxxxxxxxxxxxx">
                                <div class="form-text">Required only for private repositories or high rate limit sync.</div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-dark px-4">
                                    <i class="bi bi-link me-1"></i> Connect & Sync
                                </button>
                                <a href="${pageContext.request.contextPath}/github?action=overview&projectId=${selectedProjectId}" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
