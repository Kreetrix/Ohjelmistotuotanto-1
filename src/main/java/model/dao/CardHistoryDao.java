package model.dao;

import model.entity.CardHistory;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for CardHistory entity.
 * Handles database operations for card version history tracking.
 */
public class CardHistoryDao {

    /**
     * Retrieves all card history records from the database.
     * @return List of all CardHistory entities
     * @throws SQLException if database access error occurs
     */
    public List<CardHistory> getAllCardHistories() throws SQLException {
        List<CardHistory> histories = new ArrayList<>();
        String sql = "SELECT * FROM cardhistory";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int history_id = rs.getInt("history_id");
                int card_id = rs.getInt("card_id");
                int deck_version = rs.getInt("deck_version");
                String front_text = rs.getString("front_text");
                String back_text = rs.getString("back_text");
                String image_url = rs.getString("image_url");
                String extra_info = rs.getString("extra_info");
                Timestamp modified_at = rs.getTimestamp("modified_at");

                CardHistory history = new CardHistory(card_id, deck_version, front_text, back_text, image_url, extra_info, modified_at);
                history.setHistory_id(history_id);
                histories.add(history);
            }
        }

        return histories;
    }

    /**
     * Saves a new card history record to the database.
     * @param history the CardHistory entity to persist
     * @throws SQLException if database access error occurs
     */
    public void persist(CardHistory history) throws SQLException {
        String sql = "INSERT INTO cardhistory (card_id, deck_version, front_text, back_text, image_url, extra_info, modified_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, history.getCard_id());
            ps.setInt(2, history.getDeck_version());
            ps.setString(3, history.getFront_text());
            ps.setString(4, history.getBack_text());
            ps.setString(5, history.getImage_url());
            ps.setString(6, history.getExtra_info());
            ps.setTimestamp(7, history.getModified_at());
            ps.executeUpdate();
        }
    }
}