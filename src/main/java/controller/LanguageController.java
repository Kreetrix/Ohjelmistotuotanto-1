package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import util.I18n;

import java.util.List;
import java.util.Locale;

// TODO : ADD DOCUMENTATION


public class LanguageController {
    List<String> availableLanguages = List.of("en", "ru", "ja");
    Session session = Session.getInstance();


    @FXML private ComboBox<String> languageCombo;
    @FXML private javafx.scene.control.Label langLabel;

    @FXML
    private void initialize() {
        languageCombo.getItems().clear();
        languageCombo.getItems().addAll(availableLanguages);

        String current = session.getLanguage();
        if (current == null || current.isEmpty()) {
            current = I18n.getLocale().getLanguage();
        }
        languageCombo.setValue(current);

        languageCombo.setOnAction(e -> handleLanguageChange());
    try {
        langLabel.textProperty().bind(
            javafx.beans.binding.Bindings.createStringBinding(() -> util.I18n.get("label.lang"), util.I18n.localeProperty())
        );
    } catch (Exception ignored) {}
    }

    private void handleLanguageChange() {
        String selectedLanguage = languageCombo.getValue();
        if (selectedLanguage == null) return;
        I18n.setLocale(new Locale(selectedLanguage));
        session.setLanguage(selectedLanguage);
    }
    
}
