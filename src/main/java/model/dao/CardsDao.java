package model.dao;

import model.entity.Cards;
import java.sql.*;
import java.util.*;
import datasource.MariaDbJpaConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Cards entity.
 * Handles database operations for flashcard management.
 */
public class CardsDao {
    private static final Logger logger = Logger.getLogger(CardsDao.class.getName());

    /**
     * Retrieves all cards from the database.
     * 
     * @return List of all Cards entities
     * @throws SQLException if database access error occurs
     */
    public List<Cards> getAllCards() throws SQLException {
        String sql = "SELECT * FROM cards";
        List<Cards> cards = new ArrayList<>();

        try (Connection conn = MariaDbJpaConnection.getConnection();
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                int card_id = rs.getInt(1);
                int deck_id = rs.getInt(2);
                String front_text = rs.getString(3);
                String back_text = rs.getString(4);
                String image_url = rs.getString(5);
                String extra_info = rs.getString(6);
                boolean is_deleted = rs.getBoolean(7);

                Cards card = new Cards(deck_id, front_text, back_text, image_url, extra_info, is_deleted);
                card.setCard_id(card_id);
                cards.add(card);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error in saveResults");
            throw e;
        }

        return cards;
    }

    /**
     * Updates the deletion status of a card.
     * 
     * @param value true to mark as deleted, false to restore
     * @param id    the card ID to update
     * @throws SQLException if database access error occurs
     */
    public void isDeleted(boolean value, int id) throws SQLException {
        String sql = "UPDATE cards SET is_deleted = ? WHERE card_id = ?";

        try (Connection conn = MariaDbJpaConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Saves a new card to the database.
     * 
     * @param card the Cards entity to persist
     * @throws SQLException if database access error occurs
     */
    public void persist(Cards card) throws SQLException {
        String sql = "INSERT INTO cards (deck_id, front_text, back_text, image_url, extra_info, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, card.getDeck_id());
            ps.setString(2, card.getFront_text());
            ps.setString(3, card.getBack_text());
            ps.setString(4, card.getImage_url());
            ps.setString(5, card.getExtra_info());
            ps.setBoolean(6, card.isIs_deleted());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error in persist");
            throw new RuntimeException("Error persisting card: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all active cards for a specific deck.
     * 
     * @param deckId the deck ID to filter by
     * @return List of Cards entities for the specified deck
     * @throws SQLException if database access error occurs
     */
    public List<Cards> getCardsByDeckId(int deckId) throws SQLException {
        List<Cards> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards WHERE deck_id = ? AND is_deleted = 0";

        try (Connection conn = MariaDbJpaConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deckId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int card_id = rs.getInt("card_id");
                int d_id = rs.getInt("deck_id");
                String front_text = rs.getString("front_text");
                String back_text = rs.getString("back_text");
                String image_url = rs.getString("image_url");
                String extra_info = rs.getString("extra_info");
                boolean is_deleted = rs.getBoolean("is_deleted");

                Cards card = new Cards(d_id, front_text, back_text, image_url, extra_info, is_deleted);
                card.setCard_id(card_id);
                cards.add(card);
            }
        }

        return cards;
    }

    /**
     * Updates an existing card in the database.
     * 
     * @param card the Cards entity with updated values
     * @throws SQLException if database access error occurs
     */
    public void updateCard(Cards card) throws SQLException {
        String sql = "UPDATE cards SET deck_id = ?, front_text = ?, back_text = ?, image_url = ?, extra_info = ? WHERE card_id = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, card.getDeck_id());
            ps.setString(2, card.getFront_text());
            ps.setString(3, card.getBack_text());
            ps.setString(4, card.getImage_url());
            ps.setString(5, card.getExtra_info());
            ps.setInt(6, card.getCard_id());
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves a specific card by its ID.
     * 
     * @param cardId the card ID to search for
     * @return Cards entity if found, null otherwise
     * @throws SQLException if database access error occurs
     */
    public Cards getCardById(int cardId) throws SQLException {
        String sql = "SELECT * FROM cards WHERE card_id = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int card_id = rs.getInt("card_id");
                int deck_id = rs.getInt("deck_id");
                String front_text = rs.getString("front_text");
                String back_text = rs.getString("back_text");
                String image_url = rs.getString("image_url");
                String extra_info = rs.getString("extra_info");
                boolean is_deleted = rs.getBoolean("is_deleted");

                Cards card = new Cards(deck_id, front_text, back_text, image_url, extra_info, is_deleted);
                card.setCard_id(card_id);
                return card;
            }
        }
        return null;
    }
}