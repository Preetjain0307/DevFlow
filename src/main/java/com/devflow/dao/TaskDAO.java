package com.devflow.dao;

import com.devflow.model.Task;
import com.devflow.model.TaskComment;
import com.devflow.model.TaskHistory;
import java.util.List;
import java.util.Map;

public interface TaskDAO {
    Task findById(int id);
    List<Task> findByProjectId(int projectId);
    List<Task> findBySprintId(int sprintId);
    List<Task> findByAssigneeId(int userId);
    List<Task> search(Integer projectId, Integer sprintId, Integer assignedTo, String status, String priority, String keyword);
    boolean create(Task task);
    boolean update(Task task);
    boolean updateStatus(int taskId, String status, int userId);
    boolean delete(int taskId);

    // Comments & History
    boolean addComment(TaskComment comment);
    List<TaskComment> findCommentsByTaskId(int taskId);
    boolean addHistory(TaskHistory history);
    List<TaskHistory> findHistoryByTaskId(int taskId);

    // Metrics
    int countTotalTasks();
    int countTasksByAssignee(int userId);
    int countTasksByStatus(String status);
    int countCompletedTasks();
    Map<String, Integer> getStatusDistribution();
    Map<String, Integer> getStatusDistributionByProject(int projectId);
}
