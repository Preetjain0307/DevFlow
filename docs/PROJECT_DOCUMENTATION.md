# 📖 DevFlow Platform: Technical System Documentation

> **Complete System Specification, Entity Dictionary & Deployment Architecture**
> **Build Version:** 1.0.0-PROD
> **Backend Architecture:** Pure Java Servlet 4.0 / JSP 2.3 / JSTL 1.2 / HikariCP / MySQL 8.x/9.x

---

## 1. System Overview

DevFlow is a centralized developer collaboration and academic project management platform that bridges the gap between software development teams and academic faculty mentors.

### Core Modules:
1. **User Identity & Granular RBAC**: 5 distinct roles (`ADMIN`, `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`, `FACULTY`).
2. **Project Portfolio & Sprints**: Projects, milestones, sprint backlogs, and team member assignments.
3. **Interactive Kanban & Tasks**: HTML5 drag-and-drop task lifecycle board with real-time AJAX persistence.
4. **Defect & Bug Management**: Comprehensive defect logging, severity triage, and verification workflows.
5. **Democratic Idea & Change Proposals**: Peer voting tally engine with formal faculty mentor approval gates.
6. **Video Meetings & AI Minutes**: WebRTC video conferencing (Jitsi Meet) with automated AI notes summarization.
7. **Document Repository**: Version-controlled document repository for SRS, UML, Synopsis, and presentations.
8. **Git / GitHub Activity Synchronization**: Webhook and REST API commit/pull request tracking.
9. **Analytics & Audit Trails**: Interactive Chart.js metric visualizations and immutable security audit logs.

---

## 2. Database Entity Dictionary (26 Tables)

| # | Table Name | Purpose | Key Columns |
| :- | :--- | :--- | :--- |
| 1 | `roles` | System roles master table | `id`, `name`, `description` |
| 2 | `users` | User credentials, profiles, roles | `id`, `username`, `email`, `password_hash`, `role_id`, `status` |
| 3 | `projects` | Core projects and metadata | `id`, `project_key`, `name`, `manager_id`, `status`, `priority` |
| 4 | `project_members` | Project team rosters & roles | `id`, `project_id`, `user_id`, `project_role` |
| 5 | `milestones` | Project target milestones | `id`, `project_id`, `title`, `due_date`, `status` |
| 6 | `sprints` | Agile sprint cycles | `id`, `project_id`, `sprint_name`, `start_date`, `end_date`, `status` |
| 7 | `tasks` | Agile work items / backlog tasks | `id`, `project_id`, `sprint_id`, `title`, `priority`, `status`, `assigned_to` |
| 8 | `task_comments` | Collaboration threads on tasks | `id`, `task_id`, `user_id`, `comment`, `created_at` |
| 9 | `task_history` | Audit trail of task status changes | `id`, `task_id`, `user_id`, `field_changed`, `old_value`, `new_value` |
| 10 | `bugs` | Quality assurance defect reports | `id`, `project_id`, `title`, `severity`, `priority`, `status`, `reported_by` |
| 11 | `bug_comments` | Defect triage discussion notes | `id`, `bug_id`, `user_id`, `comment`, `created_at` |
| 12 | `meetings` | Video conference meetings | `id`, `project_id`, `title`, `meeting_date`, `room_code`, `meeting_url` |
| 13 | `meeting_participants`| Meeting attendance & invitations | `id`, `meeting_id`, `user_id`, `status`, `joined_at` |
| 14 | `meeting_notes` | Raw notes & AI summaries | `id`, `meeting_id`, `raw_notes`, `ai_summary`, `ai_decisions` |
| 15 | `meeting_action_items`| Action items extracted from meetings| `id`, `meeting_id`, `description`, `assigned_to`, `is_completed` |
| 16 | `documents` | Centralized project files | `id`, `project_id`, `title`, `category`, `file_name`, `file_path`, `version` |
| 17 | `document_versions` | Version history for documents | `id`, `document_id`, `version`, `file_name`, `change_notes` |
| 18 | `ideas` | Democratic change proposals | `id`, `project_id`, `title`, `problem_statement`, `status`, `faculty_status` |
| 19 | `idea_votes` | Team peer votes (YES/NO) | `id`, `idea_id`, `user_id`, `vote`, `comments`, `voted_at` |
| 20 | `idea_comments` | Peer discussions on proposals | `id`, `idea_id`, `user_id`, `comment`, `created_at` |
| 21 | `idea_history` | Lifecycle audit trail of proposals | `id`, `idea_id`, `user_id`, `action`, `notes` |
| 22 | `github_repositories`| Connected GitHub repositories | `id`, `project_id`, `repo_owner`, `repo_name`, `repo_url`, `is_active` |
| 23 | `github_activity` | Sync commit and PR events | `id`, `project_id`, `event_type`, `author_name`, `message`, `event_url` |
| 24 | `notifications` | In-app user notifications | `id`, `user_id`, `title`, `message`, `link_url`, `is_read` |
| 25 | `audit_logs` | Platform security & audit logging | `id`, `user_id`, `username`, `action`, `entity_type`, `details`, `ip_address` |
| 26 | `system_settings` | Global configuration key-values | `id`, `setting_key`, `setting_value`, `description` |

---

## 3. Quick-Start Deployment Options

### Option A: Standard Local Tomcat Execution (Quickest)
```cmd
:: 1. Initialize Database
setup_db.bat

:: 2. Start Application Server
run.bat
```
*Access DevFlow at:* `http://localhost:8080/DevFlow`

### Option B: Docker Containerized Execution
```bash
docker-compose up --build
```
*Access DevFlow at:* `http://localhost:8080`

### Option C: Standalone Apache Tomcat 9 Deployment
1. Run `package.bat` to generate `target/DevFlow.war`.
2. Copy `target/DevFlow.war` to `<TOMCAT_HOME>/webapps/DevFlow.war`.
3. Start Tomcat via `<TOMCAT_HOME>/bin/startup.bat`.
