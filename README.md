# DEVFLOW: Developer Collaboration Platform

> **An Enterprise-Grade, Multi-Role Academic & Agile Engineering Platform built in Pure Advanced Java.**
> **Course Project & Academic Capstone Submission Edition**

---

## 🌟 Quick Demonstration & Teacher Evaluation

DevFlow includes **1-Click Instant Demo Access** for rapid academic evaluation without typing passwords:

- 🚀 **Universal 1-Click Demo Login**: `http://localhost:8080/DevFlow/demo-login?role=demo`
- 👑 **System Admin Demo**: `http://localhost:8080/DevFlow/demo-login?role=admin`
- 📋 **Project Manager Demo**: `http://localhost:8080/DevFlow/demo-login?role=pm`
- 💻 **Lead Developer Demo**: `http://localhost:8080/DevFlow/demo-login?role=dev`
- 🐞 **QA Tester Demo**: `http://localhost:8080/DevFlow/demo-login?role=tester`
- 🎓 **Faculty Mentor / Evaluator Demo**: `http://localhost:8080/DevFlow/demo-login?role=faculty`

*During live demonstration, use the **🎭 Live Persona Switcher** dropdown in the top navbar to switch between roles in real time!*

*Default credentials for manual login:* Password is **`password123`** for all accounts.

---

## 🏗️ Technical Architecture & Technology Stack

```
   ┌─────────────────────────────────────────────────────────────┐
   │                Presentation Layer (JSP / JSTL)              │
   │   Bootstrap 5.3 · Chart.js · Kanban D&D · Jitsi Meet Embed   │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ HTTP / AJAX (JSON)
   ┌──────────────────────────────▼──────────────────────────────┐
   │                Filter Layer (Security & Auth)               │
   │   AuthenticationFilter · RoleAuthorizationFilter · Encoding │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ MVC Dispatch
   ┌──────────────────────────────▼──────────────────────────────┐
   │                Controller Layer (Java Servlets)             │
   │   AuthController · TaskController · IdeaController · etc.   │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ Business Logic
   ┌──────────────────────────────▼──────────────────────────────┐
   │                 Service Layer (Business POJOs)              │
   │   IdeaVotingEngine · AIService (NLP/Gemini) · UserService   │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ CRUD Operations
   ┌──────────────────────────────▼──────────────────────────────┐
   │                  Data Access Layer (DAOs)                   │
   │   PreparedStatement · Transaction Mgmt · Model Mapping      │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ HikariCP Pool
   ┌──────────────────────────────▼──────────────────────────────┐
   │                Database Layer (MySQL 8.x / 9.x)             │
   │        26 Normalized Tables · Foreign Keys · Indexes        │
   └─────────────────────────────────────────────────────────────┘
```

- **Backend Architecture**: Pure Java Servlets (`javax.servlet 4.0`), JSP 2.3, JSTL 1.2, JDBC
- **Database**: MySQL 8.x / 9.x with 26 normalized tables, foreign keys, and indexes
- **Connection Pool**: HikariCP 4.0.3 high-throughput JDBC connection pool
- **Security**: BCrypt password hashing (`jbcrypt 0.4`), parameterized SQL queries, RBAC filters, XSS protection
- **Integrations**:
  - **Jitsi Meet API**: In-app WebRTC video conferencing for standups and reviews
  - **AI Meeting Summaries**: Google Gemini API / OpenAI with offline NLP heuristic fallback
  - **GitHub REST API & Webhooks**: Synchronize commits, branches, issues, and pull requests
- **UI Design**: Modern Bootstrap 5.3 SaaS layout, Bootstrap Icons, Chart.js, HTML5 Drag & Drop

---

## 🚀 Quick-Start Deployment (3 Options)

### Option 1: One-Click Windows Batch (Fastest)
1. **Initialize Database**: Double-click [`setup_db.bat`](setup_db.bat) (or run in CMD).
2. **Start Server**: Double-click [`run.bat`](run.bat).
3. **Open Browser**: Navigate to `http://localhost:8080/DevFlow`.

### Option 2: Docker Compose (Zero Configuration)
```bash
docker-compose up --build
```
*Access DevFlow at:* `http://localhost:8080`

### Option 3: Standard Maven & Apache Tomcat 9 Deployment
1. Import database scripts:
   ```bash
   mysql -u root -p devflow_db < database/schema.sql
   mysql -u root -p devflow_db < database/sample_data.sql
   ```
2. Build WAR package:
   ```bash
   mvn clean package -DskipTests
   ```
3. Copy `target/DevFlow.war` to your Tomcat `webapps/` folder and start Tomcat.

---

## 👥 5 Granular System Roles

| Role | Username | Permissions & Responsibilities |
| :--- | :--- | :--- |
| **`ADMIN`** | `admin` | Complete platform governance: Manage users, system settings, AI keys, audit trails, and global projects. |
| **`PROJECT_MANAGER`** | `pm_sarah` | Project lifecycle: Create projects, assign team members, plan sprints, create milestones, manage backlogs. |
| **`DEVELOPER`** | `dev_alex` | Development work: Update tasks on Kanban, submit idea proposals, log work, connect Git repositories. |
| **`TESTER`** | `tester_mark` | Quality assurance: File bug reports, manage test triage, verify defect resolutions. |
| **`FACULTY`** | `faculty_dr_alan` | Academic oversight: Evaluate submitted ideas/proposals, approve/reject/request changes, attend milestone reviews. |

---

## 💡 Key Academic Innovation: Democratized Proposal & Faculty Approval

1. **Submit Proposal**: Developers submit change proposals with justifications and effort estimates.
2. **Team Peer Voting**: Team members cast **Upvotes / Downvotes**.
3. **Quorum Tally**: When net upvotes reach quorum (>= 2), the proposal automatically transitions to `PENDING_FACULTY`.
4. **Faculty Evaluation Desk**: Faculty mentors review proposals and make official decisions (`APPROVE`, `REJECT`, `REQUEST_CHANGES`).
5. **Automated Task Conversion**: Approved proposals **automatically generate actionable backlog Tasks in the project**.

---

## 📚 Academic Documentation & Teacher Guides

- 🎓 [Teacher Evaluation & Viva Voce Guide](docs/TEACHER_EVALUATION_GUIDE.md) — 35+ Viva Voce Q&As, architecture deep-dive, and grading rubric.
- 📖 [Technical System Documentation](docs/PROJECT_DOCUMENTATION.md) — 26-Table database dictionary and SRS specification.

---

## 📄 License
Academic and Commercial Open Source — **MIT License**.
