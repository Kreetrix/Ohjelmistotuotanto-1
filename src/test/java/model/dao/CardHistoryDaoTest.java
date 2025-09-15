package model.dao;

import model.entity.CardHistory;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardHistoryDaoTest {

    private CardHistoryDao dao;

    @BeforeAll
    void initDao() {
        dao = new CardHistoryDao();
    }

    @Test
    void testGetAllCardHistoriesReturnsInsertedHistory() throws SQLException {
        CardHistory testHistory = new CardHistory(1, 1, "Front Test", "Back Test", "http://example.com/img", "Extra info", new Timestamp(System.currentTimeMillis()));
        dao.persist(testHistory);

        List<CardHistory> histories = dao.getAllCardHistories();
        assertNotNull(histories);
        assertTrue(histories.stream().anyMatch(h ->
                h.getFront_text().equals("Front Test") &&
                h.getBack_text().equals("Back Test")
        ));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM cardhistory WHERE front_text = ? AND back_text = ?")) {
            ps.setString(1, "Front Test");
            ps.setString(2, "Back Test");
            ps.executeUpdate();
        }
    }
}