import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * The game class.
 */
public class Game extends JFrame implements Observer {

    /**
     * The number of the rows and columns of the game board.
     */
    public int PLAYFIELD_SIZE = 25;

    /**
     * Snake full life.
     */
    public int LIFE_MAX = 100;

    /**
     * Life drain rate per game tick.
     */
    public int LIFE_DRAIN = 4;

    /**
     * The score of the game. Update when the snake eats a fruit.
     * TODO: Proposal on get score based on current snake's life on each fruit collected.
     */
    private int score = 0;

    /**
     * The world object that act as the observer to update the time in the game.
     */
    private World world;

    /**
     * The playfield of the game that contains almost everything.
     */
    private Playfield playfield;

    /**
     * UI that will be displayed the playfield to window.
     */
    private PlayfieldUI playfield_ui;

    /**
     * The progress bar that will display the time since the last fruit was eaten.
     */
    private JProgressBar lifeBar = new JProgressBar(0, 100);

    /**
     * Label that will be displayed the score to window.
     */
    private JLabel scoreLabel = new JLabel("Score: " + score);

    /**
     * Label to show the game status like game over or game paused.
     */
    private JLabel gameStatusLabel = new JLabel("");

    /**
     * Button to restart the game.
     */
    private JButton restartButton = new JButton("Restart");

    /**
     * Button to pause the game.
     */
    private JButton pauseButton = new JButton("Pause");

    public Game() {
        super("Snake");

        // Add score label at the top of the screen
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        topPanel.setLayout(new GridLayout(1, 3));
        topPanel.add(lifeBar);
        topPanel.add(scoreLabel);
        topPanel.add(gameStatusLabel);
        lifeBar.setValue(LIFE_MAX);
        lifeBar.setForeground(Color.MAGENTA);
        lifeBar.setStringPainted(true);
        lifeBar.setString(LIFE_MAX + " / " + LIFE_MAX);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        gameStatusLabel.setForeground(Color.WHITE);
        gameStatusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(topPanel, BorderLayout.NORTH);

        // Add restart button at the bottom of the screen
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(restartButton);
        bottomPanel.add(pauseButton);
        add(bottomPanel, BorderLayout.SOUTH);
        initButtons();

        // Add playfield to the center of the screen
        addKeyListener(new SnakeController());
        playfield = new Playfield(PLAYFIELD_SIZE, LIFE_MAX);
        playfield_ui = new PlayfieldUI();
        world = new World();
        world.addObserver(this);
        add(playfield_ui);

        // Since stupid Java cannot use KeyListener with ActionListener
        // But I found a way from Japanese StackOverflow (teratail)
        // Just set focusable to true and here you go
        // https://teratail.com/questions/341329
        setFocusable(true);

        setResizable(false);
        setAlwaysOnTop(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // request to focus on the game window to avoid the annoying
        requestFocus();
    }

    /**
     * Initialize the buttons listeners.
     */
    private void initButtons() {
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
                // request to focus on the game window to avoid it to focus to button
                requestFocus();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseGame();
                // request to focus on the game window to avoid it to focus to button
                requestFocus();
            }
        });
    }

    /**
     * The class that represent the playfield UI that will be displayed to the window.
     */
    class PlayfieldUI extends JPanel {
        /**
         * The real pixel size of the grid.
         */
        public int CELL_SIZE = 20;

        /**
         * The constructor of the UI. Will set the size to the preferred size and add the listener of the snake.
         */
        public PlayfieldUI() {
            setPreferredSize(new Dimension(PLAYFIELD_SIZE * CELL_SIZE, PLAYFIELD_SIZE * CELL_SIZE));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int row = 0; row < PLAYFIELD_SIZE; row++) {
                for (int col = 0; col < PLAYFIELD_SIZE; col++) {
                    paintCells(g, row, col);
                }
            }
        }

        /**
         * Paint the cells of the playfield.
         * @param g The graphics object to paint on.
         * @param row The row of the cell.
         * @param col The column of the cell.
         */
        private void paintCells(Graphics g, int row, int col) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;

            // paint the fruit position
            if (playfield.getFruitPosition().getX() == row && playfield.getFruitPosition().getY() == col) {
                g.setColor(Color.RED);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }

            // paint the snake position
            for (Position position : playfield.snake.body) {
                if (position.getX() == row && position.getY() == col) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    /**
     * The class that represent the controller of the snake.
     */
    class SnakeController extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // To prevent player to swiftly change direction and make the snake can rotate back to collision with itself
            // We will get the only first input and ignore the rest in the tick.
            if (!world.getLockInput()) {
                // From real snake game:
                // Player cannot move in opposite direction
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                    if (playfield.snake.getDirection() != SnakeDirection.DOWN) {
                        playfield.snake.setDirection(SnakeDirection.UP);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                    if (playfield.snake.getDirection() != SnakeDirection.UP) {
                        playfield.snake.setDirection(SnakeDirection.DOWN);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
                    if (playfield.snake.getDirection() != SnakeDirection.RIGHT) {
                        playfield.snake.setDirection(SnakeDirection.LEFT);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                    if (playfield.snake.getDirection() != SnakeDirection.LEFT) {
                        playfield.snake.setDirection(SnakeDirection.RIGHT);
                    }
                }
                world.lockInput();
            } else {
                System.out.println("Input locked");
            }

            if (e.getKeyCode() == 192) {
                // 192 is the keycode for the tilde key (~ key)
                restartGame();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                // Space key is the pause key
                if (!world.getGameOver()) {
                    pauseGame();
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        playfield_ui.repaint();
        playfield.moveSnake();
        if (playfield.isEatenFood()) {
            // remove the fruit
            playfield.randomFruit();
            // grow the snake
            playfield.snake.grow();
            score++;
            scoreLabel.setText("Score: " + score);
            System.out.println("Score: " + score);
            playfield.snake.life = LIFE_MAX;
        }
        // Update snake's life
        // The life need to update before game over detection
        if (playfield.snake.life - LIFE_DRAIN <= 0) {
            playfield.snake.life = 0;
        } else {
            playfield.snake.life -= LIFE_DRAIN;
        }
        lifeBar.setValue(playfield.snake.life);
        lifeBar.setString(playfield.snake.life + " / " + LIFE_MAX);
        // Check if the game is over
        if (playfield.isCollisionToWall()) {
            JOptionPane.showMessageDialog(this, "You hit the wall!", "Game Over!", JOptionPane.WARNING_MESSAGE);
            setToGameOverState();
        } else if (playfield.isCollisionItself()) {
            JOptionPane.showMessageDialog(this, "You hit yourself!", "Game Over!", JOptionPane.WARNING_MESSAGE);
            setToGameOverState();
        } else if (lifeBar.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, "You ran out of life!", "Game Over!", JOptionPane.WARNING_MESSAGE);
            setToGameOverState();
        }
        world.unlockInput();
        repaint();
    }

    /**
     * Start the game.
     */
    public void startGame() {
        world.start();
        setVisible(true);
    }

    /**
     * Pause the game.
     */
    public void pauseGame() {
        if (world.getRunning() && playfield.snake.life > 0) {
            world.stop();
            world.lockInput();
            gameStatusLabel.setText("Game Paused");
            gameStatusLabel.setForeground(Color.GREEN);
            pauseButton.setText("Resume");
        } else if (playfield.snake.life <= 0) {
            // To prevent the game from continue running when the life is 0
            world.stop();
        } else {
            world.continueGame();
            world.unlockInput();
            gameStatusLabel.setText("");
            pauseButton.setText("Pause");
        }
    }

    /**
     * Restart the game by resetting everything to the initial state.
     */
    public void restartGame() {
        gameStatusLabel.setText("");
        playfield.snake.life = LIFE_MAX;
        playfield = new Playfield(PLAYFIELD_SIZE, LIFE_MAX);
        score = 0;
        scoreLabel.setText("Score: " + score);
        pauseButton.setEnabled(true);
        pauseButton.setText("Pause");
        world.resetTick();
        world.continueGame();
        world.setGameOver(false);
        repaint();
    }

    /**
     * Set the game environment to the game over state.
     */
    public void setToGameOverState() {
        System.out.println("Snake body list :");
        for (int i = 0; i < playfield.snake.length(); i++) {
            System.out.println(playfield.snake.body.get(i));
        }
        gameStatusLabel.setText("Game Over!");
        gameStatusLabel.setForeground(Color.RED);
        pauseButton.setEnabled(false);
        world.setGameOver(true);
        world.stop();
    }

    public static void main(String[] args) {
       Game game = new Game();
       game.startGame();
    }
}