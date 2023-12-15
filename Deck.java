package blackjack;

import java.util.Random;

public class Deck {
  private final Card[] cards = new Card[52];
  private final Card[] cardsArray = new Card[52];
  private int pointer;
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
        Card aCard = new Card(rankName, value, suitName, true);
        cardsArray[(suit-1)*13+rank-1] = aCard;
      }
    }
    pointer = 0;
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
	System.arraycopy(cardsArray, 0, cards, 0, 52);
	Random rand = new Random();
	pointer = 0;
	for(int i = 51;i>0;i--) {
		int index = rand.nextInt(52);
		Card temp = cards[index];
		cards[index] = cards[i];
		cards[i]=temp;
	}
	  
  }

  public Card drawCard() {
	
	if(pointer>=52)
		return null;
	return(cards[pointer++]); 
  }
}
