package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.UserDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Role;
import com.devflow.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT u.id, u.username, u.email, u.password_hash, u.full_name, u.role_id, " +
            "r.name AS role_name, u.phone, u.designation, u.bio, u.avatar_url, u.status, " +
            "u.created_at, u.updated_at " +
            "FROM users u " +
            "JOIN roles r ON u.role_id = r.id ";

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setPhone(rs.getString("phone"));
        user.setDesignation(rs.getString("designation"));
        user.setBio(rs.getString("bio"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }

    @Override
    public User findById(int id) {
        String sql = BASE_SELECT + "WHERE u.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find user by id", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        String sql = BASE_SELECT + "WHERE u.username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username {}: {}", username, e.getMessage());
            throw new DatabaseException("Failed to find user by username", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = BASE_SELECT + "WHERE u.email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email {}: {}", email, e.getMessage());
            throw new DatabaseException("Failed to find user by email", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql = BASE_SELECT + "ORDER BY u.id ASC";
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all users: {}", e.getMessage());
            throw new DatabaseException("Failed to retrieve user list", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<User> findByProjectId(int projectId) {
        String sql = BASE_SELECT + 
                "JOIN project_members pm ON u.id = pm.user_id " +
                "WHERE pm.project_id = ? ORDER BY u.full_name ASC";
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding users by project id {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve project users", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<User> findByRoleId(int roleId) {
        String sql = BASE_SELECT + "WHERE u.role_id = ? ORDER BY u.full_name ASC";
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding users by role id {}: {}", roleId, e.getMessage());
            throw new DatabaseException("Failed to retrieve users by role", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, role_id, phone, designation, bio, avatar_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setInt(5, user.getRoleId());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getDesignation());
            ps.setString(8, user.getBio());
            ps.setString(9, user.getAvatarUrl() != null ? user.getAvatarUrl() : "default-avatar.png");
            ps.setString(10, user.getStatus() != null ? user.getStatus() : "ACTIVE");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating user {}: {}", user.getUsername(), e.getMessage());
            throw new DatabaseException("Failed to create user", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, designation = ?, bio = ?, avatar_url = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getDesignation());
            ps.setString(4, user.getBio());
            ps.setString(5, user.getAvatarUrl());
            ps.setInt(6, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating user id {}: {}", user.getId(), e.getMessage());
            throw new DatabaseException("Failed to update user profile", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updatePassword(int userId, String passwordHash) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, passwordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating password for user id {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to update password", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating status for user id {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to update user status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateRole(int userId, int roleId) {
        String sql = "UPDATE users SET role_id = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating role for user id {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to update user role", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, rs);
        }
    }

    @Override
    public List<Role> findAllRoles() {
        String sql = "SELECT id, name, description, created_at FROM roles ORDER BY id ASC";
        List<Role> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Role r = new Role();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setDescription(rs.getString("description"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(r);
            }
        } catch (SQLException e) {
            logger.error("Error finding roles: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public Role findRoleById(int id) {
        String sql = "SELECT id, name, description, created_at FROM roles WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Role r = new Role();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setDescription(rs.getString("description"));
                r.setCreatedAt(rs.getTimestamp("created_at"));
                return r;
            }
        } catch (SQLException e) {
            logger.error("Error finding role by id {}: {}", id, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }
}
