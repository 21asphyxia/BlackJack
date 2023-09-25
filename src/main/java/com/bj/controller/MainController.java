package com.bj.controller;

import com.bj.MainApplication;
import com.bj.service.GameService;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.geometry.Insets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MainController {
    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGamee.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 440);
        BorderPane borderPane = fxmlLoader.getRoot();

        // Starting the game

        GameService gameService = new GameService();
        gameService.startGame();


        // Setting the game scene
        MainApplication.stage.setScene(scene);

        // Deck images
        StackPane stackPane = (StackPane) borderPane.getTop().lookup("#test");
        System.out.println(stackPane);
        BorderPane.setMargin(stackPane, new Insets(10, 0, 0, 0));

        InputStream stream = new FileInputStream("src/main/resources/images/cards/card_back.png");
        Image image = new Image(stream);
        Label shufflingLabel = new Label("Shuffling...");
        shufflingLabel.getStyleClass().add("shuffling-label");
        ImageView card = new ImageView(image);
        card.setFitWidth(100);
        card.setPreserveRatio(true);
//        card.setLayoutX(100);
//        card.setLayoutY(100);
        stackPane.getChildren().add(card);
//        stackPane.setId("game");
//        stackPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
//        scene.setRoot(stackPane);

        // Animations
        Duration rotationDuration = Duration.seconds(0.1);
        double fullRotation = 360.0;
        javafx.animation.RotateTransition rotateTransition = new javafx.animation.RotateTransition(rotationDuration, card);
        rotateTransition.setByAngle(fullRotation);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);

        Duration translationDurationX = Duration.seconds(1);
        TranslateTransition translateTransitionX = new TranslateTransition(translationDurationX, card);
        translateTransitionX.setByX(-200);
        translateTransitionX.setCycleCount(Timeline.INDEFINITE);
        translateTransitionX.setAutoReverse(true);

        Duration translationDurationY = Duration.seconds(1);
        TranslateTransition translateTransitionY = new TranslateTransition(translationDurationY, card);
        translateTransitionY.setByY(-200);
        translateTransitionY.setCycleCount(Timeline.INDEFINITE);
        translateTransitionY.setAutoReverse(true);

        rotateTransition.play();
        translateTransitionX.play();
        translateTransitionY.play();


        borderPane.setCenter(shufflingLabel);
        shufflingLabel.setAlignment(Pos.CENTER);
//        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
//                root.setCenter(null);
//        }));
//        timeline.play();
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