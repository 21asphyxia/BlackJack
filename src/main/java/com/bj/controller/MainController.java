package com.bj.controller;

import com.bj.MainApplication;
import com.bj.service.GameService;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainController {
    @FXML
    private BorderPane root;
    @FXML
    private StackPane topSide;
    @FXML
    private StackPane bottomSide;
    private int[][] deck;
    private int[][] playerCards;
    private int[][] dealerCards;


    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
        fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.load(), 840, 440);

        // Starting the game
        GameService gameService = new GameService();
        deck = gameService.createDeck();

        // Setting the game scene
        MainApplication.stage.setScene(scene);

        Label shufflingLabel = new Label("Shuffling...");
        shufflingLabel.getStyleClass().add("shuffling-label");
        root.setCenter(shufflingLabel);
        shufflingLabel.setAlignment(Pos.CENTER);

        // Deck image
        ImageView bottomBackCard = (ImageView) topSide.getChildren().get(0);
        ImageView topBackCard = (ImageView) topSide.getChildren().get(1);
        topBackCard.setViewOrder(0);


        Duration rotationDuration = Duration.seconds(0.5);
        double fullRotation = 360;
        RotateTransition rotateTransition = new RotateTransition(rotationDuration, bottomBackCard);
        rotateTransition.setByAngle(fullRotation);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);

        Duration translationDurationX = Duration.seconds(0.5);
        TranslateTransition translateTransitionX = new TranslateTransition(translationDurationX, bottomBackCard);
        translateTransitionX.setByX(-150);
        translateTransitionX.setCycleCount(Timeline.INDEFINITE);
        translateTransitionX.setAutoReverse(true);

        Duration translationDurationY = Duration.seconds(0.5);
        TranslateTransition translateTransitionY = new TranslateTransition(translationDurationY, bottomBackCard);
        translateTransitionY.setByY(150);
        translateTransitionY.setCycleCount(Timeline.INDEFINITE);
        translateTransitionY.setAutoReverse(true);

        Timeline toBackTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            bottomBackCard.setViewOrder(1);
        }));

        Timeline toFrontTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            bottomBackCard.setViewOrder(-1);
        }));

        SequentialTransition sequence = new SequentialTransition(toFrontTimeline, toBackTimeline);
        sequence.setCycleCount(Timeline.INDEFINITE);

        rotateTransition.play();
        translateTransitionX.play();
        translateTransitionY.play();
        sequence.play();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            rotateTransition.stop();
            translateTransitionX.stop();
            translateTransitionY.stop();
            sequence.stop();
        }));
        timeline.setOnFinished(event -> {
            deck = gameService.shuffleDeck(deck);
            System.out.println(Arrays.deepToString(deck));
            int[][][] drawnCardsAndDeck = gameService.drawNCards(deck, 4);
            int[][] drawnCards = drawnCardsAndDeck[0];
            deck = drawnCardsAndDeck[1];
            playerCards = new int[2][2];
            dealerCards = new int[2][2];
            playerCards = new int[][]{drawnCards[0], drawnCards[1]};
            dealerCards = new int[][]{drawnCards[2], drawnCards[3]};
            System.out.println(Arrays.deepToString(playerCards));
            System.out.println(Arrays.deepToString(dealerCards));
            HBox centerSide = new HBox();
            centerSide.setAlignment(Pos.CENTER);
            ButtonBar buttonBar = new ButtonBar();

            Button hitButton = new Button("Hit");
            hitButton.getStyleClass().add("hit-button");

            Button standButton = new Button("Stand");
            standButton.getStyleClass().add("stand-button");
            buttonBar.getButtons().addAll(standButton, hitButton);
            centerSide.getChildren().add(buttonBar);
            root.setCenter(centerSide);

            ImageView cardBack = getImageScene(new int[]{0, 0});
            cardBack.setTranslateX(-30);
            topSide.getChildren().add(cardBack);


            ImageView dealerCard = getImageScene(dealerCards[1]);
            topSide.getChildren().add(dealerCard);

            int i = -30;
            for (int[] card : playerCards) {
                ImageView imageView = getImageScene(card);
                imageView.setTranslateX(i);
                bottomSide.getChildren().add(imageView);
                i += 30;
            }

        });
        timeline.play();


//
//        int i = 60;
    }

    public ImageView getImageScene(int[] card) {
        try {
            InputStream stream;
            if (card[0] == 0 && card[1] == 0) {
                stream = new FileInputStream("src/main/resources/images/cards/card_back.png");
            } else {
                stream = new FileInputStream("src/main/resources/images/cards/" + card[0] + "-" + card[1] + ".png");
            }
            Image image = new Image(stream);
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            //Setting the image view parameters
            imageView.setX(100);
            imageView.setY(100);
            imageView.setEffect((new javafx.scene.effect.DropShadow()));
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}