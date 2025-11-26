package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;
import model.dao.DecksDao;
import model.dao.CardsDao;
import model.entity.Decks;
import model.entity.Cards;
import components.IconManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.I18n;
import javafx.beans.binding.Bindings;

import java.io.IOException;

/**
 * Controller for the Creation Management interface that handles deck and card
 * creation, editing, and deletion.
 *
 */
public class CreationController {

    public Label pageTitle;
    public Label pageSub;
    public Tab deckFilter;
    public Tab cardFilter;
    public Label filterByDeckLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button createDeckButton;
    @FXML
    private Button createCardButton;
    @FXML
    private ComboBox<Decks> deckFilterComboBox;
    @FXML
    private ScrollPane decksScrollPane;
    @FXML
    private ScrollPane cardsScrollPane;
    @FXML
    private VBox decksContainer;
    @FXML
    private VBox cardsContainer;

    private DecksDao decksDao;
    private CardsDao cardsDao;

    /**
     * Initializes the controller after FXML loading.
     * Sets up DAOs, event handlers, and loads initial data.
     */
    @FXML
    public void initialize() {
        pageTitle.setText(I18n.get("editor.title"));
        pageSub.setText(I18n.get("editor.subtext"));
        createDeckButton.setText(I18n.get("editor.createDeckBtn"));
        createCardButton.setText(I18n.get("editor.createCardBtn"));
        deckFilter.setText(I18n.get("editor.filterByDeck"));
        cardFilter.setText(I18n.get("editor.filterByCard"));
        filterByDeckLabel.setText(I18n.get("editor.filterByDeckLabel"));

        decksDao = new DecksDao();
        cardsDao = new CardsDao();

        setupEventHandlers();
        loadDecks();
        loadCards();
        setupDeckFilter();
    }

    /**
     * Sets up event handlers for buttons and interactive components.
     */
    private void setupEventHandlers() {
        createDeckButton.setOnAction(e -> createNewDeck());
        createCardButton.setOnAction(e -> createNewCard());
        deckFilterComboBox.setOnAction(e -> filterCardsByDeck());
    }

    /**
     * Initializes the deck filter combo box with available decks.
     * Includes an "All Decks" option and filters out deleted decks.
     */
    private void setupDeckFilter() {
        try {
            List<Decks> allDecks = decksDao.getAllDecks();
            deckFilterComboBox.getItems().clear();

            // Create "All Decks" option
            Decks allDecksOption = new Decks(0, I18n.get("editor.allDecks"), "", 0, "private", false, null);
            allDecksOption.setDeck_id(-1);
            deckFilterComboBox.getItems().add(allDecksOption);

            // Add active (non-deleted) decks
            deckFilterComboBox.getItems().addAll(allDecks.stream()
                    .filter(deck -> !deck.isIs_deleted())
                    .toList());

            // Set up display converter for deck objects
            deckFilterComboBox.setConverter(new javafx.util.StringConverter<Decks>() {
                @Override
                public String toString(Decks deck) {
                    return deck != null ? deck.getDeck_name() : "";
                }

                @Override
                public Decks fromString(String string) {
                    return null;
                }
            });

            deckFilterComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            System.err.println("Failed to load deck filter options: " + e.getMessage());
        }
    }

    /**
     * Loads all decks from the database and displays them in the decks container.
     * Shows an empty state message if no decks are found.
     */
    private void loadDecks() {
        try {
            List<Decks> allDecks = decksDao.getAllDecks();
            decksContainer.getChildren().clear();

            if (allDecks.isEmpty() || allDecks.stream().noneMatch(deck -> !deck.isIs_deleted())) {
                Label emptyLabel = new Label("No decks found. Create your first deck!");
                emptyLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 16; -fx-padding: 20;");
                decksContainer.getChildren().add(emptyLabel);
            } else {

                for (Decks deck : allDecks) {
                    if (!deck.isIs_deleted()) {
                        VBox deckItem = createDeckItem(deck);
                        decksContainer.getChildren().add(deckItem);
                    }
                }
            }
        } catch (SQLException e) {
            showError("Failed to load decks: " + e.getMessage());
        }
    }

    /**
     * Loads all cards from the database and displays them in the cards container.
     */
    private void loadCards() {
        try {
            List<Cards> allCards = cardsDao.getAllCards();
            displayCards(allCards);
        } catch (SQLException e) {
            showError("Failed to load cards: " + e.getMessage());
        }
    }

    /**
     * Displays the provided list of cards in the cards container.
     * Shows an empty state message if no active cards are found.
     *
     * @param cards the list of cards to display
     */
    private void displayCards(List<Cards> cards) {
        cardsContainer.getChildren().clear();

        List<Cards> activeCards = cards.stream()
                .filter(card -> !card.isIs_deleted())
                .toList();

        if (activeCards.isEmpty()) {
            Label emptyLabel = new Label("No cards found. Create your first card!");
            emptyLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 16; -fx-padding: 20;");
            cardsContainer.getChildren().add(emptyLabel);
        } else {

            for (Cards card : activeCards) {
                VBox cardItem = createCardItem(card);
                cardsContainer.getChildren().add(cardItem);
            }
        }
    }

    /**
     * Creates a visual deck item component for display in the decks list.
     *
     * @param deck the deck entity to display
     * @return a VBox containing the deck item UI
     */
    private VBox createDeckItem(Decks deck) {
        VBox container = new VBox(5);
        container.setStyle(
                "-fx-background-color: #1a1a1a; -fx-border-color: #333333; -fx-border-width: 1; -fx-padding: 15;");

        HBox mainContent = new HBox(10);
        mainContent.setAlignment(Pos.CENTER_LEFT);

        SVGPath deckIcon = new SVGPath();
        deckIcon.setContent(IconManager.getPath("book"));
        deckIcon.setFill(Color.CYAN);
        deckIcon.setScaleX(1.5);
        deckIcon.setScaleY(1.5);

        VBox textContent = new VBox(2);
        Label nameLabel = new Label(deck.getDeck_name());
        nameLabel.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 18; -fx-font-weight: bold;");

        String description = deck.getDescription();
        if (description == null || description.trim().isEmpty()) {
            description = "No description";
        }
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 14;");

        textContent.getChildren().addAll(nameLabel, descLabel);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox buttonBox = new HBox(5);

        // Add Card button - only show if user can create cards in this deck
        if (canCreateCardsInDeck(deck)) {
            Button addCardButton = new Button();
            addCardButton.textProperty()
                    .bind(Bindings.createStringBinding(() -> I18n.get("editor.addCardBtn"), I18n.localeProperty()));
            addCardButton.setStyle("-fx-background-color: #107c10; -fx-text-fill: white; -fx-padding: 5 10;");
            addCardButton.setOnAction(e -> createCardForDeck(deck));
            buttonBox.getChildren().add(addCardButton);
        }

        // Edit button - only show if user can edit this deck
        if (hasEditPermission(deck)) {
            Button editButton = new Button();
            editButton.textProperty()
                    .bind(Bindings.createStringBinding(() -> I18n.get("action.edit"), I18n.localeProperty()));
            editButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-padding: 5 10;");
            editButton.setOnAction(e -> editDeck(deck));
            buttonBox.getChildren().add(editButton);
        }

        // Delete button - only show if user can delete this deck
        if (hasDeletePermission()) {
            Button deleteButton = new Button();
            deleteButton.textProperty()
                    .bind(Bindings.createStringBinding(() -> I18n.get("action.delete"), I18n.localeProperty()));
            deleteButton.setStyle("-fx-background-color: #d13438; -fx-text-fill: white; -fx-padding: 5 10;");
            deleteButton.setOnAction(e -> deleteDeck(deck));
            buttonBox.getChildren().add(deleteButton);
        }

        mainContent.getChildren().addAll(deckIcon, textContent, spacer, buttonBox);
        container.getChildren().add(mainContent);

        return container;
    }

    /**
     * Creates a visual card item component for display in the cards list.
     *
     * @param card the card entity to display
     * @return a VBox containing the card item UI
     */
    private VBox createCardItem(Cards card) {
        VBox container = new VBox(5);
        container.setStyle(
                "-fx-background-color: #1a1a1a; -fx-border-color: #333333; -fx-border-width: 1; -fx-padding: 15;");

        HBox mainContent = new HBox(10);
        mainContent.setAlignment(Pos.CENTER_LEFT);

        VBox textContent = new VBox(2);

        String question = card.getFront_text();
        if (question.length() > 50) {
            question = question.substring(0, 47) + "...";
        }
        Label questionLabel = new Label("Q: " + question);
        questionLabel.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 16; -fx-font-weight: bold;");

        String answer = card.getBack_text();
        if (answer.length() > 50) {
            answer = answer.substring(0, 47) + "...";
        }
        Label answerLabel = new Label("A: " + answer);
        answerLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 14;");

        try {
            String deckName = getDeckNameById(card.getDeck_id());
            Label deckLabel = new Label("Deck: " + deckName);
            deckLabel.setStyle("-fx-text-fill: #0078d4; -fx-font-size: 12;");
            textContent.getChildren().addAll(questionLabel, answerLabel, deckLabel);
        } catch (SQLException e) {
            textContent.getChildren().addAll(questionLabel, answerLabel);
        }

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox buttonBox = new HBox(5);

        if (hasEditCardPermission(card)) {
            Button editButton = new Button();
            editButton.textProperty()
                    .bind(Bindings.createStringBinding(() -> I18n.get("action.edit"), I18n.localeProperty()));
            editButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-padding: 5 10;");
            editButton.setOnAction(e -> editCard(card));
            buttonBox.getChildren().add(editButton);
        }

        if (hasDeleteCardPermission(card)) {
            Button deleteButton = new Button();
            deleteButton.textProperty()
                    .bind(Bindings.createStringBinding(() -> I18n.get("action.delete"), I18n.localeProperty()));
            deleteButton.setStyle("-fx-background-color: #d13438; -fx-text-fill: white; -fx-padding: 5 10;");
            deleteButton.setOnAction(e -> deleteCard(card));
            buttonBox.getChildren().add(deleteButton);
        }

        mainContent.getChildren().addAll(textContent, spacer, buttonBox);
        container.getChildren().add(mainContent);

        return container;
    }

    /**
     * Retrieves the deck name for a given deck ID.
     *
     * @param deckId the ID of the deck
     * @return the deck name, or "Unknown Deck" if not found
     * @throws SQLException if database access fails
     */
    private String getDeckNameById(int deckId) throws SQLException {
        List<Decks> allDecks = decksDao.getAllDecks();
        return allDecks.stream()
                .filter(deck -> deck.getDeck_id() == deckId)
                .map(Decks::getDeck_name)
                .findFirst()
                .orElse("Unknown Deck");
    }

    /**
     * Filters the cards display based on the selected deck in the filter combo box.
     * Shows all cards if "All Decks" is selected, otherwise shows only cards from
     * the selected deck.
     */
    private void filterCardsByDeck() {
        Decks selectedDeck = deckFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedDeck == null)
            return;

        try {
            List<Cards> allCards = cardsDao.getAllCards();
            List<Cards> filteredCards;

            if (selectedDeck.getDeck_id() == -1) {
                filteredCards = allCards;
            } else {
                filteredCards = allCards.stream()
                        .filter(card -> card.getDeck_id() == selectedDeck.getDeck_id())
                        .toList();
            }

            displayCards(filteredCards);
        } catch (SQLException e) {
            showError("Failed to filter cards: " + e.getMessage());
        }
    }

    /**
     * Opens the deck creation dialog.
     */
    private void createNewDeck() {
        try {
            showDeckDialog(null);
        } catch (Exception e) {
            showError("Failed to open deck creation dialog: " + e.getMessage());
        }
    }

    /**
     * Opens the card creation dialog without a preselected deck.
     */
    private void createNewCard() {
        try {
            showCardDialog(null, null);
        } catch (Exception e) {
            showError("Failed to open card creation dialog: " + e.getMessage());
        }
    }

    /**
     * Opens the deck editing dialog for the specified deck.
     *
     * @param deck the deck to edit
     */
    private void editDeck(Decks deck) {
        if (!hasEditPermission(deck)) {
            showError("You do not have permission to edit this deck.");
            return;
        }
        try {
            showDeckDialog(deck);
        } catch (Exception e) {
            showError("Failed to open deck editing dialog: " + e.getMessage());
        }
    }

    /**
     * Opens the card editing dialog for the specified card.
     *
     * @param card the card to edit
     */
    private void editCard(Cards card) {
        if (!hasEditCardPermission(card)) {
            showError("You do not have permission to edit this card.");
            return;
        }
        try {
            showCardDialog(card, null);
        } catch (Exception e) {
            showError("Failed to open card editing dialog: " + e.getMessage());
        }
    }

    /**
     * Opens the card creation dialog with a preselected deck.
     *
     * @param deck the deck to preselect in the card creation dialog
     */
    private void createCardForDeck(Decks deck) {
        try {
            showCardDialog(null, deck);
        } catch (Exception e) {
            showError("Failed to open card creation dialog: " + e.getMessage());
        }
    }

    /**
     * Deletes the specified deck after confirmation.
     * Also deletes all cards in the deck (soft delete).
     *
     * @param deck the deck to delete
     */
    private void deleteDeck(Decks deck) {
        if (!hasDeletePermission()) {
            showError("You do not have permission to delete decks.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Deck");
        alert.setHeaderText("Are you sure you want to delete this deck?");
        alert.setContentText("This will also delete all cards in the deck: " + deck.getDeck_name());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                decksDao.isDeleted(true, deck.getDeck_id());
                loadDecks();
                loadCards();
                setupDeckFilter();
                showInfo("Deck deleted successfully");
            } catch (SQLException e) {
                showError("Failed to delete deck: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes the specified card after confirmation.
     *
     * @param card the card to delete
     */
    private void deleteCard(Cards card) {
        if (!hasDeleteCardPermission(card)) {
            showError("You do not have permission to delete this card.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Card");
        alert.setHeaderText("Are you sure you want to delete this card?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                cardsDao.isDeleted(true, card.getCard_id());
                filterCardsByDeck();
                showInfo("Card deleted successfully");
            } catch (SQLException e) {
                showError("Failed to delete card: " + e.getMessage());
            }
        }
    }

    /**
     * Displays an error alert dialog.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an information alert dialog.
     *
     * @param message the information message to display
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Checks if the current user has permission to edit the specified deck.
     *
     * @param deck the deck to check permissions for
     * @return true if the user can edit the deck, false otherwise
     */
    private boolean hasEditPermission(Decks deck) {
        String role = Session.getInstance().getRole();
        int userId = Session.getInstance().getUserId();

        if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
            return true;
        }

        return "student".equalsIgnoreCase(role) && deck.getUser_id() == userId;
    }

    /**
     * Checks if the current user has permission to edit the specified card.
     *
     * @param card the card to check permissions for
     * @return true if the user can edit the card, false otherwise
     */
    private boolean hasEditCardPermission(Cards card) {
        String role = Session.getInstance().getRole();
        int userId = Session.getInstance().getUserId();

        if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
            return true;
        }

        if ("student".equalsIgnoreCase(role)) {
            try {
                Decks deck = decksDao.getDeckById(card.getDeck_id());
                return deck != null && deck.getUser_id() == userId;
            } catch (SQLException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Checks if the current user has permission to delete decks.
     *
     * @return true if the user can delete decks, false otherwise
     */
    private boolean hasDeletePermission() {
        String role = Session.getInstance().getRole();
        return "admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role);
    }

    /**
     * Checks if the current user has permission to delete the specified card.
     *
     * @param card the card to check permissions for
     * @return true if the user can delete the card, false otherwise
     */
    private boolean hasDeleteCardPermission(Cards card) {
        String role = Session.getInstance().getRole();
        int userId = Session.getInstance().getUserId();

        // Admin and teacher can delete any card
        if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
            return true;
        }

        // Student can delete cards in their own decks
        if ("student".equalsIgnoreCase(role)) {
            try {
                Decks deck = decksDao.getDeckById(card.getDeck_id());
                return deck != null && deck.getUser_id() == userId;
            } catch (SQLException e) {
                return false;
            }
        }

        return false;
    }



    /**
     * Shows the deck creation/editing dialog.
     *
     * @param deck the deck to edit, or null for creation
     * @throws IOException if the FXML file cannot be loaded
     */
    private void showDeckDialog(Decks deck) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/deckDialog.fxml"));
        Parent root = loader.load();

        DeckDialogController controller = loader.getController();

        Stage dialogStage = new Stage();
        dialogStage.setTitle(deck == null ? "Create New Deck" : "Edit Deck");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(createDeckButton.getScene().getWindow());
        dialogStage.setResizable(false);

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        controller.setDialogStage(dialogStage);
        controller.setDeck(deck);

        dialogStage.showAndWait();

        if (controller.isOkClicked()) {
            loadDecks();
            setupDeckFilter();
            if (deck == null) {
                loadCards();
            }
        }
    }

    /**
     * Shows the card creation/editing dialog.
     *
     * @param card            the card to edit, or null for creation
     * @param preselectedDeck the deck to preselect, or null for no preselection
     * @throws IOException if the FXML file cannot be loaded
     */
    private void showCardDialog(Cards card, Decks preselectedDeck) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cardDialog.fxml"));
        Parent root = loader.load();

        CardDialogController controller = loader.getController();

        Stage dialogStage = new Stage();
        dialogStage.setTitle(card == null ? "Create New Card" : "Edit Card");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(createCardButton.getScene().getWindow());
        dialogStage.setResizable(false);

        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        controller.setDialogStage(dialogStage);
        controller.setCard(card);

        if (preselectedDeck != null) {
            controller.setPreselectedDeck(preselectedDeck);
        }

        dialogStage.showAndWait();

        if (controller.isOkClicked()) {
            filterCardsByDeck();
        }
    }

    /**
     * Checks if the current user can create cards in the specified deck.
     *
     * @param deck the deck to check permissions for
     * @return true if the user can create cards in the deck, false otherwise
     */
    private boolean canCreateCardsInDeck(Decks deck) {
        String role = Session.getInstance().getRole();
        int userId = Session.getInstance().getUserId();

        if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
            return true;
        }

        if ("student".equalsIgnoreCase(role)) {
            return deck.getUser_id() == userId;
        }

        return false;
    }
}