package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.entity.Cards;
import model.entity.Decks;

import java.util.List;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;


// TODO : ADD SAVING RESULTS  FOR USERS IN DB


public class StudyController {

    @FXML
    public Button closeButton;

    @FXML
    private StackPane cardContainer;

    @FXML
    private Label cardLabel;

    @FXML
    private Button knewButton;

    @FXML
    private Button didntKnowButton;

    private Decks deck;
    private List<Cards> cards;
    private int currentIndex = 0;
    private int score = 0;
    private boolean showingFront = true;

    public void setDeck(Decks deck, List<Cards> cards) {
        this.deck = deck;
        this.cards = cards;
        showCard();
    }

    private void showCard() {
        if (currentIndex < cards.size()) {
            Cards currentCard = cards.get(currentIndex);
            cardLabel.setText(currentCard.getFront_text());


            showingFront = true;
            knewButton.setVisible(false);
            didntKnowButton.setVisible(false);
            cardLabel.setOnMouseClicked(e -> flipCard(currentCard));
        } else {
            showResult();
        }
    }

    // maybe later add method for showing additional info
    // from db




    private void flipCard(Cards card) {
        if (showingFront) {
            // rotateTransition class  for flip
            // from javafx.animation
            RotateTransition rotateOut = new RotateTransition(Duration.millis(200), cardLabel);
            rotateOut.setAxis(Rotate.Y_AXIS);
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);

            rotateOut.setOnFinished(event -> {
                cardLabel.setText(card.getBack_text());
                showingFront = false;
                knewButton.setVisible(true);
                didntKnowButton.setVisible(true);

                RotateTransition rotateIn = new RotateTransition(Duration.millis(200), cardLabel);
                rotateIn.setAxis(Rotate.Y_AXIS);
                rotateIn.setFromAngle(90);
                rotateIn.setToAngle(0);
                rotateIn.play();
            });

            rotateOut.play();
        }
    }

    @FXML
    private void onKnew() {
        score++;
        nextCard();
    }


    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDidntKnow() {
        nextCard();
    }

    private void nextCard() {
        currentIndex++;
        showCard();
    }

    private void showResult() {
        cardLabel.setText("The game is over!\nScore: " + score + " / " + cards.size());
        knewButton.setVisible(false);
        didntKnowButton.setVisible(false);
        closeButton.setVisible(true);


    }
}
