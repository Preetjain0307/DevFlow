package com.devflow.dao;

import com.devflow.model.Role;
import com.devflow.model.User;
import java.util.List;

public interface UserDAO {
    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findByProjectId(int projectId);
    List<User> findByRoleId(int roleId);
    boolean create(User user);
    boolean update(User user);
    boolean updatePassword(int userId, String passwordHash);
    boolean updateStatus(int userId, String status);
    boolean updateRole(int userId, int roleId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Role> findAllRoles();
    Role findRoleById(int id);
}
