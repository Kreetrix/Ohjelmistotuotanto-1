package model.dao;

import model.entity.DeckAssignments;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeckAssignmentsDaoTest {

    private DeckAssignmentsDao dao;

    @BeforeAll
    void initDao() {
        dao = new DeckAssignmentsDao();
    }

    @Test
    void testGetAllDeckAssignmentsReturnsInsertedAssignment() throws SQLException {
        DeckAssignments testAssignment = new DeckAssignments(1, 1, new Timestamp(System.currentTimeMillis()));
        dao.persist(testAssignment);

        List<DeckAssignments> assignments = dao.getAllDeckAssignments();
        assertNotNull(assignments);
        assertTrue(assignments.stream().anyMatch(a ->
                a.getDeck_id() == 1 &&
                a.getStudent_id() == 1
        ));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM deckassignments WHERE deck_id = ? AND student_id = ?")) {
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.executeUpdate();
        }
    }
}