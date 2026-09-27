<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</div> <!-- End app-wrapper -->

<!-- Global Spotlight Quick Search Modal (Ctrl + K) -->
<div class="modal fade" id="quickSearchModal" tabindex="-1" aria-labelledby="quickSearchModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content shadow-lg border-0 rounded-4 overflow-hidden">
            <div class="modal-header border-bottom p-3">
                <div class="input-group input-group-lg border-0 w-100">
                    <span class="input-group-text bg-transparent border-0 ps-2"><i class="bi bi-search fs-4 text-primary"></i></span>
                    <input type="text" class="form-control border-0 shadow-none bg-transparent fs-5" id="globalSearchInput" placeholder="Search tasks, bugs, projects, proposals... (Esc to close)" autocomplete="off">
                    <button class="btn btn-sm btn-light rounded-pill px-2 py-0 border align-self-center me-2 text-muted" type="button" data-bs-dismiss="modal">ESC</button>
                </div>
            </div>
            <div class="modal-body p-0" style="max-height: 420px; overflow-y: auto;" id="globalSearchResultsContainer">
                <div class="p-4 text-center text-muted" id="searchDefaultHint">
                    <i class="bi bi-terminal fs-1 opacity-25 d-block mb-2"></i>
                    <span>Type at least 2 characters to search across all workspace modules</span>
                    <div class="d-flex justify-content-center gap-2 mt-3 flex-wrap">
                        <span class="badge bg-light text-secondary border"><i class="bi bi-folder me-1 text-primary"></i>Projects</span>
                        <span class="badge bg-light text-secondary border"><i class="bi bi-check2-square me-1 text-success"></i>Tasks</span>
                        <span class="badge bg-light text-secondary border"><i class="bi bi-bug me-1 text-danger"></i>Bugs</span>
                        <span class="badge bg-light text-secondary border"><i class="bi bi-lightbulb me-1 text-warning"></i>Proposals</span>
                    </div>
                </div>
                <div id="searchResultsSpinner" class="text-center py-4 d-none">
                    <div class="spinner-border spinner-border-sm text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <span class="ms-2 small text-muted">Searching workspace...</span>
                </div>
                <div id="searchResultsList" class="list-group list-group-flush d-none"></div>
            </div>
            <div class="modal-footer border-top py-2 px-3 justify-content-between small text-muted">
                <div>
                    <span class="me-2"><kbd class="bg-secondary bg-opacity-25 text-secondary border font-monospace px-1">↑</kbd> <kbd class="bg-secondary bg-opacity-25 text-secondary border font-monospace px-1">↓</kbd> to navigate</span>
                    <span><kbd class="bg-secondary bg-opacity-25 text-secondary border font-monospace px-1">↵</kbd> to select</span>
                </div>
                <span class="fw-semibold"><i class="bi bi-stars text-primary me-1"></i>DevFlow Spotlight</span>
            </div>
        </div>
    </div>
</div>

<!-- ============================================================
     Academic Architecture & Viva Voce Guide Modal
     ============================================================ -->
<div class="modal fade" id="vivaArchitectureModal" tabindex="-1" aria-labelledby="vivaArchitectureModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-xl modal-dialog-scrollable">
        <div class="modal-content shadow-lg border-0 rounded-4 overflow-hidden">
            <!-- Modal Header -->
            <div class="modal-header bg-body-tertiary border-bottom py-3 px-4 d-flex align-items-center justify-content-between">
                <div class="d-flex align-items-center gap-3">
                    <div class="avatar-sm bg-warning bg-opacity-10 text-warning d-flex align-items-center justify-content-center rounded-3 fs-4" style="width: 44px; height: 44px;">
                        🎓
                    </div>
                    <div>
                        <div class="d-flex align-items-center gap-2">
                            <h5 class="modal-title fw-bold mb-0 text-body" id="vivaArchitectureModalLabel">DevFlow Architecture & Viva Voce Defense Hub</h5>
                            <span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1 small">
                                <i class="bi bi-circle-fill text-success me-1" style="font-size: 0.5rem;"></i> System Live
                            </span>
                        </div>
                        <p class="text-muted small mb-0">Pure Advanced Java • 3-Tier Enterprise MVC Pattern • HikariCP Live Telemetry</p>
                    </div>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <!-- Modal Nav Tabs -->
            <div class="px-4 pt-3 bg-body-tertiary border-bottom">
                <ul class="nav nav-pills nav-fill gap-2" id="vivaTab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active fw-semibold" id="viva-arch-tab" data-bs-toggle="pill" data-bs-target="#viva-arch-pane" type="button" role="tab">
                            <i class="bi bi-diagram-3-fill me-1 text-primary"></i> 3-Tier Architecture
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link fw-semibold" id="viva-telemetry-tab" data-bs-toggle="pill" data-bs-target="#viva-telemetry-pane" type="button" role="tab">
                            <i class="bi bi-speedometer2 me-1 text-success"></i> Live Telemetry & HikariCP
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link fw-semibold" id="viva-rbac-tab" data-bs-toggle="pill" data-bs-target="#viva-rbac-pane" type="button" role="tab">
                            <i class="bi bi-person-badge-fill me-1 text-info"></i> RBAC Persona Switcher
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link fw-semibold" id="viva-faq-tab" data-bs-toggle="pill" data-bs-target="#viva-faq-pane" type="button" role="tab">
                            <i class="bi bi-patch-question-fill me-1 text-warning"></i> Viva Defense Q&A
                        </button>
                    </li>
                </ul>
            </div>

            <!-- Modal Body -->
            <div class="modal-body p-4">
                <div class="tab-content" id="vivaTabContent">
                    
                    <!-- TAB 1: 3-TIER ARCHITECTURE -->
                    <div class="tab-pane fade show active" id="viva-arch-pane" role="tabpanel">
                        <div class="alert alert-primary bg-primary bg-opacity-10 border-primary border-opacity-25 py-2 px-3 mb-4 rounded-3 d-flex align-items-center gap-2">
                            <i class="bi bi-info-circle-fill text-primary fs-5"></i>
                            <span class="small">DevFlow is engineered on <strong>Pure Advanced Java (Java EE)</strong> without Spring Boot magic, enforcing strict layer decoupling across 6 enterprise tiers.</span>
                        </div>

                        <div class="arch-flow-diagram d-flex flex-column gap-3">
                            <!-- Tier 1: Presentation -->
                            <div class="card border rounded-3 p-3 shadow-xs">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-primary px-2 py-1">Tier 1: Presentation Layer</span>
                                        <strong class="text-body">Client Browser & View Templates</strong>
                                    </div>
                                    <span class="badge bg-light text-secondary border font-monospace">JSP 2.3 + JSTL 1.2</span>
                                </div>
                                <p class="text-muted small mb-2">Dynamic view rendering using JavaServer Pages, expression language (<code>\${...}</code>), custom JSTL tag libraries (<code>c:forEach</code>, <code>c:if</code>), and Bootstrap 5.3 + Vanilla CSS design system.</p>
                                <div class="d-flex gap-2 flex-wrap">
                                    <span class="badge bg-secondary bg-opacity-10 text-secondary border">header.jsp (Theme Script)</span>
                                    <span class="badge bg-secondary bg-opacity-10 text-secondary border">navbar.jsp (RBAC Nav)</span>
                                    <span class="badge bg-secondary bg-opacity-10 text-secondary border">kanban.jsp (HTML5 D&D)</span>
                                    <span class="badge bg-secondary bg-opacity-10 text-secondary border">charts.js (Canvas Analytics)</span>
                                </div>
                            </div>

                            <div class="text-center text-muted fs-5 my-n1"><i class="bi bi-arrow-down"></i> <small class="fs-6 text-muted">HTTP Requests & JSON API Calls</small> <i class="bi bi-arrow-down"></i></div>

                            <!-- Tier 2: Interceptors & Security -->
                            <div class="card border rounded-3 p-3 shadow-xs">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-danger px-2 py-1">Tier 2: Security & Filter Pipeline</span>
                                        <strong class="text-body">Servlet Filters (Interception & RBAC)</strong>
                                    </div>
                                    <span class="badge bg-light text-secondary border font-monospace">javax.servlet.Filter</span>
                                </div>
                                <p class="text-muted small mb-2">Intercepts all incoming HTTP traffic before reaching controllers. Verifies authentication, enforces role-based URL access, and standardizes UTF-8 encoding.</p>
                                <div class="d-flex gap-2 flex-wrap">
                                    <span class="badge bg-danger bg-opacity-10 text-danger border">AuthenticationFilter (/dashboard, /tasks/*, /api/*)</span>
                                    <span class="badge bg-danger bg-opacity-10 text-danger border">RoleFilter (ADMIN, PM, DEV, QA, FACULTY)</span>
                                    <span class="badge bg-secondary bg-opacity-10 text-secondary border">Utf8Filter (Character Encoding)</span>
                                </div>
                            </div>

                            <div class="text-center text-muted fs-5 my-n1"><i class="bi bi-arrow-down"></i> <small class="fs-6 text-muted">Dispatches to WebServlet</small> <i class="bi bi-arrow-down"></i></div>

                            <!-- Tier 3: Controllers -->
                            <div class="card border rounded-3 p-3 shadow-xs">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-warning text-dark px-2 py-1">Tier 3: Controller Layer</span>
                                        <strong class="text-body">Java Servlets (MVC Controllers)</strong>
                                    </div>
                                    <span class="badge bg-light text-secondary border font-monospace">javax.servlet.http.HttpServlet</span>
                                </div>
                                <p class="text-muted small mb-2">Handles HTTP GET / POST dispatching via query parameters (<code>action=create|view|edit</code>). Validates inputs, manages sessions, and produces either HTML forwards or JSON REST responses via Gson.</p>
                                <div class="d-flex gap-2 flex-wrap">
                                    <span class="badge bg-warning bg-opacity-10 text-warning-emphasis border">TaskController (@WebServlet)</span>
                                    <span class="badge bg-warning bg-opacity-10 text-warning-emphasis border">BugController (@WebServlet)</span>
                                    <span class="badge bg-warning bg-opacity-10 text-warning-emphasis border">SearchController (/api/search)</span>
                                    <span class="badge bg-warning bg-opacity-10 text-warning-emphasis border">ArchitectureController (/api/architecture/status)</span>
                                </div>
                            </div>

                            <div class="text-center text-muted fs-5 my-n1"><i class="bi bi-arrow-down"></i> <small class="fs-6 text-muted">Invokes Business Rules</small> <i class="bi bi-arrow-down"></i></div>

                            <!-- Tier 4: Business Logic -->
                            <div class="card border rounded-3 p-3 shadow-xs">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-info text-dark px-2 py-1">Tier 4: Service Layer</span>
                                        <strong class="text-body">Domain Business Logic (POJO Services)</strong>
                                    </div>
                                    <span class="badge bg-light text-secondary border font-monospace">Service Interfaces & Impl</span>
                                </div>
                                <p class="text-muted small mb-2">Coordinates domain operations, state machine transitions, notification event triggers, and heuristics (e.g. AI Standup Summarizer and GitHub Webhook payload processing).</p>
                                <div class="d-flex gap-2 flex-wrap">
                                    <span class="badge bg-info bg-opacity-10 text-info-emphasis border">ProjectServiceImpl</span>
                                    <span class="badge bg-info bg-opacity-10 text-info-emphasis border">TaskServiceImpl</span>
                                    <span class="badge bg-info bg-opacity-10 text-info-emphasis border">MeetingServiceImpl (Heuristic Summaries)</span>
                                    <span class="badge bg-info bg-opacity-10 text-info-emphasis border">GitHubServiceImpl</span>
                                </div>
                            </div>

                            <div class="text-center text-muted fs-5 my-n1"><i class="bi bi-arrow-down"></i> <small class="fs-6 text-muted">Executes PreparedStatement Queries</small> <i class="bi bi-arrow-down"></i></div>

                            <!-- Tier 5 & 6: Data Access & Persistence -->
                            <div class="card border rounded-3 p-3 shadow-xs">
                                <div class="d-flex align-items-center justify-content-between mb-2">
                                    <div class="d-flex align-items-center gap-2">
                                        <span class="badge bg-success px-2 py-1">Tiers 5 & 6: DAO & Persistence</span>
                                        <strong class="text-body">JDBC DAO Pattern + HikariCP + MySQL</strong>
                                    </div>
                                    <span class="badge bg-light text-secondary border font-monospace">HikariCP 4.0.3 + MySQL 8.x</span>
                                </div>
                                <p class="text-muted small mb-2">Parameterized <code>PreparedStatement</code> instances thwart SQL Injection. HikariCP connection pooling reuses high-performance physical connections across 26 normalized relational tables.</p>
                                <div class="d-flex gap-2 flex-wrap">
                                    <span class="badge bg-success bg-opacity-10 text-success border">TaskDAOImpl / BugDAOImpl</span>
                                    <span class="badge bg-success bg-opacity-10 text-success border">DevFlowHikariPool (Max: 10, Min: 2)</span>
                                    <span class="badge bg-success bg-opacity-10 text-success border">26 Normalized Tables with Foreign Keys</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 2: LIVE TELEMETRY & HIKARICP -->
                    <div class="tab-pane fade" id="viva-telemetry-pane" role="tabpanel">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <span class="small text-muted">Live stats polled directly from <code>HikariDataSource</code>, <code>DatabaseMetaData</code>, and <code>java.lang.Runtime</code>.</span>
                            <button class="btn btn-sm btn-outline-primary d-flex align-items-center gap-1" id="btnRefreshTelemetry" type="button">
                                <i class="bi bi-arrow-clockwise"></i> <span>Refresh Telemetry</span>
                            </button>
                        </div>

                        <div class="row g-3">
                            <!-- Card 1: Connection Pool -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs">
                                    <div class="d-flex align-items-center justify-content-between mb-3">
                                        <div class="d-flex align-items-center gap-2">
                                            <i class="bi bi-layers-fill text-primary fs-5"></i>
                                            <h6 class="fw-bold mb-0">HikariCP Connection Pool</h6>
                                        </div>
                                        <span class="badge bg-primary bg-opacity-10 text-primary border" id="telPoolName">DevFlowHikariPool</span>
                                    </div>
                                    <div class="row text-center g-2 mb-3">
                                        <div class="col-4">
                                            <div class="p-2 border rounded bg-body-tertiary">
                                                <div class="fs-4 fw-bold text-primary" id="telActiveConns">0</div>
                                                <div class="text-muted" style="font-size: 0.7rem;">ACTIVE CONNS</div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="p-2 border rounded bg-body-tertiary">
                                                <div class="fs-4 fw-bold text-success" id="telIdleConns">1</div>
                                                <div class="text-muted" style="font-size: 0.7rem;">IDLE CONNS</div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="p-2 border rounded bg-body-tertiary">
                                                <div class="fs-4 fw-bold text-dark" id="telMaxPool">10</div>
                                                <div class="text-muted" style="font-size: 0.7rem;">MAX POOL SIZE</div>
                                            </div>
                                        </div>
                                    </div>
                                    <ul class="list-group list-group-flush small">
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Minimum Idle Connections:</span>
                                            <span class="fw-semibold" id="telMinIdle">2</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Connection Timeout:</span>
                                            <span class="fw-semibold" id="telConnTimeout">20000ms</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Idle Timeout:</span>
                                            <span class="fw-semibold" id="telIdleTimeout">30000ms</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Threads Awaiting Connection:</span>
                                            <span class="badge bg-success-subtle text-success" id="telThreadsAwaiting">0</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Card 2: Database Metadata -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs">
                                    <div class="d-flex align-items-center justify-content-between mb-3">
                                        <div class="d-flex align-items-center gap-2">
                                            <i class="bi bi-database-fill-gear text-success fs-5"></i>
                                            <h6 class="fw-bold mb-0">MySQL Engine & Schema</h6>
                                        </div>
                                        <span class="badge bg-success bg-opacity-10 text-success border" id="telDbTables">26 Tables</span>
                                    </div>
                                    <ul class="list-group list-group-flush small">
                                        <li class="list-group-item d-flex justify-content-between px-0 py-2 bg-transparent">
                                            <span class="text-muted">Database Product:</span>
                                            <span class="fw-semibold" id="telDbProduct">MySQL</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-2 bg-transparent">
                                            <span class="text-muted">Product Version:</span>
                                            <span class="font-monospace small" id="telDbVersion">--</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-2 bg-transparent">
                                            <span class="text-muted">Active Catalog:</span>
                                            <span class="badge bg-secondary-subtle text-secondary" id="telDbName">devflow_db</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-2 bg-transparent border-0">
                                            <span class="text-muted">JDBC Driver:</span>
                                            <span class="fw-semibold text-truncate ms-2" id="telDbDriver" style="max-width: 250px;">MySQL Connector/J</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Card 3: JVM & Container Runtime -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs">
                                    <div class="d-flex align-items-center justify-content-between mb-3">
                                        <div class="d-flex align-items-center gap-2">
                                            <i class="bi bi-cup-hot-fill text-danger fs-5"></i>
                                            <h6 class="fw-bold mb-0">Java Virtual Machine & OS</h6>
                                        </div>
                                        <span class="badge bg-danger bg-opacity-10 text-danger border" id="telServerInfo">Apache Tomcat</span>
                                    </div>
                                    <ul class="list-group list-group-flush small">
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Java Version:</span>
                                            <span class="fw-semibold" id="telJavaVer">17</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Vendor:</span>
                                            <span class="fw-semibold" id="telJavaVendor">Oracle Corporation</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Operating System:</span>
                                            <span class="fw-semibold" id="telOsName">Windows</span>
                                        </li>
                                        <li class="list-group-item d-flex justify-content-between px-0 py-1 bg-transparent border-0">
                                            <span class="text-muted">Available Processors:</span>
                                            <span class="badge bg-secondary-subtle text-secondary" id="telCpuCores">-- Cores</span>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <!-- Card 4: Memory Usage -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs">
                                    <div class="d-flex align-items-center justify-content-between mb-2">
                                        <div class="d-flex align-items-center gap-2">
                                            <i class="bi bi-memory text-info fs-5"></i>
                                            <h6 class="fw-bold mb-0">JVM Heap Memory Allocation</h6>
                                        </div>
                                        <span class="badge bg-info bg-opacity-10 text-info border">Heap Utilization</span>
                                    </div>
                                    <div class="mb-2">
                                        <div class="d-flex justify-content-between small text-muted mb-1">
                                            <span>Used: <strong id="telUsedMem">--</strong> MB</span>
                                            <span>Committed: <strong id="telTotalMem">--</strong> MB</span>
                                            <span>Max: <strong id="telMaxMem">--</strong> MB</span>
                                        </div>
                                        <div class="progress" style="height: 10px;">
                                            <div class="progress-bar progress-bar-striped progress-bar-animated bg-info" id="telMemBar" role="progressbar" style="width: 25%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                                        </div>
                                    </div>
                                    <p class="small text-muted mb-0">Garbage Collector efficiently recycles disconnected entity models and request buffers.</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 3: RBAC PERSONA SWITCHER -->
                    <div class="tab-pane fade" id="viva-rbac-pane" role="tabpanel">
                        <div class="alert alert-secondary bg-body-tertiary border py-2 px-3 mb-4 rounded-3 small">
                            <i class="bi bi-shield-lock-fill text-primary me-1"></i>
                            DevFlow enforces strict <strong>Role-Based Access Control (RBAC)</strong> using <code>RoleFilter</code>. Click any persona below to immediately log in and demonstrate that role's interface:
                        </div>

                        <div class="row g-3">
                            <!-- Admin -->
                            <div class="col-md-4">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs text-center d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="fs-2 mb-2">🛡️</div>
                                        <h6 class="fw-bold mb-1">Administrator</h6>
                                        <span class="badge bg-danger bg-opacity-10 text-danger border mb-2">admin / admin123</span>
                                        <p class="text-muted small">Global system oversight, user role provisioning, system settings, database management.</p>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/demo-login?role=admin" class="btn btn-sm btn-outline-danger w-100 mt-2">
                                        <i class="bi bi-box-arrow-in-right me-1"></i> Login as Admin
                                    </a>
                                </div>
                            </div>

                            <!-- PM -->
                            <div class="col-md-4">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs text-center d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="fs-2 mb-2">💼</div>
                                        <h6 class="fw-bold mb-1">Project Manager</h6>
                                        <span class="badge bg-primary bg-opacity-10 text-primary border mb-2">pm / password123</span>
                                        <p class="text-muted small">Project planning, sprint management, task assignment, milestone tracking, executive reports.</p>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/demo-login?role=pm" class="btn btn-sm btn-outline-primary w-100 mt-2">
                                        <i class="bi bi-box-arrow-in-right me-1"></i> Login as PM
                                    </a>
                                </div>
                            </div>

                            <!-- Dev -->
                            <div class="col-md-4">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs text-center d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="fs-2 mb-2">💻</div>
                                        <h6 class="fw-bold mb-1">Developer</h6>
                                        <span class="badge bg-success bg-opacity-10 text-success border mb-2">dev / password123</span>
                                        <p class="text-muted small">Kanban execution, task status drag-and-drop, bug fixing, idea submissions, GitHub links.</p>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/demo-login?role=dev" class="btn btn-sm btn-outline-success w-100 mt-2">
                                        <i class="bi bi-box-arrow-in-right me-1"></i> Login as Developer
                                    </a>
                                </div>
                            </div>

                            <!-- QA -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs text-center d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="fs-2 mb-2">🧪</div>
                                        <h6 class="fw-bold mb-1">QA Tester</h6>
                                        <span class="badge bg-warning bg-opacity-10 text-warning-emphasis border mb-2">tester / password123</span>
                                        <p class="text-muted small">Defect tracking, logging reproduction steps, severity triage, verifying completed bug fixes.</p>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/demo-login?role=tester" class="btn btn-sm btn-outline-warning w-100 mt-2">
                                        <i class="bi bi-box-arrow-in-right me-1"></i> Login as QA Tester
                                    </a>
                                </div>
                            </div>

                            <!-- Faculty -->
                            <div class="col-md-6">
                                <div class="card border rounded-3 p-3 h-100 shadow-xs text-center d-flex flex-column justify-content-between">
                                    <div>
                                        <div class="fs-2 mb-2">🎓</div>
                                        <h6 class="fw-bold mb-1">Faculty Mentor</h6>
                                        <span class="badge bg-info bg-opacity-10 text-info-emphasis border mb-2">faculty / password123</span>
                                        <p class="text-muted small">Idea proposal approval, capstone milestone grading, reviewing student performance metrics.</p>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/demo-login?role=faculty" class="btn btn-sm btn-outline-info w-100 mt-2">
                                        <i class="bi bi-box-arrow-in-right me-1"></i> Login as Faculty
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 4: VIVA VOCE CHEAT SHEET -->
                    <div class="tab-pane fade" id="viva-faq-pane" role="tabpanel">
                        <div class="alert alert-warning bg-warning bg-opacity-10 border-warning border-opacity-25 py-2 px-3 mb-4 rounded-3 small">
                            <i class="bi bi-lightbulb-fill text-warning me-1"></i>
                            <strong>Academic Defense Preparation</strong>: Concise, expert answers to questions most frequently asked by examiners and professors.
                        </div>

                        <div class="accordion" id="vivaAccordion">
                            <!-- Q1 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingOne">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne">
                                        1. Why did you choose Pure Java Servlets & JSP instead of Spring Boot?
                                    </button>
                                </h2>
                                <div id="collapseOne" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        Building DevFlow on <strong>Core Java EE primitives (Servlets, JSP, JSTL, Filters, JDBC)</strong> demonstrates foundational mastery of the HTTP request-response lifecycle, thread safety, session management, and filter chains without relying on opinionated framework abstractions. It proves complete understanding of how enterprise web applications operate under the hood.
                                    </div>
                                </div>
                            </div>

                            <!-- Q2 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingTwo">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo">
                                        2. Why use HikariCP connection pooling over DriverManager.getConnection()?
                                    </button>
                                </h2>
                                <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        Creating a new physical JDBC connection via <code>DriverManager.getConnection()</code> incurs an expensive 50–100ms TCP socket handshake and authentication cost per HTTP request. <strong>HikariCP</strong> maintains a pre-allocated pool of active and idle connections ready for immediate reuse. It slashes connection acquisition latency to microsecond speeds and prevents database exhaustion under high concurrent load.
                                    </div>
                                </div>
                            </div>

                            <!-- Q3 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingThree">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree">
                                        3. How does DevFlow prevent SQL Injection attacks?
                                    </button>
                                </h2>
                                <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        All DAO classes strictly enforce parameterized <strong><code>PreparedStatement</code></strong> instances. The SQL structure is pre-compiled by the database engine, and user-supplied parameters are bound as raw literal values. Even if an attacker enters malicious SQL fragments (e.g. <code>' OR 1=1 --</code>), the engine treats them purely as data values, preventing query manipulation.
                                    </div>
                                </div>
                            </div>

                            <!-- Q4 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingFour">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFour">
                                        4. How is Role-Based Access Control (RBAC) enforced?
                                    </button>
                                </h2>
                                <div id="collapseFour" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        RBAC is enforced declaratively using <strong>Servlet Filters</strong> (<code>AuthenticationFilter</code> and <code>RoleFilter</code>). The filter intercepts incoming request paths (e.g., <code>/admin/*</code>), reads the current user principal stored in <code>HttpSession</code>, and compares permissions against the target action. Unauthorized users are blocked before the servlet controller ever executes.
                                    </div>
                                </div>
                            </div>

                            <!-- Q5 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingFive">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFive">
                                        5. How are user passwords securely stored?
                                    </button>
                                </h2>
                                <div id="collapseFive" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        Passwords are never stored in plaintext. DevFlow utilizes <strong>BCrypt hashing with a cost factor of 12</strong>. BCrypt automatically generates a unique 128-bit cryptographic salt for each user, defeating precomputed rainbow table attacks, while the computational work factor resists hardware-accelerated GPU brute force.
                                    </div>
                                </div>
                            </div>

                            <!-- Q6 -->
                            <div class="accordion-item border rounded-3 mb-2 overflow-hidden shadow-xs">
                                <h2 class="accordion-header" id="headingSix">
                                    <button class="accordion-button collapsed fw-semibold py-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSix">
                                        6. Describe the lifecycle when a task is dragged to "Completed" on the Kanban board.
                                    </button>
                                </h2>
                                <div id="collapseSix" class="accordion-collapse collapse" data-bs-parent="#vivaAccordion">
                                    <div class="accordion-body small text-muted">
                                        1. The client's HTML5 drag-and-drop triggers a <code>dragend</code> event in <code>kanban.js</code>.<br>
                                        2. An asynchronous <code>fetch()</code> POST request is sent to <code>/task/status-update</code> with the task ID and status <code>COMPLETED</code>.<br>
                                        3. <code>AuthenticationFilter</code> verifies the session, then <code>TaskController</code> parses parameters and invokes <code>taskService.updateStatus()</code>.<br>
                                        4. <code>TaskDAOImpl</code> executes a <code>PreparedStatement</code> update on MySQL table <code>tasks</code>.<br>
                                        5. The controller returns JSON <code>{"success":true}</code>, causing <code>kanban.js</code> to update badge counters and trigger a floating toast via <code>DevFlow.toast()</code>.
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal Footer -->
            <div class="modal-footer bg-body-tertiary border-top py-2 px-4 justify-content-between small text-muted">
                <span><i class="bi bi-mortarboard-fill text-warning me-1"></i> DevFlow Academic Showcase Edition</span>
                <button type="button" class="btn btn-sm btn-secondary rounded-pill px-3" data-bs-dismiss="modal">Close Guide</button>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- DevFlow Core JS -->
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
