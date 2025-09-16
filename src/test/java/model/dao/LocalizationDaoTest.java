package model.dao;

import model.entity.Localization;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LocalizationDaoTest {

    private LocalizationDao dao;

    @BeforeAll
    void initDao() {
        dao = new LocalizationDao();
    }

    @Test
    void testGetAllLocalizationsReturnsInsertedLocalization() throws SQLException {
        Localization testLocalization = new Localization("deck", 1, "en", "Test Translation");
        dao.persist(testLocalization);

        List<Localization> localizations = dao.getAllLocalizations();
        assertNotNull(localizations);
        assertTrue(localizations.stream().anyMatch(l ->
                l.getEntity_type().equals("deck") &&
                l.getEntity_id() == 1 &&
                l.getLanguage_code().equals("en")
        ));

        try (Connection conn = datasource.MariaDbJpaConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM localization WHERE entity_type = ? AND entity_id = ? AND language_code = ?")) {
            ps.setString(1, "deck");
            ps.setInt(2, 1);
            ps.setString(3, "en");
            ps.executeUpdate();
        }
    }
}