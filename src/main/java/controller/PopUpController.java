package controller;

import components.MenuItemButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.dao.CardsDao;
import model.entity.Cards;
import model.entity.Decks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    // TODO : ADD SAVING RESULTS  FOR USERS IN DB

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
        closePopup();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudyView.fxml"));
            Parent root = loader.load();

            StudyController studyController = loader.getController();

            CardsDao cardsDao = new CardsDao();
            List<Cards> deckCards = cardsDao.getCardsByDeckId(selectedDeck.getDeck_id());

            studyController.setDeck(selectedDeck, deckCards);

            Stage stage = new Stage();
            stage.setTitle("Study Mode - " + selectedDeck.getDeck_name());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
