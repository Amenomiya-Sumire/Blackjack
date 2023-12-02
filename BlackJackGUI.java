package blackjack;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

public class BlackJackGUI {
  private JFrame frame;
  private JPanel controlPanel;
  private int currentPlayerIndex = 0;
  private static final ArrayList<JPanel> playerPanels = new ArrayList<>();
  private static final ArrayList<JLabel> playerScores = new ArrayList<>();
  private static ArrayList<Player> players;

  public BlackJackGUI() {
    initializePlayers();
    createFrame();
    setupStartGameScreen();
  }

  public static void resetGameInterface() {
    updateGameInterface(players);
  }

  private void createFrame() {
    frame = new JFrame("Blackjack");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 700);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private void showInstructions() {
    CustomInstructionsDialog instructionsDialog = new CustomInstructionsDialog(frame);
    instructionsDialog.setVisible(true);
  }

  private void initializePlayers() {
    players = new ArrayList<>();

    for (int i = 1; i <= 4; i++) {
      players.add(new Player("Player " + i));
    }
  }

  private void setupGameScreen() {
    frame.getContentPane().removeAll();

    GameBackgroundPanel gamePanel = new GameBackgroundPanel();
    gamePanel.setLayout(new BorderLayout()); // 设置布局管理器

    JPanel gameContentPanel = new JPanel();
    gameContentPanel.setLayout(new GridLayout(2, 2)); // 或者使用您需要的任何布局管理器
    gameContentPanel.setOpaque(false); // 设置为透明以显示背景

    playerPanels.clear();
    playerScores.clear();

    for (int i = 0; i < players.size(); i++) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setOpaque(false);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      JLabel scoreLabel = new JLabel("Player " + (i + 1) + " Score: 0");
      scoreLabel.setForeground(Color.WHITE);
      scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // 设置分数标签下的边距
      panel.add(scoreLabel, BorderLayout.NORTH);
      playerScores.add(scoreLabel);

      JPanel cardPanel = new JPanel(new FlowLayout());
      cardPanel.setOpaque(false);
      panel.add(cardPanel, BorderLayout.CENTER);
      playerPanels.add(cardPanel);

      gameContentPanel.add(panel);
    }

    gamePanel.add(gameContentPanel, BorderLayout.CENTER); // 将内容面板添加到背景面板

    frame.add(gamePanel, BorderLayout.CENTER);

    // 创建南部面板，包含控制面板和说明按钮
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
    southPanel.setBackground(Color.BLACK);

    // 设置 controlPanel 并添加到 southPanel
    setupControlPanel();
    southPanel.add(controlPanel);

    // 创建 "Instructions" 按钮
    JButton instructionsButton = new JButton("Instructions");
    styleGameScreenButton(instructionsButton);
    instructionsButton.addActionListener(e -> showInstructions());

    // 创建面板用于放置 "Instructions" 按钮，并将其放置在流布局的右侧
    JPanel instructionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    instructionsButtonPanel.setBackground(Color.BLACK);
    instructionsButtonPanel.add(instructionsButton);

    // 将 "Instructions" 按钮面板添加到 southPanel
    southPanel.add(instructionsButtonPanel);

    // 将 southPanel 添加到 frame 的 PAGE_END 位置
    frame.add(southPanel, BorderLayout.PAGE_END);

    // 刷新和重绘 frame 以应用更改
    frame.revalidate();
    frame.repaint();
  }

  private static void updatePlayerHandDisplay(int playerIndex, Player player) {

    if (playerIndex >= playerPanels.size() || playerIndex >= playerScores.size()) {
      // 如果索引超出范围，不进行更新
      return;
    }

    JPanel cardPanel = playerPanels.get(playerIndex);
    cardPanel.removeAll();
    cardPanel.setLayout(null);

    int overlap = 36;
    int cardWidth = 156;
    int cardHeight = 212;
    int offset = 0;

    for (Card card : player.getHand()) {
      if (card.isShow()) {
        ImageIcon originalIcon =
            new ImageIcon(
                Objects.requireNonNull(BlackJackGUI.class.getResource(card.generateImagePath())));
        Image image =
            originalIcon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        JLabel cardLabel = new JLabel(new ImageIcon(image));

        cardLabel.setBounds(offset, 0, cardWidth, cardHeight);
        cardPanel.add(cardLabel, 0);

        offset += overlap; // 更新偏移量以进行下一张卡牌的位置设置
      }
    }

    JLabel scoreLabel = playerScores.get(playerIndex);
    String scoreText = "Player " + (playerIndex + 1) + " Score: " + player.getScore();
    scoreLabel.setText(scoreText);

    cardPanel.setPreferredSize(
        new Dimension((player.getHand().size() - 1) * overlap + cardWidth, cardHeight));
    cardPanel.revalidate();
    cardPanel.repaint();
  }

  private void setupControlPanel() {
    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());
    controlPanel.setBackground(Color.BLACK);

    JButton hitButton = new JButton("Hit");
    hitButton.addActionListener(e -> handleHit());
    styleGameScreenButton(hitButton);
    controlPanel.add(hitButton);

    JButton standButton = new JButton("Stand");
    standButton.addActionListener(e -> handleStand());
    styleGameScreenButton(standButton);
    controlPanel.add(standButton);
  }

  private void handleHit() {
    Player currentPlayer = players.get(currentPlayerIndex);
    if (!BlackJack.getDeck().isEmpty()) {
      currentPlayer.addCard(BlackJack.getDeck().remove(0));
      updatePlayerHandDisplay(currentPlayerIndex, currentPlayer);

      if (currentPlayer.getScore() > 21) {
        // 显示自定义 "Busts!" 对话框
        CustomBustDialog bustDialog = new CustomBustDialog(frame, currentPlayer.getName());
        bustDialog.setVisible(true);

        // 处理玩家爆牌后的逻辑
        moveToNextPlayer();
      }
    }
  }

  private void handleStand() {
    moveToNextPlayer();
  }

  private void moveToNextPlayer() {
    currentPlayerIndex++;
    if (currentPlayerIndex >= players.size()) {
      currentPlayerIndex = 0;
      BlackJack.decideWinner();
      showEndGameOptions();
    }
  }

  private void showEndGameOptions() {
    // Create and display custom dialog
    CustomEndGameDialog dialog =
        new CustomEndGameDialog(frame, "Game Over", BlackJack.winnerMessage);
    dialog.setVisible(true);
  }

  static void updateGameInterface(ArrayList<Player> updatedPlayers) {
    players = updatedPlayers;
    for (int i = 0; i < players.size(); i++) {
      updatePlayerHandDisplay(i, players.get(i));
    }
  }

  private void setupStartGameScreen() {
    frame.getContentPane().removeAll();

    BackgroundPanel backgroundPanel = new BackgroundPanel("/images/blackjack_start_screen.png");

    // 创建按钮并应用样式
    JButton startButton = new JButton("Start Game");
    styleStartScreenButton(startButton);
    startButton.addActionListener(e -> startGame());

    JButton instructionsButton = new JButton("Instructions");
    styleStartScreenButton(instructionsButton);
    instructionsButton.addActionListener(e -> showInstructions());

    // 创建一个新的面板用于存放按钮
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 水平间隔20，垂直间隔20
    buttonsPanel.setOpaque(false); // 面板透明

    // 添加按钮到按钮面板
    buttonsPanel.add(startButton);
    buttonsPanel.add(instructionsButton);

    // 将按钮面板添加到背景面板的底部
    backgroundPanel.setLayout(new BorderLayout());
    backgroundPanel.add(buttonsPanel, BorderLayout.SOUTH);

    frame.add(backgroundPanel);

    frame.addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            backgroundPanel.loadImage();
          }
        });

    frame.revalidate();
    frame.repaint();
  }

  private void styleStartScreenButton(JButton button) {
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 30)); // 增加字体大小
    button.setPreferredSize(new Dimension(300, 75)); // 增加按钮尺寸
    button.setBorder(new RoundedBorder(10, Color.WHITE));
    button.setMargin(new Insets(10, 20, 10, 20)); // 增加文本与按钮边界的间距

    // 重写按钮的绘制方法来实现自定义的背景和边框
    button.setUI(
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xBEBEBE)); // 按下时的颜色
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0x828282)); // 鼠标悬浮时的颜色
            } else {
              g2.setColor(new Color(0x000000)); // 默认颜色
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2.dispose();

            super.paint(g, c);
          }
        });
  }

  private void styleGameScreenButton(JButton button) {
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setOpaque(false); // 按钮不绘制默认的背景
    button.setContentAreaFilled(false); // 不绘制默认的内容区域
    button.setForeground(Color.WHITE); // 设置文字颜色为白色
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));

    // 使用自定义边框
    button.setBorder(new RoundedBorder(10, Color.WHITE));

    // 重写按钮的绘制方法来实现自定义的背景和边框
    button.setUI(
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xBEBEBE)); // 按下时的颜色
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0x828282)); // 鼠标悬浮时的颜色
            } else {
              g2.setColor(new Color(0x000000)); // 默认颜色
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2.dispose();

            super.paint(g, c);
          }
        });
  }

  static class RoundedBorder implements Border {
    private final int radius;
    private final Color borderColor;

    RoundedBorder(int radius, Color borderColor) {
      this.radius = radius;
      this.borderColor = borderColor;
    }

    public Insets getBorderInsets(Component c) {
      return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    public boolean isBorderOpaque() {
      return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(borderColor);
      g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
  }

  private void startGame() {
    BlackJack.reset();
    setupGameScreen();
    updateGameInterface(BlackJack.players);
  }

  static class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private final String imagePath;

    public BackgroundPanel(String imagePath) {
      this.imagePath = imagePath;
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void loadImage() {
      // 确保组件已经被添加到屏幕且大小已经确定
      if (getWidth() <= 0 || getHeight() <= 0) {
        return;
      }
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
      backgroundImage =
          icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
      repaint(); // 重新绘制面板以应用新的图像大小
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, this);
      }
    }

    // 当组件大小变化时，重新加载图像
    @Override
    public void addNotify() {
      super.addNotify();
      loadImage();
    }

    // 当组件大小变化时，重新加载图像
    @Override
    public void setSize(int width, int height) {
      super.setSize(width, height);
      loadImage();
    }
  }

  static class GameBackgroundPanel extends JPanel {

    public GameBackgroundPanel() {
      setOpaque(true); // 确保面板不透明，以便绘制背景颜色
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Color bgColor = Color.BLACK; // 将HEX颜色转换为Color对象
      g.setColor(bgColor); // 设置绘制颜色
      g.fillRect(0, 0, getWidth(), getHeight()); // 绘制矩形作为背景
    }
  }
}
