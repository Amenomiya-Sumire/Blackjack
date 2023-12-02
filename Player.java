package blackjack;

import java.util.ArrayList;

public class Player {
  private final ArrayList<Card> hand = new ArrayList<>();
  private int score;
  private final String name;

  public Player(String name) {
    this.name = name;
    this.score = 0;
  }

  public ArrayList<Card> getHand() {
    return hand;
  }

  public int getScore() {
    return score;
  }

  public String getName() {
    return name;
  }

  public void addCard(Card card) {
    if (card != null) {
      hand.add(card);
      calculateScore();
    }
  }

  public void calculateScore() {
    int newScore = 0;
    int aceCount = 0;

    for (Card card : hand) {
      int cardValue = card.getValue();
      if (cardValue == 1) { // Ace
        aceCount++;
        newScore += 11; // Initially consider Ace as 11
      } else {
        newScore += cardValue;
      }
    }

    while (newScore > 21 && aceCount > 0) {
      newScore -= 10; // Convert an Ace from 11 to 1
      aceCount--;
    }

    this.score = newScore;
  }
}
