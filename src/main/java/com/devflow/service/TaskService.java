package com.devflow.service;

import com.devflow.model.Task;
import com.devflow.model.TaskComment;
import com.devflow.model.TaskHistory;
import java.util.List;

public interface TaskService {
    Task getTaskById(int id);
    List<Task> getTasksByProjectId(int projectId);
    List<Task> getTasksBySprintId(int sprintId);
    List<Task> getTasksByAssignee(int userId);
    List<Task> searchTasks(Integer projectId, Integer sprintId, Integer assignedTo, String status, String priority, String keyword);
    boolean createTask(Task task, int userId, String username, String ipAddress);
    boolean updateTask(Task task, int userId, String username, String ipAddress);
    boolean updateTaskStatus(int taskId, String status, int userId, String username, String ipAddress);
    boolean deleteTask(int taskId, int userId, String username, String ipAddress);

    // Comments & History
    boolean addComment(int taskId, int userId, String comment);
    List<TaskComment> getComments(int taskId);
    List<TaskHistory> getHistory(int taskId);
}
