import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class Game extends JFrame implements Observer {

    public int PLAYFIELD_SIZE = 20;

    private World world;

    private Playfield playfield;

    private PlayfieldUI playfield_ui;

    public Game() {
        super("Snake");
        playfield = new Playfield(PLAYFIELD_SIZE);
        playfield_ui = new PlayfieldUI();
        world = new World();
        world.addObserver(this);
        add(playfield_ui);
        addKeyListener(new SnakeController());
        setResizable(false);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class PlayfieldUI extends JPanel {
        public int CELL_SIZE = 20;

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

        private void paintCells(Graphics g, int row, int col) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;

            Cell cell = playfield.getCell(row, col);

            if (cell.hasFruit) {
                g.setColor(Color.RED);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }

            for (int i = 0; i < playfield.snake.body.size(); i++) {
                Position pos = playfield.snake.body.get(i);
                if (pos.getX() == row && pos.getY() == col) {
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
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        playfield_ui.repaint();
        playfield.update();
        repaint();
    }

    public void startGame() {
        world.start();
        setVisible(true);
    }

    public static void main(String[] args) {
       Game game = new Game();
       game.startGame();
    }
}