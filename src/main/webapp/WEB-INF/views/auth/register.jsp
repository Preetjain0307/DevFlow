<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | DevFlow Platform</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2.5rem 1rem;
            min-height: 100vh;
        }
        .auth-card {
            background: #ffffff;
            border-radius: 1rem;
            box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.3), 0 8px 10px -6px rgb(0 0 0 / 0.3);
            width: 100%;
            max-width: 600px;
            overflow: hidden;
        }
        .auth-header {
            background: #f8fafc;
            padding: 2rem 2rem 1.5rem 2rem;
            text-align: center;
            border-bottom: 1px solid var(--df-border);
        }
        .auth-body {
            padding: 2rem;
        }
    </style>
</head>
<body>

<div class="auth-card">
    <div class="auth-header">
        <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary p-3 rounded-circle mb-3">
            <i class="bi bi-person-plus-fill fs-2"></i>
        </div>
        <h4 class="fw-bold text-dark mb-1">Create DevFlow Account</h4>
        <p class="text-muted small mb-0">Join your software engineering team workspace</p>
    </div>

    <div class="auth-body">
        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2 flex-shrink-0"></i>
                <div class="small"><c:out value="${error}"/></div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <div class="row g-3 mb-3">
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Username *</label>
                    <input type="text" class="form-control" name="username" value="<c:out value='${user.username}'/>" placeholder="e.g. dev_johndoe" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Email Address *</label>
                    <input type="email" class="form-control" name="email" value="<c:out value='${user.email}'/>" placeholder="name@college.edu" required>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label small fw-semibold text-dark">Full Name *</label>
                <input type="text" class="form-control" name="fullName" value="<c:out value='${user.fullName}'/>" placeholder="e.g. John Doe" required>
            </div>

            <div class="row g-3 mb-3">
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Role *</label>
                    <select class="form-select" name="roleId" required>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.id}" ${role.id == 3 ? 'selected' : ''}>${role.name} - ${role.description}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Phone Number</label>
                    <input type="text" class="form-control" name="phone" value="<c:out value='${user.phone}'/>" placeholder="+1-555-0199">
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label small fw-semibold text-dark">Designation / Title</label>
                <input type="text" class="form-control" name="designation" value="<c:out value='${user.designation}'/>" placeholder="e.g. Backend Developer / Student Intern">
            </div>

            <div class="row g-3 mb-4">
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Password *</label>
                    <input type="password" class="form-control" name="password" placeholder="At least 6 characters" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label small fw-semibold text-dark">Confirm Password *</label>
                    <input type="password" class="form-control" name="confirmPassword" placeholder="Repeat password" required>
                </div>
            </div>

            <button type="submit" class="btn btn-df-primary w-100 py-2 mb-3">
                <i class="bi bi-check2-circle me-1"></i> Register Account
            </button>
        </form>

        <div class="text-center small text-muted">
            Already have an account? <a href="${pageContext.request.contextPath}/login" class="fw-semibold text-primary text-decoration-none">Sign In</a>
        </div>
    </div>
</div>

</body>
</html>
