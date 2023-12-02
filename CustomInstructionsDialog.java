package blackjack;

import java.awt.*;
import javax.swing.*;

public class CustomInstructionsDialog extends JDialog {
  public CustomInstructionsDialog(JFrame parent) {
    super(parent, "Instructions", true);

    // 设置对话框的大小和位置
    setSize(500, 600);
    setLocationRelativeTo(parent);

    // 创建面板并设置布局
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    // 创建并设置显示信息的标签
    JLabel messageLabel =
        new JLabel(
            "<html>"
                + "<body style='font-family:Arial; font-size:12px; text-align:left; margin: 20px;'>"
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
    messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
    messageLabel.setForeground(Color.WHITE);
    panel.add(messageLabel);

    // 将面板添加到对话框
    setContentPane(panel);
  }
}
