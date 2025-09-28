package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class LogoutController {
    Session session = Session.getInstance();
    Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth() / 3;
    double height = screenSize.getHeight() / 1.2;
    public void onLogout() throws IOException {
        session.clear();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
        Parent loginRoot = loginLoader.load();
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loginRoot, width, height));
        loginStage.setTitle("Login");
        loginStage.show();
    }
}
