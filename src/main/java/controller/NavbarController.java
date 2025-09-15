package controller;

import components.CustomButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavbarController {

    @FXML
    private Button searchBtn;

    @FXML
    private Button studyBtn;

    @FXML
    private CustomButton listBtn;

    @FXML
    private void initialize() {
        searchBtn.setOnAction(e -> System.out.println("Search clicked"));
        listBtn.setIcon("/icons/list.png");
        listBtn.setTooltipText("List");
        listBtn.setOnAction(e -> System.out.println("List clicked"));
        studyBtn.setOnAction(e -> System.out.println("Study clicked"));
    }
}