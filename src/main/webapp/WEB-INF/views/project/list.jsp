<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Projects" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Software Projects</h1>
                <p class="page-subtitle">Manage project repositories, team rosters, and milestone roadmaps</p>
            </div>
            <c:if test="${sessionScope.currentUser.projectManager || sessionScope.currentUser.admin}">
                <a href="${pageContext.request.contextPath}/projects?action=create" class="btn btn-df-primary btn-sm">
                    <i class="bi bi-plus-lg me-1"></i> New Project
                </a>
            </c:if>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp"/>

        <!-- Search and Filter Bar -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/projects" method="GET" class="row g-2 align-items-center">
                    <div class="col-md-5">
                        <div class="input-group">
                            <span class="input-group-text bg-white"><i class="bi bi-search text-muted"></i></span>
                            <input type="text" class="form-control" name="keyword" value="<c:out value='${keyword}'/>" placeholder="Search by name, key, or description...">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="status">
                            <option value="">All Statuses</option>
                            <option value="PLANNING" ${status == 'PLANNING' ? 'selected' : ''}>Planning</option>
                            <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                            <option value="ON_HOLD" ${status == 'ON_HOLD' ? 'selected' : ''}>On Hold</option>
                            <option value="COMPLETED" ${status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                            <option value="ARCHIVED" ${status == 'ARCHIVED' ? 'selected' : ''}>Archived</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select class="form-select" name="priority">
                            <option value="">All Priorities</option>
                            <option value="LOW" ${priority == 'LOW' ? 'selected' : ''}>Low</option>
                            <option value="MEDIUM" ${priority == 'MEDIUM' ? 'selected' : ''}>Medium</option>
                            <option value="HIGH" ${priority == 'HIGH' ? 'selected' : ''}>High</option>
                            <option value="CRITICAL" ${priority == 'CRITICAL' ? 'selected' : ''}>Critical</option>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex gap-2">
                        <button type="submit" class="btn btn-df-primary w-100">Filter</button>
                        <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-secondary">Reset</a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Project Cards Grid -->
        <div class="row g-4">
            <c:forEach var="p" items="${projects}">
                <div class="col-md-6 col-xl-4">
                    <div class="df-card h-100 d-flex flex-column">
                        <div class="df-card-body flex-grow-1">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <span class="badge bg-primary bg-opacity-10 text-primary fw-bold font-monospace px-2 py-1">${p.projectKey}</span>
                                <span class="badge ${p.status == 'ACTIVE' ? 'badge-soft-success' : (p.status == 'COMPLETED' ? 'badge-soft-info' : 'badge-soft-warning')}">
                                    ${p.status}
                                </span>
                            </div>

                            <h5 class="fw-bold text-dark mb-2">
                                <a href="${pageContext.request.contextPath}/projects?action=view&id=${p.id}" class="text-dark text-decoration-none hover-text-primary">
                                    <c:out value="${p.name}"/>
                                </a>
                            </h5>

                            <p class="text-muted small mb-3 text-truncate-3" style="min-height: 40px;">
                                <c:out value="${p.description}"/>
                            </p>

                            <!-- Progress Bar -->
                            <div class="mb-3">
                                <div class="d-flex justify-content-between text-muted small mb-1">
                                    <span>Task Progress</span>
                                    <span class="fw-semibold text-dark">${p.progressPercentage}% (${p.completedTasks}/${p.totalTasks})</span>
                                </div>
                                <div class="progress" style="height: 6px;">
                                    <div class="progress-bar bg-primary" role="progressbar" style="width: ${p.progressPercentage}%"></div>
                                </div>
                            </div>

                            <div class="row g-2 text-muted small pt-2 border-top">
                                <div class="col-6">
                                    <i class="bi bi-person text-secondary me-1"></i> ${p.managerName}
                                </div>
                                <div class="col-6 text-end">
                                    <i class="bi bi-people text-secondary me-1"></i> ${p.memberCount} Members
                                </div>
                            </div>
                        </div>

                        <div class="df-card-header bg-light border-top d-flex justify-content-between">
                            <div class="d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/task/kanban?projectId=${p.id}" class="btn btn-sm btn-outline-primary py-0" title="Kanban Board">
                                    <i class="bi bi-kanban"></i> Board
                                </a>
                                <a href="${pageContext.request.contextPath}/github?projectId=${p.id}" class="btn btn-sm btn-outline-dark py-0" title="GitHub">
                                    <i class="bi bi-github"></i>
                                </a>
                            </div>
                            <a href="${pageContext.request.contextPath}/projects?action=view&id=${p.id}" class="btn btn-sm btn-df-primary py-0">
                                Overview &rarr;
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty projects}">
                <div class="col-12 text-center text-muted py-5">
                    <i class="bi bi-folder2-open fs-1 d-block mb-2 text-secondary"></i>
                    <h5>No projects found</h5>
                    <p class="small">Try adjusting your search criteria or create a new project.</p>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
