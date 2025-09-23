package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private MenuItemButton deckBtn;

    @FXML
    private MenuItemButton somethingBtn;

    public void initialize() {
        deckBtn.setIcon("book");
        deckBtn.setMainText("My Decks");
        deckBtn.setSubText("Browse decks");
        deckBtn.setOnAction(e -> renderAllDecks());

        somethingBtn.setIcon("loading");
        somethingBtn.setMainText("Something");
        somethingBtn.setSubText("TBA");
        somethingBtn.setOnAction(e -> System.out.println("Bruh"));
    }

    private void renderAllDecks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/decks.fxml"));
            Scene scene = new Scene(loader.load(), 440, 956);
            
            Stage decksStage = new Stage();
            decksStage.setScene(scene);
            decksStage.setTitle("My Decks - CARDS MEMO GAME");
            decksStage.show();
            
        } catch (Exception ex) {
            System.err.println("Error opening decks window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
