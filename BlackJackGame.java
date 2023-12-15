package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackJackGame {
  private final Deck deck;
  private final List<Player> players;
  private int currentPlayerIndex;
  private GameEventListener listener;

  // Constructor for the game, initializes the deck.
  public BlackJackGame() {
    deck = new Deck();
    players = new ArrayList<>();
  }

  public void setGameEventListener(GameEventListener listener) {
    this.listener = listener;
  }

  /**
   * This method notifies the listener that the game has ended. This method is part of the Observer
   * design pattern commonly used in software engineering.
   *
   * <p\>When invoked, it calls the onGameOver method of the listener, which, in this case, is
   * typically an instance of a GUI class that implements the GameEventListener interface.
   *
   * <p\>This method allows for a clean separation of concerns between the game logic and the user
   * interface. It facilitates communication between the two without requiring the game logic to
   * have direct knowledge of the GUI's implementation details.
   *
   * @param winnerMessage A string message containing information about the game outcome.
   */
  private void notifyGameOver(String winnerMessage) {
    if (listener != null) {
      listener.onGameOver(winnerMessage);
    }
  }

  // Initializes the game by shuffling the deck and resetting the player list and current player
  // index.
  public void initializeGame() {
    deck.shuffle();
    players.clear();

    // Adds four players to the game.
    for (int i = 1; i <= 4; i++) {
      players.add(new Player("Player " + i));
    }

    dealInitialCards();
    currentPlayerIndex = 0;
  }

  // Deals one card to each player to start the game.
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

    // Notifies the listener that the player has taken a hit and whether they've busted.
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

    // Loops through all players to find the one with the highest score.
    for (Player player : players) {
      int score = player.getScore();
      if (score > highestScore && score <= 21) {
        highestScore = score;
        winner = player;
        maxCards = player.getHand().size();
        isTie = false;
      } else if (score == highestScore) {
        isTie = true;
        // Compares the number of cards in hand in case of a tie.
        if (player.getHand().size() > maxCards) {
          winner = player;
          maxCards = player.getHand().size();
          isTie = false; // If there's a player with more cards, it's not a tie.
        }
      }
    }

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
    String winnerMessage = decideWinner();
    // Notifies the game event listener that the game is over with the winner message.
    notifyGameOver(winnerMessage);
  }
}
