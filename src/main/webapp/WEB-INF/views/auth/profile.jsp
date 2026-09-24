<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="My Profile" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">User Profile</h1>
                <p class="page-subtitle">Manage your personal details and security credentials</p>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp"/>

        <div class="row g-4">
            <!-- Left Card: Profile Overview -->
            <div class="col-lg-4">
                <div class="df-card text-center p-4">
                    <div class="avatar-sm mx-auto mb-3" style="width: 80px; height: 80px; font-size: 2rem;">
                        ${user.fullName.substring(0, 1).toUpperCase()}
                    </div>
                    <h5 class="fw-bold text-dark mb-1">${user.fullName}</h5>
                    <p class="text-muted small mb-2">@${user.username}</p>
                    <span class="badge badge-soft-primary px-3 py-2 mb-3">${user.roleName}</span>

                    <div class="border-top pt-3 text-start small">
                        <div class="mb-2"><strong>Email:</strong> <span class="text-muted">${user.email}</span></div>
                        <div class="mb-2"><strong>Phone:</strong> <span class="text-muted">${user.phone != null ? user.phone : '-'}</span></div>
                        <div class="mb-2"><strong>Designation:</strong> <span class="text-muted">${user.designation != null ? user.designation : '-'}</span></div>
                        <div><strong>Account Status:</strong> <span class="badge badge-soft-success">${user.status}</span></div>
                    </div>
                </div>
            </div>

            <!-- Right Forms: Edit Profile & Password -->
            <div class="col-lg-8">
                <!-- Edit Profile Form -->
                <div class="df-card mb-4">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-person-lines-fill text-primary"></i> Personal Details</h6>
                    </div>
                    <div class="df-card-body">
                        <form action="${pageContext.request.contextPath}/profile" method="POST">
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Username</label>
                                    <input type="text" class="form-control bg-light" value="<c:out value='${user.username}'/>" readonly disabled>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Email Address</label>
                                    <input type="email" class="form-control bg-light" value="<c:out value='${user.email}'/>" readonly disabled>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Full Name *</label>
                                    <input type="text" class="form-control" name="fullName" value="<c:out value='${user.fullName}'/>" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Phone Number</label>
                                    <input type="text" class="form-control" name="phone" value="<c:out value='${user.phone}'/>">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Designation / Role Title</label>
                                <input type="text" class="form-control" name="designation" value="<c:out value='${user.designation}'/>">
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Short Bio</label>
                                <textarea class="form-control" name="bio" rows="3"><c:out value='${user.bio}'/></textarea>
                            </div>

                            <button type="submit" class="btn btn-df-primary">
                                <i class="bi bi-check-lg me-1"></i> Save Profile Changes
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Change Password Form -->
                <div class="df-card">
                    <div class="df-card-header">
                        <h6 class="df-card-title"><i class="bi bi-shield-lock text-warning"></i> Change Password</h6>
                    </div>
                    <div class="df-card-body">
                        <c:if test="${not empty passwordError}">
                            <div class="alert alert-danger small mb-3"><c:out value="${passwordError}"/></div>
                        </c:if>
                        <c:if test="${not empty passwordSuccess}">
                            <div class="alert alert-success small mb-3"><c:out value="${passwordSuccess}"/></div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/change-password" method="POST">
                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Current Password *</label>
                                <input type="password" class="form-control" name="currentPassword" required>
                            </div>
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">New Password *</label>
                                    <input type="password" class="form-control" name="newPassword" placeholder="Min. 6 characters" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label small fw-semibold">Confirm New Password *</label>
                                    <input type="password" class="form-control" name="confirmNewPassword" placeholder="Repeat new password" required>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-outline-warning">
                                <i class="bi bi-key-fill me-1"></i> Update Password
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
