package model.dao;

import model.entity.Localization;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalizationDao {

    public List<Localization> getAllLocalizations() throws SQLException {
        List<Localization> localizations = new ArrayList<>();
        String sql = "SELECT * FROM localization";

        try (Connection conn = MariaDbJpaConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int translation_id = rs.getInt("translation_id");
                String entity_type = rs.getString("entity_type");
                int entity_id = rs.getInt("entity_id");
                String language_code = rs.getString("language_code");
                String translated_text = rs.getString("translated_text");

                Localization localization = new Localization(entity_type, entity_id, language_code, translated_text);
                localization.setTranslation_id(translation_id);
                localizations.add(localization);
            }
        }

        return localizations;
    }

    public void persist(Localization localization) throws SQLException {
        String sql = "INSERT INTO localization (entity_type, entity_id, language_code, translated_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, localization.getEntity_type());
            ps.setInt(2, localization.getEntity_id());
            ps.setString(3, localization.getLanguage_code());
            ps.setString(4, localization.getTranslated_text());
            ps.executeUpdate();
        }
    }
}