package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Decks;

public class PopUpController {

    @FXML
    private VBox PopUpContainer;

    private Decks selectedDeck;
    private Stage popupStage;

    public void initialize() {
    }

    public void setDeck(Decks deck) {
        this.selectedDeck = deck;
        setupStudyConfirmation();
    }

    public void setStage(Stage stage) {
        this.popupStage = stage;
    }

    private void setupStudyConfirmation() {
        PopUpContainer.getChildren().clear();

        // Title
        Label titleLabel = new Label("Study Deck?");
        titleLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24; -fx-padding: 20;");

        // Deck info
        Label deckInfoLabel = new Label("Do you want to study: " + selectedDeck.getDeck_name() + "?");
        deckInfoLabel.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 16; -fx-padding: 10;");
        deckInfoLabel.setWrapText(true);

        // Confirm button
        MenuItemButton confirmButton = new MenuItemButton();
        confirmButton.setIcon("check");
        confirmButton.setMainText("Start Study");
        confirmButton.setSubText("Begin studying this deck");
        confirmButton.setOnAction(e -> startStudySession());

        // Cancel button
        MenuItemButton cancelButton = new MenuItemButton();
        cancelButton.setIcon("arrowup");
        cancelButton.setMainText("Cancel");
        cancelButton.setSubText("Go back to deck list");
        cancelButton.setOnAction(e -> closePopup());

        PopUpContainer.getChildren().addAll(titleLabel, deckInfoLabel, confirmButton, cancelButton);
    }

    private void startStudySession() {
        System.out.println("Starting study session for deck: " + selectedDeck.getDeck_name());
        System.out.println("Deck ID: " + selectedDeck.getDeck_id());
        // TODO: Implement actual study session logic
        // THIS IS WHERE TO NAVIGATE TO STUDY MODE
        closePopup();
    }

    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
