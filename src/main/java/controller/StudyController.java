package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import util.I18n;

import java.text.MessageFormat;

import model.dao.GameSessionsDao;
import model.dao.SessionResultsDao;
import model.dao.CardTranslationDao;
import model.entity.Cards;
import model.entity.Decks;
import model.entity.GameSessions;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.RotateTransition;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;
import model.entity.SessionResults;

// TODO : ADD JAVADOC cpmmens


/**
 * Controller for the study session view.
 * Handles flashcard study sessions with flip animations and progress tracking.
 */
public class StudyController {
    private static final Logger logger = Logger.getLogger(StudyController.class.getName());
    private Timestamp startTime;
    private Timestamp endTime;
    private int currentSessionId;

    @FXML
    public Button closeButton;

    @FXML
    private Label studyTitleLabel;

    @FXML
    private Label studySubtitleLabel;

    @FXML
    private StackPane cardContainer;

    @FXML
    private Label cardLabel;

    @FXML
    private Button knewButton;

    @FXML
    private Button didntKnowButton;

    private final GameSessionsDao gameSessionsDao = new GameSessionsDao();
    private final SessionResultsDao sessionResultsDao = new SessionResultsDao();
    private final CardTranslationDao cardTranslationDao = new CardTranslationDao(); // 🆕 для переводов

    private Decks deck;
    private List<Cards> cards;
    private int currentIndex = 0;
    private int score = 0;
    private boolean showingFront = true;

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

    @FXML
    public void initialize() {
        studyTitleLabel.textProperty().bind(Bindings.createStringBinding(() -> I18n.get("study.title"), I18n.localeProperty()));
        studySubtitleLabel.textProperty().bind(Bindings.createStringBinding(() -> I18n.get("study.instruction"), I18n.localeProperty()));

        knewButton.textProperty().bind(Bindings.createStringBinding(() -> I18n.get("study.knewBtn"), I18n.localeProperty()));
        didntKnowButton.textProperty().bind(Bindings.createStringBinding(() -> I18n.get("study.didntKnowBtn"), I18n.localeProperty()));
        closeButton.textProperty().bind(Bindings.createStringBinding(() -> I18n.get("study.closeBtn"), I18n.localeProperty()));

        cardLabel.setText(I18n.get("study.cardPlaceholder"));
    }

    /**
     * Shows the current card (with translation if available).
     */
    private void showCard() {
        if (currentIndex < cards.size()) {
            Cards currentCard = cards.get(currentIndex);

            String lang = Session.getInstance().getLanguage();

            try {
                String translatedFront = cardTranslationDao.getTranslatedFront(currentCard.getCard_id(), lang);
                if (translatedFront != null && !translatedFront.isEmpty()) {
                    currentCard.setFront_text(translatedFront);
                }

                String translatedBack = cardTranslationDao.getTranslatedBack(currentCard.getCard_id(), lang);
                if (translatedBack != null && !translatedBack.isEmpty()) {
                    currentCard.setBack_text(translatedBack);
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Translation fetch failed for card {0}", currentCard.getCard_id() + ": " + e.getMessage());
            }

            cardLabel.setText(currentCard.getFront_text());
            showingFront = true;
            knewButton.setVisible(false);
            didntKnowButton.setVisible(false);
            cardLabel.setOnMouseClicked(e -> flipCard(currentCard));
        } else {
            showResult();
        }
    }

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

    @FXML
    private void onKnew() {
        score++;
        saveCardResult(cards.get(currentIndex), true, 0);
        nextCard();
    }

    @FXML
    private void onDidntKnow() {
        saveCardResult(cards.get(currentIndex), false, 0);
        nextCard();
    }

    private void nextCard() {
        currentIndex++;
        showCard();
    }

    private void saveCardResult(Cards card, boolean isCorrect, int responseTime) {
        try {
            sessionResultsDao.persist(
                    new SessionResults(currentSessionId, card.getCard_id(), isCorrect, responseTime)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResult() {
        endTime = new Timestamp(System.currentTimeMillis());
        String formatted = MessageFormat.format(I18n.get("study.gameOver"), score, cards.size());
        cardLabel.setText(formatted);
        knewButton.setVisible(false);
        didntKnowButton.setVisible(false);
        closeButton.setVisible(true);
        saveResults();
    }

    private void saveResults() {
        try {
            gameSessionsDao.persist(new GameSessions(Session.getInstance().getUserId(), deck.getDeck_id(), startTime, endTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
