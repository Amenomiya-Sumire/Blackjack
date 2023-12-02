package blackjack;

public class BlackJack {
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(BlackJackGUI::new);
    BlackJackGame game = new BlackJackGame();
    game.initializeGame();
  }
}
