package com.devflow.service.impl;

import com.devflow.dao.NotificationDAO;
import com.devflow.dao.impl.NotificationDAOImpl;
import com.devflow.model.Notification;
import com.devflow.service.NotificationService;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationDAO notificationDAO;

    public NotificationServiceImpl() {
        this.notificationDAO = new NotificationDAOImpl();
    }

    public NotificationServiceImpl(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Override
    public List<Notification> getUserNotifications(int userId, int limit) {
        return notificationDAO.findByUserId(userId, limit);
    }

    @Override
    public List<Notification> getUnreadNotifications(int userId) {
        return notificationDAO.findUnreadByUserId(userId);
    }

    @Override
    public int getUnreadCount(int userId) {
        return notificationDAO.countUnreadByUserId(userId);
    }

    @Override
    public boolean markAsRead(int notificationId, int userId) {
        return notificationDAO.markAsRead(notificationId, userId);
    }

    @Override
    public boolean markAllAsRead(int userId) {
        return notificationDAO.markAllAsRead(userId);
    }

    @Override
    public boolean deleteNotification(int notificationId, int userId) {
        return notificationDAO.delete(notificationId, userId);
    }
}
