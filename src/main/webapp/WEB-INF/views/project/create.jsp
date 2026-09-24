<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Project" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Create New Project</h1>
                <p class="page-subtitle">Initiate a software repository workspace and configure project team</p>
            </div>
            <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Projects
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp"/>

        <div class="df-card max-w-700">
            <div class="df-card-body p-4">
                <form action="${pageContext.request.contextPath}/projects" method="POST">
                    <input type="hidden" name="action" value="create">

                    <div class="row g-3 mb-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold text-dark">Project Title *</label>
                            <input type="text" class="form-control" name="name" value="<c:out value='${project.name}'/>" placeholder="e.g. DevFlow Collaboration Portal" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold text-dark">Project Key * (2-5 letters)</label>
                            <input type="text" class="form-control text-uppercase" name="projectKey" value="<c:out value='${project.projectKey}'/>" placeholder="DEV" maxlength="5" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label small fw-semibold text-dark">Project Scope / Description *</label>
                        <textarea class="form-control" name="description" rows="4" placeholder="Detail the objectives, requirements, and deliverables..." required><c:out value='${project.description}'/></textarea>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold text-dark">Project Priority</label>
                            <select class="form-select" name="priority">
                                <option value="LOW">Low</option>
                                <option value="MEDIUM" selected>Medium</option>
                                <option value="HIGH">High</option>
                                <option value="CRITICAL">Critical</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold text-dark">Lead / Project Manager</label>
                            <select class="form-select" name="managerId">
                                <c:forEach var="mgr" items="${managers}">
                                    <option value="${mgr.id}" ${mgr.id == sessionScope.currentUser.id ? 'selected' : ''}>${mgr.fullName} (@${mgr.username})</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold text-dark">Start Date</label>
                            <input type="date" class="form-control" name="startDate">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold text-dark">Estimated End Date</label>
                            <input type="date" class="form-control" name="endDate">
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-df-primary">
                            <i class="bi bi-check-lg me-1"></i> Initialize Project
                        </button>
                        <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
