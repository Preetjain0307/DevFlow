<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Schedule Meeting" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Schedule Video Meeting</h1>
                <p class="page-subtitle">Schedule video standup, sprint demo, or faculty milestone evaluation</p>
            </div>
            <a href="${pageContext.request.contextPath}/meetings?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Meetings
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/meetings" method="POST">
                            <input type="hidden" name="action" value="create">

                            <div class="mb-3">
                                <label for="projectId" class="form-label fw-bold">Project <span class="text-danger">*</span></label>
                                <select class="form-select" id="projectId" name="projectId" required>
                                    <option value="">-- Select Project --</option>
                                    <c:forEach var="p" items="${projects}">
                                        <option value="${p.projectId}" ${selectedProjectId == p.projectId ? 'selected' : ''}>${p.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="title" class="form-label fw-bold">Meeting Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="e.g., Sprint 2 Standup / Faculty Checkpoint" required>
                            </div>

                            <div class="mb-3">
                                <label for="agenda" class="form-label fw-bold">Agenda</label>
                                <textarea class="form-control" id="agenda" name="agenda" rows="4" placeholder="Points to discuss during the session..."></textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="meetingType" class="form-label fw-bold">Type</label>
                                    <select class="form-select" id="meetingType" name="meetingType">
                                        <option value="STANDUP">Daily Standup</option>
                                        <option value="SPRINT_PLANNING">Sprint Planning</option>
                                        <option value="SPRINT_REVIEW">Sprint Review</option>
                                        <option value="FACULTY_REVIEW">Faculty Review</option>
                                        <option value="ADHOC">Ad-hoc</option>
                                    </select>
                                </div>
                                <div class="col-md-5 mb-3">
                                    <label for="scheduledAt" class="form-label fw-bold">Date & Time <span class="text-danger">*</span></label>
                                    <input type="datetime-local" class="form-control" id="scheduledAt" name="scheduledAt" required>
                                </div>
                                <div class="col-md-3 mb-3">
                                    <label for="durationMinutes" class="form-label fw-bold">Duration (min)</label>
                                    <input type="number" class="form-control" id="durationMinutes" name="durationMinutes" value="30" min="5" max="240">
                                </div>
                            </div>

                            <div class="d-flex justify-content-between mt-4">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-calendar-check me-1"></i> Schedule & Generate Room
                                </button>
                                <a href="${pageContext.request.contextPath}/meetings?action=list" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
