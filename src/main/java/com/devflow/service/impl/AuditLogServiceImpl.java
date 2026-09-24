package com.devflow.service.impl;

import com.devflow.dao.AuditLogDAO;
import com.devflow.dao.impl.AuditLogDAOImpl;
import com.devflow.model.AuditLog;
import com.devflow.service.AuditLogService;
import java.util.List;

public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogDAO auditLogDAO;

    public AuditLogServiceImpl() {
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public AuditLogServiceImpl(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public List<AuditLog> getRecentLogs(int limit) {
        return auditLogDAO.findAll(limit);
    }

    @Override
    public List<AuditLog> getUserLogs(int userId, int limit) {
        return auditLogDAO.findByUserId(userId, limit);
    }

    @Override
    public List<AuditLog> searchLogs(String action, String username, int limit) {
        return auditLogDAO.search(action, username, limit);
    }
}
