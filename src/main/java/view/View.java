package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 440, 956);
        stage.setScene(scene);
        stage.setTitle("CARDS MEMO GAME");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
