package com.bj.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class GameService {
    public int[][] deck;

    public int[][] startGame() {
        createDeck();
        shuffleDeck(deck);
        return new int[][]{drawRandomCard(deck)[0][0], drawRandomCard(deck)[0][0]};
    }

    public ImageView getImageScene(int[] card) throws FileNotFoundException {
        try {
            InputStream stream = new FileInputStream("src/main/resources/images/cards/" + card[0] + "-" + card[1] + ".png");
            Image image = new Image(stream);
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            //Setting the image view parameters
            imageView.setX(10);
            imageView.setY(10);
            imageView.setEffect((new javafx.scene.effect.DropShadow()));
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[][] nextCards(int[] card) {
        int type = card[1];
        int number = card[0];
        int cardCount = 0;
        int numberOfCards = Math.min(52 - ((13 * (card[1] - 1)) + card[0]), 52);
        int[][] deck = new int[numberOfCards][2];
        boolean isAce = number == 1;
        boolean isKing = number == 13;
        for (int i = type; i <= 4; i++) {
            for (int j = number + 1; j <= 13; j++, cardCount++) {
                deck[cardCount][0] = j;
                deck[cardCount][1] = i;

            }
            number = 0;
        }
//        System.out.println(Arrays.deepToString(deck));
        return deck;
    }

    private void createDeck() {
        int[] card = new int[]{1, 1};
        int[][] nextCards = nextCards(card);
        int[][] deck = new int[nextCards.length + 1][2];
        deck[0] = card;
        System.arraycopy(nextCards, 0, deck, 1, 51);
        this.deck = deck;
//        System.out.println(Arrays.deepToString(this.deck));
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

    public void shuffleDeck(int[][] deck) {
        int[][] shuffledDeck = new int[deck.length][2];
        int deckLength = deck.length;
        for (int i = 0; i < deckLength; i++) {
            int[][][] drawnCard = drawRandomCard(deck);
            shuffledDeck[i] = drawnCard[0][0];
            deck = drawnCard[1];
            System.out.println((Arrays.toString(drawnCard[0][0])));
        }
        System.out.println(Arrays.deepToString(shuffledDeck));
    }

}
