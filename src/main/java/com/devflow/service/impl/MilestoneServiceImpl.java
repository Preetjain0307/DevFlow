package com.devflow.service.impl;

import com.devflow.dao.MilestoneDAO;
import com.devflow.dao.impl.MilestoneDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.Milestone;
import com.devflow.service.MilestoneService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneDAO milestoneDAO;

    public MilestoneServiceImpl() {
        this.milestoneDAO = new MilestoneDAOImpl();
    }

    public MilestoneServiceImpl(MilestoneDAO milestoneDAO) {
        this.milestoneDAO = milestoneDAO;
    }

    @Override
    public Milestone getMilestoneById(int id) {
        return milestoneDAO.findById(id);
    }

    @Override
    public List<Milestone> getMilestonesByProjectId(int projectId) {
        return milestoneDAO.findByProjectId(projectId);
    }

    @Override
    public boolean createMilestone(Milestone milestone) {
        if (!ValidationUtil.isNotEmpty(milestone.getTitle())) {
            throw new ValidationException("Milestone title is required.");
        }
        return milestoneDAO.create(milestone);
    }

    @Override
    public boolean updateMilestone(Milestone milestone) {
        if (!ValidationUtil.isNotEmpty(milestone.getTitle())) {
            throw new ValidationException("Milestone title cannot be empty.");
        }
        return milestoneDAO.update(milestone);
    }

    @Override
    public boolean updateStatus(int milestoneId, String status) {
        return milestoneDAO.updateStatus(milestoneId, status);
    }

    @Override
    public boolean deleteMilestone(int milestoneId) {
        return milestoneDAO.delete(milestoneId);
    }
}
