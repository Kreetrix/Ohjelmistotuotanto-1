package model.dao;

import model.entity.DeckAssignments;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for DeckAssignments entity.
 * Handles database operations for deck assignment management.
 */
public class DeckAssignmentsDao {

    /**
     * Retrieves all deck assignments from the database.
     *
     * @return List of all DeckAssignments entities
     * @throws SQLException if database access error occurs
     */
    public List<DeckAssignments> getAllDeckAssignments() throws SQLException {
        List<DeckAssignments> assignments = new ArrayList<>();
        String sql = "SELECT * FROM deckassignments";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int assignment_id = rs.getInt("assignment_id");
                int deck_id = rs.getInt("deck_id");
                int student_id = rs.getInt("student_id");
                Timestamp assigned_at = rs.getTimestamp("assigned_at");

                DeckAssignments assignment = new DeckAssignments(deck_id, student_id, assigned_at);
                assignment.setAssignment_id(assignment_id);
                assignments.add(assignment);
            }
        }

        return assignments;
    }

    /**
     * Saves a new deck assignment to the database.
     *
     * @param assignment the DeckAssignments entity to persist
     * @throws SQLException if database access error occurs
     */
    public void persist(DeckAssignments assignment) throws SQLException {
        String sql = "INSERT INTO deckassignments (deck_id, student_id, assigned_at) VALUES (?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, assignment.getDeck_id());
            ps.setInt(2, assignment.getStudent_id());
            ps.setTimestamp(3, assignment.getAssigned_at());
            ps.executeUpdate();
        }
    }
}