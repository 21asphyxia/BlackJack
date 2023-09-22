package com.bj.controller;

import com.bj.MainApplication;
import com.bj.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 440);

        GameService gameService = new GameService();
        int[][] drawnCards = gameService.startGame();
//        BorderPane root = fxmlLoader.getRoot();
//        StackPane stackPane = (StackPane) root.getTop();
//        int i = 60;
//        for (int[] drawnCard : drawnCards) {
//            ImageView imageView = gameService.getImageScene(drawnCard);
//            imageView.setTranslateX(i);
//            stackPane.getChildren().add(imageView);
//            i += 30;
//        }
//        MainApplication.stage.setScene(scene);
    }
}