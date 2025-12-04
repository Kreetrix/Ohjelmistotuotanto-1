package components;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * A custom button component that displays an icon image and supports tooltips.
 *
 */

///TODO Replace PNG with SVG
public class CustomButton extends Button {

    private final ImageView imageView;

    /**
     * Constructs a new CustomButton with transparent background and default styling.
     * Initializes the image view for icons and sets up an empty tooltip.
     */
    public CustomButton() {
        // Set button styling - transparent background with white text
        setStyle("-fx-background-color: transparent; -fx-text-fill: #ffffff;");

        // Initialize and configure the image view for icons
        imageView = new ImageView();
        imageView.setFitHeight(47);
        imageView.setFitWidth(47);
        setGraphic(imageView);

        // Initialize with empty tooltip
        setTooltip(new Tooltip(""));
    }

    /**
     * Sets the icon for the button from the specified resource path.
     *
     * @param resourcePath the path to the image resource within the classpath
     * @throws NullPointerException if the resource cannot be found
     */
    public void setIcon(String resourcePath) {
        Image image = new Image(getClass().getResourceAsStream(resourcePath));
        imageView.setImage(image);
    }
    
    public void setSvgIcon(String resourcePath, String color, int size) {
        SVGPath svg = new SVGPath();
        svg.setContent(IconManager.getPath(resourcePath));
        svg.setFill(Color.web(color));
        svg.setScaleX(size);
        svg.setScaleY(size);

        svg.setTranslateX(0);
        svg.setTranslateY(0);

        setGraphic(svg);
    }

    /**
     * Sets the tooltip text that appears when hovering over the button.
     *
     * @param text the text to display in the tooltip
     */
    public void setTooltipText(String text) {
        getTooltip().setText(text);
    }
}