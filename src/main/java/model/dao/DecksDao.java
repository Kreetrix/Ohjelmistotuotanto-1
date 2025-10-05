package model.dao;

import model.entity.Decks;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Decks entity.
 * Handles database operations for flashcard deck management.
 */
public class DecksDao {

    /**
     * Retrieves all decks from the database.
     * @return List of all Decks entities
     * @throws SQLException if database access error occurs
     */
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
                String visibility = rs.getString("visibility");
                boolean is_deleted = rs.getBoolean("is_deleted");
                Timestamp created_at = rs.getTimestamp("created_at");

                Decks deck = new Decks(user_id, deck_name, description, version, visibility, is_deleted, created_at);
                deck.setDeck_id(deck_id);
                decks.add(deck);
            }
        }

        return decks;
    }

    /**
     * Updates the deletion status of a deck.
     * @param value true to mark as deleted, false to restore
     * @param id the deck ID to update
     * @throws SQLException if database access error occurs
     */
    public void isDeleted(boolean value, int id) throws SQLException {
        String sql = "UPDATE decks SET is_deleted = ? WHERE deck_id = ?";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Saves a new deck to the database.
     * @param deck the Decks entity to persist
     * @throws SQLException if database access error occurs
     */
    public void persist(Decks deck) throws SQLException {
        String sql = "INSERT INTO decks (user_id, deck_name, description, version, visibility, is_deleted, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deck.getUser_id());
            ps.setString(2, deck.getDeck_name());
            ps.setString(3, deck.getDescription());
            ps.setInt(4, deck.getVersion());
            ps.setString(5, deck.getVisibility());
            ps.setBoolean(6, deck.isIs_deleted());
            ps.setTimestamp(7, deck.getCreated_at());
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing deck in the database.
     * @param deck the Decks entity with updated values
     * @throws SQLException if database access error occurs
     */
    public void updateDeck(Decks deck) throws SQLException {
        String sql = "UPDATE decks SET deck_name = ?, description = ?, visibility = ? WHERE deck_id = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deck.getDeck_name());
            ps.setString(2, deck.getDescription());
            ps.setString(3, deck.getVisibility());
            ps.setInt(4, deck.getDeck_id());
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves a specific deck by its ID.
     * @param deckId the deck ID to search for
     * @return Decks entity if found, null otherwise
     * @throws SQLException if database access error occurs
     */
    public Decks getDeckById(int deckId) throws SQLException {
        String sql = "SELECT * FROM decks WHERE deck_id = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deckId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int deck_id = rs.getInt("deck_id");
                int user_id = rs.getInt("user_id");
                String deck_name = rs.getString("deck_name");
                String description = rs.getString("description");
                int version = rs.getInt("version");
                String visibility = rs.getString("visibility");
                boolean is_deleted = rs.getBoolean("is_deleted");
                Timestamp created_at = rs.getTimestamp("created_at");

                Decks deck = new Decks(user_id, deck_name, description, version, visibility, is_deleted, created_at);
                deck.setDeck_id(deck_id);
                return deck;
            }
        }
        return null;
    }

    /**
     * Retrieves all active decks for a specific user.
     * @param userId the user ID to filter by
     * @return List of Decks entities for the specified user
     * @throws SQLException if database access error occurs
     */
    public List<Decks> getDecksByUserId(int userId) throws SQLException {
        List<Decks> decks = new ArrayList<>();
        String sql = "SELECT * FROM decks WHERE user_id = ? AND is_deleted = 0";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int deck_id = rs.getInt("deck_id");
                int user_id = rs.getInt("user_id");
                String deck_name = rs.getString("deck_name");
                String description = rs.getString("description");
                int version = rs.getInt("version");
                String visibility = rs.getString("visibility");
                boolean is_deleted = rs.getBoolean("is_deleted");
                Timestamp created_at = rs.getTimestamp("created_at");

                Decks deck = new Decks(user_id, deck_name, description, version, visibility, is_deleted, created_at);
                deck.setDeck_id(deck_id);
                decks.add(deck);
            }
        }
        return decks;
    }
}