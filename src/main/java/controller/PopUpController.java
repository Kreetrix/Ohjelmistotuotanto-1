package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.dao.CardsDao;
import model.dao.DeckTranslationDao;
import model.entity.Cards;
import model.entity.Decks;
import util.I18n;
import util.Page;
import util.PageLoader;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO : ADD JAVADOC

/**
 * Controller for the study confirmation popup window.
 * Handles deck selection confirmation and navigation to study sessions.
 */
public class PopUpController {
    private static final Logger logger = Logger.getLogger(PopUpController.class.getName());

    @FXML
    private VBox popUpContainer;

    private Decks selectedDeck;
    private Stage popupStage;

    private final DeckTranslationDao deckTranslationDao = new DeckTranslationDao();

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
        popUpContainer.getChildren().clear();

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

        popUpContainer.getChildren().addAll(titleLabel, deckInfoLabel, confirmButton, cancelButton);

    }

    /**
     * Starts the study session for the selected deck.
     * Closes popup and opens study view with deck cards.
     */
    private void startStudySession() {
        closePopup();

        try {
            PageLoader.PopUp<StudyController> popup = PageLoader.getInstance().loadPopUp(Page.STUDY.getPath(),
                    Page.STUDY.getTitle(),
                    600, 400);
            StudyController studyController = popup.controller();

            CardsDao cardsDao = new CardsDao();
            List<Cards> deckCards = cardsDao.getCardsByDeckId(selectedDeck.getDeck_id());

            studyController.setDeck(selectedDeck, deckCards);

            Stage stage = popup.stage();
            stage.setTitle("Study Mode - " + selectedDeck.getDeck_name());

            stage.show();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error in startStudySession");
        }
    }

    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
