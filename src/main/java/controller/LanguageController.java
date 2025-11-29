package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import util.I18n;
import util.PageLoader;
import util.AudioPlayer;

import java.util.List;
import java.util.Locale;

// TODO : ADD DOCUMENTATION

public class LanguageController {
    List<String> availableLanguages = List.of("en", "ru", "ja","fi");
    Session session = Session.getInstance();
    PageLoader pageLoader = PageLoader.getInstance();


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
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }

    private void handleLanguageChange() {
        String selectedLanguage = languageCombo.getValue();
        if (selectedLanguage == null) return;
        I18n.setLocale(new Locale(selectedLanguage));
        session.setLanguage(selectedLanguage);
        pageLoader.reloadCurrentPage();
        
        AudioPlayer.stop();
        String audioPath = getAudioForLanguage(selectedLanguage);
        AudioPlayer.play(audioPath, 6000);

    }

    /**
     * Map language codes to audio resource paths under /music/. Returns null if
     * no audio is known for the language.
     */
    private String getAudioForLanguage(String lang) {
        if (lang == null)
            return null;
        return switch (lang) {
            case "en" -> "/music/English.mp3";
            case "ru" -> "/music/Russian.mp3";
            case "ja" -> "/music/Japanese.mp3";
            default -> null;
        };
    }

}
