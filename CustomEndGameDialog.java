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

    // 创建并设置消息标签
    JLabel messageLabel =
        new JLabel(
            "<html><div style='text-align: center; color: white; padding: 40px 0;'>"
                + message
                + "</div></html>",
            SwingConstants.CENTER);
    messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(messageLabel, BorderLayout.NORTH);

    // 创建并设置小字体的额外消息
    JLabel questionLabel = new JLabel("Play again or not?", SwingConstants.CENTER);
    questionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    questionLabel.setForeground(new Color(0x8a8b8c));
    panel.add(questionLabel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.BLACK);
    JButton playAgainButton = new JButton("Play Again");
    JButton exitButton = new JButton("Exit");

    styleButton(playAgainButton);
    styleButton(exitButton);
    
    // 添加动作监听器
    playAgainButton.addActionListener(e -> onPlayAgain());
    exitButton.addActionListener(e -> onExit());
    
    buttonPanel.add(playAgainButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    this.setContentPane(panel);
  }

  private void onPlayAgain() {
    // 关闭当前对话框
    this.dispose();
    
    // 重置游戏状态
    gui.getGame().initializeGame();
    
    // 更新游戏界面
    SwingUtilities.invokeLater(gui::resetGameInterface);
  }

  private void onExit() {
    // 处理 Exit 按钮事件
    this.dispose();
    System.exit(0);
  }

  private void styleButton(JButton button) {
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 12)); // 增加字体大小
    button.setPreferredSize(new Dimension(100, 25)); // 增加按钮尺寸
    button.setBorder(new BlackJackGUI.RoundedBorder(10, Color.WHITE));
    button.setMargin(new Insets(10, 20, 10, 20)); // 增加文本与按钮边界的间距
    button.setBorder(new RoundedBorder(10, Color.GRAY));

    // 重写按钮的绘制方法来实现自定义的背景和边框
    button.setUI(
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xff7582)); // 按下时的颜色
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0xc56c86)); // 鼠标悬浮时的颜色
            } else {
              g2.setColor(new Color(0x725a7a)); // 默认颜色
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
