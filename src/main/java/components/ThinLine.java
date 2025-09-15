package components;

import javafx.scene.layout.Region;

public class ThinLine extends Region {
    public ThinLine() {
        setPrefHeight(1);
        setMinHeight(1);
        setMaxHeight(1);
        setStyle("-fx-background-color: #6E6E6E;");
    }
}
