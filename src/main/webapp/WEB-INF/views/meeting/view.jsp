<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="${meeting.title} - Meeting Room" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <div class="d-flex align-items-center gap-2">
                    <h1 class="page-title mb-0">${meeting.title}</h1>
                    <span class="badge badge-soft-primary">${meeting.meetingType}</span>
                    <span class="badge bg-${meeting.status == 'SCHEDULED' ? 'info' : (meeting.status == 'IN_PROGRESS' ? 'success' : 'secondary')}">${meeting.status}</span>
                </div>
                <p class="page-subtitle mt-1">Project: <strong>${meeting.projectName}</strong> | Host: <strong>${meeting.hostName}</strong></p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a href="${pageContext.request.contextPath}/meetings?action=list&projectId=${meeting.projectId}" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-arrow-left me-1"></i> Back to Meetings
                </a>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Jitsi Meet Video Conference Embed -->
        <div class="df-card mb-4" id="jitsiRoom">
            <div class="df-card-header bg-dark text-white d-flex justify-content-between align-items-center py-2">
                <div class="d-flex align-items-center">
                    <i class="bi bi-camera-video me-2 text-danger"></i>
                    <span class="fw-bold">Live Jitsi Video Room: ${meeting.roomName}</span>
                </div>
                <a href="${meeting.meetingUrl}" target="_blank" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-box-arrow-up-right me-1"></i> Open in Full Window
                </a>
            </div>
            <div class="p-0 bg-black" style="min-height: 480px; position: relative;">
                <iframe src="https://meet.jit.si/${meeting.roomName}#config.prejoinPageEnabled=false&userInfo.displayName=${sessionScope.currentUser.fullName}" 
                        allow="camera; microphone; fullscreen; display-capture; autoplay" 
                        style="width: 100%; height: 520px; border: 0;"
                        id="jitsiFrame">
                </iframe>
            </div>
        </div>

        <div class="row g-4">
            <!-- Left Column: Notes & AI Summary Generator -->
            <div class="col-lg-7">
                <!-- AI Summary & Key Takeaways Card -->
                <div class="df-card mb-4">
                    <div class="df-card-header bg-primary text-white d-flex justify-content-between align-items-center py-3">
                        <h5 class="mb-0 fw-semibold text-white"><i class="bi bi-stars me-2"></i>AI Meeting Summary & Insights</h5>
                        <form action="${pageContext.request.contextPath}/meetings" method="POST" class="d-inline">
                            <input type="hidden" name="action" value="generateAISummary">
                            <input type="hidden" name="meetingId" value="${meeting.meetingId}">
                            <button type="submit" class="btn btn-light btn-sm fw-bold">
                                <i class="bi bi-robot me-1"></i> Generate AI Minutes
                            </button>
                        </form>
                    </div>
                    <div class="df-card-body p-3">
                        <c:choose>
                            <c:when test="${not empty notes}">
                                <c:forEach var="n" items="${notes}">
                                    <div class="mb-3">
                                        <div class="d-flex justify-content-between text-muted small mb-1">
                                            <span>Recorded by <strong>${n.authorName}</strong></span>
                                            <span>${n.createdAt}</span>
                                        </div>
                                        <div class="p-3 bg-light rounded" style="white-space: pre-wrap;">${n.rawNotes}</div>
                                        <c:if test="${not empty n.aiSummary}">
                                            <div class="mt-2 p-3 bg-primary-subtle text-primary-emphasis rounded border border-primary-subtle">
                                                <h6 class="fw-bold"><i class="bi bi-magic me-1"></i> AI Executive Summary:</h6>
                                                <div style="white-space: pre-wrap;">${n.aiSummary}</div>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4 text-muted">
                                    <i class="bi bi-card-text fs-2 d-block mb-2"></i>
                                    <p>No meeting notes recorded yet. Add notes below or click 'Generate AI Minutes' after adding raw notes.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Add Raw Notes Form -->
                        <hr>
                        <form action="${pageContext.request.contextPath}/meetings" method="POST">
                            <input type="hidden" name="action" value="saveNotes">
                            <input type="hidden" name="meetingId" value="${meeting.meetingId}">
                            <div class="mb-2">
                                <label class="form-label fw-bold small">Meeting Notes / Transcript</label>
                                <textarea class="form-control" name="rawNotes" rows="4" placeholder="Type key discussions, blockers, decisions made during the call..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-outline-primary btn-sm">
                                <i class="bi bi-save me-1"></i> Save Notes
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Right Column: Action Items -->
            <div class="col-lg-5">
                <div class="df-card mb-4">
                    <div class="df-card-header py-3 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-check2-circle me-2 text-success"></i>Action Items</h5>
                        <span class="badge bg-light text-dark border">${actionItems.size()} Items</span>
                    </div>
                    <div class="df-card-body p-3">
                        <!-- Action Items List -->
                        <ul class="list-group list-group-flush mb-4">
                            <c:forEach var="item" items="${actionItems}">
                                <li class="list-group-item d-flex justify-content-between align-items-center p-2">
                                    <div>
                                        <div class="${item.status == 'COMPLETED' ? 'text-decoration-line-through text-muted' : 'fw-semibold'}">
                                            ${item.description}
                                        </div>
                                        <small class="text-muted"><i class="bi bi-person me-1"></i>${item.assigneeName != null ? item.assigneeName : 'Unassigned'}</small>
                                    </div>
                                    <c:if test="${item.status != 'COMPLETED'}">
                                        <form action="${pageContext.request.contextPath}/meetings" method="POST" class="d-inline">
                                            <input type="hidden" name="action" value="completeActionItem">
                                            <input type="hidden" name="actionItemId" value="${item.actionItemId}">
                                            <input type="hidden" name="meetingId" value="${meeting.meetingId}">
                                            <button type="submit" class="btn btn-sm btn-outline-success"><i class="bi bi-check-lg"></i></button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                            <c:if test="${empty actionItems}">
                                <li class="list-group-item text-center py-3 text-muted small">No action items assigned.</li>
                            </c:if>
                        </ul>

                        <!-- Add Action Item Form -->
                        <h6 class="fw-bold small mb-2">Assign Action Item</h6>
                        <form action="${pageContext.request.contextPath}/meetings" method="POST">
                            <input type="hidden" name="action" value="addActionItem">
                            <input type="hidden" name="meetingId" value="${meeting.meetingId}">
                            <div class="mb-2">
                                <input type="text" class="form-control form-control-sm" name="description" placeholder="Action item description..." required>
                            </div>
                            <div class="row g-2 mb-2">
                                <div class="col-6">
                                    <select class="form-select form-select-sm" name="assigneeId">
                                        <option value="">-- Assignee --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.userId}">${u.fullName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-6">
                                    <input type="date" class="form-control form-control-sm" name="dueDate">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-success btn-sm w-100">
                                <i class="bi bi-plus-lg me-1"></i> Add Action Item
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
