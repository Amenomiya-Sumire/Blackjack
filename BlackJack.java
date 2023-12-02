package blackjack;

import java.util.ArrayList;
import javax.swing.*;

public class BlackJack {
  private static final Deck deck = new Deck();
  public static final ArrayList<Player> players = new ArrayList<>();
  public static String winnerMessage;

  public static Deck getDeck() {
    return deck;
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(BlackJackGUI::new);

    reset();
  }

  public static void reset() {
    deck.shuffle();
    players.clear();

    for (int i = 1; i <= 4; i++) {
      players.add(new Player("Player " + i));
    }

    for (Player player : players) {
      player.addCard(deck.drawCard());
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
