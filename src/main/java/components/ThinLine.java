package components;

import javafx.scene.layout.Region;

/**
 * A thin horizontal line component used as a visual separator.
 *
 */
public class ThinLine extends Region {

    /**
     * Constructs a new ThinLine separator with default styling.
     * The line is 1 pixel high and colored gray (#6E6E6E).
     */
    public ThinLine() {
        // Set fixed height properties to ensure consistent 1px line
        setPrefHeight(1);
        setMinHeight(1);
        setMaxHeight(1);

        // Set visual styling - gray background color
        setStyle("-fx-background-color: #6E6E6E;");
    }
}