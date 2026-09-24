package com.devflow.service;

import com.devflow.model.Milestone;
import java.util.List;

public interface MilestoneService {
    Milestone getMilestoneById(int id);
    List<Milestone> getMilestonesByProjectId(int projectId);
    boolean createMilestone(Milestone milestone);
    boolean updateMilestone(Milestone milestone);
    boolean updateStatus(int milestoneId, String status);
    boolean deleteMilestone(int milestoneId);
}
