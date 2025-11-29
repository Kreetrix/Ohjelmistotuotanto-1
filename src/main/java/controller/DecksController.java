package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.dao.DecksDao;
import model.dao.DeckTranslationDao;
import model.entity.Decks;
import util.I18n;
import util.PageLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the Decks view that displays all available flashcard decks.
 * Handles deck loading, display, and navigation to study sessions.
 */
public class DecksController {

    public Label myDecksLabel;
    public Label myDecksSubLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox decksContainer;

    private Stage currentPopupStage;

    private final DeckTranslationDao deckTranslationDao = new DeckTranslationDao();

    /**
     * Initializes the controller after FXML loading.
     * Loads and displays all available decks.
     */
    @FXML
    public void initialize() {
        myDecksLabel.setText(I18n.get("myDecks.title"));
        myDecksSubLabel.setText(I18n.get("myDecks.subText"));
        loadDecks();
    }

    /**
     * Loads decks from the database and displays them in the container.
     * Shows appropriate messages for empty states or errors.
     */
    private void loadDecks() {
        try {
            DecksDao decksDao = new DecksDao();
            List<Decks> allDecks = decksDao.getAllNotDeletedDecks();
            decksContainer.getChildren().clear();

            if (allDecks.isEmpty()) {
                MenuItemButton emptyMessage = new MenuItemButton();
                emptyMessage.setIcon("list");
                emptyMessage.setMainText("No Decks Found");
                decksContainer.getChildren().add(emptyMessage);
            } else {
                String currentLang = Session.getInstance().getLanguage();

                for (Decks deck : allDecks) {
                    MenuItemButton deckButton = new MenuItemButton();
                    deckButton.setIcon("book");



                    // 🌐 Try to get translations
                    String deckName = getTranslatedName(deck,currentLang);
                    String deckDescription = getTranslatedDescription(deck,currentLang);

                    deckButton.setMainText(deckName);
                    deckButton.setSubText(deckDescription);

                    deckButton.setOnAction(e -> openDeck(deck));
                    decksContainer.getChildren().add(deckButton);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            MenuItemButton errorMessage = new MenuItemButton();
            errorMessage.setIcon("loading");
            errorMessage.setMainText("Error Loading Decks");
            errorMessage.setSubText("Database connection failed");
            errorMessage.setOnAction(e -> loadDecks());
            decksContainer.getChildren().add(errorMessage);
        }
    }

    private String getTranslatedName(Decks deck, String lang) {
        try {
            String translatedName = deckTranslationDao.getTranslatedDeckName(deck.getDeck_id(), lang);
            if (translatedName != null && !translatedName.isEmpty()) {
                return translatedName;
            }



        } catch (SQLException e) {
            System.err.println("Translation fetch failed: " + e.getMessage());
        }

        return deck.getDeck_name();
    }
    private String getTranslatedDescription(Decks deck, String lang) {
        String deckDescription = null;
        try {
            String translatedDesc = deckTranslationDao.getTranslatedDescription(deck.getDeck_id(), lang);
            if (translatedDesc != null && !translatedDesc.isEmpty()) {
                deckDescription =  translatedDesc;
            }

            if (deckDescription == null || deckDescription.trim().isEmpty()) {
                return  "No description";
            } else if (deckDescription.length() > 50) {
                 return deckDescription.substring(0, 47) + "...";
            }

            return deckDescription;

        } catch (SQLException e) {
            System.err.println("Translation fetch failed: " + e.getMessage());
        }

        return "No description";
    }

    /**
     * Opens a deck in a study popup window.
     * @param deck the deck to open for studying
     */
    private void openDeck(Decks deck) {
        try {
            if (currentPopupStage != null && currentPopupStage.isShowing()) {
                currentPopupStage.close();
                return;
            }

            PageLoader.PopUp<PopUpController> popup = PageLoader.getInstance().loadPopUp("/fxml/popup.fxml", "Study Deck - " + deck.getDeck_name(), 400.0, 300.0);
            PopUpController popupController = popup.controller();
            popupController.setDeck(deck);
            popup.stage().show();
            currentPopupStage = popup.stage();
            popupController.setStage(currentPopupStage);



        } catch (Exception e) {
            System.err.println("Error opening popup: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
