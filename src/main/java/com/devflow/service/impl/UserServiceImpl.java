package com.devflow.service.impl;

import com.devflow.config.Constants;
import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.UserDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.dao.impl.UserDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.AuditLog;
import com.devflow.model.Role;
import com.devflow.model.User;
import com.devflow.service.UserService;
import com.devflow.util.PasswordUtil;
import com.devflow.util.ValidationUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;
    private final AuditLogDAO auditLogDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO, AuditLogDAO auditLogDAO) {
        this.userDAO = userDAO;
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public User authenticate(String usernameOrEmail, String password, String ipAddress) {
        if (!ValidationUtil.isNotEmpty(usernameOrEmail) || !ValidationUtil.isNotEmpty(password)) {
            throw new ValidationException("Username/Email and Password are required.");
        }

        User user = null;
        if (ValidationUtil.isValidEmail(usernameOrEmail)) {
            user = userDAO.findByEmail(usernameOrEmail.trim());
        } else {
            user = userDAO.findByUsername(usernameOrEmail.trim());
        }

        if (user == null) {
            logger.warn("Authentication failed: user {} not found", usernameOrEmail);
            return null;
        }

        if (!user.isActive()) {
            throw new ValidationException("Your account is currently " + user.getStatus() + ". Please contact administrator.");
        }

        if (PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            auditLogDAO.log(new AuditLog(user.getId(), user.getUsername(), Constants.AUDIT_LOGIN, "USER", user.getId(), "User logged in successfully", ipAddress));
            return user;
        }

        logger.warn("Authentication failed: invalid password for user {}", usernameOrEmail);
        return null;
    }

    @Override
    public boolean register(User user, String ipAddress) {
        if (user == null) {
            throw new ValidationException("User information is required.");
        }
        if (!ValidationUtil.isValidUsername(user.getUsername())) {
            throw new ValidationException("Username must be alphanumeric (3-30 characters).");
        }
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            throw new ValidationException("A valid email address is required.");
        }
        if (!ValidationUtil.isNotEmpty(user.getFullName())) {
            throw new ValidationException("Full name is required.");
        }
        if (!ValidationUtil.isMinLength(user.getPasswordHash(), 6)) {
            throw new ValidationException("Password must be at least 6 characters long.");
        }
        if (userDAO.existsByUsername(user.getUsername())) {
            throw new ValidationException("Username is already taken.");
        }
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email address is already registered.");
        }

        // Hash password
        user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
        if (user.getRoleId() <= 0) {
            user.setRoleId(Constants.ROLE_ID_DEVELOPER); // Default role: DEVELOPER
        }
        user.setStatus("ACTIVE");

        boolean created = userDAO.create(user);
        if (created) {
            auditLogDAO.log(new AuditLog(user.getId(), user.getUsername(), Constants.AUDIT_REGISTER, "USER", user.getId(), "New user account registered", ipAddress));
        }
        return created;
    }

    @Override
    public boolean updateProfile(User user) {
        if (user == null || user.getId() <= 0) {
            throw new ValidationException("Invalid user ID.");
        }
        if (!ValidationUtil.isNotEmpty(user.getFullName())) {
            throw new ValidationException("Full name cannot be empty.");
        }
        return userDAO.update(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (!ValidationUtil.isNotEmpty(oldPassword) || !ValidationUtil.isMinLength(newPassword, 6)) {
            throw new ValidationException("New password must be at least 6 characters.");
        }
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new ValidationException("User not found.");
        }
        if (!PasswordUtil.checkPassword(oldPassword, user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect.");
        }
        String newHash = PasswordUtil.hashPassword(newPassword);
        return userDAO.updatePassword(userId, newHash);
    }

    @Override
    public boolean updateStatus(int userId, String status, int adminUserId, String ipAddress) {
        User target = userDAO.findById(userId);
        if (target == null) return false;
        boolean updated = userDAO.updateStatus(userId, status);
        if (updated) {
            User admin = userDAO.findById(adminUserId);
            String adminName = admin != null ? admin.getUsername() : "ADMIN";
            auditLogDAO.log(new AuditLog(adminUserId, adminName, Constants.AUDIT_USER_STATUS_CHANGE, "USER", userId, "Changed status of " + target.getUsername() + " to " + status, ipAddress));
        }
        return updated;
    }

    @Override
    public boolean updateRole(int userId, int roleId, int adminUserId, String ipAddress) {
        User target = userDAO.findById(userId);
        if (target == null) return false;
        boolean updated = userDAO.updateRole(userId, roleId);
        if (updated) {
            User admin = userDAO.findById(adminUserId);
            String adminName = admin != null ? admin.getUsername() : "ADMIN";
            auditLogDAO.log(new AuditLog(adminUserId, adminName, "USER_ROLE_CHANGE", "USER", userId, "Changed role of " + target.getUsername() + " to role ID " + roleId, ipAddress));
        }
        return updated;
    }

    @Override
    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public List<User> getProjectUsers(int projectId) {
        return userDAO.findByProjectId(projectId);
    }

    @Override
    public List<Role> getAllRoles() {
        return userDAO.findAllRoles();
    }
}
