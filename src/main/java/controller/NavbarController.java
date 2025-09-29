package controller;

import components.CustomButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class NavbarController {
    private LogoutController logout = new  LogoutController();
    @FXML
    public CustomButton logoutBtn;

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
        logoutBtn.setIcon("/icons/logout.png");
        logoutBtn.setOnAction(e -> {
            try {
                logout.onLogout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
