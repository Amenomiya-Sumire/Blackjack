package blackjack;

public interface GameEventListener {
  void onGameOver(String winnerMessage);
  
  void onPlayerHit(Player player, boolean isBusted);
}
