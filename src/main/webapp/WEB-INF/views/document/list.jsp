<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Documents & Repository" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Project Documents & Repository</h1>
                <p class="page-subtitle">Centralized storage for SRS, Architecture Diagrams, Specs, and Assets</p>
            </div>
            <a href="${pageContext.request.contextPath}/documents?action=upload${projectId != null ? '&projectId='.concat(projectId) : ''}" class="btn btn-df-primary btn-sm">
                <i class="bi bi-cloud-arrow-up me-1"></i> Upload Document
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <!-- Filter -->
        <div class="df-card mb-4">
            <div class="df-card-body p-3">
                <form action="${pageContext.request.contextPath}/documents" method="GET" class="row g-3 align-items-center">
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

        <!-- Documents Table -->
        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Document Title</th>
                            <th>Category</th>
                            <th>Version</th>
                            <th>File Size</th>
                            <th>Uploaded By</th>
                            <th>Date</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${documents}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-file-earmark-pdf fs-4 text-danger me-2"></i>
                                        <div>
                                            <div class="fw-semibold text-dark">${d.title}</div>
                                            <small class="text-muted">${d.fileName}</small>
                                        </div>
                                    </div>
                                </td>
                                <td><span class="badge badge-soft-info">${d.category}</span></td>
                                <td><span class="badge bg-secondary">v${d.version}</span></td>
                                <td><small class="text-muted">${d.formattedFileSize != null ? d.formattedFileSize : (d.fileSize / 1024).concat(' KB')}</small></td>
                                <td>
                                    <span class="fw-medium text-dark">${d.uploadedByName}</span>
                                </td>
                                <td><small class="text-muted">${d.createdAt}</small></td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/documents?action=download&id=${d.documentId}" class="btn btn-sm btn-outline-primary" title="Download Document">
                                        <i class="bi bi-download me-1"></i> Download
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty documents}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="bi bi-folder2-open fs-1 d-block mb-2 text-secondary"></i>
                                    No documents uploaded for this project yet.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
