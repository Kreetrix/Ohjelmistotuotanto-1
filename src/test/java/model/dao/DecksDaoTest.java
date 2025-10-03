//DecksDaoTest.java
package model.dao;

import model.entity.Decks;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DecksDaoTest {

    private DecksDao dao;

    @BeforeAll
    void initDao() {
        dao = new DecksDao();
    }

    @Test
    void testGetAllDecksReturnsInsertedDeck() throws SQLException {
        // Create test deck and insert into db
        Decks testDeck = new Decks(1, "Test Deck", "Test Description", 1, "private", false, new Timestamp(System.currentTimeMillis()));
        dao.persist(testDeck);

        // Fetch all decks and test if test deck is in there
        List<Decks> decks = dao.getAllDecks();
        assertNotNull(decks);
        assertTrue(decks.stream().anyMatch(d ->
                d.getDeck_name().equals("Test Deck") &&
                d.getDescription().equals("Test Description")
        ));


        //delete test deck from db
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM decks WHERE deck_name = ? AND description = ?")) {
            ps.setString(1, "Test Deck");
            ps.setString(2, "Test Description");
            ps.executeUpdate();
        }

    }

    @Test
    void testIsDeleted() throws SQLException {
        // Create test deck with is_deleted = false
        Decks testDeck = new Decks(1, "Delete Test Deck", "Delete Test Description", 1, "private", false, new Timestamp(System.currentTimeMillis()));
        dao.persist(testDeck);

        // Get the deck ID
        int deckId = 0;
        List<Decks> decks = dao.getAllDecks();
        for (Decks deck : decks) {
            if (deck.getDeck_name().equals("Delete Test Deck")) {
                deckId = deck.getDeck_id();
                break;
            }
        }

        // Test setting is_deleted to true
        dao.isDeleted(true, deckId);
        decks = dao.getAllDecks();
        Decks updatedDeck = decks.stream()
                .filter(d -> d.getDeck_name().equals("Delete Test Deck"))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedDeck);
        assertTrue(updatedDeck.isIs_deleted());

        // Test setting is_deleted to false
        dao.isDeleted(false, deckId);
        decks = dao.getAllDecks();
        updatedDeck = decks.stream()
                .filter(d -> d.getDeck_name().equals("Delete Test Deck"))
                .findFirst()
                .orElse(null);
        assertNotNull(updatedDeck);
        assertFalse(updatedDeck.isIs_deleted());

        // Clean up - delete test deck from db
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM decks WHERE deck_name = ? AND description = ?")) {
            ps.setString(1, "Delete Test Deck");
            ps.setString(2, "Delete Test Description");
            ps.executeUpdate();
        }
    }
}
