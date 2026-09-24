<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Upload Document" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Upload Project Document</h1>
                <p class="page-subtitle">Upload SRS specifications, architecture diagrams, or technical presentations</p>
            </div>
            <a href="${pageContext.request.contextPath}/documents?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Documents
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/documents" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="upload">

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
                                <label for="title" class="form-label fw-bold">Document Title <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="e.g., Software Requirements Specification v1.0" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" placeholder="Brief summary of this document..."></textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="category" class="form-label fw-bold">Category</label>
                                    <select class="form-select" id="category" name="category">
                                        <option value="SRS">SRS / Requirements</option>
                                        <option value="ARCHITECTURE">Architecture & Diagrams</option>
                                        <option value="DESIGN">UI/UX Design Specs</option>
                                        <option value="REPORT">Progress / Final Report</option>
                                        <option value="MANUAL">User Manual</option>
                                        <option value="OTHER">Other</option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="version" class="form-label fw-bold">Version</label>
                                    <input type="text" class="form-control" id="version" name="version" value="1.0" placeholder="1.0">
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="file" class="form-label fw-bold">Select File (PDF, DOCX, PNG, ZIP) <span class="text-danger">*</span></label>
                                <input class="form-control" type="file" id="file" name="file" required>
                            </div>

                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-cloud-upload me-1"></i> Upload File
                                </button>
                                <a href="${pageContext.request.contextPath}/documents?action=list" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
