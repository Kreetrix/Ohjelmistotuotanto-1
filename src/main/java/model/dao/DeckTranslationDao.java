package model.dao;

import datasource.MariaDbJpaConnection;

import java.sql.*;

public class DeckTranslationDao {

    public String getTranslatedDeckName(int deckId, String languageCode) throws SQLException {
        String sql = "SELECT deck_name FROM deck_translations WHERE deck_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deckId);
            ps.setString(2, languageCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("deck_name");
            }
        }
        return null;
    }

    public String getTranslatedDescription(int deckId, String languageCode) throws SQLException {
        String sql = "SELECT description FROM deck_translations WHERE deck_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deckId);
            ps.setString(2, languageCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("description");
            }
        }
        return null;
    }
}
