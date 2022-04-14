import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Game extends JFrame implements Observer {

    public int PLAYFIELD_SIZE = 20;

    private int score = 0;

    private World world;

    private Playfield playfield;

    private PlayfieldUI playfield_ui;

    private JLabel scoreLabel = new JLabel("Score: " + score);

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

        // Need to disable restart button since its listener is conflicting with the key listener
        // Add restart button at the bottom of the screen
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setBackground(Color.BLACK);
//        bottomPanel.setLayout(new FlowLayout());
//        bottomPanel.add(restartButton);
//        add(bottomPanel, BorderLayout.SOUTH);
//        initButtons();

        setResizable(false);
        setAlwaysOnTop(true);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initButtons() {
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
    }

    class PlayfieldUI extends JPanel {
        public int CELL_SIZE = 20;

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

    class SnakeController extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
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
            } else if (e.getKeyCode() == 192) {
                // 192 is the keycode for the tilde key
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
            world.stop();
        } else if (playfield.isCollisionItself()) {
            JOptionPane.showMessageDialog(this, "Game Over!", "You hit yourself!", JOptionPane.WARNING_MESSAGE);
            world.stop();
        }
        repaint();
    }

    public void startGame() {
        world.start();
        setVisible(true);
    }

    public void restartGame() {
        playfield = new Playfield(PLAYFIELD_SIZE);
        score = 0;
        scoreLabel.setText("Score: " + score);
        // To make the world start again without conflict
        world.continueGame();
        repaint();
    }

    public static void main(String[] args) {
       Game game = new Game();
       game.startGame();
    }
}