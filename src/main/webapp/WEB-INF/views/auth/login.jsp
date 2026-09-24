<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In & Demo Portal | DevFlow Platform</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #0f172a 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2.5rem 1rem;
            font-family: 'Inter', sans-serif;
        }
        .auth-container {
            width: 100%;
            max-width: 540px;
        }
        .auth-card {
            background: #ffffff;
            border-radius: 1.25rem;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.45);
            overflow: hidden;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        .auth-header {
            background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
            padding: 2rem 2rem 1.5rem 2rem;
            text-align: center;
            border-bottom: 1px solid #e2e8f0;
        }
        .auth-body {
            padding: 2rem;
        }
        .demo-hero-box {
            background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
            border-radius: 0.75rem;
            padding: 1.25rem;
            color: #ffffff;
            margin-bottom: 1.5rem;
            box-shadow: 0 10px 15px -3px rgba(79, 70, 229, 0.3);
            text-align: center;
        }
        .btn-universal-demo {
            background: #ffffff;
            color: #4338ca;
            font-weight: 700;
            border: none;
            padding: 0.65rem 1.25rem;
            border-radius: 0.5rem;
            transition: all 0.2s ease;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            width: 100%;
        }
        .btn-universal-demo:hover {
            background: #f8fafc;
            color: #3730a3;
            transform: translateY(-1px);
            box-shadow: 0 6px 12px -2px rgba(0, 0, 0, 0.15);
        }
        .role-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 0.5rem;
            margin-bottom: 1.5rem;
        }
        .role-btn {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.6rem 0.75rem;
            border-radius: 0.5rem;
            font-size: 0.8rem;
            font-weight: 600;
            border: 1px solid #e2e8f0;
            background: #f8fafc;
            color: #334155;
            text-decoration: none;
            transition: all 0.15s ease;
        }
        .role-btn:hover {
            background: #ffffff;
            border-color: #cbd5e1;
            transform: translateY(-1px);
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            color: #0f172a;
        }
        .role-btn i {
            font-size: 1.1rem;
        }
        .eval-badge {
            font-size: 0.7rem;
            padding: 0.2rem 0.5rem;
            border-radius: 1rem;
            background: #e0e7ff;
            color: #3730a3;
            font-weight: 600;
        }
        .divider-text {
            display: flex;
            align-items: center;
            text-align: center;
            color: #94a3b8;
            font-size: 0.8rem;
            margin: 1.25rem 0;
        }
        .divider-text::before, .divider-text::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid #e2e8f0;
        }
        .divider-text::before {
            margin-right: .5em;
        }
        .divider-text::after {
            margin-left: .5em;
        }
    </style>
</head>
<body>

<div class="auth-container">
    <div class="auth-card">
        <div class="auth-header">
            <div class="d-inline-flex align-items-center justify-content-center bg-primary bg-opacity-10 text-primary p-3 rounded-circle mb-2">
                <i class="bi bi-cpu-fill fs-2"></i>
            </div>
            <h3 class="fw-bold text-dark mb-1">DevFlow Platform</h3>
            <p class="text-muted small mb-0">Developer Collaboration & Academic Project System</p>
        </div>

        <div class="auth-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2 flex-shrink-0"></i>
                    <div class="small"><c:out value="${error}"/></div>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.flashSuccess}">
                <div class="alert alert-success d-flex align-items-center mb-3" role="alert">
                    <i class="bi bi-check-circle-fill me-2 flex-shrink-0"></i>
                    <div class="small"><c:out value="${sessionScope.flashSuccess}"/></div>
                </div>
                <c:remove var="flashSuccess" scope="session"/>
            </c:if>

            <c:if test="${param.logout == 'true'}">
                <div class="alert alert-info d-flex align-items-center mb-3" role="alert">
                    <i class="bi bi-info-circle-fill me-2 flex-shrink-0"></i>
                    <div class="small">You have been signed out successfully.</div>
                </div>
            </c:if>

            <!-- 1-Click Universal Demo Login Box (For Fast Teacher Demonstration) -->
            <div class="demo-hero-box">
                <div class="d-flex align-items-center justify-content-between mb-2">
                    <span class="small text-white-50 fw-semibold text-uppercase" style="font-size: 0.68rem; letter-spacing: 0.5px;">Teacher & Examiner Demo Mode</span>
                    <span class="eval-badge bg-white text-dark">1-Click Live</span>
                </div>
                <h5 class="fw-bold mb-1">Instant Demo Sign In</h5>
                <p class="small text-white-50 mb-3" style="font-size: 0.8rem;">Instant access to all modules, full sample dataset, metrics & analytics.</p>
                
                <a href="${pageContext.request.contextPath}/demo-login?role=demo" class="btn-universal-demo">
                    <i class="bi bi-lightning-charge-fill text-warning"></i>
                    <span>Instant 1-Click Full Demo Access</span>
                </a>
            </div>

            <!-- Role-Based Instant Access Grid -->
            <div class="mb-3">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="small fw-semibold text-muted text-uppercase" style="font-size: 0.72rem; letter-spacing: 0.5px;">
                        <i class="bi bi-people-fill me-1"></i> Or Sign In as Specific Persona:
                    </span>
                    <span class="badge bg-light text-secondary border" style="font-size: 0.65rem;">No password needed</span>
                </div>
                
                <div class="role-grid">
                    <a href="${pageContext.request.contextPath}/demo-login?role=admin" class="role-btn">
                        <i class="bi bi-shield-lock-fill text-danger"></i>
                        <div>
                            <div>System Admin</div>
                            <div class="text-muted" style="font-size: 0.68rem;">Platform governance</div>
                        </div>
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/demo-login?role=pm" class="role-btn">
                        <i class="bi bi-person-gear text-primary"></i>
                        <div>
                            <div>Project Manager</div>
                            <div class="text-muted" style="font-size: 0.68rem;">Sprints & Backlog</div>
                        </div>
                    </a>

                    <a href="${pageContext.request.contextPath}/demo-login?role=dev" class="role-btn">
                        <i class="bi bi-code-slash text-success"></i>
                        <div>
                            <div>Lead Developer</div>
                            <div class="text-muted" style="font-size: 0.68rem;">Kanban & Proposals</div>
                        </div>
                    </a>

                    <a href="${pageContext.request.contextPath}/demo-login?role=tester" class="role-btn">
                        <i class="bi bi-bug-fill text-warning"></i>
                        <div>
                            <div>QA Tester</div>
                            <div class="text-muted" style="font-size: 0.68rem;">Bug Triage & Tests</div>
                        </div>
                    </a>

                    <a href="${pageContext.request.contextPath}/demo-login?role=faculty" class="role-btn" style="grid-column: span 2;">
                        <i class="bi bi-award-fill" style="color: #6366f1;"></i>
                        <div>
                            <div>Faculty Mentor / Evaluator (Dr. Alan Grant)</div>
                            <div class="text-muted" style="font-size: 0.68rem;">Formal Idea Approval, Milestone Oversight & Academic Grading</div>
                        </div>
                    </a>
                </div>
            </div>

            <div class="divider-text">or manual credentials</div>

            <!-- Standard Manual Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="POST">
                <input type="hidden" name="redirect" value="<c:out value='${param.redirect}'/>">

                <div class="mb-3">
                    <label class="form-label small fw-semibold text-dark">Username or Email</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-person"></i></span>
                        <input type="text" class="form-control border-start-0" id="loginUsername" name="username" value="<c:out value='${username}'/>" placeholder="e.g. admin or dev_alex" required autofocus>
                    </div>
                </div>

                <div class="mb-3">
                    <div class="d-flex justify-content-between align-items-center">
                        <label class="form-label small fw-semibold text-dark mb-1">Password</label>
                        <span class="text-muted" style="font-size: 0.72rem;">Default: <code>password123</code></span>
                    </div>
                    <div class="input-group">
                        <span class="input-group-text bg-light border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                        <input type="password" class="form-control border-start-0" id="loginPassword" name="password" placeholder="Enter your password" required>
                    </div>
                </div>

                <button type="submit" class="btn btn-df-primary w-100 py-2 mb-3">
                    <i class="bi bi-box-arrow-in-right me-1"></i> Sign In Manually
                </button>
            </form>

            <div class="text-center small text-muted">
                Need a new user account? <a href="${pageContext.request.contextPath}/register" class="fw-semibold text-primary text-decoration-none">Register</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
