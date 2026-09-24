<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Team Members - ${project.name}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Team Members</h1>
                <p class="page-subtitle">Manage roles and project team members for <strong>${project.name}</strong></p>
            </div>
            <a href="${pageContext.request.contextPath}/projects?action=view&id=${project.projectId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to Project
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="row g-4">
            <!-- Current Team Members -->
            <div class="col-lg-7">
                <div class="df-card">
                    <div class="df-card-header py-3">
                        <h5 class="mb-0 fw-semibold"><i class="bi bi-people me-2 text-primary"></i>Assigned Team (${members.size()})</h5>
                    </div>
                    <div class="table-responsive">
                        <table class="df-table align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Member</th>
                                    <th>Project Role</th>
                                    <th>Joined At</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="member" items="${members}">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center">
                                                <div class="avatar bg-primary text-white rounded-circle me-2 d-flex align-items-center justify-content-center" style="width:32px;height:32px;font-size:13px;">
                                                    ${member.userFullName != null ? member.userFullName.substring(0,1).toUpperCase() : 'U'}
                                                </div>
                                                <div>
                                                    <div class="fw-semibold text-dark">${member.userFullName}</div>
                                                    <small class="text-muted">${member.userEmail}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td><span class="badge badge-soft-info">${member.projectRole}</span></td>
                                        <td><small class="text-muted">${member.joinedAt}</small></td>
                                        <td class="text-end">
                                            <c:if test="${sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.roleName == 'PROJECT_MANAGER' || sessionScope.currentUser.role.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'PROJECT_MANAGER'}">
                                                <form action="${pageContext.request.contextPath}/projects" method="POST" class="d-inline" onsubmit="return confirm('Remove this member from the project?');">
                                                    <input type="hidden" name="action" value="removeMember">
                                                    <input type="hidden" name="projectId" value="${project.projectId}">
                                                    <input type="hidden" name="userId" value="${member.userId}">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Remove Member"><i class="bi bi-trash"></i></button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty members}">
                                    <tr><td colspan="4" class="text-center py-4 text-muted">No members assigned yet.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Add New Member Form -->
            <c:if test="${sessionScope.currentUser.roleName == 'ADMIN' || sessionScope.currentUser.roleName == 'PROJECT_MANAGER' || sessionScope.currentUser.role.roleName == 'ADMIN' || sessionScope.currentUser.role.roleName == 'PROJECT_MANAGER'}">
                <div class="col-lg-5">
                    <div class="df-card">
                        <div class="df-card-header py-3">
                            <h5 class="mb-0 fw-semibold"><i class="bi bi-person-plus me-2 text-success"></i>Add Member</h5>
                        </div>
                        <div class="df-card-body p-4">
                            <form action="${pageContext.request.contextPath}/projects" method="POST">
                                <input type="hidden" name="action" value="addMember">
                                <input type="hidden" name="projectId" value="${project.projectId}">

                                <div class="mb-3">
                                    <label for="userId" class="form-label fw-bold">Select User <span class="text-danger">*</span></label>
                                    <select class="form-select" id="userId" name="userId" required>
                                        <option value="">-- Choose User --</option>
                                        <c:forEach var="u" items="${allUsers}">
                                            <option value="${u.userId}">${u.fullName} (${u.roleName != null ? u.roleName : u.role.name} - ${u.email})</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-4">
                                    <label for="projectRole" class="form-label fw-bold">Project Role <span class="text-danger">*</span></label>
                                    <select class="form-select" id="projectRole" name="projectRole" required>
                                        <option value="DEVELOPER">Developer</option>
                                        <option value="PROJECT_MANAGER">Project Manager</option>
                                        <option value="TESTER">Tester</option>
                                        <option value="FACULTY_MENTOR">Faculty Mentor</option>
                                        <option value="VIEWER">Viewer</option>
                                    </select>
                                </div>

                                <button type="submit" class="btn btn-df-primary w-100">
                                    <i class="bi bi-person-check me-1"></i> Add to Project
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
