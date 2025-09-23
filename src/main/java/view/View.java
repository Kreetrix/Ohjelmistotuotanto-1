package view;

import java.awt.Dimension;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 3;
		double height = screenSize.getHeight() / 1.2;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), width, height);
        stage.setScene(scene);
        stage.setTitle("CARDS MEMO GAME");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
