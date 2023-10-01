package com.bj.service;

public class GameService {

    private int[][] nextCards(int[] card) {
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
        return deck;
    }

    public int[][] createDeck() {
        int[] card = new int[]{1, 1};
        int[][] nextCards = nextCards(card);
        int[][] deck = new int[nextCards.length + 1][2];
        deck[0] = card;
        System.arraycopy(nextCards, 0, deck, 1, 51);
        return deck;
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

    public int[][] shuffleDeck(int[][] deck) {
        int[][] shuffledDeck = new int[deck.length][2];
        int deckLength = deck.length;
        for (int i = 0; i < deckLength; i++) {
            int[][][] drawnCard = drawRandomCard(deck);
            shuffledDeck[i] = drawnCard[0][0];
            deck = drawnCard[1];
        }
        return shuffledDeck;
    }

    public int[][][] drawNCards(int[][] deck, int n) {
        int[][] drawnCards = new int[n][2];
        for (int i = 0; i < n; i++) {
            int[][][] cardAndDeck = drawNthCard(deck, 0);
            int[] drawnCard = cardAndDeck[0][0];
            drawnCards[i] = drawnCard;
            deck = cardAndDeck[1];
        }
        return new int[][][]{drawnCards, deck};
    }

}
