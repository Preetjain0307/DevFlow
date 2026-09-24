package com.devflow.dao;

import com.devflow.model.Notification;
import java.util.List;

public interface NotificationDAO {
    Notification findById(int id);
    List<Notification> findByUserId(int userId, int limit);
    List<Notification> findUnreadByUserId(int userId);
    int countUnreadByUserId(int userId);
    boolean create(Notification notification);
    boolean markAsRead(int notificationId, int userId);
    boolean markAllAsRead(int userId);
    boolean delete(int notificationId, int userId);
    boolean notifyProjectMembers(int projectId, int excludeUserId, String title, String message, String linkUrl, String type);
}
