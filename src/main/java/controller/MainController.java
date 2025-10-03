package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Dimension;

public class MainController {

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 3;
		double height = screenSize.getHeight() / 1.2;

    @FXML
    private MenuItemButton deckBtn;

    @FXML
    private MenuItemButton createBtn;

    public void initialize() {
        deckBtn.setIcon("book");
        deckBtn.setMainText("My Decks");
        deckBtn.setSubText("Browse decks");
        deckBtn.setOnAction(e -> renderAllDecks());

        createBtn.setIcon("loading");
        createBtn.setMainText("edit and create cards/decks");
        createBtn.setSubText("SO MUCH FUN!");
        createBtn.setOnAction(e -> renderCardDeckCreation());
    }

    private void renderCardDeckCreation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/creation.fxml"));
            Scene scene = new Scene(loader.load(), width, height);

            Stage creationStage = new Stage();
            creationStage.setScene(scene);
            creationStage.setTitle("Memory Master - Create Cards and Decks");
            creationStage.show();
        } catch (Exception ex) {
            System.err.println("Error opening creation window: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void renderAllDecks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/decks.fxml"));
            Scene scene = new Scene(loader.load(), width, height);
            
            Stage decksStage = new Stage();
            decksStage.setScene(scene);
            decksStage.setTitle("Memory Master - Decks");
            decksStage.show();
            
        } catch (Exception ex) {
            System.err.println("Error opening decks window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
