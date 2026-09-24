package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.dao.SystemSettingDAO;
import com.devflow.dao.impl.SystemSettingDAOImpl;
import com.devflow.model.AuditLog;
import com.devflow.model.User;
import com.devflow.service.AuditLogService;
import com.devflow.service.UserService;
import com.devflow.service.impl.AuditLogServiceImpl;
import com.devflow.service.impl.UserServiceImpl;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminController", urlPatterns = {
        "/admin", "/admin/users", "/admin/user-status",
        "/admin/user-role", "/admin/audit-logs", "/admin/settings"
})
public class AdminController extends HttpServlet {
    private UserService userService;
    private AuditLogService auditLogService;
    private SystemSettingDAO systemSettingDAO;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl();
        this.auditLogService = new AuditLogServiceImpl();
        this.systemSettingDAO = new SystemSettingDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        switch (servletPath) {
            case "/admin/users":
                showUsers(request, response);
                break;
            case "/admin/audit-logs":
                showAuditLogs(request, response);
                break;
            case "/admin/settings":
                showSettings(request, response);
                break;
            case "/admin":
            default:
                showAdminOverview(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        switch (servletPath) {
            case "/admin/user-status": {
                int targetUserId = Integer.parseInt(request.getParameter("userId"));
                String newStatus = request.getParameter("status");
                userService.updateStatus(targetUserId, newStatus, currentUser.getId(), request.getRemoteAddr());
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;
            }
            case "/admin/user-role": {
                int targetUserId = Integer.parseInt(request.getParameter("userId"));
                int newRoleId = Integer.parseInt(request.getParameter("roleId"));
                userService.updateRole(targetUserId, newRoleId, currentUser.getId(), request.getRemoteAddr());
                response.sendRedirect(request.getContextPath() + "/admin/users");
                break;
            }
            case "/admin/settings": {
                String settingKey = request.getParameter("settingKey");
                String settingValue = request.getParameter("settingValue");
                String description = request.getParameter("description");
                systemSettingDAO.set(settingKey, settingValue, description);
                response.sendRedirect(request.getContextPath() + "/admin/settings?saved=true");
                break;
            }
            default:
                response.sendRedirect(request.getContextPath() + "/admin");
                break;
        }
    }

    private void showAdminOverview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("users", userService.getAllUsers());
        request.setAttribute("roles", userService.getAllRoles());
        request.setAttribute("recentLogs", auditLogService.getRecentLogs(15));
        request.setAttribute("settings", systemSettingDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    private void showUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("users", userService.getAllUsers());
        request.setAttribute("roles", userService.getAllRoles());
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    private void showAuditLogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String username = request.getParameter("username");

        List<AuditLog> logs;
        if ((action != null && !action.isEmpty()) || (username != null && !username.isEmpty())) {
            logs = auditLogService.searchLogs(action, username, 100);
        } else {
            logs = auditLogService.getRecentLogs(100);
        }

        request.setAttribute("logs", logs);
        request.setAttribute("selectedAction", action);
        request.setAttribute("selectedUsername", username);
        request.getRequestDispatcher("/WEB-INF/views/admin/audit_logs.jsp").forward(request, response);
    }

    private void showSettings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("settings", systemSettingDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/settings.jsp").forward(request, response);
    }
}
