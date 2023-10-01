package com.bj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        MainApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 440);
        stage.setTitle("Aouad Casino");
        stage.setScene(scene);
//        stage.getIcons().add(new javafx.scene.image.Image("/images/icon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}