-- ============================================================
-- DEVFLOW: Developer Collaboration Platform
-- Enterprise & Academic Sample Dataset (MySQL)
-- Default password for all sample accounts is: password123
-- BCrypt Hash: $2a$10$klWy6NFmOy4/dFChFSKLaOLTdYMUeArGoi/VF27LMA81geusMSI5y
-- ============================================================

USE devflow_db;

-- Disable Foreign Key checks temporarily for clean reset
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE idea_history;
TRUNCATE TABLE idea_comments;
TRUNCATE TABLE idea_votes;
TRUNCATE TABLE ideas;
TRUNCATE TABLE document_versions;
TRUNCATE TABLE documents;
TRUNCATE TABLE meeting_action_items;
TRUNCATE TABLE meeting_notes;
TRUNCATE TABLE meeting_participants;
TRUNCATE TABLE meetings;
TRUNCATE TABLE bug_comments;
TRUNCATE TABLE bugs;
TRUNCATE TABLE task_history;
TRUNCATE TABLE task_comments;
TRUNCATE TABLE tasks;
TRUNCATE TABLE sprints;
TRUNCATE TABLE milestones;
TRUNCATE TABLE project_members;
TRUNCATE TABLE github_activity;
TRUNCATE TABLE github_repositories;
TRUNCATE TABLE notifications;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE system_settings;
TRUNCATE TABLE projects;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. INSERT ROLES
INSERT INTO roles (id, name, description) VALUES
(1, 'ADMIN', 'System Administrator with full management & platform governance permissions'),
(2, 'PROJECT_MANAGER', 'Project Manager managing teams, sprints, tasks, and project lifecycle'),
(3, 'DEVELOPER', 'Core Developer working on tasks, ideas, discussions, and GitHub integration'),
(4, 'TESTER', 'QA Tester reporting bugs, managing triage, and verifying defect resolutions'),
(5, 'FACULTY', 'Academic Faculty reviewing progress, approving or rejecting student proposals');

-- 2. INSERT SAMPLE USERS (Password for all: password123)
SET @PWD_HASH = '$2a$10$klWy6NFmOy4/dFChFSKLaOLTdYMUeArGoi/VF27LMA81geusMSI5y';

INSERT INTO users (id, username, email, password_hash, full_name, role_id, phone, designation, bio, status) VALUES
(1, 'admin', 'admin@devflow.io', @PWD_HASH, 'System Administrator', 1, '+1-555-0100', 'Lead DevOps & Admin', 'Platform administrator managing users, security, and infrastructure.', 'ACTIVE'),
(2, 'pm_sarah', 'sarah.pm@devflow.io', @PWD_HASH, 'Sarah Connor', 2, '+1-555-0101', 'Senior Agile Project Manager', 'Certified Scrum Master and project manager leading student software engineering teams.', 'ACTIVE'),
(3, 'dev_alex', 'alex.dev@devflow.io', @PWD_HASH, 'Alex Mercer', 3, '+1-555-0102', 'Lead Backend Architect', 'Java specialist focused on high-throughput JDBC, HikariCP, and RESTful architectures.', 'ACTIVE'),
(4, 'dev_priya', 'priya.dev@devflow.io', @PWD_HASH, 'Priya Sharma', 3, '+1-555-0103', 'Full-Stack UI/UX Specialist', 'Frontend & JSP engineer specializing in responsive modern SaaS layouts and Chart.js analytics.', 'ACTIVE'),
(5, 'dev_david', 'david.dev@devflow.io', @PWD_HASH, 'David Chen', 3, '+1-555-0106', 'Cloud & Systems Engineer', 'Specialist in distributed microservices, Docker, CI/CD, and asynchronous messaging.', 'ACTIVE'),
(6, 'dev_elena', 'elena.dev@devflow.io', @PWD_HASH, 'Elena Rostova', 3, '+1-555-0107', 'Security & Database Engineer', 'Focused on zero-trust RBAC, BCrypt password hashing, and MySQL query performance tuning.', 'ACTIVE'),
(7, 'tester_mark', 'mark.qa@devflow.io', @PWD_HASH, 'Mark Davis', 4, '+1-555-0104', 'Lead QA Automation Engineer', 'Specialist in automated test suites, regression testing, and security bug triage.', 'ACTIVE'),
(8, 'tester_anita', 'anita.qa@devflow.io', @PWD_HASH, 'Anita Desai', 4, '+1-555-0108', 'Software Quality Analyst', 'Focused on edge-case testing, end-to-end user workflows, and performance stress testing.', 'ACTIVE'),
(9, 'faculty_dr_alan', 'alan.faculty@devflow.io', @PWD_HASH, 'Dr. Alan Grant', 5, '+1-555-0105', 'Professor & Project Mentor', 'Academic mentor overseeing student project milestones, proposal approval, and architecture reviews.', 'ACTIVE'),
(10, 'faculty_prof_vance', 'vance.faculty@devflow.io', @PWD_HASH, 'Prof. Robert Vance', 5, '+1-555-0109', 'Department Head & Capstone Evaluator', 'Senior faculty member evaluating semester capstone deliverables and viva voce assessments.', 'ACTIVE'),
(11, 'demo', 'demo@devflow.io', @PWD_HASH, 'Demo Universal Superuser', 1, '+1-555-0110', 'Evaluation Superuser', 'Universal demo account with full omni-access to explore all platform features.', 'ACTIVE'),
(12, 'pm_marcus', 'marcus.pm@devflow.io', @PWD_HASH, 'Marcus Brody', 2, '+1-555-0111', 'Enterprise Delivery Manager', 'Manages cross-functional enterprise deliverables and client stakeholder communications.', 'ACTIVE');

-- 3. INSERT PROJECTS (6 Varied Enterprise & Academic Projects)
INSERT INTO projects (id, project_key, name, description, status, priority, manager_id, start_date, end_date) VALUES
(1, 'DEV', 'DevFlow Developer Collaboration Portal', 'Centralized developer collaboration workspace supporting Kanban boards, Jitsi video conferencing, GitHub activity sync, and democratized Idea & Change Proposals with faculty reviews.', 'ACTIVE', 'HIGH', 2, '2026-08-01', '2026-12-15'),
(2, 'FIN', 'FinTech AI Fraud Detection Shield', 'High-throughput real-time financial transaction risk assessment engine using streaming analytics, machine learning heuristics, and distributed ledger audit logging.', 'ACTIVE', 'CRITICAL', 2, '2026-09-01', '2027-02-28'),
(3, 'MED', 'MedFlow Telehealth & EHR Platform', 'HIPAA-compliant telemedicine portal connecting doctors and patients with WebRTC video consults, encrypted prescriptions, and vital stats monitoring.', 'ACTIVE', 'HIGH', 12, '2026-07-15', '2026-11-30'),
(4, 'IOT', 'SmartCity IoT Traffic & Mobility Hub', 'Scalable telemetry ingestion hub processing sensor data from 50,000+ smart traffic lights and public transit feeds with predictive traffic optimization.', 'PLANNING', 'MEDIUM', 2, '2026-10-01', '2027-04-15'),
(5, 'EDU', 'EduFlow Campus Academic ERP', 'Comprehensive university management system for student enrollment, attendance tracking, faculty grading portals, and timetable scheduling.', 'COMPLETED', 'HIGH', 12, '2026-01-10', '2026-06-30'),
(6, 'SEC', 'CyberGuard Zero-Trust SIEM Suite', 'Next-generation Security Information & Event Management (SIEM) dashboard offering real-time anomaly detection, threat intelligence feeds, and automated incident containment.', 'ACTIVE', 'CRITICAL', 2, '2026-08-20', '2027-03-31');

-- 4. INSERT PROJECT MEMBERS
INSERT INTO project_members (project_id, user_id, project_role) VALUES
-- DEV Platform
(1, 2, 'LEAD'), (1, 3, 'DEVELOPER'), (1, 4, 'DEVELOPER'), (1, 5, 'DEVELOPER'), (1, 6, 'DEVELOPER'), (1, 7, 'TESTER'), (1, 8, 'TESTER'), (1, 9, 'REVIEWER'), (1, 10, 'OBSERVER'), (1, 11, 'LEAD'),
-- FIN Tech
(2, 2, 'LEAD'), (2, 3, 'DEVELOPER'), (2, 5, 'DEVELOPER'), (2, 6, 'DEVELOPER'), (2, 7, 'TESTER'), (2, 9, 'REVIEWER'), (2, 11, 'LEAD'),
-- MED Flow
(3, 12, 'LEAD'), (3, 4, 'DEVELOPER'), (3, 5, 'DEVELOPER'), (3, 8, 'TESTER'), (3, 10, 'REVIEWER'), (3, 11, 'LEAD'),
-- IOT SmartCity
(4, 2, 'LEAD'), (4, 3, 'DEVELOPER'), (4, 5, 'DEVELOPER'), (4, 7, 'TESTER'), (4, 11, 'LEAD'),
-- EDU Flow
(5, 12, 'LEAD'), (5, 4, 'DEVELOPER'), (5, 6, 'DEVELOPER'), (5, 8, 'TESTER'), (5, 9, 'REVIEWER'), (5, 11, 'LEAD'),
-- SEC CyberGuard
(6, 2, 'LEAD'), (6, 3, 'DEVELOPER'), (6, 6, 'DEVELOPER'), (6, 7, 'TESTER'), (6, 8, 'TESTER'), (6, 9, 'REVIEWER'), (6, 11, 'LEAD');

-- 5. INSERT MILESTONES
INSERT INTO milestones (id, project_id, title, description, due_date, status) VALUES
(1, 1, 'Milestone 1: Architecture & Auth Foundation', 'Design normalized MySQL schema, HikariCP connection pool, MVC controllers, and BCrypt authentication filter.', '2026-09-01', 'REACHED'),
(2, 1, 'Milestone 2: Agile Kanban & Defect Triage', 'Deliver interactive drag-and-drop task lifecycle board, sprint burndown charts, and complete bug management workflow.', '2026-10-01', 'IN_PROGRESS'),
(3, 1, 'Milestone 3: AI Video Meetings & Idea Governance', 'Integrate Jitsi Meet WebRTC, automated AI meeting minutes summarization, and democratic idea proposal voting with faculty approval.', '2026-11-01', 'OPEN'),
(4, 1, 'Milestone 4: Final Capstone Defense & Deployment', 'Final system validation, automated test suite completion, Docker containerization, and faculty viva voce presentation.', '2026-12-10', 'OPEN'),
(5, 2, 'Milestone 1: Stream Ingestion & Rule Engine', 'Build Apache Kafka/JDBC consumer pipeline capable of evaluating 10,000 transactions per second against rule sets.', '2026-10-15', 'IN_PROGRESS'),
(6, 2, 'Milestone 2: ML Model Inference & Live Alerts', 'Integrate Python XGBoost fraud score inference service via gRPC and deliver real-time manager alerts.', '2026-12-01', 'OPEN'),
(7, 3, 'Milestone 1: Video Consults & Encrypted Records', 'Implement WebRTC video consultation rooms and AES-256 encrypted patient health records.', '2026-09-20', 'REACHED'),
(8, 3, 'Milestone 2: Pharmacy Integration & Prescription Dispatch', 'Automate digital signature verification for prescriptions and external pharmacy API dispatch.', '2026-11-15', 'IN_PROGRESS'),
(9, 6, 'Milestone 1: Log Aggregator & Parser Engine', 'Normalize Syslog, Windows Event, and Apache logs into unified JSON schema with indexing.', '2026-10-10', 'IN_PROGRESS'),
(10, 6, 'Milestone 2: Threat Matrix & Anomaly Detection', 'Correlate network anomalies against MITRE ATT&CK framework and trigger automated IP blocks.', '2026-12-15', 'OPEN');

-- 6. INSERT SPRINTS
INSERT INTO sprints (id, project_id, sprint_name, goal, start_date, end_date, status) VALUES
(1, 1, 'Sprint 1 - Foundation & Security', 'Build authentication filter, user session handling, password hashing, and HikariCP connection pool.', '2026-08-01', '2026-08-15', 'COMPLETED'),
(2, 1, 'Sprint 2 - Kanban Board & Task Engine', 'Deliver interactive task boards, AJAX status persistence, sprint backlog planning, and bug tracking.', '2026-08-16', '2026-08-31', 'COMPLETED'),
(3, 1, 'Sprint 3 - Idea Proposal & Faculty Review', 'Build proposal submission workflow, team peer voting tally engine, faculty decision desk, and task auto-conversion.', '2026-09-01', '2026-09-30', 'ACTIVE'),
(4, 1, 'Sprint 4 - AI Meetings & GitHub Sync', 'Integrate Jitsi Meet, AI meeting notes extractor, and GitHub repository commit/PR synchronizer.', '2026-10-01', '2026-10-15', 'PLANNING'),
(5, 2, 'Sprint 1 - Ingestion Pipeline & DB Pool', 'Establish high-performance JDBC batching and transaction schema for millisecond audit logging.', '2026-09-01', '2026-09-20', 'COMPLETED'),
(6, 2, 'Sprint 2 - Fraud Scoring & Rule Validator', 'Implement heuristic scoring engine and live websocket push notifications for flagged transactions.', '2026-09-21', '2026-10-10', 'ACTIVE'),
(7, 3, 'Sprint 1 - EHR Schema & WebRTC Consultation', 'Deliver HIPAA compliant patient records table, doctor appointment scheduling, and video calling.', '2026-08-01', '2026-08-31', 'COMPLETED'),
(8, 6, 'Sprint 1 - Anomaly Ingestion & SIEM Dashboard', 'Build high-speed log parser, IP blacklist sync, and Chart.js attack vector radar maps.', '2026-09-01', '2026-09-30', 'ACTIVE');

-- 7. INSERT TASKS (50+ Diverse Tasks across Projects & Statuses)
INSERT INTO tasks (id, project_id, sprint_id, milestone_id, title, description, task_type, priority, status, created_by, assigned_to, estimated_hours, logged_hours, due_date) VALUES
-- Project 1 (DEV) - Sprint 3 (Active)
(1, 1, 3, 2, 'Implement Kanban Task Drag & Drop UI', 'Build interactive client-side drag-and-drop UI with smooth CSS transitions and connect to `/task/status-update` AJAX endpoint.', 'FEATURE', 'HIGH', 'COMPLETED', 2, 4, 12.00, 12.00, '2026-09-12'),
(2, 1, 3, 3, 'Design Democratic Idea Voting Tally Engine', 'Implement business logic in `IdeaService` to calculate quorum percentage and automatically transition proposals from IN_VOTING to PENDING_FACULTY.', 'FEATURE', 'CRITICAL', 'COMPLETED', 2, 3, 16.00, 16.00, '2026-09-15'),
(3, 1, 3, 3, 'Build Faculty Approval & Rejection Modal', 'Develop secure faculty-only JSP modal for Dr. Alan Grant to record formal approval notes, rejection justifications, or change requests.', 'FEATURE', 'HIGH', 'IN_PROGRESS', 2, 4, 8.00, 5.50, '2026-09-24'),
(4, 1, 3, 3, 'Automate Task Conversion on Proposal Approval', 'When a faculty mentor approves a proposal, automatically instantiate a new backlog Task linked to the proposal with pre-filled estimates.', 'FEATURE', 'HIGH', 'IN_PROGRESS', 2, 3, 10.00, 6.00, '2026-09-25'),
(5, 1, 3, 3, 'Configure Jitsi Meet Web API Video Bridge', 'Generate unique cryptographic room tokens, configure Jitsi iframe parameters, and enable screen sharing during live standups.', 'FEATURE', 'MEDIUM', 'IN_REVIEW', 2, 5, 8.00, 7.50, '2026-09-22'),
(6, 1, 3, 3, 'Implement AI Meeting Minutes Summarization', 'Build `AIService` connecting to Gemini/OpenAI API with offline heuristic NLP fallback to extract executive summaries and action items.', 'FEATURE', 'HIGH', 'IN_REVIEW', 2, 3, 14.00, 13.00, '2026-09-23'),
(7, 1, 3, 2, 'Add Chart.js Metrics to PM Dashboard', 'Embed dynamic canvas charts showing task status distribution, bug severity breakdown, and team workload distribution.', 'FEATURE', 'MEDIUM', 'COMPLETED', 2, 4, 10.00, 10.00, '2026-09-10'),
(8, 1, 3, 2, 'Validate Secure Document Upload & MIME Types', 'Implement servlet multipart request filtering to prevent executable file uploads and enforce 25MB maximum size restriction.', 'FEATURE', 'HIGH', 'COMPLETED', 2, 6, 6.00, 6.00, '2026-09-08'),
(9, 1, 3, 3, 'Implement Real-Time Notification Polling', 'Create `/api/notifications/count` endpoint and lightweight client-side polling with Bootstrap toast notification banners.', 'FEATURE', 'MEDIUM', 'TODO', 2, 4, 8.00, 0.00, '2026-09-28'),
(10, 1, 3, 4, 'Create Dockerfile and Docker Compose Setup', 'Author multi-stage Docker build for Apache Tomcat 9 and MySQL 8 with automated schema and sample data initialization.', 'FEATURE', 'MEDIUM', 'TODO', 2, 5, 6.00, 0.00, '2026-09-30'),
(11, 1, 3, 4, 'Author Comprehensive Unit & Integration Test Suite', 'Write JUnit 5 test cases for UserDAO, TaskDAO, IdeaDAO, and PasswordUtil ensuring 85%+ branch code coverage.', 'TESTING', 'HIGH', 'TODO', 2, 7, 16.00, 2.00, '2026-10-05'),
(12, 1, 3, 2, 'Refactor PreparedStatement SQL in TaskDAOImpl', 'Optimize JDBC query parameter bindings and eliminate duplicate result set mapping code.', 'REFACTOR', 'LOW', 'COMPLETED', 2, 3, 4.00, 4.00, '2026-09-05'),
(13, 1, 3, 2, 'Fix Mobile View Sidebar Toggle Glitch', 'Resolve Bootstrap offcanvas overlay glitch on iOS Safari mobile screens when toggling navigation sidebar.', 'BUGFIX', 'LOW', 'COMPLETED', 2, 4, 3.00, 3.00, '2026-09-09'),
(14, 1, 3, 2, 'Write IEEE 830 Compliant SRS Document', 'Draft formal Software Requirement Specification document covering functional, non-functional, and interface constraints.', 'DOCS', 'HIGH', 'COMPLETED', 2, 2, 12.00, 12.00, '2026-08-28'),

-- Project 2 (FIN Tech AI Fraud Shield)
(15, 2, 5, 5, 'Design JDBC Batch Insertion for Transactions', 'Optimize MySQL batch writes to handle 5,000 transaction records per second with HikariCP connection tuning.', 'FEATURE', 'CRITICAL', 'COMPLETED', 2, 3, 20.00, 20.00, '2026-09-10'),
(16, 2, 6, 5, 'Implement Rule-Based Anomaly Evaluation Engine', 'Develop rule validator checking for high-velocity transfers, geographic IP mismatches, and sudden spike amounts.', 'FEATURE', 'CRITICAL', 'IN_PROGRESS', 2, 5, 18.00, 11.00, '2026-09-27'),
(17, 2, 6, 6, 'Integrate Machine Learning Fraud Scoring Hook', 'Connect Java backend to Python FastAPI model server via HTTP/JSON to retrieve real-time transaction risk probabilities.', 'FEATURE', 'HIGH', 'IN_PROGRESS', 2, 3, 15.00, 7.00, '2026-10-02'),
(18, 2, 6, 6, 'Create Fraud Alert Notification Channel', 'Send urgent email and SMS alerts to account holders when high-confidence fraud score (>0.85) is detected.', 'FEATURE', 'HIGH', 'TODO', 2, 6, 8.00, 0.00, '2026-10-08'),
(19, 2, 6, 5, 'Conduct Load Testing with 50,000 Mock Records', 'Use Apache JMeter to simulate concurrent transaction floods and measure 99th percentile response latency.', 'TESTING', 'MEDIUM', 'TODO', 2, 7, 10.00, 0.00, '2026-10-12'),

-- Project 3 (MED MedFlow Telehealth)
(20, 3, 7, 7, 'Build HIPAA-Compliant Patient Record Schema', 'Design encrypted patient health record tables with AES-256 column encryption for sensitive medical diagnoses.', 'FEATURE', 'CRITICAL', 'COMPLETED', 12, 5, 14.00, 14.00, '2026-08-20'),
(21, 3, 7, 7, 'Implement WebRTC Doctor-Patient Video Room', 'Integrate secure peer-to-peer video calling with end-to-end encryption for virtual medical consults.', 'FEATURE', 'HIGH', 'COMPLETED', 12, 4, 16.00, 16.00, '2026-08-28'),
(22, 3, 7, 8, 'Create Digital Prescription PDF Generator', 'Generate tamper-evident prescription PDFs with digital signatures and QR code verification links.', 'FEATURE', 'HIGH', 'IN_PROGRESS', 12, 4, 12.00, 8.00, '2026-09-26'),
(23, 3, 7, 8, 'Implement Appointment Slot Booking Calendar', 'Build dynamic full-calendar UI for patients to reserve time slots based on doctor working hours.', 'FEATURE', 'MEDIUM', 'IN_REVIEW', 12, 4, 10.00, 9.50, '2026-09-22'),
(24, 3, 7, 8, 'Verify EMR Security Audit Trail Compliance', 'Ensure every doctor and nurse record access is logged in the immutable audit log table with IP and timestamp.', 'TESTING', 'HIGH', 'COMPLETED', 12, 8, 8.00, 8.00, '2026-09-02'),

-- Project 4 (IOT SmartCity Mobility Hub)
(25, 4, NULL, NULL, 'Design MQTT/CoAP Telemetry Ingestion Gateway', 'Architect high-concurrency ingestion endpoint to process telemetry packets from 50,000 smart sensor nodes.', 'FEATURE', 'HIGH', 'TODO', 2, 5, 24.00, 0.00, '2026-10-25'),
(26, 4, NULL, NULL, 'Create Real-Time Leaflet GIS Traffic Map', 'Render live traffic congestion color overlays on interactive OpenStreetMap canvas with dynamic refresh.', 'FEATURE', 'MEDIUM', 'TODO', 2, 4, 16.00, 0.00, '2026-11-05'),
(27, 4, NULL, NULL, 'Simulate City Traffic Grid with Python Generator', 'Write generator script to publish mock speed, vehicle count, and congestion status every 5 seconds.', 'TESTING', 'LOW', 'TODO', 2, 7, 8.00, 0.00, '2026-11-12'),

-- Project 5 (EDU EduFlow ERP - Completed)
(28, 5, NULL, NULL, 'Build Student Semester Registration Workflow', 'Implement online course add/drop enrollment with prerequisite credit validation rules.', 'FEATURE', 'HIGH', 'COMPLETED', 12, 4, 18.00, 18.00, '2026-03-15'),
(29, 5, NULL, NULL, 'Create Faculty Gradebook & Transcript Calculator', 'Automate GPA calculation based on university credit weightings and generate official PDF transcripts.', 'FEATURE', 'HIGH', 'COMPLETED', 12, 6, 14.00, 14.00, '2026-04-10'),
(30, 5, NULL, NULL, 'Deploy Campus RFID Attendance Reader Listener', 'Process real-time student RFID card swipes at classroom doors to mark lecture attendance automatically.', 'FEATURE', 'MEDIUM', 'COMPLETED', 12, 4, 12.00, 12.00, '2026-05-02'),

-- Project 6 (SEC CyberGuard SIEM)
(31, 6, 8, 9, 'Build High-Throughput Syslog Ingestion Listener', 'Create UDP/TCP socket listener receiving RFC 5424 syslog packets and pushing to normalized MySQL staging table.', 'FEATURE', 'CRITICAL', 'COMPLETED', 2, 3, 16.00, 16.00, '2026-09-08'),
(32, 6, 8, 9, 'Implement Regex Parser for Apache & Nginx Logs', 'Extract client IP, user agent, HTTP status codes, and request URIs into structured fields.', 'FEATURE', 'HIGH', 'COMPLETED', 2, 6, 10.00, 10.00, '2026-09-14'),
(33, 6, 8, 10, 'Implement Brute Force SSH Login Detection Rule', 'Trigger security alert if more than 5 failed SSH authentication attempts occur from same IP in 60 seconds.', 'FEATURE', 'CRITICAL', 'IN_PROGRESS', 2, 6, 8.00, 6.00, '2026-09-26'),
(34, 6, 8, 10, 'Develop Threat Incident Response Playbook UI', 'Provide 1-click firewall IP blacklist block and automated account suspension from SIEM dashboard.', 'FEATURE', 'HIGH', 'IN_REVIEW', 2, 3, 12.00, 11.00, '2026-09-23'),
(35, 6, 8, 10, 'Penetration Testing & SQL Injection Vulnerability Scan', 'Execute OWASP ZAP automated security scan against all servlet controllers and verify parameterized queries.', 'TESTING', 'CRITICAL', 'COMPLETED', 2, 7, 12.00, 12.00, '2026-09-18');

-- 8. INSERT TASK COMMENTS & WORK LOGS
INSERT INTO task_comments (task_id, user_id, comment) VALUES
(1, 4, 'Completed the drag-and-drop event handlers in `kanban.js`. State transitions trigger the AJAX endpoint smoothly without screen jitter.'),
(1, 2, 'Tested and verified! Dragging between TODO, IN_PROGRESS, IN_REVIEW, and COMPLETED updates the database instantly.'),
(2, 3, 'Implemented the quorum threshold formula in `IdeaServiceImpl`. Net upvotes >= 2 automatically promotes the idea to faculty review.'),
(3, 4, 'The review modal now displays historical peer voting comments and allows Dr. Alan Grant to submit formal verdict remarks.'),
(4, 3, 'Linked task creation trigger directly to `FacultyReviewServlet`. Task title is prefixed with `[Proposal Action Item]`.'),
(5, 5, 'Jitsi Meet API bridge is tested on `meet.jit.si`. Unique room names prevent cross-session collision.'),
(6, 3, 'Integrated local NLP tokenizer and sentiment scoring as fallback whenever external AI API keys are not supplied.'),
(15, 3, 'HikariCP batch size tuned to 500 records per transaction. Benchmark achieved 5,200 writes/sec on MySQL InnoDB.'),
(21, 4, 'WebRTC media streams are working with peer-to-peer STUN fallback.'),
(31, 3, 'Syslog listener handles 2,000 UDP packets per second using Java NIO non-blocking channel buffers.');

-- 9. INSERT TASK HISTORY
INSERT INTO task_history (task_id, user_id, field_changed, old_value, new_value) VALUES
(1, 4, 'status', 'TODO', 'IN_PROGRESS'),
(1, 4, 'status', 'IN_PROGRESS', 'COMPLETED'),
(2, 3, 'status', 'TODO', 'IN_PROGRESS'),
(2, 3, 'status', 'IN_PROGRESS', 'COMPLETED'),
(3, 4, 'status', 'TODO', 'IN_PROGRESS'),
(5, 5, 'status', 'IN_PROGRESS', 'IN_REVIEW'),
(6, 3, 'status', 'IN_PROGRESS', 'IN_REVIEW'),
(7, 4, 'status', 'IN_PROGRESS', 'COMPLETED'),
(8, 6, 'status', 'IN_PROGRESS', 'COMPLETED');

-- 10. INSERT BUGS & DEFECTS (30+ Realistic Bugs with Stack Traces & Triage)
INSERT INTO bugs (id, project_id, task_id, title, description, steps_to_reproduce, expected_result, actual_result, severity, priority, status, reported_by, assigned_to, resolution_notes) VALUES
(1, 1, 1, 'Task status update throws NullPointerException when assignee is unassigned', 'Moving a task with no assigned user from TODO to IN_PROGRESS resulted in HTTP 500 error in TaskDAOImpl.', '1. Open Kanban board\n2. Create task without selecting an assignee\n3. Drag task to IN_PROGRESS column\n4. Observe 500 error response', 'Task moves to IN_PROGRESS cleanly with assigned_to remaining NULL', 'NullPointerException thrown at TaskDAOImpl.java:142 on getUserId() call', 'HIGH', 'HIGH', 'RESOLVED', 7, 3, 'Added null-safe check `pstmt.setNull(..., java.sql.Types.INTEGER)` in TaskDAOImpl updateStatus method.'),
(2, 1, 8, 'Large document upload fails with connection reset for files over 20MB', 'Attempting to upload a 22MB scanned system architecture diagram times out with connection reset.', '1. Navigate to Project Documents\n2. Select 22MB PDF file\n3. Click Upload Document\n4. Server times out after 30s', 'Document should upload successfully or display friendly validation error', 'Connection reset by peer error', 'MEDIUM', 'MEDIUM', 'RESOLVED', 7, 6, 'Increased Tomcat maxPostSize to 50MB and updated multipart config in DocumentController.'),
(3, 1, 5, 'Jitsi meeting iframe audio muted by default on Safari macOS', 'When launching a video meeting on Safari browser, microphone permissions prompt is blocked.', '1. Open Safari on macOS\n2. Click Join Video Meeting\n3. Observe mic is disabled', 'Microphone and camera permissions prompt appears', 'Iframe sandbox attribute blocked WebRTC permissions', 'MEDIUM', 'MEDIUM', 'RESOLVED', 8, 5, 'Added `allow="camera; microphone; display-capture; autoplay"` attribute to Jitsi iframe container.'),
(4, 1, 2, 'Duplicate vote submission possible by rapid double clicking', 'Rapidly double-clicking the Upvote button on a change proposal created duplicate voting entries.', '1. Open Idea Proposal #1\n2. Rapidly double-click Upvote button\n3. Check database table `idea_votes`', 'Only 1 vote should be recorded per user', 'Database threw DuplicateKeyException and showed uncaught error page', 'LOW', 'LOW', 'RESOLVED', 7, 3, 'Added client-side button debounce disable and wrapped vote insert in `ON DUPLICATE KEY UPDATE`.'),
(5, 1, 7, 'Chart.js canvas overflows container width on mobile portrait mode', 'On mobile screens < 400px width, the sprint burndown chart caused horizontal body scroll.', '1. Open Chrome DevTools\n2. Emulate iPhone 14 (390px)\n3. View Project Dashboard', 'Dashboard should remain within viewport width', 'Canvas element exceeded 100% viewport width', 'LOW', 'LOW', 'RESOLVED', 8, 4, 'Added `responsive: true, maintainAspectRatio: false` and wrapped canvas in `.chart-container`.'),
(6, 1, NULL, 'Session timeout message does not preserve original target URL redirect', 'After 30 minutes of inactivity, user is redirected to login without the destination query param.', '1. Wait for session timeout\n2. Click deep link `/tasks?id=12`\n3. Sign in on login page', 'User should be redirected back to `/tasks?id=12` after login', 'User is sent to `/dashboard` default route', 'MEDIUM', 'LOW', 'RESOLVED', 7, 3, 'Fixed AuthenticationFilter to URL-encode original request URI into `redirect` query parameter.'),
(7, 1, 3, 'Faculty approval timestamp saved in server local timezone instead of UTC', 'When Dr. Alan Grant approved proposal #3, timestamp showed 5.5 hour offset in activity feed.', '1. Log in as faculty_dr_alan\n2. Approve proposal\n3. View activity feed timestamp', 'Timestamp should match standardized UTC format', 'Timestamp saved with local server timezone offset', 'LOW', 'LOW', 'OPEN', 8, 3, NULL),
(8, 2, 15, 'HikariCP connection leak under high concurrent transaction load', 'Simulating 500 concurrent threads caused connection pool exhaustion after 3 minutes.', '1. Run JMeter load test script\n2. Monitor HikariCP active connections\n3. Connections stay at 10/10 and requests hang', 'Connections should be returned to pool immediately after executeBatch', 'Connection pool exhausted (ConnectionTimeoutException)', 'CRITICAL', 'CRITICAL', 'RESOLVED', 7, 3, 'Refactored TransactionDAO to ensure all connections and statements use try-with-resources blocks.'),
(9, 2, 16, 'Negative transaction amount bypasses basic fraud threshold check', 'Submitting a transaction with amount `-5000.00` bypassed the maximum single transfer limit of $10,000.', '1. Send API POST to `/api/transaction` with `amount: -5000`\n2. Observe transaction status', 'Negative amounts must be rejected with HTTP 400 Bad Request', 'Transaction accepted and recorded with negative balance', 'CRITICAL', 'CRITICAL', 'RESOLVED', 7, 6, 'Added strict validation `@Min(0.01)` and checked `amount > 0` in TransactionService.'),
(10, 3, 20, 'Special characters in patient prescription notes cause SQL syntax error', 'Entering single quotes or apostrophes (e.g. "Patient\'s symptoms") caused SQL error.', '1. Open Patient Record\n2. Enter "Patient\'s condition improved"\n3. Click Save', 'Record saves cleanly without errors', 'SQLException: Syntax error near s condition', 'HIGH', 'HIGH', 'RESOLVED', 8, 5, 'Replaced string concatenation with parameterized `PreparedStatement.setString()`.'),
(11, 6, 31, 'Syslog UDP packet buffer overflow on high traffic burst', 'Burst of 10,000 packets in 1 second resulted in dropped packets in Syslog listener.', '1. Replay 10,000 syslog packets via tcpreplay\n2. Count received logs in staging table', 'All packets buffered and processed asynchronously', 'Dropped ~1,200 packets due to small receive buffer', 'HIGH', 'HIGH', 'RESOLVED', 7, 3, 'Increased SO_RCVBUF to 8MB and added concurrent queue background worker threads.');

-- 11. INSERT BUG COMMENTS
INSERT INTO bug_comments (bug_id, user_id, comment) VALUES
(1, 3, 'Fixed in commit `d7a4b1c`. Verified that unassigned tasks transition properly.'),
(1, 7, 'Verified on build 1.0.0-rc2. Null assignee handled cleanly with test case passed.'),
(2, 6, 'Updated `web.xml` multipart configuration maxFileSize to 52428800 (50MB).'),
(8, 3, 'Root cause identified: unclosed PreparedStatement inside batch loop. Fixed with try-with-resources.'),
(8, 7, 'Re-ran JMeter benchmark with 500 threads for 10 minutes. Zero connection leaks detected!');

-- 12. INSERT MEETINGS (15+ Virtual Meetings with Jitsi Video Links)
INSERT INTO meetings (id, project_id, title, description, meeting_date, start_time, end_time, room_code, meeting_url, status, created_by) VALUES
(1, 1, 'Sprint 3 Kickoff & Faculty Review Briefing', 'Review Sprint 3 backlog, triage open bugs, and discuss AI Assistant idea proposal with Dr. Alan Grant.', '2026-09-20', '14:00:00', '15:00:00', 'devflow-sprint3-kickoff', 'https://meet.jit.si/devflow-sprint3-kickoff', 'SCHEDULED', 2),
(2, 1, 'Architecture Milestone 1 Retrospective', 'Retrospective on MVC setup, HikariCP connection pooling benchmarks, and security filters.', '2026-09-14', '11:00:00', '12:00:00', 'devflow-m1-retro', 'https://meet.jit.si/devflow-m1-retro', 'COMPLETED', 2),
(3, 1, 'Daily Standup - Sprint 3 Checkpoint', 'Quick 15-minute sync on Kanban drag-and-drop integration and proposal voting engine.', '2026-09-18', '09:30:00', '09:45:00', 'devflow-daily-standup-s3', 'https://meet.jit.si/devflow-daily-standup-s3', 'COMPLETED', 2),
(4, 1, 'Mid-Semester Capstone Review with Dr. Grant', 'Formal academic presentation of DevFlow architecture, database schema, and live proposal voting.', '2026-09-25', '16:00:00', '17:30:00', 'devflow-capstone-midterm-review', 'https://meet.jit.si/devflow-capstone-midterm-review', 'SCHEDULED', 2),
(5, 2, 'FinTech Fraud Shield Architecture Review', 'Evaluate Kafka ingestion latency, MySQL batch tuning, and XGBoost machine learning model accuracy.', '2026-09-22', '15:00:00', '16:00:00', 'fintech-fraud-arch-sync', 'https://meet.jit.si/fintech-fraud-arch-sync', 'SCHEDULED', 2),
(6, 3, 'MedFlow Telehealth HIPAA Compliance Audit', 'Review end-to-end encryption for WebRTC video consults and database AES-256 field encryption.', '2026-09-12', '10:00:00', '11:30:00', 'medflow-hipaa-audit', 'https://meet.jit.si/medflow-hipaa-audit', 'COMPLETED', 12),
(7, 6, 'CyberGuard SIEM Threat Matrix Workshop', 'Map system event logs to MITRE ATT&CK vectors and configure automatic firewall rule generator.', '2026-09-19', '13:00:00', '14:30:00', 'cyberguard-threat-matrix', 'https://meet.jit.si/cyberguard-threat-matrix', 'COMPLETED', 2);

-- 13. INSERT MEETING PARTICIPANTS
INSERT INTO meeting_participants (meeting_id, user_id, status) VALUES
(1, 2, 'ACCEPTED'), (1, 3, 'ACCEPTED'), (1, 4, 'ACCEPTED'), (1, 5, 'ACCEPTED'), (1, 7, 'ACCEPTED'), (1, 9, 'ACCEPTED'),
(2, 2, 'ATTENDED'), (2, 3, 'ATTENDED'), (2, 4, 'ATTENDED'), (2, 5, 'ATTENDED'), (2, 6, 'ATTENDED'), (2, 7, 'ATTENDED'), (2, 9, 'ATTENDED'),
(3, 2, 'ATTENDED'), (3, 3, 'ATTENDED'), (3, 4, 'ATTENDED'), (3, 7, 'ATTENDED'),
(4, 2, 'ACCEPTED'), (4, 3, 'ACCEPTED'), (4, 4, 'ACCEPTED'), (4, 9, 'ACCEPTED'), (4, 10, 'INVITED'),
(5, 2, 'ACCEPTED'), (5, 3, 'ACCEPTED'), (5, 5, 'ACCEPTED'), (5, 7, 'ACCEPTED'),
(6, 12, 'ATTENDED'), (6, 4, 'ATTENDED'), (6, 5, 'ATTENDED'), (6, 8, 'ATTENDED'), (6, 10, 'ATTENDED'),
(7, 2, 'ATTENDED'), (7, 3, 'ATTENDED'), (7, 6, 'ATTENDED'), (7, 7, 'ATTENDED');

-- 14. INSERT MEETING NOTES & AI SUMMARIES
INSERT INTO meeting_notes (meeting_id, raw_notes, ai_summary, ai_decisions, ai_action_items, ai_responsibilities, updated_by) VALUES
(2, 
'Discussed JDBC pooling using HikariCP. Alex presented the benchmark results showing 40% latency reduction compared to single connections. Priya showed the responsive dark UI template. Dr. Alan Grant emphasized adhering to standard MVC and keeping SQL out of JSP. Mark verified the authentication test cases.',
'The engineering team conducted the Milestone 1 retrospective. HikariCP connection pooling was verified with substantial performance gains (40% latency reduction). The responsive SaaS UI template was formally approved. Dr. Alan Grant reaffirmed strict adherence to MVC separation of concerns and BCrypt password hashing.',
'1. Standardize on HikariCP connection pool across all DAOs.\n2. Enforce MVC strictly: Servlets as controllers, JSP as view only.\n3. Add BCrypt password hashing and session authorization filters.',
'1. Alex: Implement BCrypt in PasswordUtil and update UserDAO.\n2. Priya: Finalize Bootstrap 5 sidebar and Kanban cards.\n3. Mark: Write automated test suite for login and registration.',
'Alex (Security & JDBC), Priya (Frontend UI), Mark (QA Verification)',
2),

(3,
'Alex reported the completion of the democratic voting calculation engine in IdeaService. Priya demonstrated the AJAX Kanban status updates. Mark reported bug #1 regarding null assignee handling.',
'The team held the Sprint 3 daily standup. The democratic proposal voting engine was completed. Kanban drag-and-drop was integrated. QA identified bug #1 (null pointer on unassigned tasks), which Alex committed to resolving immediately.',
'1. Prioritize bug #1 fix before moving to Sprint 4 items.\n2. Connect Chart.js analytics to live database servlet endpoints.',
'1. Alex: Fix null check in TaskDAOImpl.\n2. Priya: Hook up Chart.js to `/reports/data`.',
'Alex (Backend Fixes), Priya (Reporting UI)',
2);

-- 15. INSERT MEETING ACTION ITEMS
INSERT INTO meeting_action_items (meeting_id, description, assigned_to, due_date, is_completed) VALUES
(2, 'Integrate BCrypt library into pom.xml and implement PasswordUtil', 3, '2026-09-16', TRUE),
(2, 'Complete responsive Kanban board layout with Bootstrap 5 cards', 4, '2026-09-18', TRUE),
(2, 'Draft test plan for role-based authorization filter', 7, '2026-09-20', TRUE),
(3, 'Fix NullPointerException in TaskDAOImpl for unassigned tasks', 3, '2026-09-19', TRUE),
(3, 'Connect Chart.js dashboard charts to live `/reports/data` JSON endpoint', 4, '2026-09-22', TRUE);

-- 16. INSERT DOCUMENTS (15+ Categorized Academic & Technical Documents)
INSERT INTO documents (id, project_id, title, description, category, file_name, file_path, file_size, file_type, version, uploaded_by) VALUES
(1, 1, 'DevFlow Project Synopsis v1.0', 'Formal academic project synopsis covering problem statement, scope, objectives, and technology stack.', 'Synopsis', 'DevFlow_Synopsis_v1.0.pdf', 'uploads/docs/DevFlow_Synopsis_v1.0.pdf', 1048576, 'application/pdf', 1, 2),
(2, 1, 'Software Requirement Specification (SRS)', 'Comprehensive IEEE 830 compliant SRS specification detailing functional and non-functional requirements.', 'SRS', 'DevFlow_SRS_IEEE830.pdf', 'uploads/docs/DevFlow_SRS_IEEE830.pdf', 3145728, 'application/pdf', 2, 4),
(3, 1, 'DevFlow System UML & Relational ER Diagrams', 'Complete suite of UML Class diagrams, Sequence diagrams, Use-Case diagrams, and MySQL 26-table ER diagram.', 'UML', 'DevFlow_UML_Architecture.png', 'uploads/docs/DevFlow_UML_Architecture.png', 2097152, 'image/png', 1, 3),
(4, 1, 'Teacher Evaluation & Viva Voce Guide', 'Academic presentation guide containing architectural rationales, design pattern summaries, and 30+ Viva Voce Q&As.', 'Presentation', 'DevFlow_Teacher_Evaluation_Guide.pdf', 'uploads/docs/DevFlow_Teacher_Evaluation_Guide.pdf', 1572864, 'application/pdf', 1, 2),
(5, 1, 'Milestone 1 Retrospective Report', 'Detailed analysis of HikariCP connection pooling latency benchmarks and security audit findings.', 'Reports', 'DevFlow_M1_Retro_Report.pdf', 'uploads/docs/DevFlow_M1_Retro_Report.pdf', 838860, 'application/pdf', 1, 2),
(6, 2, 'FinTech AI Fraud Detection SRS & Architecture', 'High-throughput architecture design for stream processing and real-time transaction scoring.', 'SRS', 'FinTech_Fraud_Architecture.pdf', 'uploads/docs/FinTech_Fraud_Architecture.pdf', 4194304, 'application/pdf', 1, 3),
(7, 3, 'MedFlow HIPAA Compliance & Security Specification', 'Security analysis detailing AES-256 field level encryption and WebRTC media stream isolation.', 'SRS', 'MedFlow_HIPAA_Specification.pdf', 'uploads/docs/MedFlow_HIPAA_Specification.pdf', 2621440, 'application/pdf', 1, 12),
(8, 6, 'CyberGuard SIEM Threat Matrix Mapping', 'Mapping of Windows Event and Linux Syslog signatures to MITRE ATT&CK framework tactics.', 'UML', 'CyberGuard_Threat_Matrix.pdf', 'uploads/docs/CyberGuard_Threat_Matrix.pdf', 3670016, 'application/pdf', 1, 6);

-- 17. INSERT IDEAS / CHANGE PROPOSALS (20+ Proposals Across All Workflow Stages)
INSERT INTO ideas (id, project_id, title, description, problem_statement, proposed_solution, expected_benefit, priority, estimated_effort_days, status, faculty_status, submitted_by, faculty_reviewed_by, faculty_rejection_reason, faculty_review_date) VALUES
(1, 1, 'Automated Code Review AI Assistant', 
'Integrate an automated code review bot that analyzes GitHub pull requests against Clean Code standards and Java conventions.', 
'Students frequently submit code with unclosed JDBC resources or SQL anti-patterns that delay manual code reviews.',
'Provide an AI service hook on pull requests to highlight potential leaks, missing PreparedStatement parameters, or naming violations.',
'Faster code reviews, early bug detection, and higher code quality for academic evaluations.',
'HIGH', 5, 'IN_VOTING', 'PENDING', 3, NULL, NULL, NULL),

(2, 1, 'Docker Containerization & Dev Compose Suite',
'Create Dockerfile and docker-compose.yml for zero-friction local setup of Apache Tomcat 9 and MySQL 8.x.',
'New team members and faculty evaluators spend excessive time configuring local environment variables and database credentials.',
'Provide a single `docker-compose up` command that initializes Tomcat, imports schema.sql and sample_data.sql automatically.',
'Instant onboarding of developers and consistent evaluation environment for faculty.',
'MEDIUM', 3, 'PENDING_FACULTY', 'PENDING', 4, NULL, NULL, NULL),

(3, 1, 'Real-Time Notification Polling Engine',
'Implement lightweight AJAX polling for task assignments and bug alerts without full page reload.',
'Team members miss urgent bug reports and meeting invites unless they refresh the browser page manually.',
'Create `/api/notifications/count` endpoint and JavaScript poller with dynamic toast popups.',
'Immediate team awareness and seamless real-time collaboration experience.',
'HIGH', 4, 'APPROVED', 'APPROVED', 3, 9, NULL, '2026-09-15 14:30:00'),

(4, 1, 'Legacy SOAP 1.2 XML Web Service Bridge',
'Build SOAP 1.2 XML service endpoints alongside modern JSON endpoints.',
'Proposed to support legacy SOAP enterprise clients.',
'Add JAX-WS handlers for task status updates.',
'Overcomplicates project architecture without clear educational value or real-world consumer requirement.',
'LOW', 7, 'REJECTED', 'REJECTED', 3, 9, 'Rejected by Faculty: Project is modern REST/MVC oriented. Adding SOAP introduces unnecessary complexity and deviates from core objectives.', '2026-09-12 10:15:00'),

(5, 1, 'WebSocket Live Collaboration Cursor & Chat',
'Implement Jakarta WebSocket endpoints for real-time multiplayer cursor sharing and team chat on Kanban boards.',
'Team members cannot see when another developer is currently moving or updating a task on the Kanban board.',
'Use HTML5 WebSockets to broadcast task state updates instantly to all connected project members.',
'Zero-latency visual collaboration resembling Figma/Trello live multiplayer features.',
'MEDIUM', 6, 'IN_VOTING', 'PENDING', 4, NULL, NULL, NULL),

(6, 1, 'Dark Mode SaaS Theme Switcher',
'Provide a user preference toggle for high-contrast dark theme optimized for developer eye strain.',
'Developers working late hours prefer a dark IDE-like theme palette over light backgrounds.',
'Implement CSS custom property tokens and store user preference in localStorage and database profile.',
'Enhanced user experience and modern developer-first aesthetics.',
'LOW', 2, 'APPROVED', 'APPROVED', 4, 9, NULL, '2026-09-08 11:00:00'),

(7, 2, 'Distributed Redis Cache for Fraud Rule Lookup',
'Deploy Redis cache cluster in front of MySQL to cache high-frequency merchant risk rules with sub-millisecond lookup.',
'Direct database lookups for each of 10,000 tx/sec create significant read contention on the MySQL primary node.',
'Cache active fraud evaluation rule sets in Redis with 60-second TTL invalidation.',
'Reduces transaction rule evaluation latency from 18ms down to 1.2ms.',
'CRITICAL', 5, 'APPROVED', 'APPROVED', 3, 9, NULL, '2026-09-16 16:45:00'),

(8, 3, 'Automated SMS Appointment Reminders',
'Integrate Twilio SMS gateway to send appointment reminders 2 hours prior to virtual consults.',
'Patients miss 15% of scheduled telehealth video appointments due to lack of mobile reminders.',
'Automate SMS dispatch upon appointment booking and 2-hour reminder trigger.',
'Reduces patient no-show rate and optimizes doctor consultation schedules.',
'MEDIUM', 3, 'PENDING_FACULTY', 'PENDING', 12, NULL, NULL, NULL);

-- 18. INSERT IDEA VOTES
-- Idea 1: IN_VOTING
INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES
(1, 3, 'YES', 'Huge time saver during sprint reviews!'),
(1, 4, 'YES', 'Will help avoid syntax and unclosed JDBC resource bugs.'),
(1, 7, 'NO', 'Concerned about AI API rate limits on free student tiers.');

-- Idea 2: PENDING_FACULTY (Unanimous YES)
INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES
(2, 3, 'YES', 'Essential for consistent deployment on Tomcat 9.'),
(2, 4, 'YES', 'Makes running sample data effortless for our teachers.'),
(2, 7, 'YES', 'Helps QA replicate test environments reliably.');

-- Idea 3: APPROVED
INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES
(3, 3, 'YES', 'Crucial for dynamic collaboration.'),
(3, 4, 'YES', 'Clean JS polling works great with our servlet backend.');

-- Idea 5: IN_VOTING
INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES
(5, 3, 'YES', 'WebSocket support would be amazing for live demo!'),
(5, 4, 'YES', 'Great for showing real-time responsiveness to teachers.'),
(5, 5, 'YES', 'Java Servlet 4.0 has great WebSocket endpoint support.');

-- Idea 6: APPROVED
INSERT INTO idea_votes (idea_id, user_id, vote, comments) VALUES
(6, 3, 'YES', 'Love dark mode for night coding sessions.'),
(6, 4, 'YES', 'CSS variables make dark theme implementation very clean.');

-- 19. INSERT IDEA COMMENTS
INSERT INTO idea_comments (idea_id, user_id, comment) VALUES
(1, 2, 'Let us make sure the AI integration is configurable with an offline heuristic fallback so tests never fail without API keys.'),
(1, 3, 'Agreed Sarah, `AIServiceImpl` will support both live OpenAI/Gemini endpoints and smart local heuristics.'),
(2, 9, 'Looking forward to reviewing the Docker configuration in our next mentor session.'),
(3, 9, 'Approved by Dr. Alan Grant. Ensure polling interval does not overwhelm MySQL pool (keep at 10-15s interval).'),
(4, 9, 'Rejected. Advanced Java project should focus on modern RESTful patterns rather than outdated SOAP XML interfaces.');

-- 20. INSERT GITHUB REPOSITORIES & ACTIVITIES
INSERT INTO github_repositories (project_id, repo_owner, repo_name, repo_url, default_branch, is_active, last_synced_at) VALUES
(1, 'devflow-org', 'devflow-collaboration-platform', 'https://github.com/devflow-org/devflow-collaboration-platform', 'main', TRUE, '2026-09-19 14:00:00'),
(2, 'devflow-org', 'fintech-fraud-detection-engine', 'https://github.com/devflow-org/fintech-fraud-detection-engine', 'main', TRUE, '2026-09-19 13:30:00'),
(3, 'devflow-org', 'medflow-telehealth-ehr', 'https://github.com/devflow-org/medflow-telehealth-ehr', 'main', TRUE, '2026-09-18 17:00:00'),
(6, 'devflow-org', 'cyberguard-zero-trust-siem', 'https://github.com/devflow-org/cyberguard-zero-trust-siem', 'main', TRUE, '2026-09-19 11:00:00');

INSERT INTO github_activity (project_id, event_type, author_name, author_avatar, message, event_url, event_timestamp) VALUES
(1, 'PUSH', 'Alex Mercer', 'https://avatars.githubusercontent.com/u/1001', 'feat(auth): implement BCrypt password hashing and AuthenticationFilter', 'https://github.com/devflow-org/devflow-collaboration-platform/commit/a1b2c3d', '2026-09-18 16:45:00'),
(1, 'PUSH', 'Priya Sharma', 'https://avatars.githubusercontent.com/u/1002', 'feat(ui): complete Kanban drag-and-drop and Chart.js integration', 'https://github.com/devflow-org/devflow-collaboration-platform/commit/e4f5a6b', '2026-09-19 09:30:00'),
(1, 'PULL_REQUEST', 'Mark Davis', 'https://avatars.githubusercontent.com/u/1003', 'test: add JUnit 5 test suite for UserDAO and ProjectService (#14)', 'https://github.com/devflow-org/devflow-collaboration-platform/pull/14', '2026-09-19 11:15:00'),
(1, 'PUSH', 'Alex Mercer', 'https://avatars.githubusercontent.com/u/1001', 'fix(dao): prevent NullPointerException on unassigned task status updates', 'https://github.com/devflow-org/devflow-collaboration-platform/commit/c7d8e9f', '2026-09-19 12:40:00'),
(2, 'PUSH', 'David Chen', 'https://avatars.githubusercontent.com/u/1004', 'perf(batch): optimize HikariCP JDBC batch writes to 5,200 tx/sec', 'https://github.com/devflow-org/fintech-fraud-detection-engine/commit/b2c3d4e', '2026-09-19 13:10:00'),
(6, 'PUSH', 'Elena Rostova', 'https://avatars.githubusercontent.com/u/1005', 'feat(siem): add brute-force SSH login detection correlation rule', 'https://github.com/devflow-org/cyberguard-zero-trust-siem/commit/f5a6b7c', '2026-09-19 10:20:00');

-- 21. INSERT NOTIFICATIONS (40+ Notifications for Real-Time UI)
INSERT INTO notifications (user_id, project_id, title, message, link_url, notification_type, is_read) VALUES
(3, 1, 'Task Assigned', 'You have been assigned to task: Design Democratic Idea Voting Tally Engine', '/tasks?id=2', 'TASK', FALSE),
(3, 1, 'Proposal Approved by Faculty', 'Dr. Alan Grant approved your proposal: Real-Time Notification Polling Engine', '/ideas?id=3', 'IDEA', TRUE),
(4, 1, 'New Bug Reported', 'Mark reported a bug: Large document upload fails with connection reset', '/bugs?id=2', 'BUG', FALSE),
(9, 1, 'Proposal Awaiting Faculty Review', 'Proposal: Docker Containerization & Dev Compose passed team voting and is ready for your evaluation.', '/ideas?id=2', 'FACULTY', FALSE),
(2, 1, 'Sprint 3 Checkpoint Scheduled', 'Sarah Connor scheduled meeting: Mid-Semester Capstone Review with Dr. Grant for Sept 25, 4:00 PM.', '/meetings?id=4', 'MEETING', FALSE),
(7, 1, 'Bug Fix Ready for Verification', 'Alex marked bug #1 (NullPointerException on unassigned task) as RESOLVED.', '/bugs?id=1', 'BUG', FALSE),
(11, 1, 'Welcome to DevFlow Demo', 'Universal Demo Superuser initialized with full access across all 6 projects and administrative features.', '/dashboard', 'GENERAL', FALSE);

-- 22. INSERT AUDIT LOGS
INSERT INTO audit_logs (user_id, username, action, entity_type, entity_id, details, ip_address) VALUES
(1, 'admin', 'SYSTEM_INIT', 'SYSTEM', 1, 'Enterprise database schema and comprehensive 26-table dataset initialized successfully.', '127.0.0.1'),
(2, 'pm_sarah', 'CREATE_PROJECT', 'PROJECT', 1, 'Created project: DevFlow Collaboration Platform (DEV)', '127.0.0.1'),
(3, 'dev_alex', 'SUBMIT_IDEA', 'IDEA', 1, 'Submitted change proposal: Automated Code Review AI Assistant', '127.0.0.1'),
(9, 'faculty_dr_alan', 'FACULTY_APPROVAL', 'IDEA', 3, 'Approved student proposal: Real-Time Notification Polling Engine with commendation.', '127.0.0.1'),
(9, 'faculty_dr_alan', 'FACULTY_REJECTION', 'IDEA', 4, 'Rejected student proposal: Legacy SOAP 1.2 XML Web Service Bridge (deviates from REST objectives).', '127.0.0.1'),
(2, 'pm_sarah', 'CREATE_SPRINT', 'SPRINT', 3, 'Created Sprint 3: Idea Proposal & Faculty Review (Sept 1 - Sept 30, 2026)', '127.0.0.1'),
(11, 'demo', 'DEMO_LOGIN', 'AUTH', 11, 'Universal Demo Superuser authenticated via 1-Click Demo Portal.', '127.0.0.1');

-- 23. INSERT SYSTEM SETTINGS
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('app.name', 'DevFlow', 'Application Title'),
('app.version', '1.0.0-PROD', 'Platform Build Version'),
('app.environment', 'production', 'Runtime Environment (development / production)'),
('ai.provider', 'DEFAULT', 'AI Summarization Provider (DEFAULT, OPENAI, GEMINI)'),
('ai.api_key', '', 'API Key for AI Meeting Summaries (optional)'),
('github.api_token', '', 'Personal Access Token for GitHub REST API (optional)'),
('jitsi.domain', 'meet.jit.si', 'Jitsi Meet Server Domain'),
('file.upload_max_mb', '25', 'Maximum allowed file upload size in Megabytes'),
('demo.quick_login_enabled', 'true', 'Enable 1-Click Teacher Evaluation & Demo Sign In Buttons');
