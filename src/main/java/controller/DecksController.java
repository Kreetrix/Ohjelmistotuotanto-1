package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.dao.DecksDao;
import model.entity.Decks;
import java.io.IOException;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
            Parent root = loader.load();
            
            PopUpController popupController = loader.getController();
            popupController.setDeck(deck);
            
            Stage popupStage = new Stage();
            popupController.setStage(popupStage);
            
            popupStage.setTitle("Study Deck - " + deck.getDeck_name());
            popupStage.setScene(new Scene(root, 400, 300));
            
            popupStage.showAndWait();
            
        } catch (IOException e) {
            System.err.println("Error opening popup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}