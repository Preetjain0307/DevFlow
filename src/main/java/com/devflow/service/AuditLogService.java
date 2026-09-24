package com.devflow.service;

import com.devflow.model.AuditLog;
import java.util.List;

public interface AuditLogService {
    List<AuditLog> getRecentLogs(int limit);
    List<AuditLog> getUserLogs(int userId, int limit);
    List<AuditLog> searchLogs(String action, String username, int limit);
}
