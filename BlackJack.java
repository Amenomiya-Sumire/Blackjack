package blackjack;

public class BlackJack {
  public static void main(String[] args) {
    // Schedules a job for the event-dispatching thread: creating and showing the game's GUI.
    javax.swing.SwingUtilities.invokeLater(BlackJackGUI::new);
    
    // Creates an instance of the game logic controller.
    BlackJackGame game = new BlackJackGame();
    
    // Initializes the game by shuffling the deck and dealing initial cards to players.
    game.initializeGame();
  }
}
