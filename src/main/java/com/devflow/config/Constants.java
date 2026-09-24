package com.devflow.config;

public final class Constants {
    private Constants() {}

    // Session Attribute Keys
    public static final String SESSION_USER = "currentUser";
    public static final String SESSION_USER_ROLE = "currentUserRole";
    public static final String SESSION_USER_ID = "currentUserId";
    public static final String FLASH_SUCCESS = "flashSuccess";
    public static final String FLASH_ERROR = "flashError";

    // System Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_PROJECT_MANAGER = "PROJECT_MANAGER";
    public static final String ROLE_DEVELOPER = "DEVELOPER";
    public static final String ROLE_TESTER = "TESTER";
    public static final String ROLE_FACULTY = "FACULTY";

    // Role IDs
    public static final int ROLE_ID_ADMIN = 1;
    public static final int ROLE_ID_PROJECT_MANAGER = 2;
    public static final int ROLE_ID_DEVELOPER = 3;
    public static final int ROLE_ID_TESTER = 4;
    public static final int ROLE_ID_FACULTY = 5;

    // Task Statuses
    public static final String TASK_STATUS_TODO = "TODO";
    public static final String TASK_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String TASK_STATUS_IN_REVIEW = "IN_REVIEW";
    public static final String TASK_STATUS_COMPLETED = "COMPLETED";

    // Bug Statuses
    public static final String BUG_STATUS_OPEN = "OPEN";
    public static final String BUG_STATUS_ASSIGNED = "ASSIGNED";
    public static final String BUG_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String BUG_STATUS_RESOLVED = "RESOLVED";
    public static final String BUG_STATUS_REOPENED = "REOPENED";
    public static final String BUG_STATUS_CLOSED = "CLOSED";

    // Idea Statuses
    public static final String IDEA_STATUS_SUBMITTED = "SUBMITTED";
    public static final String IDEA_STATUS_IN_VOTING = "IN_VOTING";
    public static final String IDEA_STATUS_VOTING_PASSED = "VOTING_PASSED";
    public static final String IDEA_STATUS_VOTING_FAILED = "VOTING_FAILED";
    public static final String IDEA_STATUS_PENDING_FACULTY = "PENDING_FACULTY";
    public static final String IDEA_STATUS_APPROVED = "APPROVED";
    public static final String IDEA_STATUS_REJECTED = "REJECTED";
    public static final String IDEA_STATUS_CHANGES_REQUESTED = "CHANGES_REQUESTED";

    // Audit Log Actions
    public static final String AUDIT_LOGIN = "LOGIN";
    public static final String AUDIT_LOGOUT = "LOGOUT";
    public static final String AUDIT_REGISTER = "REGISTER";
    public static final String AUDIT_CREATE_PROJECT = "CREATE_PROJECT";
    public static final String AUDIT_UPDATE_PROJECT = "UPDATE_PROJECT";
    public static final String AUDIT_CREATE_TASK = "CREATE_TASK";
    public static final String AUDIT_UPDATE_TASK = "UPDATE_TASK";
    public static final String AUDIT_REPORT_BUG = "REPORT_BUG";
    public static final String AUDIT_UPDATE_BUG = "UPDATE_BUG";
    public static final String AUDIT_SUBMIT_IDEA = "SUBMIT_IDEA";
    public static final String AUDIT_VOTE_IDEA = "VOTE_IDEA";
    public static final String AUDIT_FACULTY_DECISION = "FACULTY_DECISION";
    public static final String AUDIT_SCHEDULE_MEETING = "SCHEDULE_MEETING";
    public static final String AUDIT_UPLOAD_DOCUMENT = "UPLOAD_DOCUMENT";
    public static final String AUDIT_DELETE_DOCUMENT = "DELETE_DOCUMENT";
    public static final String AUDIT_USER_STATUS_CHANGE = "USER_STATUS_CHANGE";
}
