package com.devflow.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static HikariDataSource dataSource;

    static {
        try {
            Class.forName(AppConfig.get("db.driver", "com.mysql.cj.jdbc.Driver"));
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(AppConfig.get("db.driver", "com.mysql.cj.jdbc.Driver"));
            config.setJdbcUrl(AppConfig.get("db.url", "jdbc:mysql://localhost:3306/devflow_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"));
            config.setUsername(AppConfig.get("db.username", "root"));
            config.setPassword(AppConfig.get("db.password", "root"));
            config.setMaximumPoolSize(AppConfig.getInt("db.pool.maximumPoolSize", 10));
            config.setMinimumIdle(AppConfig.getInt("db.pool.minimumIdle", 2));
            config.setIdleTimeout(AppConfig.getInt("db.pool.idleTimeout", 30000));
            config.setConnectionTimeout(AppConfig.getInt("db.pool.connectionTimeout", 20000));
            config.setMaxLifetime(AppConfig.getInt("db.pool.maxLifetime", 1800000));
            config.setPoolName("DevFlowHikariPool");

            // Recommended MySQL optimization flags
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP connection pool initialized successfully.");
        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP connection pool: {}", e.getMessage(), e);
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("Database connection pool is not initialized or closed.");
        }
        return dataSource.getConnection();
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error closing connection: {}", e.getMessage());
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Error closing statement: {}", e.getMessage());
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Error closing result set: {}", e.getMessage());
            }
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.warn("Error rolling back transaction: {}", e.getMessage());
            }
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP connection pool closed.");
        }
    }
}
