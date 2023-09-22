package com.bj.service;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class GameService {
    public int[][] deck;

    public int[] startGame() {
        createDeck();
        return drawRandomCard(deck)[0][0];
    }

    public Scene getImageScene(int[] card) throws FileNotFoundException {
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/cards/" + card[0] + "-" + card[1] + ".png");
            Image image = new Image(stream);
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            //Setting the image view parameters
            imageView.setX(10);
            imageView.setY(10);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            //Setting the Scene object
            Group root = new Group(imageView);
            return new Scene(root, 595, 370);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int[][] nextCards(int[] card) {
        int type = card[1];
        int number = card[0];
        int cardCount = 0;
        int numberOfCards = Math.min(52 - ((13 * (card[1] - 1)) + card[0]), 52);
        int[][] deck = new int[numberOfCards][2];
        for (int i = type; i <= 4; i++) {
            if (number == 13 || number == 0) {
                number = 1;
                continue;
            }
            for (int j = number; j <= 13; j++) {
                deck[cardCount][0] = j;
                deck[cardCount][1] = i;
                cardCount++;
            }
        }
        return deck;
    }

    private void createDeck() {
        this.deck = nextCards(new int[]{0, 0});
    }

    private int[][][] drawNthCard(int[][] deck, int n) {
        int[][] newDeck = new int[deck.length - 1][2];
        int[][] drawnCard = new int[1][2];
        for (int i = 0, j = 0; i < deck.length; i++) {
            if (i == n) {
                drawnCard[0] = deck[i];
            } else {
                newDeck[j] = deck[i];
                j++;
            }
        }
        return new int[][][]{drawnCard, newDeck};
    }

    public int[][][] drawRandomCard(int[][] deck) {
        int randomCard = (int) (Math.random() * deck.length);
        return drawNthCard(deck, randomCard);
    }

}
