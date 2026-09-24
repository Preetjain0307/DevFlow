<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Bug Tracker" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Defects & Bug Tracker</h1>
                <p class="page-subtitle">Report, triage, assign, and resolve project bugs and issues</p>
            </div>
            <a href="${pageContext.request.contextPath}/bugs?action=create${projectId != null ? '&projectId='.concat(projectId) : ''}" class="btn btn-danger btn-sm">
                <i class="bi bi-bug me-1"></i> Report Bug
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Filter -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/bugs" method="GET" class="row g-3 align-items-center">
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

        <!-- Bugs Table -->
        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Key</th>
                            <th>Title</th>
                            <th>Severity</th>
                            <th>Status</th>
                            <th>Reporter</th>
                            <th>Assignee</th>
                            <th>Created At</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bugs}">
                            <tr>
                                <td><span class="badge bg-danger">${b.bugKey}</span></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/bugs?action=view&id=${b.bugId}" class="text-decoration-none fw-semibold text-dark">
                                        ${b.title}
                                    </a>
                                </td>
                                <td>
                                    <span class="badge bg-${b.severity == 'BLOCKER' || b.severity == 'CRITICAL' ? 'danger' : (b.severity == 'MAJOR' ? 'warning text-dark' : 'info text-dark')}">
                                        ${b.severity}
                                    </span>
                                </td>
                                <td>
                                    <span class="badge bg-${b.status == 'CLOSED' ? 'secondary' : (b.status == 'RESOLVED' ? 'success' : (b.status == 'IN_PROGRESS' ? 'primary' : 'warning text-dark'))}">
                                        ${b.status}
                                    </span>
                                </td>
                                <td>${b.reporterName}</td>
                                <td>${b.assigneeName != null ? b.assigneeName : '<span class="text-muted">Unassigned</span>'}</td>
                                <td><small class="text-muted">${b.createdAt}</small></td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/bugs?action=view&id=${b.bugId}" class="btn btn-sm btn-outline-secondary">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/bugs?action=edit&id=${b.bugId}" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bugs}">
                            <tr>
                                <td colspan="8" class="text-center py-5 text-muted">
                                    <i class="bi bi-shield-check fs-1 d-block mb-2 text-success"></i>
                                    No defects or bugs reported. Everything is clean!
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
