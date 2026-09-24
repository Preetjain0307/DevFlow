package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.AuditLogDAO;
import com.devflow.model.AuditLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLogDAOImpl implements AuditLogDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogDAOImpl.class);

    private AuditLog mapRow(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id");
        log.setUserId(rs.wasNull() ? null : uid);
        log.setUsername(rs.getString("username"));
        log.setAction(rs.getString("action"));
        log.setEntityType(rs.getString("entity_type"));
        int eid = rs.getInt("entity_id");
        log.setEntityId(rs.wasNull() ? null : eid);
        log.setDetails(rs.getString("details"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }

    @Override
    public boolean log(AuditLog auditLog) {
        String sql = "INSERT INTO audit_logs (user_id, username, action, entity_type, entity_id, details, ip_address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (auditLog.getUserId() != null) ps.setInt(1, auditLog.getUserId()); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, auditLog.getUsername() != null ? auditLog.getUsername() : "ANONYMOUS");
            ps.setString(3, auditLog.getAction());
            ps.setString(4, auditLog.getEntityType());
            if (auditLog.getEntityId() != null) ps.setInt(5, auditLog.getEntityId()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, auditLog.getDetails());
            ps.setString(7, auditLog.getIpAddress() != null ? auditLog.getIpAddress() : "127.0.0.1");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error writing audit log: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<AuditLog> findAll(int limit) {
        String sql = "SELECT id, user_id, username, action, entity_type, entity_id, details, ip_address, created_at " +
                     "FROM audit_logs ORDER BY created_at DESC LIMIT ?";
        List<AuditLog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving audit logs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<AuditLog> findByUserId(int userId, int limit) {
        String sql = "SELECT id, user_id, username, action, entity_type, entity_id, details, ip_address, created_at " +
                     "FROM audit_logs WHERE user_id = ? ORDER BY created_at DESC LIMIT ?";
        List<AuditLog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding user audit logs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<AuditLog> search(String action, String username, int limit) {
        StringBuilder sb = new StringBuilder(
                "SELECT id, user_id, username, action, entity_type, entity_id, details, ip_address, created_at FROM audit_logs WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (action != null && !action.trim().isEmpty() && !"ALL".equalsIgnoreCase(action)) {
            sb.append("AND action = ? ");
            params.add(action.trim());
        }
        if (username != null && !username.trim().isEmpty()) {
            sb.append("AND username LIKE ? ");
            params.add("%" + username.trim() + "%");
        }
        sb.append("ORDER BY created_at DESC LIMIT ?");
        params.add(limit);

        List<AuditLog> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sb.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error searching audit logs: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }
}
