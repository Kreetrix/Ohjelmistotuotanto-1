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

                    String deckName = deck.getDeck_name();
                    String deckDescription = deck.getDescription();

                    // 🌐 Try to get translations
                    try {
                        String translatedName = deckTranslationDao.getTranslatedDeckName(deck.getDeck_id(), currentLang);
                        if (translatedName != null && !translatedName.isEmpty()) {
                            deckName = translatedName;
                        }

                        String translatedDesc = deckTranslationDao.getTranslatedDescription(deck.getDeck_id(), currentLang);
                        if (translatedDesc != null && !translatedDesc.isEmpty()) {
                            deckDescription = translatedDesc;
                        }

                    } catch (SQLException e) {
                        System.err.println("Translation fetch failed: " + e.getMessage());
                    }

                    // fallback defaults
                    if (deckDescription == null || deckDescription.trim().isEmpty()) {
                        deckDescription = "No description";
                    } else if (deckDescription.length() > 50) {
                        deckDescription = deckDescription.substring(0, 47) + "...";
                    }

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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
            Parent root = loader.load();

            PopUpController popupController = loader.getController();
            popupController.setDeck(deck);

            Stage popupStage = new Stage();
            popupController.setStage(popupStage);

            popupStage.setTitle("Study Deck - " + deck.getDeck_name());
            popupStage.setScene(new Scene(root, 400, 300));
            currentPopupStage = popupStage;

            popupStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error opening popup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
