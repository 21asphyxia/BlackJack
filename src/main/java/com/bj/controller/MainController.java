package com.bj.controller;

import com.bj.MainApplication;
import com.bj.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class MainController {
    @FXML
    protected void onStartButtonClick() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
//         Scene scene = new Scene(fxmlLoader.load(), 612, 408);

        GameService gameService = new GameService();
        int[] drawnCard = gameService.startGame();
        Scene scene = gameService.getImageScene(drawnCard);
        MainApplication.stage.setScene(scene);
    }
}