<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Edit Project - ${project.name}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Edit Project</h1>
                <p class="page-subtitle">Update project metadata, milestones, and status</p>
            </div>
            <a href="${pageContext.request.contextPath}/projects?action=view&id=${project.projectId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Project
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row">
            <div class="col-lg-8">
                <div class="df-card">
                    <div class="df-card-body p-4">
                        <form action="${pageContext.request.contextPath}/projects" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="projectId" value="${project.projectId}">

                            <div class="mb-3">
                                <label for="name" class="form-label fw-bold">Project Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="name" name="name" value="${project.name}" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="4">${project.description}</textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="startDate" class="form-label fw-bold">Start Date</label>
                                    <input type="date" class="form-control" id="startDate" name="startDate" value="${project.startDate}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="endDate" class="form-label fw-bold">End Date</label>
                                    <input type="date" class="form-control" id="endDate" name="endDate" value="${project.endDate}">
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="status" class="form-label fw-bold">Status</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="ACTIVE" ${project.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                                    <option value="PLANNING" ${project.status == 'PLANNING' ? 'selected' : ''}>Planning</option>
                                    <option value="ON_HOLD" ${project.status == 'ON_HOLD' ? 'selected' : ''}>On Hold</option>
                                    <option value="COMPLETED" ${project.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                    <option value="ARCHIVED" ${project.status == 'ARCHIVED' ? 'selected' : ''}>Archived</option>
                                </select>
                            </div>

                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-df-primary px-4">
                                    <i class="bi bi-save me-1"></i> Save Changes
                                </button>
                                <a href="${pageContext.request.contextPath}/projects?action=view&id=${project.projectId}" class="btn btn-light">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
