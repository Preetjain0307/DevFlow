package com.devflow.service;

import com.devflow.model.Role;
import com.devflow.model.User;
import java.util.List;

public interface UserService {
    User authenticate(String usernameOrEmail, String password, String ipAddress);
    boolean register(User user, String ipAddress);
    boolean updateProfile(User user);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    boolean updateStatus(int userId, String status, int adminUserId, String ipAddress);
    boolean updateRole(int userId, int roleId, int adminUserId, String ipAddress);
    User getUserById(int id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    List<User> getProjectUsers(int projectId);
    List<Role> getAllRoles();
}
