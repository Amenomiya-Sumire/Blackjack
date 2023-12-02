package blackjack;

import java.awt.*;
import javax.swing.*;

public class CustomBustDialog extends JDialog {
  public CustomBustDialog(JFrame parent, String player) {
    super(parent, "Busted!", true);

    // 设置对话框的大小和位置
    setSize(300, 150);
    setLocationRelativeTo(parent);

    // 创建面板并设置布局
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.BLACK);

    // 创建并设置显示信息的标签
    JLabel messageLabel = new JLabel(player + " busts!", SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
    messageLabel.setForeground(Color.WHITE);
    panel.add(messageLabel);

    // 将面板添加到对话框
    setContentPane(panel);
  }
}
