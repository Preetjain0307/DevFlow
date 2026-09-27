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
                <div class="df-card mb-4 border-0 shadow-sm">
                    <div class="df-card-header bg-gradient-primary text-white d-flex justify-content-between align-items-center py-3" style="background: linear-gradient(135deg, #4f46e5 0%, #6366f1 100%);">
                        <div class="d-flex align-items-center gap-2">
                            <i class="bi bi-robot fs-5 text-warning"></i>
                            <h5 class="mb-0 fw-semibold text-white">AI Meeting Intelligence & Minutes</h5>
                        </div>
                        <form id="aiSummaryForm" action="${pageContext.request.contextPath}/meetings" method="POST" class="d-inline">
                            <input type="hidden" name="action" value="generateAISummary">
                            <input type="hidden" name="meetingId" value="${meeting.id}">
                            <input type="hidden" name="rawNotes" id="hiddenRawNotes" value="">
                            <button type="submit" id="btnGenerateAiMinutes" class="btn btn-light btn-sm fw-bold shadow-sm d-flex align-items-center gap-1">
                                <i class="bi bi-stars text-primary"></i>
                                <span>Generate AI Minutes</span>
                            </button>
                        </form>
                    </div>
                    <div class="df-card-body p-4">
                        <c:choose>
                            <c:when test="${not empty notes}">
                                <c:forEach var="n" items="${notes}">
                                    <div class="mb-4">
                                        <div class="d-flex justify-content-between text-muted small mb-2">
                                            <span><i class="bi bi-person-fill me-1"></i>Recorded by <strong>${n.authorName}</strong></span>
                                            <span><i class="bi bi-clock me-1"></i>${n.updatedAt != null ? n.updatedAt : n.createdAt}</span>
                                        </div>
                                        <div class="p-3 bg-light rounded border border-light-subtle small font-monospace" style="white-space: pre-wrap; max-height: 220px; overflow-y: auto;">${n.rawNotes}</div>

                                        <c:if test="${not empty n.aiSummary}">
                                            <div class="mt-3 p-3 bg-primary-subtle text-primary-emphasis rounded-3 border border-primary-subtle shadow-sm">
                                                <div class="d-flex align-items-center gap-2 mb-2">
                                                    <span class="badge bg-primary px-2 py-1"><i class="bi bi-magic me-1"></i>AI Summary</span>
                                                    <h6 class="fw-bold mb-0 text-primary-emphasis">Executive Summary</h6>
                                                </div>
                                                <div class="small lh-base" style="white-space: pre-wrap;">${n.aiSummary}</div>
                                            </div>
                                        </c:if>

                                        <div class="row g-2 mt-2">
                                            <c:if test="${not empty n.aiDecisions && n.aiDecisions != '-'}">
                                                <div class="col-md-6">
                                                    <div class="p-3 bg-success-subtle text-success-emphasis rounded-3 border border-success-subtle h-100">
                                                        <h6 class="fw-bold small mb-2"><i class="bi bi-check-circle-fill me-1 text-success"></i>Key Decisions</h6>
                                                        <div class="small" style="white-space: pre-wrap;">${n.aiDecisions}</div>
                                                    </div>
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty n.aiActionItems && n.aiActionItems != '-'}">
                                                <div class="col-md-6">
                                                    <div class="p-3 bg-warning-subtle text-warning-emphasis rounded-3 border border-warning-subtle h-100">
                                                        <h6 class="fw-bold small mb-2"><i class="bi bi-lightning-charge-fill me-1 text-warning"></i>Extracted Action Items</h6>
                                                        <div class="small" style="white-space: pre-wrap;">${n.aiActionItems}</div>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>

                                        <c:if test="${not empty n.aiResponsibilities && n.aiResponsibilities != '-'}">
                                            <div class="mt-2 p-3 bg-info-subtle text-info-emphasis rounded-3 border border-info-subtle">
                                                <h6 class="fw-bold small mb-2"><i class="bi bi-people-fill me-1 text-info"></i>Identified Assignees & Roles</h6>
                                                <div class="small" style="white-space: pre-wrap;">${n.aiResponsibilities}</div>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4 text-muted">
                                    <div class="avatar-lg bg-light rounded-circle mx-auto mb-3 d-flex align-items-center justify-content-center" style="width: 64px; height: 64px;">
                                        <i class="bi bi-chat-square-quote fs-2 text-secondary"></i>
                                    </div>
                                    <h6 class="fw-bold text-dark">No Meeting Minutes Generated Yet</h6>
                                    <p class="small text-muted mb-3" style="max-width: 420px; margin: 0 auto;">
                                        Use the preloader below to populate realistic standup notes in 1 click, then run the heuristic NLP parser to extract decisions and action items.
                                    </p>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <!-- Add / Edit Raw Notes Form -->
                        <hr class="my-3">
                        <div class="d-flex justify-content-between align-items-center mb-2 flex-wrap gap-2">
                            <label for="rawNotesTextarea" class="form-label fw-bold small mb-0 text-dark">
                                <i class="bi bi-journal-text me-1 text-primary"></i>Meeting Notes / Raw Transcript
                            </label>
                            
                            <!-- 1-Click Sample Preloader Button Group -->
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-sm btn-outline-primary fw-semibold" id="btnLoadSampleNotes" onclick="DevFlowMeeting.loadSampleNotes('standup')">
                                    <i class="bi bi-lightning-fill text-warning me-1"></i>Load Sample Standup Notes
                                </button>
                                <button type="button" class="btn btn-sm btn-outline-primary dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
                                    <span class="visually-hidden">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                                    <li><h6 class="dropdown-header">Engineering Presets</h6></li>
                                    <li><a class="dropdown-item small" href="javascript:void(0)" onclick="DevFlowMeeting.loadSampleNotes('standup')"><i class="bi bi-people me-2 text-primary"></i>Daily Standup & Sprint Sync</a></li>
                                    <li><a class="dropdown-item small" href="javascript:void(0)" onclick="DevFlowMeeting.loadSampleNotes('retro')"><i class="bi bi-arrow-repeat me-2 text-success"></i>Sprint Retrospective & Bug Triage</a></li>
                                    <li><a class="dropdown-item small" href="javascript:void(0)" onclick="DevFlowMeeting.loadSampleNotes('arch')"><i class="bi bi-cpu me-2 text-danger"></i>Architecture & Security Review</a></li>
                                </ul>
                            </div>
                        </div>

                        <form id="notesForm" action="${pageContext.request.contextPath}/meetings" method="POST">
                            <input type="hidden" name="action" value="saveNotes">
                            <input type="hidden" name="meetingId" value="${meeting.id}">
                            <div class="mb-3">
                                <textarea class="form-control form-control-sm font-monospace" id="rawNotesTextarea" name="rawNotes" rows="7" placeholder="Type key discussions, blockers, decisions made during the call, or click 'Load Sample Standup Notes'..." required></textarea>
                            </div>
                            <div class="d-flex justify-content-between align-items-center">
                                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="DevFlowMeeting.clearNotes()">
                                    <i class="bi bi-trash me-1"></i>Clear
                                </button>
                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-outline-primary btn-sm">
                                        <i class="bi bi-save me-1"></i> Save Raw Notes
                                    </button>
                                    <button type="button" class="btn btn-primary btn-sm fw-semibold" onclick="DevFlowMeeting.saveAndAnalyze()">
                                        <i class="bi bi-magic me-1"></i> Run AI Analysis
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Right Column: Action Items -->
            <div class="col-lg-5">
                <div class="df-card mb-4 border-0 shadow-sm">
                    <div class="df-card-header py-3 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-check2-circle me-2 text-success"></i>Action Items</h5>
                        <span class="badge bg-light text-dark border">${actionItems != null ? actionItems.size() : 0} Items</span>
                    </div>
                    <div class="df-card-body p-3">
                        <!-- Action Items List -->
                        <ul class="list-group list-group-flush mb-4">
                            <c:forEach var="item" items="${actionItems}">
                                <li class="list-group-item d-flex justify-content-between align-items-center px-2 py-2 border-bottom">
                                    <div>
                                        <div class="${item.completed || item.status == 'COMPLETED' ? 'text-decoration-line-through text-muted' : 'fw-semibold text-dark'} small">
                                            ${item.description}
                                        </div>
                                        <small class="text-muted"><i class="bi bi-person me-1"></i>${item.assigneeName != null ? item.assigneeName : 'Unassigned'} | Due: ${item.dueDate != null ? item.dueDate : 'No due date'}</small>
                                    </div>
                                    <c:if test="${!item.completed && item.status != 'COMPLETED'}">
                                        <form action="${pageContext.request.contextPath}/meetings" method="POST" class="d-inline">
                                            <input type="hidden" name="action" value="completeActionItem">
                                            <input type="hidden" name="actionItemId" value="${item.id}">
                                            <input type="hidden" name="meetingId" value="${meeting.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-success" title="Mark as Completed"><i class="bi bi-check-lg"></i></button>
                                        </form>
                                    </c:if>
                                </li>
                            </c:forEach>
                            <c:if test="${empty actionItems}">
                                <li class="list-group-item text-center py-4 text-muted small border-0">
                                    <i class="bi bi-clipboard-check fs-3 d-block text-secondary mb-1"></i>
                                    No action items assigned for this meeting yet.
                                </li>
                            </c:if>
                        </ul>

                        <!-- Add Action Item Form -->
                        <h6 class="fw-bold small mb-2"><i class="bi bi-plus-circle me-1 text-primary"></i>Assign Action Item</h6>
                        <form action="${pageContext.request.contextPath}/meetings" method="POST">
                            <input type="hidden" name="action" value="addActionItem">
                            <input type="hidden" name="meetingId" value="${meeting.id}">
                            <div class="mb-2">
                                <input type="text" class="form-control form-control-sm" name="description" placeholder="Action item description..." required>
                            </div>
                            <div class="row g-2 mb-2">
                                <div class="col-6">
                                    <select class="form-select form-select-sm" name="assigneeId">
                                        <option value="">-- Assignee --</option>
                                        <c:forEach var="u" items="${users}">
                                            <option value="${u.id}">${u.fullName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-6">
                                    <input type="date" class="form-control form-control-sm" name="dueDate">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-success btn-sm w-100 fw-semibold">
                                <i class="bi bi-plus-lg me-1"></i> Add Action Item
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Sample Standup Notes Preloader & AI Sync Script -->
        <script>
        const DevFlowMeeting = {
            presets: {
                standup: `Daily Standup & Sprint Sync - Core Engineering Team
Date: September 27, 2026 | Attendees: Alex (Lead), Priya (Backend), Sarah (Frontend), Mark (QA)

Updates & Discussion:
- Alex: Completed BCrypt password hashing migration and reviewed HikariCP connection pooling configurations.
- Priya: Finished the CSV export engine for Tasks and Bugs. Currently optimizing PreparedStatement batches.
- Sarah: Finalized responsive Dark/Light mode theme switching and keyboard shortcut handler for Global Quick Search.
- Mark: Discovered edge case with session timeouts during file uploads; reported defect DEF-104.

Key Decisions:
- Decided to adopt HikariCP as standard connection pool with max pool size of 15 connections.
- Agreed to enforce RFC-4180 UTF-8 BOM encoding across all exported CSV files for Excel compatibility.
- Architecture decision: maintain pure Java EE 7 Servlet standard without external framework bloat for maximum performance.

Action Items:
- Alex: Will deploy release candidate build 2.4.0 to staging environment by 5 PM.
- Priya: Must implement audit logging for all executive report print requests.
- Sarah: Will polish modal transitions for the Viva Voce architecture inspector.
- Mark: Need to verify cross-browser compatibility on Safari and Edge for WebSocket feeds.`,

                retro: `Sprint Retrospective & Defect Triage Meeting
Date: September 27, 2026 | Attendees: Alex (Lead), Sarah (PM), Mark (QA), Alan (Mentor)

Discussion & What Went Well:
- Delivered 28 story points across Kanban workflow; zero regression issues on core authentication.
- Mark completed automated smoke tests covering all 5 user persona access profiles.
- Sarah updated project burndown metrics showing 92% sprint delivery velocity.

Blockers & Defect Triage:
- Safari macOS WebRTC audio permissions need explicit allow attributes on Jitsi embed container.
- High memory footprint observed during large CSV report downloads; requires streaming writer.

Key Decisions:
- Decided to migrate file uploads to non-blocking chunked byte streams.
- Agreed to add live JVM heap memory telemetry to the internal Viva Voce dashboard.

Action Items:
- Mark: Must create automated test suite for role-based permission boundaries.
- Alex: Will add Jitsi iframe sandbox allow attributes for Safari WebRTC support.
- Priya: Will review database indexes on audit_logs table for query optimization.`,

                arch: `Technical Architecture & Security Evaluation Review
Date: September 27, 2026 | Evaluators: Dr. Alan (Faculty), Alex (Lead Architect)

Core Architecture Defense:
- Evaluated 3-Tier Enterprise Java Architecture: JSP/JSTL View Layer, Servlet Controller Layer, JDBC PreparedStatement DAO Layer.
- Validated thread safety of Servlet singletons: avoiding mutable instance fields and utilizing request-scoped state.
- Verified HikariCP connection pooling metrics with zero connection leaks under concurrent load.

Security & Governance Decisions:
- Decided to mandate OWASP session fixation protections via Session.invalidate() and Session.regenerateId().
- Decided to implement SHA-256 integrity checks on exported executive evaluation reports.

Action Items:
- Alex: Will compile architecture slides and HikariCP telemetry benchmark graphs for examiner review.
- Sarah: Will prepare printable executive project summaries with academic guide signature blocks.
- Priya: Must verify BCrypt salt work factor 12 across all database seed accounts.`
            },

            loadSampleNotes: function(presetKey) {
                const text = this.presets[presetKey] || this.presets.standup;
                const textarea = document.getElementById('rawNotesTextarea');
                if (textarea) {
                    textarea.value = text;
                    textarea.focus();
                    if (window.DevFlow && typeof window.DevFlow.toast === 'function') {
                        window.DevFlow.toast('Sample standup notes loaded! Click "Run AI Analysis" to parse.', 'success');
                    }
                }
            },

            clearNotes: function() {
                const textarea = document.getElementById('rawNotesTextarea');
                if (textarea) {
                    textarea.value = '';
                    textarea.focus();
                }
            },

            saveAndAnalyze: function() {
                const textarea = document.getElementById('rawNotesTextarea');
                if (!textarea || !textarea.value.trim()) {
                    if (window.DevFlow && typeof window.DevFlow.toast === 'function') {
                        window.DevFlow.toast('Please enter notes or click "Load Sample Standup Notes" first.', 'warning');
                    } else {
                        alert('Please enter notes or click "Load Sample Standup Notes" first.');
                    }
                    return;
                }
                const hiddenInput = document.getElementById('hiddenRawNotes');
                if (hiddenInput) {
                    hiddenInput.value = textarea.value;
                }
                const aiForm = document.getElementById('aiSummaryForm');
                if (aiForm) {
                    if (window.DevFlow && typeof window.DevFlow.toast === 'function') {
                        window.DevFlow.toast('Generating AI Meeting Intelligence...', 'info');
                    }
                    aiForm.submit();
                }
            }
        };

        // Sync hiddenRawNotes whenever user types or clicks header button
        document.addEventListener('DOMContentLoaded', function() {
            const btnGenerate = document.getElementById('btnGenerateAiMinutes');
            const textarea = document.getElementById('rawNotesTextarea');
            const hiddenRawNotes = document.getElementById('hiddenRawNotes');
            if (btnGenerate && textarea && hiddenRawNotes) {
                btnGenerate.addEventListener('click', function() {
                    hiddenRawNotes.value = textarea.value;
                });
            }
        });
        </script>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
