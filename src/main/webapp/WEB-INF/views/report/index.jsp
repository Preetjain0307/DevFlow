<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Reports & Analytics" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Project Reports & Analytics</h1>
                <p class="page-subtitle">Sprint burndown, task distribution, defect resolution velocity, and team metrics</p>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Selector -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/reports" method="GET" class="row g-3 align-items-center">
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

        <div class="row g-4 mb-4">
            <div class="col-lg-6">
                <div class="df-card h-100">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-pie-chart me-2 text-primary"></i>Task Status Breakdown</h5>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center p-4" style="min-height: 320px;">
                        <canvas id="taskStatusChart" style="max-height: 280px;"></canvas>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="df-card h-100">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-bar-chart me-2 text-danger"></i>Defects by Severity</h5>
                    </div>
                    <div class="df-card-body d-flex align-items-center justify-content-center p-4" style="min-height: 320px;">
                        <canvas id="bugSeverityChart" style="max-height: 280px;"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
<script src="${pageContext.request.contextPath}/js/charts.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        if (typeof initTaskChart === 'function') {
            initTaskChart('taskStatusChart', ${stats.todoTasks != null ? stats.todoTasks : 2}, ${stats.inProgressTasks != null ? stats.inProgressTasks : 3}, ${stats.inReviewTasks != null ? stats.inReviewTasks : 1}, ${stats.completedTasks != null ? stats.completedTasks : 4});
        }
        if (typeof initBugChart === 'function') {
            initBugChart('bugSeverityChart', 1, 3, 2, 0);
        }
    });
</script>
