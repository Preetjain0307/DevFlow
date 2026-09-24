package com.devflow.service;

import com.devflow.model.Sprint;
import java.util.List;

public interface SprintService {
    Sprint getSprintById(int id);
    List<Sprint> getSprintsByProjectId(int projectId);
    Sprint getActiveSprint(int projectId);
    boolean createSprint(Sprint sprint);
    boolean updateSprint(Sprint sprint);
    boolean updateStatus(int sprintId, String status);
    boolean deleteSprint(int sprintId);
}
