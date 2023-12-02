package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackJackGame {
  private Deck deck;
  private List<Player> players = new ArrayList<>();
  private int currentPlayerIndex;
  private GameEventListener listener;

  public BlackJackGame() {
    deck = new Deck();
    players = new ArrayList<>();
  }

  public Deck getDeck() {
    return deck;
  }

  public void setGameEventListener(GameEventListener listener) {
    this.listener = listener;
  }

  private void notifyGameOver(String winnerMessage) {
    if (listener != null) {
      listener.onGameOver(winnerMessage);
    }
  }

  public void initializeGame() {
    deck.shuffle();
    players.clear();

    for (int i = 1; i <= 4; i++) {
      players.add(new Player("Player " + i));
    }

    dealInitialCards();
    currentPlayerIndex = 0;
  }

  private void dealInitialCards() {
    for (Player player : players) {
      player.addCard(deck.drawCard());
    }
  }

  public void hit() {
    if (currentPlayerIndex >= players.size()) {
      return;
    }

    Player currentPlayer = players.get(currentPlayerIndex);
    currentPlayer.addCard(deck.drawCard());
    boolean isBusted = currentPlayer.isBusted();
    
    if (listener != null) {
      listener.onPlayerHit(currentPlayer, isBusted);
    }

    if (isBusted) {
      moveToNextPlayer();
    }
  }

  public void stand() {
    Player currentPlayer = players.get(currentPlayerIndex);
    currentPlayer.stand();
    moveToNextPlayer();
  }

  public String decideWinner() {
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
        // 比较手牌数量
        if (player.getHand().size() > maxCards) {
          winner = player;
          maxCards = player.getHand().size();
          isTie = false; // 如果找到手牌更多的玩家，则不是平局
        }
      }
    }

    // 判断结果
    if (highestScore == 0) {
      return "No winner in this round.";
    } else if (isTie) {
      return "It's a tie!";
    } else {
      return "Winner is " + winner.getName() + " with score " + highestScore;
    }
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public boolean isGameOver() {
    for (Player player : players) {
      if (!player.hasStood() && !player.isBusted()) {
        return false;
      }
    }
    return true;
  }

  public void moveToNextPlayer() {
    currentPlayerIndex++;
    if (currentPlayerIndex >= players.size()) {
      currentPlayerIndex = 0;
    }

    if (isGameOver()) {
      onGameOver();
    }
  }

  private void onGameOver() {
    // 处理游戏结束逻辑，例如决定胜者
    String winnerMessage = decideWinner();
    // 可以在这里调用一个回调函数，通知GUI类游戏结束
    notifyGameOver(winnerMessage);
  }
}
