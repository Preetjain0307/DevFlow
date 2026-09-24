<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Propose Idea" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Submit Idea / Change Proposal</h1>
                <p class="page-subtitle">Submit a proposal for peer review, team voting, and faculty approval</p>
            </div>
            <a href="${pageContext.request.contextPath}/ideas?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Ideas
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/ideas" method="POST">
                            <input type="hidden" name="action" value="submit">

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
                                <label for="title" class="form-label fw-bold">Proposal Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="e.g., Implement Redis Caching for Hot API Endpoints" required>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="category" class="form-label fw-bold">Category</label>
                                    <select class="form-select" id="category" name="category">
                                        <option value="FEATURE">New Feature</option>
                                        <option value="ARCHITECTURE">Architecture Refactor</option>
                                        <option value="OPTIMIZATION">Performance Optimization</option>
                                        <option value="SECURITY">Security Enhancement</option>
                                        <option value="PROCESS">Development Process</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label for="impactLevel" class="form-label fw-bold">Estimated Impact</label>
                                    <select class="form-select" id="impactLevel" name="impactLevel">
                                        <option value="LOW">Low Impact</option>
                                        <option value="MEDIUM" selected>Medium Impact</option>
                                        <option value="HIGH">High Impact</option>
                                        <option value="CRITICAL">Major Architecture Shift</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Proposal Description <span class="text-danger">*</span></label>
                                <textarea class="form-control" id="description" name="description" rows="5" placeholder="Explain the context, proposed solution, trade-offs, and implementation plan..." required></textarea>
                            </div>

                            <div class="mb-4">
                                <label for="justification" class="form-label fw-bold">Business / Technical Justification</label>
                                <textarea class="form-control" id="justification" name="justification" rows="3" placeholder="Why should this be implemented? What problem does it solve?"></textarea>
                            </div>

                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-warning px-4 fw-bold">
                                    <i class="bi bi-send-check me-1"></i> Submit Proposal for Voting
                                </button>
                                <a href="${pageContext.request.contextPath}/ideas?action=list" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
