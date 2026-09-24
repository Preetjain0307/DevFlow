package com.devflow.service.impl;

import com.devflow.dao.SprintDAO;
import com.devflow.dao.impl.SprintDAOImpl;
import com.devflow.exception.ValidationException;
import com.devflow.model.Sprint;
import com.devflow.service.SprintService;
import com.devflow.util.ValidationUtil;
import java.util.List;

public class SprintServiceImpl implements SprintService {
    private final SprintDAO sprintDAO;

    public SprintServiceImpl() {
        this.sprintDAO = new SprintDAOImpl();
    }

    public SprintServiceImpl(SprintDAO sprintDAO) {
        this.sprintDAO = sprintDAO;
    }

    @Override
    public Sprint getSprintById(int id) {
        return sprintDAO.findById(id);
    }

    @Override
    public List<Sprint> getSprintsByProjectId(int projectId) {
        return sprintDAO.findByProjectId(projectId);
    }

    @Override
    public Sprint getActiveSprint(int projectId) {
        return sprintDAO.findActiveSprintByProjectId(projectId);
    }

    @Override
    public boolean createSprint(Sprint sprint) {
        if (!ValidationUtil.isNotEmpty(sprint.getSprintName())) {
            throw new ValidationException("Sprint name is required.");
        }
        if (sprint.getStartDate() == null || sprint.getEndDate() == null) {
            throw new ValidationException("Start date and End date are required.");
        }
        if (sprint.getStartDate().after(sprint.getEndDate())) {
            throw new ValidationException("Start date cannot be after End date.");
        }
        return sprintDAO.create(sprint);
    }

    @Override
    public boolean updateSprint(Sprint sprint) {
        if (!ValidationUtil.isNotEmpty(sprint.getSprintName())) {
            throw new ValidationException("Sprint name cannot be empty.");
        }
        return sprintDAO.update(sprint);
    }

    @Override
    public boolean updateStatus(int sprintId, String status) {
        return sprintDAO.updateStatus(sprintId, status);
    }

    @Override
    public boolean deleteSprint(int sprintId) {
        return sprintDAO.delete(sprintId);
    }
}
