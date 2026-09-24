package com.devflow.dao;

import com.devflow.model.Bug;
import com.devflow.model.BugComment;
import java.util.List;
import java.util.Map;

public interface BugDAO {
    Bug findById(int id);
    List<Bug> findByProjectId(int projectId);
    List<Bug> findByAssigneeId(int userId);
    List<Bug> findByReporterId(int userId);
    List<Bug> search(Integer projectId, Integer assignedTo, String severity, String status, String keyword);
    boolean create(Bug bug);
    boolean update(Bug bug);
    boolean updateStatus(int bugId, String status, String resolutionNotes);
    boolean delete(int bugId);

    // Comments
    boolean addComment(BugComment comment);
    List<BugComment> findCommentsByBugId(int bugId);

    // Metrics
    int countTotalBugs();
    int countOpenBugs();
    int countBugsByAssignee(int userId);
    int countCriticalBugs();
    Map<String, Integer> getSeverityDistribution();
    Map<String, Integer> getStatusDistribution();
    Map<String, Integer> getSeverityDistributionByProject(int projectId);
}
