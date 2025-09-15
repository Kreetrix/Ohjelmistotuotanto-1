package model.dao;

import model.entity.Cards;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardsDaoTest {

    private CardsDao dao;

    @BeforeAll
    void initDao() {
        dao = new CardsDao();
    }

    @Test
    void testGetAllCardsReturnsInsertedCard() throws SQLException {
        // create test card and add to db
        Cards testCard = new Cards(1, "Front Test", "Back Test", "http://example.com/img", "Extra info", false);
        dao.persist(testCard);

        // fetch all cards and sees if the created card is in the list of all cards in db
        List<Cards> cards = dao.getAllCards();
        assertNotNull(cards);
        assertTrue(cards.stream().anyMatch(c -> 
                c.getFront_text().equals("Front Test") &&
                c.getBack_text().equals("Back Test")
        ));

        // deletes test card from db
        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM cards WHERE front_text = ? AND back_text = ?")) {
            ps.setString(1, "Front Test");
            ps.setString(2, "Back Test");
            ps.executeUpdate();
        }
    }
}
