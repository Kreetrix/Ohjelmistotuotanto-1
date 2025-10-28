package model.dao;

import model.entity.Localization;
import datasource.MariaDbJpaConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Localization entity.
 * Handles database operations for multi-language text translations.
 *
 * <p>Currently not used in the application and will be added in OTP2</p>
 */
public class LocalizationDao {

    /**
     * Retrieves all localization records from the database.
     * @return List of all Localization entities
     * @throws SQLException if database access error occurs
     */
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

    /**
     * Saves a new localization record to the database.
     * @param localization the Localization entity to persist
     * @throws SQLException if database access error occurs
     */
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

    // TODO : ADD DOCUMENTATION
    // added method for fetching localization for a specific entity

    public Localization getLocalizationForEntity(String entityType, int entityId, String languageCode) throws SQLException {
        String sql = "SELECT * FROM localization WHERE entity_type = ? AND entity_id = ? AND language_code = ?";
        try (Connection conn = MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entityType);
            ps.setInt(2, entityId);
            ps.setString(3, languageCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Localization localization = new Localization(
                        rs.getString("entity_type"),
                        rs.getInt("entity_id"),
                        rs.getString("language_code"),
                        rs.getString("translated_text")
                );
                localization.setTranslation_id(rs.getInt("translation_id"));
                return localization;
            }
        }
        return null;
    }




}