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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
         Scene scene = new Scene(fxmlLoader.load(), 612, 408);
         MainApplication.stage.setScene(scene);

        GameService gameService = new GameService();
        gameService.startGame();
    }
}