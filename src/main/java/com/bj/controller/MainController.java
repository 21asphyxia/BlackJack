package com.bj.controller;

import com.bj.MainApplication;
import com.bj.service.GameService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {
    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 440);

        GameService gameService = new GameService();
        gameService.startGame();
        MainApplication.stage.setScene(scene);
        Label shufflingLabel = new Label("Shuffling...");
//        shufflingLabel
        BorderPane root = fxmlLoader.getRoot();
        root.setCenter(new Label("Hello World!"));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                root.setCenter(null);
        }));
        timeline.play();
        gameService.shuffleDeck(gameService.deck);


//        BorderPane root = fxmlLoader.getRoot();
//        StackPane stackPane = (StackPane) root.getTop();
//        int i = 60;
//        for (int[] drawnCard : drawnCards) {
//            ImageView imageView = gameService.getImageScene(drawnCard);
//            imageView.setTranslateX(i);
//            stackPane.getChildren().add(imageView);
//            i += 30;
//        }
    }
}