package blackjack;

public class Card {
  private final String mask;
  private final int value;
  private final String suit;
  private final boolean isShow;

  public Card(String mask, int value, String suit, boolean isShow) {
    this.mask = mask;
    this.value = value;
    this.suit = suit;
    this.isShow = isShow;
  }

  public String generateImagePath() {
    return "/images/" + mask + "_of_" + suit + ".png";
  }

  public boolean isShow() {
    return isShow;
  }

  public int getValue() {
    return value;
  }

  public static String getSuitName(int suit) {
    return switch (suit) {
      case 1 -> "hearts";
      case 2 -> "diamonds";
      case 3 -> "clubs";
      case 4 -> "spades";
      default -> "unknown";
    };
  }
}
