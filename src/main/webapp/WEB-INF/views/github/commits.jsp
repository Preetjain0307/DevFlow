<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Commit History" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title"><i class="bi bi-git me-2"></i>Git Commits</h1>
                <p class="page-subtitle">Commit log synced from connected GitHub repository</p>
            </div>
            <a href="${pageContext.request.contextPath}/github?action=overview&projectId=${projectId}" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to GitHub Overview
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Commit SHA</th>
                            <th>Commit Message</th>
                            <th>Author</th>
                            <th>Committed Date</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${commits}">
                            <tr>
                                <td><span class="badge bg-secondary font-monospace">${c.commitHash != null && c.commitHash.length() >= 7 ? c.commitHash.substring(0, 7) : c.commitHash}</span></td>
                                <td class="fw-semibold text-dark">${c.message}</td>
                                <td>${c.authorName}</td>
                                <td><small class="text-muted">${c.committedAt}</small></td>
                                <td class="text-end">
                                    <c:if test="${not empty c.htmlUrl}">
                                        <a href="${c.htmlUrl}" target="_blank" class="btn btn-sm btn-outline-dark" title="View on GitHub">
                                            <i class="bi bi-box-arrow-up-right"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty commits}">
                            <tr><td colspan="5" class="text-center py-4 text-muted">No commits found for this project.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
