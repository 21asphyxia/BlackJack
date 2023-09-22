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
        int[] drawnCard = gameService.startGame();
        ImageView imageView = gameService.getImageScene(drawnCard);
        imageView.setTranslateX(40);
        BorderPane root = fxmlLoader.getRoot();
        StackPane stackPane = (StackPane) root.getTop();
        stackPane.getChildren().add(imageView);
        MainApplication.stage.setScene(scene);
    }
}