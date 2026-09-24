<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Pull Requests" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title"><i class="bi bi-diagram-2 me-2"></i>Pull Requests</h1>
                <p class="page-subtitle">Active and merged code reviews on connected GitHub repository</p>
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
                            <th># PR</th>
                            <th>Title</th>
                            <th>State</th>
                            <th>Author</th>
                            <th>Updated At</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="pr" items="${pullRequests}">
                            <tr>
                                <td><span class="badge bg-secondary">#${pr.issueNumber}</span></td>
                                <td class="fw-semibold text-dark">${pr.title}</td>
                                <td><span class="badge bg-${pr.state == 'open' ? 'success' : 'secondary'}">${pr.state}</span></td>
                                <td>${pr.authorName}</td>
                                <td><small class="text-muted">${pr.updatedAt}</small></td>
                                <td class="text-end">
                                    <c:if test="${not empty pr.htmlUrl}">
                                        <a href="${pr.htmlUrl}" target="_blank" class="btn btn-sm btn-outline-dark" title="View on GitHub">
                                            <i class="bi bi-box-arrow-up-right"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pullRequests}">
                            <tr><td colspan="6" class="text-center py-4 text-muted">No pull requests found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
