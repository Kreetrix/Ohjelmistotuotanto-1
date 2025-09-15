package model.dao;

import model.entity.DeckHistory;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeckHistoryDaoTest {

    private DeckHistoryDao dao;

    @BeforeAll
    void initDao() {
        dao = new DeckHistoryDao();
    }

    @Test
    void testGetAllDeckHistoriesReturnsInsertedHistory() throws SQLException {
        DeckHistory testHistory = new DeckHistory(1, 1, "Test Deck", "Test Description", new Timestamp(System.currentTimeMillis()));
        dao.persist(testHistory);

        List<DeckHistory> histories = dao.getAllDeckHistories();
        assertNotNull(histories);
        assertTrue(histories.stream().anyMatch(h ->
                h.getDeck_name().equals("Test Deck") &&
                h.getDescription().equals("Test Description")
        ));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM deckhistory WHERE deck_name = ? AND description = ?")) {
            ps.setString(1, "Test Deck");
            ps.setString(2, "Test Description");
            ps.executeUpdate();
        }
    }
}