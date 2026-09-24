package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.SystemSettingDAO;
import com.devflow.model.SystemSetting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemSettingDAOImpl implements SystemSettingDAO {
    private static final Logger logger = LoggerFactory.getLogger(SystemSettingDAOImpl.class);

    @Override
    public String get(String key, String defaultValue) {
        String sql = "SELECT setting_value FROM system_settings WHERE setting_key = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("setting_value");
            }
        } catch (SQLException e) {
            logger.error("Error fetching system setting {}: {}", key, e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return defaultValue;
    }

    @Override
    public boolean set(String key, String value, String description) {
        String sql = "INSERT INTO system_settings (setting_key, setting_value, description) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value), description = COALESCE(VALUES(description), description)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ps.setString(2, value);
            ps.setString(3, description);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error setting system setting {}: {}", key, e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<SystemSetting> findAll() {
        String sql = "SELECT id, setting_key, setting_value, description, updated_at FROM system_settings ORDER BY setting_key ASC";
        List<SystemSetting> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SystemSetting ss = new SystemSetting();
                ss.setId(rs.getInt("id"));
                ss.setSettingKey(rs.getString("setting_key"));
                ss.setSettingValue(rs.getString("setting_value"));
                ss.setDescription(rs.getString("description"));
                ss.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(ss);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving system settings: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public Map<String, String> getAllAsMap() {
        Map<String, String> map = new HashMap<>();
        List<SystemSetting> list = findAll();
        for (SystemSetting ss : list) {
            map.put(ss.getSettingKey(), ss.getSettingValue());
        }
        return map;
    }
}
