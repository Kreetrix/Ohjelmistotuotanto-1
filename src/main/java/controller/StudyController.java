package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.dao.GameSessionsDao;
import model.dao.SessionResultsDao;
import model.entity.Cards;
import model.entity.Decks;
import model.entity.GameSessions;
import java.sql.Timestamp;

import java.util.List;
import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import model.entity.SessionResults;

/**
 * Controller for the study session view.
 * Handles flashcard study sessions with flip animations and progress tracking.
 */
public class StudyController {
    private Timestamp startTime;
    private Timestamp endTime;
    private int currentSessionId;

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

    GameSessionsDao gameSessionsDao = new GameSessionsDao();
    private SessionResultsDao sessionResultsDao = new SessionResultsDao();

    private Decks deck;
    private List<Cards> cards;
    private int currentIndex = 0;
    private int score = 0;
    private boolean showingFront = true;

    /**
     * Sets up the study session with the selected deck and cards.
     * @param deck the deck being studied
     * @param cards the list of cards in the deck
     */
    public void setDeck(Decks deck, List<Cards> cards) {
        this.deck = deck;
        this.cards = cards;
        this.startTime = new Timestamp(System.currentTimeMillis());
        GameSessions session = new GameSessions(Session.getInstance().getUserId(), deck.getDeck_id(), startTime, null);
        try {
            currentSessionId = gameSessionsDao.persist(session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        showCard();
    }

    /**
     * Displays the current card or shows results if all cards are completed.
     */
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

    // TODO: Add method for showing additional info from database

    /**
     * Flips the card to show the back side with flip animation.
     * @param card the current card being flipped
     */
    private void flipCard(Cards card) {
        if (showingFront) {
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

    /**
     * Handles when user indicates they knew the card answer.
     */
    @FXML
    private void onKnew() {
        score++;
        saveCardResult(cards.get(currentIndex), true, 0);
        nextCard();
    }

    /**
     * Closes the study session window.
     */
    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles when user indicates they didn't know the card answer.
     */
    @FXML
    private void onDidntKnow() {
        saveCardResult(cards.get(currentIndex), false, 0);
        nextCard();
    }

    /**
     * Advances to the next card in the study session.
     */
    private void nextCard() {
        currentIndex++;
        showCard();
    }

    /**
     * Saves the result for the current card.
     * @param card the card being evaluated
     * @param isCorrect whether the user knew the answer
     * @param responseTime time taken to respond (not currently used)
     */
    private void saveCardResult(Cards card, boolean isCorrect, int responseTime) {
        try {
            sessionResultsDao.persist(
                    new SessionResults(currentSessionId, card.getCard_id(), isCorrect, responseTime)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the final results of the study session.
     */
    private void showResult() {
        endTime = new Timestamp(System.currentTimeMillis());
        cardLabel.setText("The game is over!\nScore: " + score + " / " + cards.size());
        knewButton.setVisible(false);
        didntKnowButton.setVisible(false);
        closeButton.setVisible(true);
        saveResults();
    }

    /**
     * Saves the final session results to the database.
     */
    private void saveResults() {
        try {
            gameSessionsDao.persist(new GameSessions(Session.getInstance().getUserId(), deck.getDeck_id(), startTime, endTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}