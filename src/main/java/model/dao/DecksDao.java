package model.dao;

import model.entity.Decks;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DecksDao {

    public List<Decks> getAllDecks() throws SQLException {
        List<Decks> decks = new ArrayList<>();
        String sql = "SELECT * FROM decks";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int deck_id = rs.getInt("deck_id");
                int user_id = rs.getInt("user_id");
                String deck_name = rs.getString("deck_name");
                String description = rs.getString("description");
                int version = rs.getInt("version");
                boolean visibility = rs.getBoolean("visibility");
                boolean is_deleted = rs.getBoolean("is_deleted");
                Timestamp created_at = rs.getTimestamp("created_at");

                Decks deck = new Decks(user_id, deck_name, description, version, visibility, is_deleted, created_at);
                deck.setDeck_id(deck_id);
                decks.add(deck);
            }
        }

        return decks;
    }

    // switches deck value to is_deleted or not is_deleted also updates all cards in the deck to have the same is_deleted status
    public void isDeleted(boolean value, int id) throws SQLException {
        String updateDeckSql = "UPDATE decks SET is_deleted = ? WHERE deck_id = ?";
        String updateCardsSql = "UPDATE cards SET is_deleted = ? WHERE deck_id = ?";
        
        try (Connection conn = MariaDbJpaConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update the deck
                try (PreparedStatement ps1 = conn.prepareStatement(updateDeckSql)) {
                    ps1.setInt(1, value ? 1 : 0);
                    ps1.setInt(2, id);
                    ps1.executeUpdate();
                }
                
                // Update all cards in the deck
                try (PreparedStatement ps2 = conn.prepareStatement(updateCardsSql)) {
                    ps2.setInt(1, value ? 1 : 0);
                    ps2.setInt(2, id);
                    ps2.executeUpdate();
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void persist(Decks deck) throws SQLException {
        String sql = "INSERT INTO decks (user_id, deck_name, description, version, visibility, is_deleted, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deck.getUser_id());
            ps.setString(2, deck.getDeck_name());
            ps.setString(3, deck.getDescription());
            ps.setInt(4, deck.getVersion());
            ps.setBoolean(5, deck.isVisibility());
            ps.setBoolean(6, deck.isIs_deleted());
            ps.setTimestamp(7, deck.getCreated_at());
            ps.executeUpdate();
        }
    }
}
