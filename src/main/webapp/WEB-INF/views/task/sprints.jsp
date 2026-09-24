<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Sprint Planning" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Sprint Planning & Iterations</h1>
                <p class="page-subtitle">Manage agile sprint cycles, milestones, and sprint goals</p>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Selector -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/sprints" method="GET" class="row g-3 align-items-center">
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

        <div class="row g-4">
            <!-- Sprints List -->
            <div class="col-lg-7">
                <div class="df-card">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-arrow-repeat me-2 text-primary"></i>Sprint Cycles</h5>
                    </div>
                    <div class="df-card-body p-0">
                        <div class="list-group list-group-flush">
                            <c:forEach var="sprint" items="${sprints}">
                                <div class="list-group-item p-3">
                                    <div class="d-flex justify-content-between align-items-center mb-1">
                                        <h6 class="mb-0 fw-bold text-dark">${sprint.name}</h6>
                                        <span class="badge bg-${sprint.status == 'ACTIVE' ? 'success' : (sprint.status == 'COMPLETED' ? 'secondary' : 'warning text-dark')}">${sprint.status}</span>
                                    </div>
                                    <p class="text-muted small mb-2">${sprint.goal}</p>
                                    <div class="d-flex justify-content-between align-items-center text-muted small">
                                        <span><i class="bi bi-calendar-event me-1"></i> ${sprint.startDate} to ${sprint.endDate}</span>
                                        <c:if test="${sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.roleName == 'PROJECT_MANAGER' || sessionScope.currentUser.role.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'PROJECT_MANAGER'}">
                                            <div class="btn-group btn-group-sm">
                                                <c:if test="${sprint.status == 'PLANNED'}">
                                                    <form action="${pageContext.request.contextPath}/sprints" method="POST" class="d-inline">
                                                        <input type="hidden" name="action" value="start">
                                                        <input type="hidden" name="sprintId" value="${sprint.sprintId}">
                                                        <input type="hidden" name="projectId" value="${selectedProjectId}">
                                                        <button type="submit" class="btn btn-sm btn-outline-success">Start Sprint</button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${sprint.status == 'ACTIVE'}">
                                                    <form action="${pageContext.request.contextPath}/sprints" method="POST" class="d-inline ms-1">
                                                        <input type="hidden" name="action" value="complete">
                                                        <input type="hidden" name="sprintId" value="${sprint.sprintId}">
                                                        <input type="hidden" name="projectId" value="${selectedProjectId}">
                                                        <button type="submit" class="btn btn-sm btn-outline-secondary">Complete Sprint</button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty sprints}">
                                <div class="text-center py-4 text-muted">No sprints found for this project.</div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Create Sprint Card -->
            <c:if test="${sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.roleName == 'PROJECT_MANAGER' || sessionScope.currentUser.role.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'PROJECT_MANAGER'}">
                <div class="col-lg-5">
                    <div class="df-card">
                        <div class="df-card-header py-3">
                            <h5 class="mb-0 fw-semibold"><i class="bi bi-plus-circle me-2 text-success"></i>Create New Sprint</h5>
                        </div>
                        <div class="df-card-body p-4">
                            <form action="${pageContext.request.contextPath}/sprints" method="POST">
                                <input type="hidden" name="action" value="create">
                                <input type="hidden" name="projectId" value="${selectedProjectId}">

                                <div class="mb-3">
                                    <label for="name" class="form-label fw-bold">Sprint Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" name="name" placeholder="e.g., Sprint 3: Core API" required>
                                </div>

                                <div class="mb-3">
                                    <label for="goal" class="form-label fw-bold">Sprint Goal</label>
                                    <textarea class="form-control" id="goal" name="goal" rows="3" placeholder="What is the objective of this sprint?"></textarea>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="startDate" class="form-label fw-bold">Start Date <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" id="startDate" name="startDate" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="endDate" class="form-label fw-bold">End Date <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" id="endDate" name="endDate" required>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-df-primary w-100">
                                    <i class="bi bi-plus-lg me-1"></i> Create Sprint
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
