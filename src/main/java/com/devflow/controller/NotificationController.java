package com.devflow.controller;

import com.devflow.config.Constants;
import com.devflow.model.Notification;
import com.devflow.model.User;
import com.devflow.service.NotificationService;
import com.devflow.service.impl.NotificationServiceImpl;
import com.devflow.util.JsonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "NotificationController", urlPatterns = {
        "/notifications", "/notifications/read", "/notifications/read-all", "/api/notifications/count"
})
public class NotificationController extends HttpServlet {
    private NotificationService notificationService;

    @Override
    public void init() throws ServletException {
        this.notificationService = new NotificationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        if ("/api/notifications/count".equals(servletPath)) {
            int unreadCount = (currentUser != null) ? notificationService.getUnreadCount(currentUser.getId()) : 0;
            Map<String, Object> map = new HashMap<>();
            map.put("unreadCount", unreadCount);
            JsonUtil.sendJsonResponse(response, map);
            return;
        }

        List<Notification> notifications = notificationService.getUserNotifications(currentUser.getId(), 50);
        request.setAttribute("notifications", notifications);
        request.getRequestDispatcher("/WEB-INF/views/notification/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(Constants.SESSION_USER);

        if ("/notifications/read-all".equals(servletPath)) {
            notificationService.markAllAsRead(currentUser.getId());
            response.sendRedirect(request.getContextPath() + "/notifications");
            return;
        }

        if ("/notifications/read".equals(servletPath)) {
            int id = Integer.parseInt(request.getParameter("id"));
            notificationService.markAsRead(id, currentUser.getId());
            String redirect = request.getParameter("redirect");
            if (redirect != null && !redirect.isEmpty()) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/notifications");
            }
        }
    }
}
