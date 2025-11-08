package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.dao.CardsDao;
import model.dao.DeckTranslationDao;
import model.entity.Cards;
import model.entity.Decks;
import util.I18n;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


// TODO : ADD JAVADOC

/**
 * Controller for the study confirmation popup window.
 * Handles deck selection confirmation and navigation to study sessions.
 */
public class PopUpController {

    @FXML
    private VBox PopUpContainer;

    private Decks selectedDeck;
    private Stage popupStage;

    private final DeckTranslationDao deckTranslationDao = new DeckTranslationDao();

    public void initialize() {
    }

    public void setDeck(Decks deck) {
        this.selectedDeck = deck;
        setupStudyConfirmation();
    }

    public void setStage(Stage stage) {
        this.popupStage = stage;
    }

    /**
     * Sets up the study confirmation interface with deck info and action buttons.
     */
    private void setupStudyConfirmation() {
        PopUpContainer.getChildren().clear();

        String currentLang = Session.getInstance().getLanguage();
        String deckName = selectedDeck.getDeck_name();

        try {
            String translatedName = deckTranslationDao.getTranslatedDeckName(selectedDeck.getDeck_id(), currentLang);
            if (translatedName != null && !translatedName.isEmpty()) {
                deckName = translatedName;
            }
        } catch (SQLException e) {
            System.err.println("Failed to load translation: " + e.getMessage());
        }

        // Title
        Label titleLabel = new Label(I18n.get("myDecks.popup.title"));
        titleLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24; -fx-padding: 20;");

        // Deck info (с переводом)
        Label deckInfoLabel = new Label(I18n.get("myDecks.popup.deckInfo") + deckName + "?");
        deckInfoLabel.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 16; -fx-padding: 10;");
        deckInfoLabel.setWrapText(true);

        // Confirm button
        MenuItemButton confirmButton = new MenuItemButton();
        confirmButton.setIcon("check");
        confirmButton.setMainText(I18n.get("myDecks.popup.confirm"));
        confirmButton.setSubText(I18n.get("myDecks.popup.confirm.sub"));
        confirmButton.setOnAction(e -> startStudySession());

        // Cancel button
        MenuItemButton cancelButton = new MenuItemButton();
        cancelButton.setIcon("arrowup");
        cancelButton.setMainText(I18n.get("myDecks.popup.cancel"));
        cancelButton.setSubText(I18n.get("myDecks.popup.cancel.sub"));
        cancelButton.setOnAction(e -> closePopup());

        PopUpContainer.getChildren().addAll(titleLabel, deckInfoLabel, confirmButton, cancelButton);
    }

    /**
     * Starts the study session for the selected deck.
     * Closes popup and opens study view with deck cards.
     */
    private void startStudySession() {
        closePopup();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudyView.fxml"));
            Parent root = loader.load();

            StudyController studyController = loader.getController();

            CardsDao cardsDao = new CardsDao();
            List<Cards> deckCards = cardsDao.getCardsByDeckId(selectedDeck.getDeck_id());

            studyController.setDeck(selectedDeck, deckCards);

            Stage stage = new Stage();
            stage.setTitle("Study Mode - " + selectedDeck.getDeck_name());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
