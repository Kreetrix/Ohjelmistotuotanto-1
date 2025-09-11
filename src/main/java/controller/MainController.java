package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    private MenuItemButton deckBtn;

    @FXML
    private MenuItemButton somethingBtn;

    public void initialize() {
        deckBtn.setIcon("book");
        deckBtn.setMainText("My Decks");
        deckBtn.setSubText("Browse decks");
        deckBtn.setOnAction(e -> System.out.println("Imma deck"));

        somethingBtn.setIcon("loading");
        somethingBtn.setMainText("Something");
        somethingBtn.setSubText("TBA");
        somethingBtn.setOnAction(e -> System.out.println("Bruh"));
    }

}
