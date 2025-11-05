package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.Dimension;
import util.I18n;
import util.PageLoader;


/**
 * Controller for the main application dashboard.
 * Handles navigation to different sections of the application.
 */
public class MainController {

    PageLoader pageLoader = PageLoader.getInstance();

    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth() / 3;
    double height = screenSize.getHeight() / 1.2;

    @FXML
    private MenuItemButton deckBtn;

    @FXML
    private MenuItemButton createBtn;

    /**
     * Initializes the main controller after FXML loading.
     * Sets up the menu buttons and their actions.
     */
    public void initialize() {
        deckBtn.setIcon("book");
        deckBtn.setMainText(I18n.get("main.myDecks"));
        deckBtn.setSubText(I18n.get("main.browseDecks"));
        deckBtn.setOnAction(e -> renderAllDecks());

        createBtn.setIcon("loading");
        createBtn.setMainText(I18n.get("main.editor"));
        createBtn.setSubText(I18n.get("main.editorSub"));
        createBtn.setOnAction(e -> renderCardDeckCreation());
    }

    /**
     * Opens the card and deck creation interface in a new window.
     */
    private void renderCardDeckCreation() {

        pageLoader.loadPopUp("/fxml/creation.fxml", "Memory Master - Create Cards and Decks");
        /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/creation.fxml"));
            Scene scene = new Scene(loader.load(), width, height);

            Stage creationStage = new Stage();
            creationStage.setScene(scene);
            creationStage.setTitle("Card Memo - Create Cards and Decks");
            creationStage.show();
        } catch (Exception ex) {
            System.err.println("Error opening creation window: " + ex.getMessage());
            ex.printStackTrace();
        }*/
    }

    /**
     * Opens the decks browsing interface in a new window.
     */
    private void renderAllDecks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/decks.fxml"));
            Scene scene = new Scene(loader.load(), width, height);

            Stage decksStage = new Stage();
            decksStage.setScene(scene);
            decksStage.setTitle("Card Memo - Decks");
            decksStage.show();

        } catch (Exception ex) {
            System.err.println("Error opening decks window: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}