package model.dao;

import datasource.MariaDbJpaConnection;

import java.sql.*;

// TODO : ADD JAVADOC comments


public class CardTranslationDao {

    public String getTranslatedFront(int cardId, String lang) throws SQLException {
        String sql = "SELECT front_text FROM card_translations WHERE card_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setString(2, lang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("front_text");
            }
        }
        return null;
    }

    public String getTranslatedBack(int cardId, String lang) throws SQLException {
        String sql = "SELECT back_text FROM card_translations WHERE card_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setString(2, lang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("back_text");
            }
        }
        return null;
    }
}
