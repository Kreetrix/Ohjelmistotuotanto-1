package components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class MenuItemButton extends VBox {

    private final SVGPath mainIcon;
    private final Label mainText;
    private final Label subText;
    private final SVGPath arrowIcon;
    private final HBox content;

    public MenuItemButton() {
        setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #333333; -fx-border-width: 1;");
        setSpacing(0);

        content = new HBox(10);
        content.setStyle("-fx-padding: 5;");
        content.setMinHeight(105);
        content.setMaxHeight(105);
        content.setAlignment(Pos.CENTER);

        // Icon on the left
        mainIcon = new SVGPath();
        mainIcon.setContent(IconManager.getPath("book"));
        mainIcon.setFill(Color.CYAN);
        mainIcon.setScaleX(2);
        mainIcon.setScaleY(2);

        // Margin for icon itself
        HBox.setMargin(mainIcon, new Insets(20));

        // Text part top
        mainText = new Label("placeholder");
        mainText.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 24;");

        subText = new Label("placeholder");
        subText.setStyle("-fx-text-fill: #a9a9a9; -fx-font-size: 16;");

        VBox textBox = new VBox(2, mainText, subText);
        textBox.setAlignment(Pos.CENTER_LEFT);

        // Right arrow
        arrowIcon = new SVGPath();
        arrowIcon.setContent(IconManager.getPath("arrowRight"));
        arrowIcon.setFill(Color.web("#757575"));
        arrowIcon.setScaleX(3);
        arrowIcon.setScaleY(3);

        // Just here to push the right icon to the righ
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(arrowIcon, new Insets(20));

        content.getChildren().addAll(mainIcon, textBox, spacer, arrowIcon);

        ThinLine separator = new ThinLine();

        getChildren().addAll(content, separator);

        hover();
    }

    private void hover() {
        content.setOnMouseEntered(e -> {
            content.setStyle(
                    "-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #333333; -fx-border-width: 1;");
            mainText.setTextFill(Color.LIGHTBLUE);
            subText.setTextFill(Color.LIGHTGRAY);
        });

        content.setOnMouseExited(e -> {
            content.setStyle(
                    "-fx-background-color: #1a1a1a; -fx-padding: 5; -fx-border-color: #333333; -fx-border-width: 1;");
            mainText.setTextFill(Color.DEEPSKYBLUE);
            subText.setTextFill(Color.GRAY);
        });
    }

    public void setOnAction(EventHandler<ActionEvent> handler) {
        content.setOnMouseClicked(e -> {
            if (e.isStillSincePress()) {
                handler.handle(new ActionEvent(this, null));
            }
        });
    }

    public void setIcon(String icon) {
        mainIcon.setContent(IconManager.getPath(icon));
    }

    public void setMainText(String text) {
        mainText.setText(text);
    }

    public void setSubText(String text) {
        subText.setText(text);
    }
}
