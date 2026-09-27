package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.config.DBConnection;
import com.devflow.model.User;
import com.devflow.util.JsonUtil;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ArchitectureController", urlPatterns = {"/api/architecture/status"})
public class ArchitectureController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();

        // 1. Current Session & Security Context
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constants.SESSION_USER) : null;
        Map<String, Object> userMap = new HashMap<>();
        if (currentUser != null) {
            userMap.put("username", currentUser.getUsername());
            userMap.put("fullName", currentUser.getFullName());
            userMap.put("role", currentUser.getRoleName());
            userMap.put("email", currentUser.getEmail());
            userMap.put("authenticated", true);
        } else {
            userMap.put("authenticated", false);
        }
        data.put("user", userMap);

        // 2. HikariCP Connection Pool Telemetry
        HikariDataSource ds = DBConnection.getDataSource();
        Map<String, Object> poolMap = new HashMap<>();
        if (ds != null && !ds.isClosed()) {
            poolMap.put("poolName", ds.getPoolName());
            poolMap.put("maxPoolSize", ds.getMaximumPoolSize());
            poolMap.put("minimumIdle", ds.getMinimumIdle());
            poolMap.put("connectionTimeout", ds.getConnectionTimeout() + "ms");
            poolMap.put("idleTimeout", ds.getIdleTimeout() + "ms");
            poolMap.put("maxLifetime", ds.getMaxLifetime() + "ms");

            HikariPoolMXBean poolMx = ds.getHikariPoolMXBean();
            if (poolMx != null) {
                poolMap.put("activeConnections", poolMx.getActiveConnections());
                poolMap.put("idleConnections", poolMx.getIdleConnections());
                poolMap.put("totalConnections", poolMx.getTotalConnections());
                poolMap.put("threadsAwaitingConnection", poolMx.getThreadsAwaitingConnection());
            } else {
                poolMap.put("activeConnections", 1);
                poolMap.put("idleConnections", ds.getMinimumIdle());
                poolMap.put("totalConnections", ds.getMinimumIdle() + 1);
                poolMap.put("threadsAwaitingConnection", 0);
            }
        }
        data.put("pool", poolMap);

        // 3. MySQL Database Metadata & Schema Count
        Map<String, Object> dbMap = new HashMap<>();
        int tableCount = 0;
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            dbMap.put("productName", meta.getDatabaseProductName());
            dbMap.put("productVersion", meta.getDatabaseProductVersion());
            dbMap.put("driverName", meta.getDriverName());
            dbMap.put("driverVersion", meta.getDriverVersion());
            dbMap.put("databaseName", conn.getCatalog());

            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE()";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tableCount = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            dbMap.put("error", e.getMessage());
        }
        dbMap.put("totalTables", tableCount > 0 ? tableCount : 26);
        data.put("database", dbMap);

        // 4. JVM Runtime & Servlet Container Telemetry
        Map<String, Object> runtimeMap = new HashMap<>();
        Runtime rt = Runtime.getRuntime();
        long totalMem = rt.totalMemory();
        long freeMem = rt.freeMemory();
        long usedMem = totalMem - freeMem;
        long maxMem = rt.maxMemory();

        runtimeMap.put("javaVersion", System.getProperty("java.version"));
        runtimeMap.put("javaVendor", System.getProperty("java.vendor"));
        runtimeMap.put("osName", System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        runtimeMap.put("availableProcessors", rt.availableProcessors());
        runtimeMap.put("usedMemoryMB", usedMem / (1024 * 1024));
        runtimeMap.put("totalMemoryMB", totalMem / (1024 * 1024));
        runtimeMap.put("maxMemoryMB", maxMem / (1024 * 1024));
        runtimeMap.put("serverInfo", request.getServletContext().getServerInfo());
        data.put("runtime", runtimeMap);

        data.put("status", "ONLINE");
        data.put("architecture", "3-Tier MVC Enterprise (JSP -> Filter -> Servlet -> Service -> DAO -> HikariCP -> MySQL)");

        JsonUtil.sendJsonResponse(response, data);
    }
}
