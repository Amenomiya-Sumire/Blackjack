package blackjack;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

public class BlackJackGUI implements GameEventListener {
  private JFrame frame;
  private JPanel controlPanel;
  private static final ArrayList<JPanel> playerPanels = new ArrayList<>();
  private static final ArrayList<JLabel> playerScores = new ArrayList<>();
  private final BlackJackGame game;

  public BlackJackGUI() {
    this.game = new BlackJackGame();
    this.game.setGameEventListener(this);
    createFrame();
    setupStartGameScreen();
    game.initializeGame();
    updateGameInterface(game.getPlayers());
  }

  public BlackJackGame getGame() {
    return game;
  }

  public void resetGameInterface() {
    updateGameInterface(game.getPlayers());
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

  private void setupGameScreen() {
    // Clear the frame's content pane before setting up a new game screen.
    frame.getContentPane().removeAll();

    GameBackgroundPanel gamePanel = new GameBackgroundPanel();
    gamePanel.setLayout(new BorderLayout());

    JPanel gameContentPanel = new JPanel();
    gameContentPanel.setLayout(new GridLayout(2, 2)); // Grid layout for 4 players.
    gameContentPanel.setOpaque(false);

    // Clear and reinitialize player panels and scores.
    playerPanels.clear();
    playerScores.clear();

    for (int i = 0; i < game.getPlayers().size(); i++) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setOpaque(false);
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      JLabel scoreLabel = new JLabel("Player " + (i + 1) + " Score: 0");
      scoreLabel.setFont(new Font("华文细黑", Font.BOLD, 20));
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

    gamePanel.add(gameContentPanel, BorderLayout.CENTER);

    frame.add(gamePanel, BorderLayout.CENTER);

    // The south panel will contain the control panel and instruction button.
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
    southPanel.setBackground(Color.BLACK);

    setupControlPanel();
    southPanel.add(controlPanel);

    // Instructions button at the bottom right.
    JButton instructionsButton = new JButton("Instructions");
    styleGameScreenButton(instructionsButton);
    instructionsButton.addActionListener(e -> showInstructions());

    // Panel for the instructions button to align it to the right.
    JPanel instructionsButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    instructionsButtonPanel.setBackground(Color.BLACK);
    instructionsButtonPanel.add(instructionsButton);

    southPanel.add(instructionsButtonPanel);

    /// Add the south panel to the bottom of the frame.
    frame.add(southPanel, BorderLayout.PAGE_END);

    // Refresh and repaint the frame to apply all changes.
    frame.revalidate();
    frame.repaint();
  }

  private void updatePlayerHandDisplay(int playerIndex, Player player) {
    // If the index is out of bounds, do not attempt to update the display.
    if (playerIndex >= playerPanels.size() || playerIndex >= playerScores.size()) {
      return;
    }

    JPanel cardPanel = playerPanels.get(playerIndex);
    cardPanel.removeAll();
    cardPanel.setLayout(null);

    // Variables for positioning and sizing the cards.
    int overlap = 36; // Overlap between cards.
    int cardWidth = 156; // Width of a card image.
    int cardHeight = 212; // Height of a card image.
    int offset = 0; // Offset for placing each card.

    for (Card card : player.getHand()) {
      if (card.isShow()) {

        // Create an image icon for the card.
        ImageIcon originalIcon =
            new ImageIcon(
                Objects.requireNonNull(BlackJackGUI.class.getResource(card.generateImagePath())));

        // Scale the image to fit the card size.
        Image image =
            originalIcon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        JLabel cardLabel = new JLabel(new ImageIcon(image));

        cardLabel.setBounds(offset, 0, cardWidth, cardHeight);
        cardPanel.add(cardLabel, 0);

        offset += overlap; // Increment the offset for the next card.
      }
    }

    // Update the player's score display.
    JLabel scoreLabel = playerScores.get(playerIndex);
    String scoreText = "Player " + (playerIndex + 1) + " Score: " + player.getScore();
    scoreLabel.setText(scoreText);

    // Set the preferred size of the card panel based on the number of cards.
    cardPanel.setPreferredSize(
        new Dimension((player.getHand().size() - 1) * overlap + cardWidth, cardHeight));
    cardPanel.revalidate();
    cardPanel.repaint();
  }

  private void setupControlPanel() {
    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());
    controlPanel.setBackground(Color.BLACK);

    // Create and style the 'Hit' button.
    JButton hitButton = new JButton("Hit");
    hitButton.addActionListener(e -> handleHit());
    styleGameScreenButton(hitButton);
    controlPanel.add(hitButton);

    // Create and style the 'Stand' button.
    JButton standButton = new JButton("Stand");
    standButton.addActionListener(e -> handleStand());
    styleGameScreenButton(standButton);
    controlPanel.add(standButton);
  }

  private void handleHit() {
    game.hit();
  }

  private void handleStand() {
    game.stand();
  }

  // Game event listener method that is called when the game is over.
  @Override
  public void onGameOver(String winnerMessage) {
    showEndGameOptions(winnerMessage);
  }

  @Override
  public void onPlayerHit(Player player, boolean isBusted) {
    int playerIndex = game.getPlayers().indexOf(player);
    updatePlayerHandDisplay(playerIndex, player);
    if (isBusted) {
      CustomBustDialog bustDialog = new CustomBustDialog(frame, player.getName());
      bustDialog.setVisible(true);
    }
  }

  private void showEndGameOptions(String winnerMessage) {
    // Create and display custom dialog
    CustomEndGameDialog dialog = new CustomEndGameDialog(frame, "Game Over", winnerMessage, this);
    dialog.setVisible(true);
  }

  void updateGameInterface(List<Player> updatedPlayers) {
    for (int i = 0; i < updatedPlayers.size(); i++) {
      updatePlayerHandDisplay(i, updatedPlayers.get(i));
    }
  }

  private void setupStartGameScreen() {
    // Clear the frame's content pane before setting up a new start screen.
    frame.getContentPane().removeAll();

    BackgroundPanel backgroundPanel = new BackgroundPanel("/images/blackjack_start_screen.png");

    // Create and style the 'Start Game' button.
    JButton startButton = new JButton("Start Game");
    styleStartScreenButton(startButton);
    startButton.addActionListener(e -> startGame());

    // Create and style the 'Instructions' button.
    JButton instructionsButton = new JButton("Instructions");
    styleStartScreenButton(instructionsButton);
    instructionsButton.addActionListener(e -> showInstructions());

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 水平间隔20，垂直间隔20
    buttonsPanel.setOpaque(false); // 面板透明

    buttonsPanel.add(startButton);
    buttonsPanel.add(instructionsButton);

    backgroundPanel.setLayout(new BorderLayout());
    backgroundPanel.add(buttonsPanel, BorderLayout.SOUTH);

    frame.add(backgroundPanel);

    // Add a component listener to handle resizing of the background
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
    button.setFont(new Font("华文细黑", Font.BOLD, 30));
    button.setPreferredSize(new Dimension(300, 75));
    button.setBorder(new RoundedBorder(10, Color.WHITE));
    button.setMargin(new Insets(10, 20, 10, 20));

    button.setUI(
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            // Cast the Graphics object to Graphics2D to access advanced features.
            Graphics2D g2 = (Graphics2D) g.create();

            // Enable antialiasing to smooth out the jagged edges of the button graphics.
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xBEBEBE));
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0x828282));
            } else {
              g2.setColor(new Color(0x000000));
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);

            // Dispose of the Graphics2D object to release system resources.
            g2.dispose();

            super.paint(g, c);
          }
        });
  }

  private void styleGameScreenButton(JButton button) {
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setFont(new Font("华文细黑", Font.BOLD, 14));

    button.setBorder(new RoundedBorder(10, Color.WHITE));

    button.setUI(
        // Using anonymous class to create a customization which is specific to this button and is
        // defined inline for convenience. It will only use once.
        new BasicButtonUI() {
          @Override
          public void paint(Graphics g, JComponent c) {
            // Cast the Graphics object to Graphics2D to access advanced features.
            Graphics2D g2 = (Graphics2D) g.create();

            // Enable antialiasing to smooth out the jagged edges of the button graphics.
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (button.getModel().isPressed()) {
              g2.setColor(new Color(0xBEBEBE)); // The color when pressing the button
            } else if (button.getModel().isRollover()) {
              g2.setColor(new Color(0x828282)); // The color when hovering over the button
            } else {
              g2.setColor(new Color(0x000000)); // The default color
            }

            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2.dispose();

            super.paint(g, c);
          }
        });
  }

  // Custom border class with rounded corners.
  static class RoundedBorder implements Border {
    private final int radius;
    private final Color borderColor;

    RoundedBorder(int radius, Color borderColor) {
      this.radius = radius;
      this.borderColor = borderColor;
    }

    /**
     * The Insets in RoundedBorder class add space around the component's border to ensure that the
     * rounded edges display nicely and don't cover the content area.
     */
    public Insets getBorderInsets(Component c) {
      return new Insets(this.radius, this.radius, this.radius, this.radius);
    }

    public boolean isBorderOpaque() {
      return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
      // Cast the Graphics object to Graphics2D to access advanced features.
      Graphics2D g2 = (Graphics2D) g;

      // Enable antialiasing to smooth out the jagged edges of the button graphics.
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(borderColor);
      g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
  }

  private void startGame() {
    game.initializeGame();
    setupGameScreen();
    updateGameInterface(game.getPlayers());
  }

  static class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private final String imagePath;

    public BackgroundPanel(String imagePath) {
      this.imagePath = imagePath;
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    private void loadImage() {
      if (getWidth() <= 0 || getHeight() <= 0) {
        return;
      }

      /*
       This code loads an image and scales it to fit the size of the component, then triggers a
       redrawing of the component with the new image.
      */
      ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
      backgroundImage =
          icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
      repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, this);
      }
    }

    /**
     * The addNotify method makes sure that the image is loaded at the right time — just after the
     * component is added to its container.
     */
    @Override
    public void addNotify() {
      super.addNotify();
      loadImage();
    }

    // Reload image when the frame size is changing
    @Override
    public void setSize(int width, int height) {
      super.setSize(width, height);
      loadImage();
    }
  }

  static class GameBackgroundPanel extends JPanel {
    public GameBackgroundPanel() {
      setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
  }
}
