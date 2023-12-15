package blackjack;

import java.awt.*;
import javax.swing.*;

/**
 * html: Starts the HTML content.
 *
 * <p\>body: Contains all the content with styles for font, size, alignment, and margin.
 *
 * <p\>h2: Creates a main heading for the welcome message, styled with a specific color.
 *
 * <p\>h3: Used for subheadings like "Rules" and "Winning Criteria," styled with enlarged font size
 * and color.
 *
 * <p\>ul: Defines an unordered list to present the game rules and winning criteria.
 *
 * <p\>li: List items within the unordered list, detailing individual rules and criteria.
 *
 * <p\>p: A paragraph at the end, styled with a specific font size and color, providing a closing
 * message.
 */
public class CustomInstructionsDialog extends JDialog {
  public CustomInstructionsDialog(JFrame parent) {
    super(parent, "Instructions", true);

    setSize(500, 600);
    setLocationRelativeTo(parent);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    JLabel messageLabel =
        new JLabel(
            "<html>"
                + "<body style='font-family:华文细黑; font-size:12px; text-align:left; margin: 20px;'>"
                + "<h2 style='color:#8693ab;'>Welcome to Blackjack!</h2>"
                + "<h3 style='font-size:14px; color:#8693ab;'>Rules:</h3>"
                + // Enlarged font size for <h3> and set color
                "<ul>"
                + "<li>The game is played without a dealer, among four players.</li>"
                + "<li>The aim is to have the highest card point total, which should not exceed 21.</li>"
                + "<li>Each player can 'hit' to draw an additional card or 'stand' to stop.</li>"
                + "<li>Card values: Number cards count as their number, face cards (J, Q, K) are worth 10, and an Ace can be 1 or 11.</li>"
                + "<li>If a player exceeds 21 points ('busts'), they lose immediately.</li>"
                + "</ul>"
                + "<h3 style='font-size:14px; color:#8693ab;'>Winning Criteria:</h3>"
                + // Enlarged font size for <h3> and set color
                "<ul>"
                + "<li>The player with the highest point total wins.</li>"
                + "<li>In case of a tie in points, the player with the most cards wins.</li>"
                + "<li>If there is still a tie, the tied players share the victory.</li>"
                + "</ul>"
                + "<p style='font-size:16px; color:#8693ab;'>Enjoy the game!</p >"
                + // Enlarged font size for this paragraph and set color
                "</body>"
                + "</html>",
            SwingConstants.LEFT);
    messageLabel.setFont(new Font("华文细黑", Font.BOLD, 18));
    messageLabel.setForeground(Color.WHITE);
    panel.add(messageLabel);

    setContentPane(panel);
  }
}
