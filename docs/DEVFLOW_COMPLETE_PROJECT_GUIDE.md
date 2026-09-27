# 🚀 DEVFLOW: Master Project Architecture & Feature Manual

> **An Enterprise-Grade, Multi-Role Academic & Agile Engineering Platform built in Pure Advanced Java.**  
> *Course Project, Academic Capstone & Enterprise Showcase Edition*

---

## 📑 Table of Contents
1. [Executive Vision & Platform Overview](#1-executive-vision--platform-overview)
2. [End-to-End System Architecture](#2-end-to-end-system-architecture)
3. [Design Patterns & Java EE Architectural Standards](#3-design-patterns--java-ee-architectural-standards)
4. [Role-Based Access Control (RBAC) & Personas](#4-role-based-access-control-rbac--personas)
5. [Comprehensive Feature-by-Feature Deep Dive](#5-comprehensive-feature-by-feature-deep-dive)
   - 5.1 [Authentication, Security & Identity Governance](#51-authentication-security--identity-governance)
   - 5.2 [Project Workspace & Multi-Tenancy](#52-project-workspace--multi-tenancy)
   - 5.3 [Sprint & Milestone Tracking](#53-sprint--milestone-tracking)
   - 5.4 [Task Management & Interactive Kanban Drag-and-Drop](#54-task-management--interactive-kanban-drag-and-drop)
   - 5.5 [Defect & Bug Tracking Lifecycle](#55-defect--bug-tracking-lifecycle)
   - 5.6 [Idea Incubator & Democratic Peer Voting Engine](#56-idea-incubator--democratic-peer-voting-engine)
   - 5.7 [Faculty Review Desk & Academic Governance](#57-faculty-review-desk--academic-governance)
   - 5.8 [Virtual Meetings & AI Standup Assistant](#58-virtual-meetings--ai-standup-assistant)
   - 5.9 [Analytics, Reports & Printable Executive Layout](#59-analytics-reports--printable-executive-layout)
   - 5.10 [Data Exports to CSV (RFC 4180 + Excel BOM)](#510-data-exports-to-csv-rfc-4180--excel-bom)
   - 5.11 [Global Quick Search Command Palette (`Ctrl + K`)](#511-global-quick-search-command-palette-ctrl--k)
   - 5.12 [Document Repository & Multipart Uploads](#512-document-repository--multipart-uploads)
   - 5.13 [GitHub REST Integration & Activity Feeds](#513-github-rest-integration--activity-feeds)
   - 5.14 [Real-Time Notification Dispatcher](#514-real-time-notification-dispatcher)
   - 5.15 [In-App "Architecture & Viva Voce Guide" Modal](#515-in-app-architecture--viva-voce-guide-modal)
   - 5.16 [System-Wide Dark / Light Mode Theme Engine](#516-system-wide-dark--light-mode-theme-engine)
   - 5.17 [Floating Toast Notification Engine](#517-floating-toast-notification-engine)
6. [Database Architecture & 26-Table Relational Schema](#6-database-architecture--26-table-relational-schema)
7. [Examiner Viva Voce Defense & Model Answers](#7-examiner-viva-voce-defense--model-answers)
8. [Setup, Deployment & Testing Reference](#8-setup-deployment--testing-reference)

---

## 1. Executive Vision & Platform Overview

**DevFlow** is a comprehensive, enterprise-class developer collaboration and academic capstone governance platform. Designed to bridge the gap between industrial Agile Scrum project tracking and academic thesis evaluation, DevFlow integrates project tracking, defect resolution, democratic innovation incubation, faculty mentor reviews, AI-assisted standup notes, and real-time HikariCP telemetry.

### Core Engineering Philosophy: Pure Advanced Java
While modern web development frequently leans on opaque full-stack frameworks (such as Spring Boot), DevFlow is deliberately crafted in **Pure Advanced Java (Java EE)** using standard Java Servlets (`javax.servlet 4.0`), JavaServer Pages (`JSP 2.3`), JavaServer Pages Standard Tag Library (`JSTL 1.2`), and raw JDBC with HikariCP. 

This guarantees:
- **Zero Framework Magic**: Complete transparency over the HTTP request-response cycle, filter chains, session management, and database transactions.
- **Ultra-High Performance & Low Memory Footprint**: Boots in seconds on Apache Tomcat with minimal JVM overhead.
- **Maximum Academic Rigor**: Demonstrates mastery of core computer science fundamentals, design patterns, concurrent connection pooling, and multi-tenant relational database schemas.

---

## 2. End-to-End System Architecture

DevFlow strictly adheres to the classical **3-Tier Model-View-Controller (MVC)** enterprise architecture:

```
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           1. CLIENT / PRESENTATION LAYER                         │
│  - Modern Vanilla CSS Design Tokens (Light/Dark themes in style.css)             │
│  - Responsive Bootstrap 5.3 & Bootstrap Icons                                     │
│  - JSP / JSTL Templates (header, navbar, sidebar, footer, views)                 │
│  - HTML5 Drag-and-Drop Engine (kanban.js)                                        │
│  - Dynamic Chart.js Analytics (charts.js with center total metric plugin)       │
│  - Client Interceptors & Command Palette (app.js: Ctrl+K, toasts, AJAX forms)    │
└────────────────────────────────────────┬─────────────────────────────────────────┘
                                         │ HTTP Request / Fetch AJAX (JSON)
                                         ▼
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           2. SECURITY & FILTER PIPELINE                          │
│  - CharacterEncodingFilter: Enforces UTF-8 encoding across all requests          │
│  - AuthenticationFilter: Validates HTTP Sessions, protects restricted endpoints  │
│  - RoleAuthorizationFilter: Validates granular RBAC permissions against URI      │
└────────────────────────────────────────┬─────────────────────────────────────────┘
                                         │ Filter Chain Continuation
                                         ▼
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           3. CONTROLLER LAYER (SERVLETS)                         │
│  - 16 Modular Java Servlets (@WebServlet): AuthController, TaskController,       │
│    BugController, IdeaController, MeetingController, ProjectController, etc.     │
│  - Dispatches GET/POST, handles multipart uploads, and serializes JSON / CSV     │
└────────────────────────────────────────┬─────────────────────────────────────────┘
                                         │ Method Invocations
                                         ▼
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           4. BUSINESS SERVICE LAYER                              │
│  - Plain Old Java Objects (POJO Services): TaskService, IdeaService, etc.        │
│  - Democratic Idea Voting Logic, Validation, Heuristic NLP Meeting Analyzer      │
│  - Transaction Orchestration & Audit Event Logging                               │
└────────────────────────────────────────┬─────────────────────────────────────────┘
                                         │ Parameterized Queries
                                         ▼
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           5. DATA ACCESS OBJECT (DAO) LAYER                      │
│  - Pure JDBC DAOs: TaskDAO, BugDAO, IdeaDAO, UserDAO, AuditLogDAO, etc.          │
│  - Parameterized PreparedStatements (100% SQL-injection immune)                  │
│  - ResultSet to Model Object Mapping                                             │
└────────────────────────────────────────┬─────────────────────────────────────────┘
                                         │ Connection Borrow / Return
                                         ▼
┌──────────────────────────────────────────────────────────────────────────────────┐
│                           6. CONNECTION POOL & PERSISTENCE                       │
│  - HikariCP 4.0.3 High-Throughput Connection Pool (DevFlowHikariPool)            │
│  - MySQL 8.x / 9.x Relational Database with 26 Normalized Tables                 │
│  - Foreign Keys, Cascade Rules, Unique Constraints, and Composite Indexes        │
└──────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Design Patterns & Java EE Architectural Standards

| Pattern | Implementation in DevFlow | Concrete Purpose |
| :--- | :--- | :--- |
| **Model-View-Controller (MVC)** | Servlets (Controller), JSP/JSTL (View), Models (JavaBeans) | Decouples business logic from rendering and routing. |
| **Data Access Object (DAO)** | `com.devflow.dao.*` interfaces and `*.impl` implementations | Isolates all raw SQL and JDBC interactions from business logic. |
| **Service Layer Pattern** | `com.devflow.service.*` interfaces and `*.impl` | Houses domain logic, input sanitization, and workflow rules. |
| **Connection Pooling (Factory)** | `com.devflow.config.DBConnection` wrapping `HikariDataSource` | Manages physical database connections, preventing socket exhaustion. |
| **Intercepting Filter** | `AuthenticationFilter`, `RoleAuthorizationFilter` | Declarative pre-processing for authentication and access control. |
| **Template Method / Heuristic Fallback**| `AIServiceImpl` | Dynamically detects API keys, seamlessly falling back to heuristic NLP parser. |
| **Front Controller per Domain**| Domain-specific Servlets (`/tasks`, `/bugs`, `/ideas`, `/meetings`) | Clean REST-like URI structure without a monolithic god-servlet. |

---

## 4. Role-Based Access Control (RBAC) & Personas

DevFlow enforces a strict 5-tier Role-Based Access Control hierarchy stored in the `roles` and `users` tables:

```
               👑 SYSTEM ADMINISTRATOR (Role ID: 1)
                                │
               📋 PROJECT MANAGER (Role ID: 2)
                                │
         ┌──────────────────────┴──────────────────────┐
         ▼                                             ▼
  💻 LEAD DEVELOPER (Role ID: 3)                 🐞 QA TESTER (Role ID: 4)
  
               🎓 FACULTY MENTOR (Role ID: 5)
```

### Detailed Persona Entitlement Matrix

| Persona | Code Identifier | Primary Responsibilities & Permitted Actions |
| :--- | :--- | :--- |
| **👑 System Administrator** | `ADMIN` | Total system governance, user status toggling (Active/Suspended), global system settings, system health telemetry, full audit log inspection. |
| **📋 Project Manager** | `PROJECT_MANAGER` | Creates projects, defines milestones, manages Scrum sprints, allocates tasks, monitors burndown charts and team velocity. |
| **💻 Lead Developer** | `DEVELOPER` | Executes assigned tasks, transitions cards on Kanban board, commits git activity, reviews pull requests, reports defects. |
| **🐞 QA / Tester** | `TESTER` | Logs defect findings, defines steps to reproduce, sets bug severity/priority, verifies resolved bugs, tracks defect density. |
| **🎓 Faculty Mentor** | `FACULTY` | Evaluates student proposals at the Faculty Review Desk, approves ideas (which auto-generates backlog tasks), requests revisions, and reviews code compliance. |

### 🎭 1-Click Instant Persona Switcher
For live demonstrations and examiner evaluation, DevFlow includes **instant demo credentials** accessible through the top navbar dropdown or direct URLs:
- `http://localhost:8080/DevFlow/demo-login?role=admin` $\rightarrow$ Logs in as **Alex Mercer** (Admin)
- `http://localhost:8080/DevFlow/demo-login?role=pm` $\rightarrow$ Logs in as **Sarah Chen** (Project Manager)
- `http://localhost:8080/DevFlow/demo-login?role=dev` $\rightarrow$ Logs in as **Alex Developer** (Developer)
- `http://localhost:8080/DevFlow/demo-login?role=tester` $\rightarrow$ Logs in as **Mark QA** (Tester)
- `http://localhost:8080/DevFlow/demo-login?role=faculty` $\rightarrow$ Logs in as **Dr. Alan Turing** (Faculty Mentor)

---

## 5. Comprehensive Feature-by-Feature Deep Dive

### 5.1 Authentication, Security & Identity Governance
- **Password Security**: Implemented with BCrypt one-way adaptive cryptographic hashing (`jbcrypt 0.4`) with salt rounds stored directly in MySQL.
- **Session Lifecycle**: Explicit HTTP session invalidation upon logout; automatic session timeout protection.
- **Audit Logging**: Every security-sensitive mutation (`LOGIN`, `CREATE_TASK`, `STATUS_CHANGE`, `REPORT_BUG`, `CAST_VOTE`) records the user ID, username, action code, target entity, client IP address, and timestamp into `audit_logs`.
- **User Profile Dashboard (`/profile`)**:
  - Displays user profile metadata with instant inline updates.
  - **Visual RBAC Entitlement Matrix**: Renders a dynamic 9-point permission checklist with green checkmarks or muted restrictions.
  - **Assigned Deliverables Tabs**: Direct lists of assigned tasks and assigned bugs.
  - **User Activity Audit Trail**: Tabular timeline showing the user's last 15 security events.

### 5.2 Project Workspace & Multi-Tenancy
- **Project Repository**: Supports multiple projects with unique project keys (e.g. `DEVFLOW`, `ACAD`).
- **Team Allocation**: Assigns project members with explicit roles.
- **Contextual Filtering**: Dropdown filter across Tasks, Bugs, Meetings, and Reports persists the selected project across views.

### 5.3 Sprint & Milestone Tracking
- **Scrum Sprints (`/sprints`)**: Time-boxed sprint planning with start dates, end dates, sprint goals, and status (`PLANNED`, `ACTIVE`, `COMPLETED`).
- **Milestones (`/milestones`)**: High-level deliverables with target due dates and dynamic completion progress bars based on associated task resolution.

### 5.4 Task Management & Interactive Kanban Drag-and-Drop
- **Task Attributes**: Priority (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`), Task Type (`FEATURE`, `BUG`, `REFACTOR`, `DOCUMENTATION`), estimated hours, due dates, assignee, and sprint association.
- **Interactive HTML5 Drag-and-Drop Kanban Board (`/task/kanban`)**:
  - Organizes work into 4 synchronized lanes: `TODO`, `IN_PROGRESS`, `IN_REVIEW`, and `COMPLETED`.
  - **Smooth Visual Feedback**: Draggable cards feature elevation shadows, ghost drag effects (`.is-dragging`), and drop target dashed highlighting (`.drag-over`).
  - **Dynamic Empty Column Hints**: Shows helpful guidance hints when columns become empty.
  - **Asynchronous AJAX Persistence**: Dropping a card makes a non-blocking `POST` to `/task/status-update`, recalculates column counters, and triggers an immediate floating toast alert.

### 5.5 Defect & Bug Tracking Lifecycle
- **Bug Attributes**: Summary, steps to reproduce, expected vs. actual results, severity (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`), priority, and status (`OPEN`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`).
- **Traceability**: Directly links defects to specific parent tasks and projects.
- **Resolution Verification**: Captures resolution notes and notifies reporters when fixed.
- **Asynchronous Comments**: Submitting a defect comment updates the comments list in real time with `.fade-in-slide` CSS animations without page refresh.

### 5.6 Idea Incubator & Democratic Peer Voting Engine
- **Democratized Innovation**: Students or team members propose project enhancements, architectural revisions, or feature additions.
- **Interactive Peer Voting**:
  - Proposals in status `IN_VOTING` display a dedicated **Team Peer Voting** card.
  - **Real-Time Dynamic Voting**: Clicking **Upvote** or **Downvote** immediately sends an asynchronous AJAX request, updating counters with a scale-up animation.
  - **Active Button Styling**: Active selections are visually highlighted (filled green `btn-success` for upvote, filled red `btn-danger` for downvote).
  - **User Status Badge**: Displays dynamic badges: `"You upvoted this proposal"` or `"You downvoted this proposal"`.
  - **Persistent Storage**: Recorded in the MySQL `idea_votes` table with `ON DUPLICATE KEY UPDATE` handling to support flipping votes seamlessly.

### 5.7 Faculty Review Desk & Academic Governance
- **Dedicated Review Portal (`/ideas?action=facultyReview`)**:
  - Reserved for users with `FACULTY` or `ADMIN` roles.
  - Allows faculty to inspect student proposals, review peer voting consensus, and submit formal verdicts:
    - **Approve**: Automatically changes status to `APPROVED` and generates a backlog task in the Scrum sprint.
    - **Reject**: Marks proposal `REJECTED` with mandatory faculty guidance remarks.
    - **Request Changes**: Reverts status to `CHANGES_REQUESTED` so students can revise and resubmit.

### 5.8 Virtual Meetings & AI Standup Assistant
- **Meeting Management (`/meetings`)**: Schedules standups, sprint retrospectives, and architecture reviews with start/end timestamps and room codes.
- **Embedded Video Conferencing**: Integrated **Jitsi Meet API** (`meet.jit.si`) for browser-based WebRTC video and audio calls with zero third-party software installation.
- **AI Standup Meeting Assistant (`/meetings?action=view`)**:
  - **⚡ 1-Click Sample Notes Preloader**: Instant dropdown preloading 3 realistic engineering presets:
    1. *Daily Standup & Sprint Sync* (Alex, Priya, Sarah, Mark)
    2. *Sprint Retrospective & Bug Triage*
    3. *Architecture & Security Review*
  - **Heuristic NLP Analyzer**: Uses an intelligent pattern parser that extracts executive summaries, architectural decisions, action items, and team responsibilities even when external AI API keys are offline.

### 5.9 Analytics, Reports & Printable Executive Layout
- **Polished Visual Engine (`/reports`)**:
  - **Dynamic Doughnut Center Metric Plugin**: Custom Chart.js plugin that computes and renders the live `TOTAL` metric counter directly in the doughnut cutout.
  - **Vertical Gradient Severity Bars**: Custom vertical linear gradients with rounded pill tops on bug severity charts.
  - **Live Filter Synchronization**: Selecting a project asynchronously pulls fresh chart distributions via `/reports/data`.
- **Printable Executive Project Report View**:
  - Accessible via the **"Print / Export PDF"** button.
  - Optimized `@media print` CSS strips navbars, sidebars, buttons, and dark backgrounds, formatting clean black-and-white margins.
  - Includes **Academic Evaluation Sign-Off Blocks** with formal signature lines for **Candidate**, **Internal Guide**, and **External Examiner**.

### 5.10 Data Exports to CSV (RFC 4180 + Excel BOM)
- **1-Click CSV Exports**:
  - Export all Tasks: `/tasks?action=export`
  - Export all Defects: `/bugs?action=export`
- **Excel Compatibility**: Automatically prepends the UTF-8 Byte Order Mark (`\ufeff`) so Microsoft Excel natively opens CSV files with special characters and symbols without character corruption.

### 5.11 Global Quick Search Command Palette (`Ctrl + K`)
- **Spotlight Search**: Triggerable via `Ctrl + K` (Windows/Linux), `Cmd + K` (macOS), or the persistent search button in the top navbar.
- **Fast Multithreaded Search**: Powered by [`SearchController.java`](file:///d:/adv_java/DevFlow/src/main/java/com/devflow/controller/SearchController.java) querying across Projects, Tasks, Bugs, and Proposals with debounce optimization (250ms).
- **Keyboard Navigation**: Supports Arrow Up/Down navigation, Enter to open, and Escape to close.

### 5.12 Document Repository & Multipart Uploads
- **File Management (`/documents`)**: Upload project deliverables, requirement specifications, and architecture diagrams.
- **Multipart Config**: Handled via standard `@MultipartConfig` with file size validations up to 25MB.

### 5.13 GitHub REST Integration & Activity Feeds
- **Git Telemetry (`/github`)**: Displays repository metadata, commit histories, branch lists, pull requests, and webhooks.
- **Simulated Real-World Commits**: Pre-seeded with realistic commit messages and code diff histories matching project architecture.

### 5.14 Real-Time Notification Dispatcher
- **Notification Inbox (`/notifications`)**: Automatically notifies assignees upon task allocation, defect assignment, proposal decisions, and meeting invitations.
- **Unread Badge Polling**: Polling endpoint `/api/notifications/count` keeps the top navigation bell badge up to date.
- **1-Click Mark All Read**: Clears all notifications instantly.

### 5.15 In-App "Architecture & Viva Voce Guide" Modal
- **Prominent Navbar Trigger**: Glowing amber **🎓 Viva & Architecture** button in the top navigation bar.
- **Interactive Multi-Tab Hub**:
  - **Tab 1: Live Architecture**: Interactive visual diagram of the 3-tier MVC request workflow.
  - **Tab 2: Live Telemetry**: Live metrics pulled via `/api/architecture/status`:
    - HikariCP active vs. idle connection count.
    - MySQL database engine, driver version, and total table count.
    - JVM Heap memory utilization progress bar and total available memory.
  - **Tab 3: Persona Switcher**: 1-click login cards for all 5 roles.
  - **Tab 4: Examiner Viva Voce Cheat Sheet**: 6 expandable accordions with verified academic defense answers.

### 5.16 System-Wide Dark / Light Mode Theme Engine
- **Sleek SaaS Color Palette**: Built on curated dark slate tones (`#0f172a`, `#1e293b`, `#334155`) paired with clean light tones.
- **Anti-Flicker Injection**: Instant script in `<head>` applies saved theme from `localStorage` before the DOM renders, preventing blinding white flashes.
- **Smooth Theme Transitions**: Configured via CSS variables with rotational icon animations on the navbar Sun/Moon toggle.

### 5.17 Floating Toast Notification Engine
- **Non-Blocking Feedback Engine**: Accessible across all scripts via `DevFlow.toast(message, type, title)`.
- **Themed Variants**: Distinct styles and SVG icons for `success`, `danger`, `warning`, and `info`.
- **Auto-Dismiss**: Automatically animates into view and dismisses after 3.5 seconds.

---

## 6. Database Architecture & 26-Table Relational Schema

DevFlow's backend is backed by **26 normalized MySQL tables** enforcing referential integrity with cascading updates and deletes:

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│      roles      │◄──────┤      users      │◄──────┤   audit_logs    │
└─────────────────┘       └────────┬────────┘       └─────────────────┘
                                   │
                                   ├─────────────────────────────┐
                                   ▼                             ▼
                        ┌──────────────────┐          ┌───────────────────┐
                        │     projects     │          │   notifications   │
                        └────────┬─────────┘          └───────────────────┘
                                 │
     ┌───────────────────────────┼───────────────────────────┐
     ▼                           ▼                           ▼
┌─────────┐                ┌───────────┐               ┌───────────┐
│ sprints │                │   tasks   │               │   bugs    │
└────┬────┘                └─────┬─────┘               └─────┬─────┘
     │                           │                           │
     ▼                           ▼                           ▼
┌──────────────┐           ┌──────────────┐            ┌──────────────┐
│  milestones  │           │ task_comments│            │ bug_comments │
└──────────────┘           └──────────────┘            └──────────────┘
```

### Table Directory & Descriptions

| # | Table Name | Purpose & Contents |
| :-: | :--- | :--- |
| 1 | `roles` | Defines the 5 core RBAC personas (`ADMIN`, `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `FACULTY`). |
| 2 | `users` | User credentials, BCrypt password hashes, email, phone, designation, bio, and account status. |
| 3 | `projects` | Projects with project keys, descriptions, start/end dates, and owner references. |
| 4 | `project_members` | Join table mapping users to projects with project-specific roles. |
| 5 | `tasks` | Backlog and sprint tasks with priority, status, estimated hours, and assignees. |
| 6 | `task_comments` | Discussion threads on tasks with author references and timestamps. |
| 7 | `task_attachments` | Metadata for files and assets attached to specific tasks. |
| 8 | `task_history` | Audit trail of task status and assignment transitions. |
| 9 | `sprints` | Scrum sprints with start dates, end dates, and sprint goals. |
| 10 | `milestones` | High-level project deliverables with target completion dates. |
| 11 | `bugs` | Defect findings with severity, priority, replication steps, and status. |
| 12 | `bug_comments` | Discussion and triage comments on reported bugs. |
| 13 | `bug_attachments` | Screenshots and logs attached to defect reports. |
| 14 | `bug_history` | Historical audit log of defect status transitions. |
| 15 | `ideas` | Democratic innovation proposals submitted for peer voting and faculty review. |
| 16 | `idea_votes` | Individual peer votes (`YES` / `NO`) cast by users with composite unique constraint. |
| 17 | `idea_comments` | Community discussion comments on submitted proposals. |
| 18 | `idea_history` | Lifecycle transition log of proposal review decisions. |
| 19 | `meetings` | Scheduled team syncs and standups with dates, times, and Jitsi URLs. |
| 20 | `meeting_participants`| Join table tracking meeting invitations and attendance status. |
| 21 | `meeting_notes` | Raw standup notes and structured AI summary JSON outputs. |
| 22 | `action_items` | Follow-up action tasks generated during meetings with assignees. |
| 23 | `documents` | Uploaded project documents with MIME types, file sizes, and paths. |
| 24 | `document_versions`| Version history of uploaded project documents. |
| 25 | `notifications` | User notifications with read/unread flags and target URI links. |
| 26 | `audit_logs` | Comprehensive security and data mutation audit trail. |

---

## 7. Examiner Viva Voce Defense & Model Answers

### Q1: Why use Pure Advanced Java (Servlets & JSP) instead of Spring Boot?
> **Answer**:  
> "Spring Boot hides the underlying mechanics of web applications through auto-configuration, reflection, and proxying. Building DevFlow in Pure Advanced Java demonstrates a fundamental mastery of the Java Enterprise Edition specification (`javax.servlet`). We explicitly manage the HTTP request-response lifecycle, implement custom security filter chains, manage database connection pools via HikariCP, and design thread-safe DAOs using JDBC PreparedStatements. This approach provides transparency, ultra-fast container startup, and low memory consumption."

### Q2: Why HikariCP over standard `DriverManager.getConnection()`?
> **Answer**:  
> "`DriverManager.getConnection()` creates a physical TCP socket handshake to MySQL on every request, which introduces high latency (~50-100ms) and quickly exhausts operating system sockets under concurrent load. HikariCP is a zero-overhead, highly optimized byte-code connection pool that keeps a configured pool of warm, authenticated database connections ready for immediate borrowing, reducing connection acquisition time to sub-millisecond speeds."

### Q3: How is SQL Injection prevented?
> **Answer**:  
> "Every database query in DevFlow strictly utilizes `PreparedStatement` with parameterized placeholders (`?`). The SQL query structure is pre-compiled by the MySQL database engine before parameter values are bound. User inputs are treated strictly as data literals, never executable SQL tokens, completely neutralizing SQL Injection vulnerabilities."

### Q4: How does the Kanban drag-and-drop persist data?
> **Answer**:  
> "When a task card is dropped into a new column, the HTML5 Drag-and-Drop listener in `kanban.js` intercepts the event, reads the `data-task-id` and target column's `data-status`, and initiates an asynchronous `fetch()` POST request with `X-Requested-With: XMLHttpRequest` to `/task/status-update`. `TaskController` processes the status change, updates the database via `TaskDAO`, logs an audit entry, and returns a JSON response. The client dynamically increments/decrements column badge counters and displays a floating toast confirmation without reloading the browser."

---

## 8. Setup, Deployment & Testing Reference

### Prerequisites
- **Java Development Kit (JDK)**: JDK 17 (or JDK 11+)
- **Build Tool**: Apache Maven 3.8+
- **Database**: MySQL 8.0+ running on port `3306`
- **Application Server**: Apache Tomcat 7+ (or embedded Maven plugin)

### 1. Database Initialization
```sql
CREATE DATABASE IF NOT EXISTS devflow_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Import schema and seed data:
```bash
mysql -u root -p devflow_db < database/schema.sql
mysql -u root -p devflow_db < database/sample_data.sql
```

### 2. Configure Database Credentials
Edit `src/main/resources/db.properties`:
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/devflow_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=your_password
db.pool.maximumPoolSize=10
```

### 3. Build & Run with Maven
```bash
mvn clean compile
mvn tomcat7:run
```
The application will be live at:
👉 **`http://localhost:8080/DevFlow/`**

### 4. Running the Automated Test Suite
DevFlow includes an automated integration test script validating all 50 endpoints and mutation workflows:
```powershell
powershell -ExecutionPolicy Bypass -File target/test_full_suite.ps1
```
Expected output:
```text
==========================================
   DEVFLOW FULL APPLICATION TEST SUITE    
==========================================
Total Tests: 50 | Passed: 50 | Failed: 0
==========================================
```

---

*DevFlow: Engineered with precision for advanced software engineering coursework and enterprise technical evaluations.*
