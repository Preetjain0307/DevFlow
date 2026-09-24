<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Audit Logs - Admin" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Security & Audit Logs</h1>
                <p class="page-subtitle">Immutable tracking of user authentication, state changes, and critical actions</p>
            </div>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="df-card">
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Timestamp</th>
                            <th>User</th>
                            <th>Action</th>
                            <th>Entity</th>
                            <th>Entity ID</th>
                            <th>IP Address</th>
                            <th>Details</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="log" items="${logs}">
                            <tr>
                                <td><small class="text-muted">${log.createdAt}</small></td>
                                <td><span class="fw-semibold text-dark">${log.username != null ? log.username : 'System'}</span></td>
                                <td><span class="badge bg-secondary font-monospace">${log.action}</span></td>
                                <td>${log.entityType}</td>
                                <td><code>${log.entityId != null ? log.entityId : '-'}</code></td>
                                <td><small class="text-muted">${log.ipAddress}</small></td>
                                <td><small class="text-muted" style="max-width: 250px; display: inline-block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${log.details}</small></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty logs}">
                            <tr><td colspan="7" class="text-center py-4 text-muted">No audit events recorded.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
