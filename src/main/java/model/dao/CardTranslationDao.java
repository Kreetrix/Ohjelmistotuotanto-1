package model.dao;

import datasource.MariaDbJpaConnection;

import java.sql.*;

public class CardTranslationDao {

    public String getTranslatedFront(int cardId, String languageCode) throws SQLException {
        String sql = "SELECT front_textFROM card_translations WHERE card_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setString(2, languageCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("front_text");
            }
        }
        return null;
    }

    public String getTranslatedBack(int cardId, String languageCode) throws SQLException {
        String sql = "SELECT back_text FROM card_translation WHERE card_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setString(2, languageCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("back_text");
            }
        }
        return null;
    }
}
