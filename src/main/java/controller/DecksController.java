package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.dao.DecksDao;
import model.entity.Decks;
import java.sql.SQLException;
import java.util.List;

public class DecksController {

    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private VBox decksContainer;

    public void initialize() {
        loadDecks();
    }

    private void loadDecks() {
        try {
            DecksDao decksDao = new DecksDao();
            List<Decks> allDecks = decksDao.getAllDecks();
            
            decksContainer.getChildren().clear();
            
            if (allDecks.isEmpty()) {
                MenuItemButton emptyMessage = new MenuItemButton();
                emptyMessage.setIcon("list");
                emptyMessage.setMainText("No Decks Found");
                emptyMessage.setSubText("Create your first deck to get started");
                emptyMessage.setOnAction(e -> System.out.println("Create new deck"));
                decksContainer.getChildren().add(emptyMessage);
            } else {
                for (Decks deck : allDecks) {
                    MenuItemButton deckButton = new MenuItemButton();
                    deckButton.setIcon("book");
                    deckButton.setMainText(deck.getDeck_name());
                    
                    String subText = deck.getDescription();
                    if (subText == null || subText.trim().isEmpty()) {
                        subText = "No description";
                    }
                    if (subText.length() > 50) {
                        subText = subText.substring(0, 47) + "...";
                    }
                    deckButton.setSubText(subText);
                    
                    deckButton.setOnAction(e -> openDeck(deck));
                    
                    decksContainer.getChildren().add(deckButton);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading decks: " + ex.getMessage());
            ex.printStackTrace();
            
            MenuItemButton errorMessage = new MenuItemButton();
            errorMessage.setIcon("loading");
            errorMessage.setMainText("Error Loading Decks");
            errorMessage.setSubText("Database connection failed");
            errorMessage.setOnAction(e -> loadDecks());
            decksContainer.getChildren().add(errorMessage);
        }
    }
    
    private void openDeck(Decks deck) {
        System.out.println("Opening deck: " + deck.getDeck_name());
        System.out.println("Deck ID: " + deck.getDeck_id());
        System.out.println("Description: " + deck.getDescription());
        System.out.println("User ID: " + deck.getUser_id());
        System.out.println("Version: " + deck.getVersion());
        System.out.println("Visibility: " + deck.isVisibility());
        System.out.println("Created: " + deck.getCreated_at());
        // TODO: Implement deck opening functionality
    }
}