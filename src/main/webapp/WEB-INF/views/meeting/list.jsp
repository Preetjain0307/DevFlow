<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Video Meetings & Standups" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Video Meetings & Standups</h1>
                <p class="page-subtitle">Live team conferencing powered by Jitsi Meet & AI-generated Meeting Minutes</p>
            </div>
            <a href="${pageContext.request.contextPath}/meetings?action=schedule${projectId != null ? '&projectId='.concat(projectId) : ''}" class="btn btn-df-primary btn-sm">
                <i class="bi bi-camera-video me-1"></i> Schedule Meeting
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Project Filter -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/meetings" method="GET" class="row g-3 align-items-center">
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
            <c:forEach var="m" items="${meetings}">
                <div class="col-md-6 col-lg-4">
                    <div class="df-card h-100 d-flex flex-column">
                        <div class="df-card-header d-flex justify-content-between align-items-center py-3">
                            <span class="badge badge-soft-primary">${m.meetingType}</span>
                            <span class="badge bg-${m.status == 'SCHEDULED' ? 'info' : (m.status == 'IN_PROGRESS' ? 'success' : 'secondary')}">${m.status}</span>
                        </div>
                        <div class="df-card-body flex-grow-1 p-3">
                            <h5 class="fw-bold text-dark mb-2">${m.title}</h5>
                            <p class="card-text text-muted small mb-3">${m.agenda != null ? m.agenda : 'No agenda specified.'}</p>
                            
                            <div class="small text-muted mb-2">
                                <i class="bi bi-folder me-1 text-primary"></i> ${m.projectName}
                            </div>
                            <div class="small text-muted mb-2">
                                <i class="bi bi-calendar-event me-1 text-info"></i> ${m.scheduledAt} (${m.durationMinutes} mins)
                            </div>
                            <div class="small text-muted mb-3">
                                <i class="bi bi-person me-1 text-secondary"></i> Host: ${m.hostName}
                            </div>
                        </div>
                        <div class="df-card-footer border-top d-flex justify-content-between p-3">
                            <a href="${pageContext.request.contextPath}/meetings?action=view&id=${m.meetingId}" class="btn btn-outline-primary btn-sm">
                                <i class="bi bi-info-circle me-1"></i> Details & AI Notes
                            </a>
                            <c:if test="${m.status != 'COMPLETED'}">
                                <a href="${pageContext.request.contextPath}/meetings?action=join&id=${m.meetingId}" target="_blank" class="btn btn-success btn-sm">
                                    <i class="bi bi-camera-video-fill me-1"></i> Join
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty meetings}">
                <div class="col-12">
                    <div class="df-card py-5 text-center text-muted">
                        <i class="bi bi-camera-video fs-1 mb-2 text-primary"></i>
                        <h5>No scheduled meetings found</h5>
                        <p class="mb-0">Schedule a sprint review, daily standup, or faculty presentation!</p>
                    </div>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
