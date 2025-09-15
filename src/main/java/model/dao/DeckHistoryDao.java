package model.dao;

import model.entity.DeckHistory;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckHistoryDao {

    public List<DeckHistory> getAllDeckHistories() throws SQLException {
        List<DeckHistory> histories = new ArrayList<>();
        String sql = "SELECT * FROM deckhistory";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int history_id = rs.getInt("history_id");
                int deck_id = rs.getInt("deck_id");
                int version = rs.getInt("version");
                String deck_name = rs.getString("deck_name");
                String description = rs.getString("description");
                Timestamp modified_at = rs.getTimestamp("modified_at");

                DeckHistory history = new DeckHistory(deck_id, version, deck_name, description, modified_at);
                history.setHistory_id(history_id);
                histories.add(history);
            }
        }

        return histories;
    }

    public void persist(DeckHistory history) throws SQLException {
        String sql = "INSERT INTO deckhistory (deck_id, version, deck_name, description, modified_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, history.getDeck_id());
            ps.setInt(2, history.getVersion());
            ps.setString(3, history.getDeck_name());
            ps.setString(4, history.getDescription());
            ps.setTimestamp(5, history.getModified_at());
            ps.executeUpdate();
        }
    }
}