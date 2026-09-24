package com.devflow.dao;

import com.devflow.model.Sprint;
import java.util.List;

public interface SprintDAO {
    Sprint findById(int id);
    List<Sprint> findByProjectId(int projectId);
    Sprint findActiveSprintByProjectId(int projectId);
    boolean create(Sprint sprint);
    boolean update(Sprint sprint);
    boolean updateStatus(int sprintId, String status);
    boolean delete(int sprintId);
}
