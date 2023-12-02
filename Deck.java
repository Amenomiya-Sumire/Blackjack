package blackjack;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
  private final ArrayList<Card> cards = new ArrayList<>();

  public Deck() {
    initialize();
    shuffle();
  }

  private void initialize() {
    for (int suit = 1; suit <= 4; suit++) {
      for (int rank = 1; rank <= 13; rank++) {
        String suitName = Card.getSuitName(suit);
        String rankName = getRankName(rank);
        int value = Math.min(rank, 10);
        cards.add(new Card(rankName, value, suitName, true));
      }
    }
  }

  private static String getRankName(int number) {
    return switch (number) {
      case 1 -> "ace";
      case 11 -> "jack";
      case 12 -> "queen";
      case 13 -> "king";
      default -> String.valueOf(number);
    };
  }

  public void shuffle() {
    Collections.shuffle(cards);
  }

  public Card drawCard() {
    return cards.isEmpty() ? null : cards.remove(0);
  }
}
