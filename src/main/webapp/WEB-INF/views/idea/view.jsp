<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Proposal: ${idea.title}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <div class="d-flex align-items-center gap-2">
                    <h1 class="page-title mb-0">${idea.title}</h1>
                    <span class="badge badge-soft-primary">${idea.category}</span>
                    <span class="badge bg-${idea.status == 'APPROVED' ? 'success' : (idea.status == 'REJECTED' ? 'danger' : (idea.status == 'UNDER_FACULTY_REVIEW' || idea.status == 'PENDING_FACULTY' ? 'info' : 'warning text-dark'))}">
                        ${idea.status}
                    </span>
                </div>
                <p class="page-subtitle mt-1">Project: <strong>${idea.projectName}</strong> | Proposed by: <strong>${idea.authorName}</strong></p>
            </div>
            <a href="${pageContext.request.contextPath}/ideas?action=list&projectId=${idea.projectId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Ideas
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row g-4">
            <!-- Left Column: Details, Justification, Comments -->
            <div class="col-lg-8">
                <!-- Proposal Content -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-file-text me-2 text-primary"></i>Proposal Details</h5>
                    </div>
                    <div class="df-card-body p-3">
                        <h6>Description & Plan</h6>
                        <div class="p-3 bg-light rounded mb-4" style="white-space: pre-wrap;">${idea.description}</div>

                        <c:if test="${not empty idea.justification}">
                            <h6>Business & Technical Justification</h6>
                            <div class="p-3 bg-light rounded mb-4" style="white-space: pre-wrap;">${idea.justification}</div>
                        </c:if>

                        <!-- If converted into task -->
                        <c:if test="${idea.convertedTaskId != null}">
                            <div class="alert alert-success d-flex align-items-center mb-0">
                                <i class="bi bi-check-circle-fill fs-4 me-3"></i>
                                <div>
                                    <strong>Approved & Converted to Task!</strong><br>
                                    This approved proposal was automatically turned into Task #${idea.convertedTaskId}.
                                    <a href="${pageContext.request.contextPath}/tasks?action=view&id=${idea.convertedTaskId}" class="alert-link fw-bold">View Task</a>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Faculty Decision Section (if reviewed) -->
                <c:if test="${not empty idea.facultyNotes || idea.status == 'APPROVED' || idea.status == 'REJECTED'}">
                    <div class="df-card mb-4">
                        <div class="df-card-header py-3">
                            <h5 class="mb-0 fw-semibold"><i class="bi bi-mortarboard me-2 text-primary"></i>Faculty Review Decision</h5>
                        </div>
                        <div class="df-card-body p-3">
                            <div class="d-flex justify-content-between text-muted small mb-2">
                                <span>Faculty Mentor: <strong>${idea.facultyReviewerName != null ? idea.facultyReviewerName : 'Assigned Mentor'}</strong></span>
                                <span>Decision: <strong class="text-${idea.status == 'APPROVED' ? 'success' : 'danger'}">${idea.status}</strong></span>
                            </div>
                            <div class="p-3 bg-light rounded" style="white-space: pre-wrap;">${idea.facultyNotes != null ? idea.facultyNotes : 'No written remarks provided.'}</div>
                        </div>
                    </div>
                </c:if>

                <!-- Discussion & Peer Comments -->
                <div class="df-card">
                    <div class="df-card-header py-3 d-flex justify-content-between align-items-center">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-chat-left-dots me-2 text-info"></i>Peer Discussion</h5>
                        <span class="badge bg-light text-dark border" id="ideaCommentCount">${comments.size()} Comments</span>
                    </div>
                    <div class="df-card-body p-3">
                        <!-- Comment form -->
                        <form id="ideaCommentForm" action="${pageContext.request.contextPath}/ideas" method="POST" class="mb-4">
                            <input type="hidden" name="action" value="addComment">
                            <input type="hidden" name="ideaId" value="${idea.ideaId}">
                            <div class="mb-2">
                                <textarea class="form-control" name="comment" rows="3" placeholder="Share your feedback, architectural concerns, or suggestions..." required></textarea>
                            </div>
                            <div class="text-end">
                                <button type="submit" class="btn btn-df-primary btn-sm px-3">
                                    <i class="bi bi-send me-1"></i> Post Comment
                                </button>
                            </div>
                        </form>

                        <hr>

                        <!-- Comments list -->
                        <div class="comments-list" id="ideaCommentsList">
                            <c:forEach var="c" items="${comments}">
                                <div class="d-flex mb-3">
                                    <div class="avatar bg-warning text-dark rounded-circle me-3 d-flex align-items-center justify-content-center flex-shrink-0" style="width:36px;height:36px;">
                                        ${c.userFullName != null ? c.userFullName.substring(0,1).toUpperCase() : 'U'}
                                    </div>
                                    <div class="flex-grow-1 bg-light p-3 rounded">
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold">${c.userFullName}</span>
                                            <small class="text-muted">${c.createdAt}</small>
                                        </div>
                                        <p class="mb-0 small" style="white-space: pre-wrap;">${c.comment}</p>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${empty comments}">
                                <p class="text-muted small text-center my-3">No comments posted yet.</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Right Column: Voting & Faculty Actions -->
            <div class="col-lg-4">
                <!-- Voting Panel -->
                <div class="df-card mb-4">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-hand-thumbs-up me-2 text-success"></i>Team Peer Voting</h5>
                    </div>
                    <div class="df-card-body text-center p-3">
                        <div class="d-flex justify-content-center gap-4 my-3">
                            <div>
                                <div class="display-6 fw-bold text-success" id="ideaUpvotesCount">${idea.upvotes}</div>
                                <div class="small text-muted">Upvotes</div>
                            </div>
                            <div class="vr"></div>
                            <div>
                                <div class="display-6 fw-bold text-danger" id="ideaDownvotesCount">${idea.downvotes}</div>
                                <div class="small text-muted">Downvotes</div>
                            </div>
                        </div>

                        <!-- Vote Actions -->
                        <c:if test="${idea.status != 'APPROVED' && idea.status != 'REJECTED'}">
                            <c:set var="userVotedUp" value="${idea.userVote == 'YES' || idea.userVote == 'UPVOTE'}" />
                            <c:set var="userVotedDown" value="${idea.userVote == 'NO' || idea.userVote == 'DOWNVOTE'}" />
                            <div class="d-flex justify-content-center gap-2 mt-4" id="voteActionContainer">
                                <form action="${pageContext.request.contextPath}/ideas" method="POST" class="d-inline ajax-vote-form" data-vote="YES">
                                    <input type="hidden" name="action" value="vote">
                                    <input type="hidden" name="ideaId" value="${idea.ideaId}">
                                    <input type="hidden" name="vote" value="YES">
                                    <input type="hidden" name="voteType" value="UPVOTE">
                                    <button type="submit" id="btnVoteUp" class="btn ${userVotedUp ? 'btn-success text-white' : 'btn-outline-success'} px-3 shadow-sm">
                                        <i class="bi bi-hand-thumbs-up-fill me-1"></i> Upvote
                                    </button>
                                </form>

                                <form action="${pageContext.request.contextPath}/ideas" method="POST" class="d-inline ajax-vote-form" data-vote="NO">
                                    <input type="hidden" name="action" value="vote">
                                    <input type="hidden" name="ideaId" value="${idea.ideaId}">
                                    <input type="hidden" name="vote" value="NO">
                                    <input type="hidden" name="voteType" value="DOWNVOTE">
                                    <button type="submit" id="btnVoteDown" class="btn ${userVotedDown ? 'btn-danger text-white' : 'btn-outline-danger'} px-3 shadow-sm">
                                        <i class="bi bi-hand-thumbs-down-fill me-1"></i> Downvote
                                    </button>
                                </form>
                            </div>
                            <div id="userVoteBadge" class="mt-2 small text-muted">
                                <c:choose>
                                    <c:when test="${userVotedUp}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle"><i class="bi bi-check-circle me-1"></i>You upvoted this proposal</span>
                                    </c:when>
                                    <c:when test="${userVotedDown}">
                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle"><i class="bi bi-x-circle me-1"></i>You downvoted this proposal</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-secondary opacity-75">Click to cast your peer vote</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Faculty Action Card -->
                <c:if test="${(sessionScope.currentUser.roleName == 'FACULTY' || sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'FACULTY' || sessionScope.currentUser.role.roleName == 'ADMIN') && idea.status != 'APPROVED' && idea.status != 'REJECTED'}">
                    <div class="df-card mb-4 border-primary">
                        <div class="df-card-header bg-primary text-white py-3">
                            <h5 class="mb-0 fw-semibold text-white"><i class="bi bi-mortarboard me-2"></i>Faculty Review Decision</h5>
                        </div>
                        <div class="df-card-body p-3">
                            <form action="${pageContext.request.contextPath}/ideas" method="POST">
                                <input type="hidden" name="action" value="facultyDecision">
                                <input type="hidden" name="ideaId" value="${idea.ideaId}">

                                <div class="mb-3">
                                    <label for="decision" class="form-label fw-bold small">Decision</label>
                                    <select class="form-select form-select-sm" id="decision" name="decision" required>
                                        <option value="APPROVE">Approve & Auto-Create Task</option>
                                        <option value="REJECT">Reject Proposal</option>
                                        <option value="REQUEST_CHANGES">Request Revisions</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="facultyNotes" class="form-label fw-bold small">Faculty Remarks / Guidance</label>
                                    <textarea class="form-control form-control-sm" id="facultyNotes" name="facultyNotes" rows="3" placeholder="Guidance for the student developer..."></textarea>
                                </div>

                                <button type="submit" class="btn btn-df-primary btn-sm w-100 fw-bold">
                                    <i class="bi bi-check-circle me-1"></i> Submit Decision
                                </button>
                            </form>
                        </div>
                    </div>
                </c:if>

                <!-- Metadata -->
                <div class="df-card">
                    <div class="df-card-body p-3">
                        <ul class="list-group list-group-flush small">
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Impact Level:</span>
                                <span class="fw-semibold">${idea.impactLevel}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between px-0">
                                <span class="text-muted">Proposed Date:</span>
                                <span class="fw-semibold">${idea.createdAt}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
