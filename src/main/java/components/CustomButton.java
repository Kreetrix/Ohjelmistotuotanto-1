package components;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomButton extends Button {

    private ImageView imageView;

    public CustomButton() {
        setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff;");

        imageView = new ImageView();
        imageView.setFitHeight(47);
        imageView.setFitWidth(47);
        setGraphic(imageView);
        setTooltip(new Tooltip(""));
    }

    public void setIcon(String resourcePath) {
        Image image = new Image(getClass().getResourceAsStream(resourcePath));
        imageView.setImage(image);
    }

    public void setTooltipText(String text) {
        getTooltip().setText(text);
    }
}
