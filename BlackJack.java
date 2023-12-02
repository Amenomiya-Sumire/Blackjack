package blackjack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class BlackJack {
  private static final ArrayList<Card> deck = new ArrayList<>();
  public static final ArrayList<Player> players = new ArrayList<>();
  public static String winnerMessage;

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(BlackJackGUI::new);

    reset();
  }

  public static ArrayList<Card> getDeck() {
    return deck;
  }

  public static void init() {
    initializeDeck(1);
    initializeDeck(2);
    initializeDeck(3);
    initializeDeck(4);
  }

  public static void initializeDeck(int suit) {
    String suitName = Card.getSuitName(suit); // 使用 Card 类的方法获取花色名称

    for (int i = 1; i <= 13; i++) {
      String rank = getRankName(i); // 使用一个新的方法来获取面值名称
      int value = Math.min(i, 10); // J, Q, K 的值为 10
      deck.add(new Card(rank, value, suitName, true));
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

  public static void reset() {
    init();
    Collections.shuffle(deck);

    players.clear();

    for (int i = 1; i <= 4; i++) {
      players.add(new Player("Player " + i));
    }

    for (Player player : players) {
      player.addCard(deck.remove(0));
    }

    SwingUtilities.invokeLater(
        () -> {
          BlackJackGUI.updateGameInterface(players); // 假设您有这样的方法在 BlackJackGUI 类中
        });
  }

  public static void decideWinner() {
    Player winner = null;
    boolean isTie = false;
    int highestScore = 0;
    int maxCards = 0;

    // 找出得分最高的玩家
    for (Player player : players) {
      int score = player.getScore();
      if (score > highestScore && score <= 21) {
        highestScore = score;
        winner = player;
        maxCards = player.getHand().size();
        isTie = false; // 重置平局标志
      } else if (score == highestScore) {
        isTie = true; // 标记存在平局情况
        // 比较手牌数量eiy
        if (player.getHand().size() > maxCards) {
          winner = player;
          maxCards = player.getHand().size();
          isTie = false; // 如果找到手牌更多的玩家，则不是平局
        }
      }
    }

    // 判断结果
    if (highestScore == 0) {
      winnerMessage = "No winner in this round.";
    } else if (isTie) {
      winnerMessage = "It's a tie!";
    } else {
      winnerMessage = "Winner is " + winner.getName() + " with score " + highestScore;
    }
  }
}
