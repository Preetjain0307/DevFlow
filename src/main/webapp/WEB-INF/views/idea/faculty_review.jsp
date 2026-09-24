<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Faculty Review Desk" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

<div class="main-content">
    <jsp:include page="/WEB-INF/views/common/navbar.jsp"/>

    <main class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">Faculty Review Desk</h1>
                <p class="page-subtitle">Review pending project proposals and peer voting outcomes from student teams</p>
            </div>
            <a href="${pageContext.request.contextPath}/ideas?action=list" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-arrow-left me-1"></i> Back to All Ideas
            </a>
        </div>

        <jsp:include page="/WEB-INF/views/common/alerts.jsp" />

        <div class="df-card">
            <div class="df-card-header py-3">
                <h5 class="mb-0 fw-semibold"><i class="bi bi-hourglass-split me-2 text-warning"></i>Proposals Awaiting Faculty Evaluation</h5>
            </div>
            <div class="table-responsive">
                <table class="df-table align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Proposal Title</th>
                            <th>Project</th>
                            <th>Category</th>
                            <th>Impact</th>
                            <th>Peer Votes</th>
                            <th>Submitted By</th>
                            <th class="text-end">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="idea" items="${pendingIdeas}">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.ideaId}" class="fw-semibold text-decoration-none text-dark">
                                        ${idea.title}
                                    </a>
                                </td>
                                <td>${idea.projectName}</td>
                                <td><span class="badge badge-soft-primary">${idea.category}</span></td>
                                <td><span class="badge bg-secondary">${idea.impactLevel}</span></td>
                                <td>
                                    <span class="text-success fw-bold">+${idea.upvotes}</span> / 
                                    <span class="text-danger fw-bold">-${idea.downvotes}</span>
                                </td>
                                <td>${idea.authorName}</td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/ideas?action=view&id=${idea.ideaId}" class="btn btn-sm btn-df-primary">
                                        <i class="bi bi-mortarboard me-1"></i> Review & Decide
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pendingIdeas}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="bi bi-check2-all fs-1 d-block mb-2 text-success"></i>
                                    No proposals currently awaiting review. All caught up!
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
