package blackjack;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class CustomEndGameDialog extends JDialog {
  private final BlackJackGUI gui;

  public CustomEndGameDialog(JFrame parent, String title, String message, BlackJackGUI gui) {
    super(parent, title, true);
    this.gui = gui;

    // Set dialog size
    this.setSize(400, 250);
    this.setLocationRelativeTo(parent);

    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(Color.BLACK);

    JLabel messageLabel = getjLabel(message);
    panel.add(messageLabel, BorderLayout.NORTH);

    JLabel questionLabel = new JLabel("Play again or not?", SwingConstants.CENTER);
    questionLabel.setFont(new Font("华文细黑", Font.PLAIN, 12));
    questionLabel.setForeground(new Color(0x8a8b8c));
    panel.add(questionLabel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.BLACK);
    JButton playAgainButton = new JButton("Play Again");
    JButton exitButton = new JButton("Exit");

    styleButton(playAgainButton);
    styleButton(exitButton);

    playAgainButton.addActionListener(e -> onPlayAgain());
    exitButton.addActionListener(e -> onExit());

    buttonPanel.add(playAgainButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    this.setContentPane(panel);
  }

  /**
   * The HTML tags in the messageLabel are used to format the text of the label.
   *
   * <p\>Specifically, html and div tags allow for advanced styling, such as center alignment
   * (text-align: center), text color (color: white), and padding (padding: 40px 0).
   *
   * <p\>This HTML formatting is used to enhance the visual presentation of the message in the label
   * within the dialog box.
   */
  private static JLabel getjLabel(String message) {
    JLabel messageLabel =
        new JLabel(
            "<html><div style='text-align: center; color: white; padding: 40px 0;'>"
                + message
                + "</div></html>",
            SwingConstants.CENTER);
    messageLabel.setFont(new Font("华文细黑", Font.BOLD, 20));
    return messageLabel;
  }

  private void onPlayAgain() {
    // Close this dialog
    this.dispose();

    // Reset game
    gui.getGame().initializeGame();

    // Update the game Interface
    SwingUtilities.invokeLater(gui::resetGameInterface);
  }

  private void onExit() {
    // Close this dialog and the program
    this.dispose();
    System.exit(0);
  }

  private void styleButton(JButton button) {
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("华文细黑", Font.BOLD, 12));
    button.setPreferredSize(new Dimension(100, 25));
    button.setBorder(new BlackJackGUI.RoundedBorder(10, Color.WHITE));
    button.setMargin(new Insets(10, 20, 10, 20));
    button.setBorder(new RoundedBorder(10, Color.GRAY));

    button.setUI(
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xff7582));
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0xc56c86));
            } else {
              g2.setColor(new Color(0x725a7a));
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2.dispose();

            super.paint(g, c);
          }
        });
  }

  static class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color borderColor;

    RoundedBorder(int radius, Color borderColor) {
      this.radius = radius;
      this.borderColor = borderColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      g.setColor(borderColor);
      g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
  }
}
