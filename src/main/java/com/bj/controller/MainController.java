package com.bj.controller;

import com.bj.service.GameService;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.bj.MainApplication.stage;

public class MainController {
    private GameService gameService;
    @FXML
    private BorderPane root;
    @FXML
    private StackPane topSide;
    @FXML
    private HBox centerSide;
    @FXML
    private StackPane bottomSide;
    @FXML
    private ImageView topBackCard;
    @FXML
    private ImageView bottomBackCard;
    @FXML
    private VBox centerBox;

    private int[][] deck;
    private int[][] playerCards;
    private int[][] dealerCards;


    @FXML
    protected void onStartButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/startGame.fxml"));
        fxmlLoader.setController(this);
        // Setting the game scene
        stage.getScene().setRoot(fxmlLoader.load());
        startGame();
    }

    @FXML
    public void initialize() {
        if (topBackCard != null && bottomBackCard != null) {
            topBackCard.fitHeightProperty().bind(stage.heightProperty().divide(3).subtract(20));
            bottomBackCard.fitHeightProperty().bind(stage.heightProperty().divide(3).subtract(20));
        }
        gameService = new GameService();
    }

    public void startGame() {
        playerCards = new int[2][2];
        dealerCards = new int[2][2];
        bottomSide.getChildren().clear();
        topSide.getChildren().setAll(bottomBackCard, topBackCard);

        GameService gameService = new GameService();
        deck = gameService.createDeck();

        setShufflingLabel();

        Timeline timeline = setAnimations();

        timeline.setOnFinished(event -> {
            deck = gameService.shuffleDeck(deck);
            int[][][] drawnCardsAndDeck = gameService.drawNCards(deck, 4);
            int[][] drawnCards = drawnCardsAndDeck[0];
            deck = drawnCardsAndDeck[1];
            playerCards = new int[][]{drawnCards[0], drawnCards[1]};
            dealerCards = new int[][]{drawnCards[2], drawnCards[3]};
            setPlayButtons();
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
            Label sumLabel = new Label(String.valueOf(gameService.getSum(playerCards)));
            sumLabel.getStyleClass().add("sum-label");
            sumLabel.translateXProperty().bind(bottomSide.widthProperty().multiply(-0.15).subtract(30));
            bottomSide.getChildren().add(0, sumLabel);
        });
        timeline.play();
    }

    private void setPlayButtons() {
        Button hitButton = new Button("Hit");
        hitButton.getStyleClass().add("hit-button");
        hitButton.setOnAction(e -> {
            int[][][] drawnCardAndDeck = gameService.drawNCards(deck, 1);
            int[] drawnCard = drawnCardAndDeck[0][0];
            deck = drawnCardAndDeck[1];
            playerCards = gameService.addCard(playerCards, drawnCard);
            Label sumLabel = (Label) bottomSide.getChildren().get(0);
            sumLabel.setText(String.valueOf(gameService.getSum(playerCards)));

            System.out.println(Arrays.deepToString(playerCards));
            ImageView imageView = getImageScene(drawnCard);
            imageView.setTranslateX((bottomSide.getChildren().size() - 1) * 30 - 30);
            bottomSide.getChildren().add(imageView);
            if (gameService.didBust(playerCards)) {
                setLoserNotification();
            }
        });

        Button standButton = new Button("Stand");
        standButton.getStyleClass().add("stand-button");
        standButton.setOnAction(e -> {
            hitButton.setDisable(true);
            standButton.setDisable(true);
            ImageView backCard = (ImageView) topSide.getChildren().get(2);
            try {
                backCard.setImage(new Image(new FileInputStream("src/main/resources/images/cards/" + dealerCards[0][0] + "-" + dealerCards[0][1] + ".png")));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            Label sumLabel = new Label(String.valueOf(gameService.getSum(dealerCards)));
            sumLabel.getStyleClass().add("sum-label");
            sumLabel.translateXProperty().bind(topSide.widthProperty().multiply(-0.15).subtract(30));
            topSide.getChildren().add(0, sumLabel);
            Timeline timeline = new Timeline();
            KeyFrame kf = new KeyFrame(Duration.seconds(0.5), event -> {
                int dealerSum = gameService.getSum(dealerCards);
                int playerSum = gameService.getSum(playerCards);
                if (gameService.didBust(dealerCards)) {
                    setWinnerNotification();
                    timeline.stop();
                    return;
                }
                if (gameService.getSum(dealerCards) > 16) {
                    if (dealerSum > playerSum) {
                        setLoserNotification();
                    } else if (dealerSum < playerSum) {
                        setWinnerNotification();
                    } else {
                        setDrawNotification();
                    }
                    timeline.stop();
                    return;
                }
                int[][][] drawnCardAndDeck = gameService.drawNCards(deck, 1);
                int[] drawnCard = drawnCardAndDeck[0][0];
                deck = drawnCardAndDeck[1];
                dealerCards = gameService.addCard(dealerCards, drawnCard);
                ImageView imageView = getImageScene(drawnCard);
                imageView.setTranslateX((topSide.getChildren().size() - 2) * 30 - 30);
                topSide.getChildren().add(imageView);
                sumLabel.setText(String.valueOf(gameService.getSum(dealerCards)));
            });
            timeline.getKeyFrames().add(kf);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();


        });
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(standButton, hitButton);
        centerSide.getChildren().setAll(buttonBar);
        centerBox.getChildren().setAll(centerSide);
    }

    private void setDrawNotification() {
        Label lostLabel = new Label("Draw");
        lostLabel.getStyleClass().add("shuffling-label");
        centerBox.getChildren().add(0, lostLabel);

        Button playAgainButton = new Button("Play Again");
        playAgainButton.getStyleClass().add("hit-button");
        playAgainButton.setOnAction(event -> {
            startGame();
        });
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(playAgainButton);
        centerSide.getChildren().setAll(buttonBar);
    }

    private Timeline setAnimations() {
        Duration rotationDuration = Duration.seconds(0.5);
        double fullRotation = 360;
        RotateTransition rotateTransition = new RotateTransition(rotationDuration, bottomBackCard);
        rotateTransition.setByAngle(fullRotation);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);

        Duration translationDurationX = Duration.seconds(0.3);
        TranslateTransition translateTransitionX = new TranslateTransition(translationDurationX, bottomBackCard);
        translateTransitionX.setByX(bottomBackCard.fitHeightProperty().multiply(-0.8).get());
        translateTransitionX.setCycleCount(Timeline.INDEFINITE);
        translateTransitionX.setAutoReverse(true);

        Duration translationDurationY = Duration.seconds(0.5);
        TranslateTransition translateTransitionY = new TranslateTransition(translationDurationY, bottomBackCard);
        translateTransitionY.setByY(150);
        translateTransitionY.setCycleCount(Timeline.INDEFINITE);
        translateTransitionY.setAutoReverse(true);

        Timeline toBackTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            bottomBackCard.setViewOrder(1);
        }));

        Timeline toFrontTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            bottomBackCard.setViewOrder(-1);
        }));

        SequentialTransition sequence = new SequentialTransition(toFrontTimeline, toBackTimeline);
        sequence.setCycleCount(Timeline.INDEFINITE);

        translateTransitionX.play();

        sequence.play();

        return new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            rotateTransition.stop();
            translateTransitionX.stop();
            translateTransitionY.stop();
            sequence.stop();
        }));
    }

    private void setShufflingLabel() {
        Label shufflingLabel = new Label("Shuffling...");
        shufflingLabel.getStyleClass().add("shuffling-label");
        shufflingLabel.setAlignment(Pos.CENTER);
        centerBox.getChildren().setAll(shufflingLabel);
    }

    private void setLoserNotification() {
        Label lostLabel = new Label("zreg hh");
        lostLabel.getStyleClass().add("shuffling-label");
        centerBox.getChildren().add(0, lostLabel);

        Button playAgainButton = new Button("Play Again");
        playAgainButton.getStyleClass().add("hit-button");
        playAgainButton.setOnAction(event -> {
            startGame();
        });
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(playAgainButton);
        centerSide.getChildren().setAll(buttonBar);
    }

    private void setWinnerNotification() {
        Label lostLabel = new Label("m9wd hh");
        lostLabel.getStyleClass().add("shuffling-label");
        centerBox.getChildren().add(0, lostLabel);

        Button playAgainButton = new Button("Play Again");
        playAgainButton.getStyleClass().add("hit-button");
        playAgainButton.setOnAction(event -> {
            startGame();
        });
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(playAgainButton);
        centerSide.getChildren().setAll(buttonBar);
    }

    private ImageView getImageScene(int[] card) {
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
            imageView.setEffect((new javafx.scene.effect.DropShadow()));
            imageView.fitHeightProperty().bind(stage.heightProperty().divide(3).subtract(20));
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}