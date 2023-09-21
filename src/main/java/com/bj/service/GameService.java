package com.bj.service;

import java.util.Arrays;

public class GameService {
    public int[][] deck;

    public void startGame() {
        createDeck();
        System.out.println(Arrays.deepToString(this.deck));
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

}
