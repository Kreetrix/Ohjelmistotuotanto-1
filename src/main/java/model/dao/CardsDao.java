package model.dao;
import model.entity.Cards;
import java.sql.*;
import java.util.*;
import datasource.MariaDbJpaConnection;

public class CardsDao {

    public List<Cards> getAllCards() throws SQLException {
        Connection conn = MariaDbJpaConnection.getConnection();
        String sql = "SELECT * FROM cards";
        List<Cards> cards = new ArrayList<>();

        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);

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
            e.printStackTrace();
        }

        return cards;
    }

    // switches cards value to is_deleted or not is_deleted
    public void isDeleted(boolean value, int id) throws SQLException {
        String sql = "UPDATE cards SET is_deleted = ? WHERE card_id = ?";
        
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value ? 1 : 0);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void persist(Cards card) throws SQLException {
        Connection conn = MariaDbJpaConnection.getConnection();
        String sql = "INSERT INTO cards (deck_id, front_text, back_text, image_url, extra_info, is_deleted) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, card.getDeck_id());
            ps.setString(2, card.getFront_text());
            ps.setString(3, card.getBack_text());
            ps.setString(4, card.getImage_url());
            ps.setString(5, card.getExtra_info());
            ps.setBoolean(6, card.isIs_deleted());
            ps.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error persisting card: " + e.getMessage(), e);
        }
    }
}
