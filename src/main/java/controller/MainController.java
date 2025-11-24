package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import util.I18n;
import util.PageLoader;


/**
 * Controller for the main application dashboard.
 * Handles navigation to different sections of the application.
 */
public class MainController {

    PageLoader pageLoader = PageLoader.getInstance();


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
    }

    /**
     * Opens the decks browsing interface in a new window.
     */
    private void renderAllDecks() {

        pageLoader.loadPopUp("/fxml/decks.fxml", "Card Memo - Decks");
    }
}