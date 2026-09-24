<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="System Settings - Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">System Settings</h1>
                <p class="page-subtitle">Configure AI Providers, Jitsi Video, GitHub API, and Platform Parameters</p>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/admin" method="POST">
                            <input type="hidden" name="action" value="saveSettings">

                            <h5 class="fw-bold mb-3"><i class="bi bi-robot me-2 text-primary"></i>AI Provider Configuration</h5>
                            <div class="mb-3">
                                <label class="form-label fw-bold small">AI Meeting Minutes Engine</label>
                                <select class="form-select" name="setting_AI_PROVIDER">
                                    <option value="HEURISTIC">Local Intelligent NLP Extractor (No API Key Required)</option>
                                    <option value="GEMINI">Google Gemini API (Live)</option>
                                    <option value="OPENAI">OpenAI GPT-4o-mini (Live)</option>
                                </select>
                            </div>
                            <div class="mb-4">
                                <label class="form-label fw-bold small">AI API Key (Optional)</label>
                                <input type="password" class="form-control" name="setting_AI_API_KEY" placeholder="AIzaSy... or sk-...">
                                <div class="form-text">Leave blank to use the built-in local heuristic AI summarizer fallback.</div>
                            </div>

                            <hr class="my-4">

                            <h5 class="fw-bold mb-3"><i class="bi bi-camera-video me-2 text-info"></i>Video Conferencing (Jitsi)</h5>
                            <div class="mb-4">
                                <label class="form-label fw-bold small">Jitsi Domain Server</label>
                                <input type="text" class="form-control" name="setting_JITSI_DOMAIN" value="meet.jit.si">
                            </div>

                            <hr class="my-4">

                            <h5 class="fw-bold mb-3"><i class="bi bi-shield-lock me-2 text-success"></i>Voting & Governance</h5>
                            <div class="mb-4">
                                <label class="form-label fw-bold small">Idea Auto-Advance Threshold (Net Upvotes to trigger Faculty Review)</label>
                                <input type="number" class="form-control" name="setting_VOTE_THRESHOLD" value="3" min="1" max="50">
                            </div>

                            <button type="submit" class="btn btn-df-primary px-4">
                                <i class="bi bi-save me-1"></i> Save System Configuration
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
