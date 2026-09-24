package com.devflow.service;

import com.devflow.model.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(int userId, int limit);
    List<Notification> getUnreadNotifications(int userId);
    int getUnreadCount(int userId);
    boolean markAsRead(int notificationId, int userId);
    boolean markAllAsRead(int userId);
    boolean deleteNotification(int notificationId, int userId);
}
