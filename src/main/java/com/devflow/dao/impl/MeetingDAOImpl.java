package com.devflow.dao.impl;

import com.devflow.config.DBConnection;
import com.devflow.dao.MeetingDAO;
import com.devflow.exception.DatabaseException;
import com.devflow.model.Meeting;
import com.devflow.model.MeetingActionItem;
import com.devflow.model.MeetingNote;
import com.devflow.model.MeetingParticipant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeetingDAOImpl implements MeetingDAO {
    private static final Logger logger = LoggerFactory.getLogger(MeetingDAOImpl.class);

    private static final String BASE_SELECT = 
            "SELECT m.id, m.project_id, p.project_key, p.name AS project_name, " +
            "m.title, m.description, m.meeting_date, m.start_time, m.end_time, " +
            "m.room_code, m.meeting_url, m.status, m.created_by, u.full_name AS creator_name, " +
            "m.created_at " +
            "FROM meetings m " +
            "JOIN projects p ON m.project_id = p.id " +
            "JOIN users u ON m.created_by = u.id ";

    private Meeting mapRow(ResultSet rs) throws SQLException {
        Meeting m = new Meeting();
        m.setId(rs.getInt("id"));
        m.setProjectId(rs.getInt("project_id"));
        m.setProjectKey(rs.getString("project_key"));
        m.setProjectName(rs.getString("project_name"));
        m.setTitle(rs.getString("title"));
        m.setDescription(rs.getString("description"));
        m.setMeetingDate(rs.getDate("meeting_date"));
        m.setStartTime(rs.getTime("start_time"));
        m.setEndTime(rs.getTime("end_time"));
        m.setRoomCode(rs.getString("room_code"));
        m.setMeetingUrl(rs.getString("meeting_url"));
        m.setStatus(rs.getString("status"));
        m.setCreatedBy(rs.getInt("created_by"));
        m.setCreatorName(rs.getString("creator_name"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }

    @Override
    public Meeting findById(int id) {
        String sql = BASE_SELECT + "WHERE m.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Meeting m = mapRow(rs);
                m.setParticipants(findParticipantsByMeetingId(id));
                m.setMeetingNote(findNotesByMeetingId(id));
                m.setActionItems(findActionItemsByMeetingId(id));
                return m;
            }
        } catch (SQLException e) {
            logger.error("Error finding meeting by id {}: {}", id, e.getMessage());
            throw new DatabaseException("Failed to find meeting", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Meeting> findByProjectId(int projectId) {
        String sql = BASE_SELECT + "WHERE m.project_id = ? ORDER BY m.meeting_date DESC, m.start_time DESC";
        List<Meeting> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding meetings for project {}: {}", projectId, e.getMessage());
            throw new DatabaseException("Failed to retrieve meetings", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Meeting> findByUserId(int userId) {
        String sql = BASE_SELECT + 
                "WHERE m.created_by = ? OR m.id IN (SELECT meeting_id FROM meeting_participants WHERE user_id = ?) " +
                "ORDER BY m.meeting_date DESC, m.start_time DESC";
        List<Meeting> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding meetings for user {}: {}", userId, e.getMessage());
            throw new DatabaseException("Failed to retrieve user meetings", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public List<Meeting> findUpcomingMeetings(int limit) {
        String sql = BASE_SELECT + "WHERE m.status IN ('SCHEDULED', 'IN_PROGRESS') AND m.meeting_date >= CURDATE() " +
                     "ORDER BY m.meeting_date ASC, m.start_time ASC LIMIT ?";
        List<Meeting> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding upcoming meetings: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean create(Meeting meeting) {
        String sql = "INSERT INTO meetings (project_id, title, description, meeting_date, start_time, end_time, room_code, meeting_url, status, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, meeting.getProjectId());
            ps.setString(2, meeting.getTitle());
            ps.setString(3, meeting.getDescription());
            ps.setDate(4, meeting.getMeetingDate());
            ps.setTime(5, meeting.getStartTime());
            ps.setTime(6, meeting.getEndTime());
            ps.setString(7, meeting.getRoomCode());
            ps.setString(8, meeting.getMeetingUrl());
            ps.setString(9, meeting.getStatus() != null ? meeting.getStatus() : "SCHEDULED");
            ps.setInt(10, meeting.getCreatedBy());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    meeting.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating meeting: {}", e.getMessage());
            throw new DatabaseException("Failed to create meeting", e);
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return false;
    }

    @Override
    public boolean update(Meeting meeting) {
        String sql = "UPDATE meetings SET title = ?, description = ?, meeting_date = ?, start_time = ?, end_time = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, meeting.getTitle());
            ps.setString(2, meeting.getDescription());
            ps.setDate(3, meeting.getMeetingDate());
            ps.setTime(4, meeting.getStartTime());
            ps.setTime(5, meeting.getEndTime());
            ps.setString(6, meeting.getStatus());
            ps.setInt(7, meeting.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating meeting {}: {}", meeting.getId(), e.getMessage());
            throw new DatabaseException("Failed to update meeting", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateStatus(int meetingId, String status) {
        String sql = "UPDATE meetings SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, meetingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating meeting status: {}", e.getMessage());
            throw new DatabaseException("Failed to update meeting status", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean delete(int meetingId) {
        String sql = "DELETE FROM meetings WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, meetingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting meeting: {}", e.getMessage());
            throw new DatabaseException("Failed to delete meeting", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean addParticipant(int meetingId, int userId, String status) {
        String sql = "INSERT INTO meeting_participants (meeting_id, user_id, status) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status = VALUES(status)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, meetingId);
            ps.setInt(2, userId);
            ps.setString(3, status != null ? status : "INVITED");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding participant: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean updateParticipantStatus(int meetingId, int userId, String status) {
        String sql = "UPDATE meeting_participants SET status = ?, joined_at = CASE WHEN ? = 'ATTENDED' THEN CURRENT_TIMESTAMP ELSE joined_at END WHERE meeting_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, meetingId);
            ps.setInt(4, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating participant status: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<MeetingParticipant> findParticipantsByMeetingId(int meetingId) {
        String sql = "SELECT mp.id, mp.meeting_id, mp.user_id, u.username, u.full_name, u.email, mp.status, mp.joined_at " +
                     "FROM meeting_participants mp " +
                     "JOIN users u ON mp.user_id = u.id " +
                     "WHERE mp.meeting_id = ? ORDER BY u.full_name ASC";
        List<MeetingParticipant> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, meetingId);
            rs = ps.executeQuery();
            while (rs.next()) {
                MeetingParticipant mp = new MeetingParticipant();
                mp.setId(rs.getInt("id"));
                mp.setMeetingId(rs.getInt("meeting_id"));
                mp.setUserId(rs.getInt("user_id"));
                mp.setUsername(rs.getString("username"));
                mp.setFullName(rs.getString("full_name"));
                mp.setEmail(rs.getString("email"));
                mp.setStatus(rs.getString("status"));
                mp.setJoinedAt(rs.getTimestamp("joined_at"));
                list.add(mp);
            }
        } catch (SQLException e) {
            logger.error("Error finding participants: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public boolean saveNotes(MeetingNote note) {
        String sql = "INSERT INTO meeting_notes (meeting_id, raw_notes, ai_summary, ai_decisions, ai_action_items, ai_responsibilities, updated_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE raw_notes = VALUES(raw_notes), ai_summary = VALUES(ai_summary), " +
                     "ai_decisions = VALUES(ai_decisions), ai_action_items = VALUES(ai_action_items), " +
                     "ai_responsibilities = VALUES(ai_responsibilities), updated_by = VALUES(updated_by)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, note.getMeetingId());
            ps.setString(2, note.getRawNotes());
            ps.setString(3, note.getAiSummary());
            ps.setString(4, note.getAiDecisions());
            ps.setString(5, note.getAiActionItems());
            ps.setString(6, note.getAiResponsibilities());
            ps.setInt(7, note.getUpdatedBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error saving meeting notes: {}", e.getMessage());
            throw new DatabaseException("Failed to save meeting notes", e);
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public MeetingNote findNotesByMeetingId(int meetingId) {
        String sql = "SELECT mn.id, mn.meeting_id, mn.raw_notes, mn.ai_summary, mn.ai_decisions, mn.ai_action_items, mn.ai_responsibilities, mn.updated_by, u.full_name AS updater_name, mn.created_at, mn.updated_at " +
                     "FROM meeting_notes mn " +
                     "JOIN users u ON mn.updated_by = u.id " +
                     "WHERE mn.meeting_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, meetingId);
            rs = ps.executeQuery();
            if (rs.next()) {
                MeetingNote mn = new MeetingNote();
                mn.setId(rs.getInt("id"));
                mn.setMeetingId(rs.getInt("meeting_id"));
                mn.setRawNotes(rs.getString("raw_notes"));
                mn.setAiSummary(rs.getString("ai_summary"));
                mn.setAiDecisions(rs.getString("ai_decisions"));
                mn.setAiActionItems(rs.getString("ai_action_items"));
                mn.setAiResponsibilities(rs.getString("ai_responsibilities"));
                mn.setUpdatedBy(rs.getInt("updated_by"));
                mn.setUpdaterName(rs.getString("updater_name"));
                mn.setCreatedAt(rs.getTimestamp("created_at"));
                mn.setUpdatedAt(rs.getTimestamp("updated_at"));
                return mn;
            }
        } catch (SQLException e) {
            logger.error("Error finding meeting notes: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public boolean addActionItem(MeetingActionItem item) {
        String sql = "INSERT INTO meeting_action_items (meeting_id, description, assigned_to, due_date, is_completed) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, item.getMeetingId());
            ps.setString(2, item.getDescription());
            if (item.getAssignedTo() != null) ps.setInt(3, item.getAssignedTo()); else ps.setNull(3, Types.INTEGER);
            ps.setDate(4, item.getDueDate());
            ps.setBoolean(5, item.isCompleted());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding meeting action item: {}", e.getMessage());
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public boolean toggleActionItem(int itemId, boolean completed) {
        String sql = "UPDATE meeting_action_items SET is_completed = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, completed);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            DBConnection.close(conn, ps, null);
        }
    }

    @Override
    public List<MeetingActionItem> findActionItemsByMeetingId(int meetingId) {
        String sql = "SELECT mai.id, mai.meeting_id, mai.description, mai.assigned_to, u.full_name AS assignee_name, mai.due_date, mai.is_completed, mai.created_at " +
                     "FROM meeting_action_items mai " +
                     "LEFT JOIN users u ON mai.assigned_to = u.id " +
                     "WHERE mai.meeting_id = ? ORDER BY mai.id ASC";
        List<MeetingActionItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, meetingId);
            rs = ps.executeQuery();
            while (rs.next()) {
                MeetingActionItem mai = new MeetingActionItem();
                mai.setId(rs.getInt("id"));
                mai.setMeetingId(rs.getInt("meeting_id"));
                mai.setDescription(rs.getString("description"));
                int assignedTo = rs.getInt("assigned_to");
                mai.setAssignedTo(rs.wasNull() ? null : assignedTo);
                mai.setAssigneeName(rs.getString("assignee_name"));
                mai.setDueDate(rs.getDate("due_date"));
                mai.setCompleted(rs.getBoolean("is_completed"));
                mai.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(mai);
            }
        } catch (SQLException e) {
            logger.error("Error finding action items: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return list;
    }

    @Override
    public int countUpcomingMeetings() {
        String sql = "SELECT COUNT(*) FROM meetings WHERE status IN ('SCHEDULED', 'IN_PROGRESS') AND meeting_date >= CURDATE()";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("Error counting upcoming meetings: {}", e.getMessage());
        } finally {
            DBConnection.close(conn, ps, rs);
        }
        return 0;
    }
}
