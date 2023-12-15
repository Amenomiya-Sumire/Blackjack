package blackjack;

import java.awt.*;
import javax.swing.*;

public class CustomBustDialog extends JDialog {
  public CustomBustDialog(JFrame parent, String player) {
    super(parent, "Busted!", true);
    
    setSize(300, 150);
    setLocationRelativeTo(parent);

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    JLabel messageLabel = new JLabel(player + " busts!", SwingConstants.CENTER);
    messageLabel.setFont(new Font("华文细黑", Font.BOLD, 18));
    messageLabel.setForeground(Color.WHITE);
    panel.add(messageLabel);

    setContentPane(panel);
  }
}
