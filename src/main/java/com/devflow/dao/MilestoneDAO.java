package com.devflow.dao;

import com.devflow.model.Milestone;
import java.util.List;

public interface MilestoneDAO {
    Milestone findById(int id);
    List<Milestone> findByProjectId(int projectId);
    boolean create(Milestone milestone);
    boolean update(Milestone milestone);
    boolean updateStatus(int milestoneId, String status);
    boolean delete(int milestoneId);
}
