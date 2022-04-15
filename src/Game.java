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
    public int PLAYFIELD_SIZE = 20;

    /**
     * The score of the game. Update when the snake eats a fruit.
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
     * Label that will be displayed the score to window.
     */
    private JLabel scoreLabel = new JLabel("Score: " + score);

    /**
     * Button to restart the game.
     */
    private JButton restartButton = new JButton("Restart");

    public Game() {
        super("Snake");
        addKeyListener(new SnakeController());
        playfield = new Playfield(PLAYFIELD_SIZE);
        playfield_ui = new PlayfieldUI();
        world = new World();
        world.addObserver(this);
        add(playfield_ui);

        // Add score label at the top of the screen
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        topPanel.setLayout(new FlowLayout());
        topPanel.add(scoreLabel);
        scoreLabel.setForeground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        // Add restart button at the bottom of the screen
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(restartButton);
        add(bottomPanel, BorderLayout.SOUTH);
        initButtons();

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
                // request to focus on the game window to avoid the annoying
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
            addKeyListener(new SnakeController());
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
            if (!world.getLockInput()) {
                // From real snake game:
                // Player cannot move in opposite direction
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (playfield.snake.getDirection() != SnakeDirection.DOWN) {
                        playfield.snake.setDirection(SnakeDirection.UP);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (playfield.snake.getDirection() != SnakeDirection.UP) {
                        playfield.snake.setDirection(SnakeDirection.DOWN);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (playfield.snake.getDirection() != SnakeDirection.RIGHT) {
                        playfield.snake.setDirection(SnakeDirection.LEFT);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
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
        }
        if (playfield.isCollisionToWall()) {
            JOptionPane.showMessageDialog(this, "Game Over!", "You hit the wall!", JOptionPane.WARNING_MESSAGE);
            System.out.println("Snake body list :");
            for (int i = 0; i < playfield.snake.body.size(); i++) {
                System.out.println(playfield.snake.body.get(i));
            }
            world.stop();
        } else if (playfield.isCollisionItself()) {
            JOptionPane.showMessageDialog(this, "Game Over!", "You hit yourself!", JOptionPane.WARNING_MESSAGE);
            System.out.println("Snake body list :");
            for (int i = 0; i < playfield.snake.body.size(); i++) {
                System.out.println(playfield.snake.body.get(i));
            }
            world.stop();
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
     * Restart the game.
     */
    public void restartGame() {
        playfield = new Playfield(PLAYFIELD_SIZE);
        score = 0;
        scoreLabel.setText("Score: " + score);
        world.continueGame();
        repaint();
    }

    public static void main(String[] args) {
       Game game = new Game();
       game.startGame();
    }
}