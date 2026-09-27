<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Executive Reports & Analytics" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <!-- Print-Only Executive Header -->
        <div class="d-none d-print-block mb-4 p-3 border rounded text-center bg-light">
            <h3 class="fw-bold mb-1">DEVFLOW: Executive Capstone Evaluation Report</h3>
            <p class="text-muted small mb-1">Pure Advanced Java Enterprise Platform • Evaluated on <%= new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm").format(new java.util.Date()) %></p>
            <div class="d-flex justify-content-center gap-3 small fw-semibold">
                <span>Project: <strong>${selectedProject.name}</strong></span>
                <span>•</span>
                <span>Project Key: <strong>${selectedProject.projectKey}</strong></span>
                <span>•</span>
                <span>Lead Manager: <strong>${selectedProject.managerName != null ? selectedProject.managerName : 'Unassigned'}</strong></span>
            </div>
        </div>

        <div class="page-header d-flex flex-wrap align-items-center justify-content-between gap-3 mb-4">
            <div>
                <h1 class="page-title"><i class="bi bi-graph-up me-2 text-primary"></i>Executive Project Report</h1>
                <p class="page-subtitle">Sprint burn-down, task distribution, defect resolution velocity, and team audit logs</p>
            </div>
            <div class="d-flex align-items-center gap-2 flex-wrap d-print-none">
                <button type="button" class="btn btn-primary btn-sm d-flex align-items-center gap-1 shadow-sm" onclick="window.print()" title="Print Executive Summary to Paper or PDF">
                    <i class="bi bi-printer"></i> <span>Print / Export PDF</span>
                </button>
                <a href="${pageContext.request.contextPath}/tasks?action=export&projectId=${projectId}" class="btn btn-outline-success btn-sm d-flex align-items-center gap-1" title="Export Tasks to CSV">
                    <i class="bi bi-filetype-csv"></i> <span>Tasks CSV</span>
                </a>
                <a href="${pageContext.request.contextPath}/bugs?action=export&projectId=${projectId}" class="btn btn-outline-danger btn-sm d-flex align-items-center gap-1" title="Export Defects to CSV">
                    <i class="bi bi-filetype-csv"></i> <span>Defects CSV</span>
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Selector (Hidden during Print) -->
        <div class="df-card mb-4 d-print-none">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/reports" method="GET" class="row g-3 align-items-center">
                    <div class="col-md-5">
                        <label for="projectId" class="form-label small fw-bold mb-1">Select Target Project for Audit</label>
                        <select class="form-select form-select-sm" id="projectId" name="projectId" onchange="this.form.submit()">
                            <c:forEach var="p" items="${projects}">
                                <option value="${p.id}" ${projectId == p.id ? 'selected' : ''}>${p.name} (${p.projectKey})</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <!-- Project Executive Summary Card -->
        <div class="df-card mb-4">
            <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                <h5 class="mb-0 fw-semibold"><i class="bi bi-info-circle me-2 text-primary"></i>Project Overview: ${selectedProject.name}</h5>
                <span class="badge bg-primary bg-opacity-10 text-primary border border-primary border-opacity-25 px-3 py-1 font-monospace">Key: ${selectedProject.projectKey}</span>
            </div>
            <div class="df-card-body p-4">
                <div class="row g-3 mb-4">
                    <div class="col-md-3 col-6">
                        <div class="p-3 border rounded-3 bg-body-tertiary">
                            <span class="text-muted small d-block">Status</span>
                            <span class="badge bg-success-subtle text-success border border-success-subtle mt-1">${selectedProject.status}</span>
                        </div>
                    </div>
                    <div class="col-md-3 col-6">
                        <div class="p-3 border rounded-3 bg-body-tertiary">
                            <span class="text-muted small d-block">Project Manager</span>
                            <strong class="d-block mt-1 text-truncate">${selectedProject.managerName != null ? selectedProject.managerName : 'Alan Turing'}</strong>
                        </div>
                    </div>
                    <div class="col-md-3 col-6">
                        <div class="p-3 border rounded-3 bg-body-tertiary">
                            <span class="text-muted small d-block">Total Tasks Logged</span>
                            <strong class="fs-5 text-primary mt-1 d-block">${projectTasks != null ? projectTasks.size() : 0}</strong>
                        </div>
                    </div>
                    <div class="col-md-3 col-6">
                        <div class="p-3 border rounded-3 bg-body-tertiary">
                            <span class="text-muted small d-block">Active Defects Logged</span>
                            <strong class="fs-5 text-danger mt-1 d-block">${projectBugs != null ? projectBugs.size() : 0}</strong>
                        </div>
                    </div>
                </div>

                <p class="text-muted small mb-0">${selectedProject.description}</p>
            </div>
        </div>

        <!-- Charts Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="df-card h-100">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-pie-chart me-2 text-primary"></i>Task Status Breakdown</h5>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center p-4" style="min-height: 290px;">
                        <canvas id="taskStatusChart" style="max-height: 260px;"></canvas>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="df-card h-100">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-bar-chart me-2 text-danger"></i>Defects by Severity</h5>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center p-4" style="min-height: 290px;">
                        <canvas id="bugSeverityChart" style="max-height: 260px;"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Detailed Tasks Audit Table -->
        <div class="df-card mb-4">
            <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                <h5 class="mb-0 fw-semibold"><i class="bi bi-list-check me-2 text-primary"></i>Work Items Audit Trail</h5>
                <span class="badge bg-light text-secondary border">${projectTasks.size()} Items</span>
            </div>
            <div class="df-card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Key</th>
                                <th>Title</th>
                                <th>Type</th>
                                <th>Priority</th>
                                <th>Status</th>
                                <th>Assignee</th>
                                <th>Est. Hours</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="task" items="${projectTasks}">
                                <tr>
                                    <td><span class="font-monospace fw-semibold">${task.taskKey}</span></td>
                                    <td><span class="fw-semibold text-truncate d-inline-block" style="max-width: 320px;">${task.title}</span></td>
                                    <td><span class="badge bg-secondary-subtle text-secondary">${task.taskType}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${task.priority == 'CRITICAL'}"><span class="badge bg-danger">Critical</span></c:when>
                                            <c:when test="${task.priority == 'HIGH'}"><span class="badge bg-warning text-dark">High</span></c:when>
                                            <c:when test="${task.priority == 'MEDIUM'}"><span class="badge bg-info text-dark">Medium</span></c:when>
                                            <c:otherwise><span class="badge bg-light text-secondary border">Low</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${task.status == 'COMPLETED'}"><span class="badge bg-success">Completed</span></c:when>
                                            <c:when test="${task.status == 'IN_PROGRESS'}"><span class="badge bg-primary">In Progress</span></c:when>
                                            <c:when test="${task.status == 'IN_REVIEW'}"><span class="badge bg-warning text-dark">In Review</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary">To Do</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><small class="text-muted">${task.assigneeName != null ? task.assigneeName : 'Unassigned'}</small></td>
                                    <td><small class="text-muted">${task.estimatedHours} hrs</small></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty projectTasks}">
                                <tr>
                                    <td colspan="7" class="text-center py-4 text-muted">No tasks logged under this project.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Detailed Defects Audit Table -->
        <div class="df-card mb-4">
            <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                <h5 class="mb-0 fw-semibold"><i class="bi bi-bug me-2 text-danger"></i>Defect Resolution Ledger</h5>
                <span class="badge bg-light text-secondary border">${projectBugs.size()} Bugs</span>
            </div>
            <div class="df-card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>Key</th>
                                <th>Title</th>
                                <th>Severity</th>
                                <th>Status</th>
                                <th>Assignee</th>
                                <th>Reported By</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="bug" items="${projectBugs}">
                                <tr>
                                    <td><span class="font-monospace fw-semibold text-danger">${bug.bugKey}</span></td>
                                    <td><span class="fw-semibold text-truncate d-inline-block" style="max-width: 320px;">${bug.title}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${bug.severity == 'CRITICAL'}"><span class="badge bg-danger">Critical</span></c:when>
                                            <c:when test="${bug.severity == 'HIGH'}"><span class="badge bg-warning text-dark">High</span></c:when>
                                            <c:when test="${bug.severity == 'MEDIUM'}"><span class="badge bg-info text-dark">Medium</span></c:when>
                                            <c:otherwise><span class="badge bg-light text-secondary border">Low</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge bg-secondary">${bug.status}</span></td>
                                    <td><small class="text-muted">${bug.assigneeName != null ? bug.assigneeName : 'Unassigned'}</small></td>
                                    <td><small class="text-muted">${bug.reporterName != null ? bug.reporterName : 'System'}</small></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty projectBugs}">
                                <tr>
                                    <td colspan="6" class="text-center py-4 text-muted">No defects logged for this project.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Academic Evaluation Sign-Off Block (Print Only) -->
        <div class="d-none d-print-block mt-5 pt-4 border-top">
            <h6 class="fw-bold mb-4 text-center">ACADEMIC CAPSTONE EVALUATION SIGN-OFF</h6>
            <div class="row text-center mt-5">
                <div class="col-4">
                    <p class="mb-4">________________________________</p>
                    <p class="fw-bold mb-0">Student / Candidate</p>
                    <small class="text-muted">Signature & Date</small>
                </div>
                <div class="col-4">
                    <p class="mb-4">________________________________</p>
                    <p class="fw-bold mb-0">Internal Faculty Guide</p>
                    <small class="text-muted">Signature & Date</small>
                </div>
                <div class="col-4">
                    <p class="mb-4">________________________________</p>
                    <p class="fw-bold mb-0">External Examiner</p>
                    <small class="text-muted">Signature & Date</small>
                </div>
            </div>
        </div>

    </main>
</div>

<!-- Chart initialization -->
<script src="${pageContext.request.contextPath}/js/charts.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const taskData = {
            TODO: ${taskStats.TODO != null ? taskStats.TODO : 0},
            IN_PROGRESS: ${taskStats.IN_PROGRESS != null ? taskStats.IN_PROGRESS : 0},
            IN_REVIEW: ${taskStats.IN_REVIEW != null ? taskStats.IN_REVIEW : 0},
            COMPLETED: ${taskStats.COMPLETED != null ? taskStats.COMPLETED : 0}
        };
        initTaskStatusChart('taskStatusChart', taskData);

        const bugData = {
            LOW: ${bugStats.LOW != null ? bugStats.LOW : 0},
            MEDIUM: ${bugStats.MEDIUM != null ? bugStats.MEDIUM : 0},
            HIGH: ${bugStats.HIGH != null ? bugStats.HIGH : 0},
            CRITICAL: ${bugStats.CRITICAL != null ? bugStats.CRITICAL : 0}
        };
        initBugSeverityChart('bugSeverityChart', bugData);
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
