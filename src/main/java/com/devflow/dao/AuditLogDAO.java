package com.devflow.dao;

import com.devflow.model.AuditLog;
import java.util.List;

public interface AuditLogDAO {
    boolean log(AuditLog auditLog);
    List<AuditLog> findAll(int limit);
    List<AuditLog> findByUserId(int userId, int limit);
    List<AuditLog> search(String action, String username, int limit);
}
