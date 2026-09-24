<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="User Management - Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">User Management</h1>
                <p class="page-subtitle">Manage platform users, roles, permissions, and account statuses</p>
            </div>
            <button type="button" class="btn btn-df-primary btn-sm" data-bs-toggle="modal" data-bs-target="#createUserModal">
                <i class="bi bi-person-plus me-1"></i> Add New User
            </button>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>User</th>
                            <th>Username</th>
                            <th>Role</th>
                            <th>Department</th>
                            <th>Status</th>
                            <th>Created At</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <div class="avatar bg-primary text-white rounded-circle me-2 d-flex align-items-center justify-content-center" style="width:32px;height:32px;font-size:13px;">
                                            ${u.fullName != null ? u.fullName.substring(0,1).toUpperCase() : 'U'}
                                        </div>
                                        <div>
                                            <div class="fw-semibold text-dark">${u.fullName}</div>
                                            <small class="text-muted">${u.email}</small>
                                        </div>
                                    </div>
                                </td>
                                <td><code>${u.username}</code></td>
                                <td><span class="badge badge-soft-primary">${u.roleName != null ? u.roleName : u.role.name}</span></td>
                                <td><small class="text-muted">${u.department != null ? u.department : '-'}</small></td>
                                <td>
                                    <span class="badge bg-${u.active ? 'success' : 'danger'}">
                                        ${u.active ? 'ACTIVE' : 'DEACTIVATED'}
                                    </span>
                                </td>
                                <td><small class="text-muted">${u.createdAt}</small></td>
                                <td class="text-end">
                                    <form action="${pageContext.request.contextPath}/admin" method="POST" class="d-inline">
                                        <input type="hidden" name="action" value="toggleUserStatus">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <button type="submit" class="btn btn-sm btn-outline-${u.active ? 'warning' : 'success'}" title="${u.active ? 'Deactivate' : 'Activate'}">
                                            <i class="bi bi-power"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<!-- Create User Modal -->
<div class="modal fade" id="createUserModal" tabindex="-1" aria-labelledby="createUserModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin" method="POST">
                <input type="hidden" name="action" value="createUser">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold" id="createUserModalLabel">Add New System User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Full Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="fullName" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Email Address <span class="text-danger">*</span></label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Username <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="username" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Temporary Password <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" name="password" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Role <span class="text-danger">*</span></label>
                        <select class="form-select" name="roleId" required>
                            <c:forEach var="r" items="${roles}">
                                <option value="${r.roleId}">${r.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Department / Class</label>
                        <input type="text" class="form-control" name="department" placeholder="e.g., Computer Science / Final Year">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-df-primary">Create User Account</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
