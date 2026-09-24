<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Notifications" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Notifications</h1>
                <p class="page-subtitle">Stay updated on task assignments, proposal reviews, meeting alerts, and mentions</p>
            </div>
            <form action="${pageContext.request.contextPath}/notifications" method="POST" class="d-inline">
                <input type="hidden" name="action" value="markAllRead">
                <button type="submit" class="btn btn-outline-secondary btn-sm">
                    <i class="bi bi-check2-all me-1"></i> Mark All as Read
                </button>
            </form>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="df-card">
            <div class="df-card-body p-0">
                <div class="list-group list-group-flush">
                    <c:forEach var="n" items="${notifications}">
                        <div class="list-group-item p-3 ${n.read ? 'bg-white' : 'bg-light border-start border-4 border-primary'}">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <h6 class="mb-0 fw-bold ${n.read ? 'text-muted' : 'text-dark'}">${n.title}</h6>
                                <small class="text-muted">${n.createdAt}</small>
                            </div>
                            <p class="mb-2 text-muted small">${n.message}</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <c:if test="${not empty n.linkUrl}">
                                    <a href="${pageContext.request.contextPath}${n.linkUrl}" class="btn btn-sm btn-outline-primary py-0 px-2" style="font-size: 12px;">View Details</a>
                                </c:if>
                                <c:if test="${!n.read}">
                                    <form action="${pageContext.request.contextPath}/notifications" method="POST" class="d-inline ms-auto">
                                        <input type="hidden" name="action" value="markRead">
                                        <input type="hidden" name="id" value="${n.notificationId}">
                                        <button type="submit" class="btn btn-sm btn-link text-muted p-0 text-decoration-none small">Mark read</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty notifications}">
                        <div class="text-center py-5 text-muted">
                            <i class="bi bi-bell-slash fs-1 d-block mb-2 text-secondary"></i>
                            No notifications to display.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
