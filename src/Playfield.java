import java.util.List;

public class Playfield {
    private final int size;

    private Cell[][] cells;

    public Snake snake;

    public Playfield(int size) {
        this.size = size;
        initCells();
        initSnake();
        randomFruit();
    }

    /**
     * Initialize the cells.
     */
    private void initCells() {
        cells = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void initSnake() {
        // spawn the snake at the center of the playfield
        snake = new Snake();
        int middle = (int)(size / 2);
        snake.addBodyPart(new Position(middle, middle));
        snake.addBodyPart(new Position(middle, middle - 1));
        snake.addBodyPart(new Position(middle, middle - 2));
        System.out.println("Snake at " + middle + "," + middle);
        System.out.println("Snake at " + middle + "," + (middle - 1));
        System.out.println("Snake at " + middle + "," + (middle - 2));
    }

    public void setSnakeDirection(SnakeDirection direction) {
        snake.setDirection(direction);
    }

    /**
     * Random the fruit position in the playfield.
     */
    private void randomFruit() {
        int x = (int) (Math.random() * size);
        int y = (int) (Math.random() * size);
        cells[x][y].setHasFruit(true);
        System.out.println("Fruit at " + x + "," + y);
    }

    private void removeFruit() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setHasFruit(false);
            }
        }
    }

    public void update() {
        moveSnake();
        if (isEatenFood()) {
            System.out.println(snake.checkTailDirection());
            // remove the fruit
            removeFruit();
            randomFruit();
            // grow the snake
            snake.grow();
        }
        isCollision();
    }

    public void moveSnake() {
        snake.moveBody();
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    /*
     * Check that on is the snake's head position has a fruit ot not.
     */
    public Boolean isEatenFood() {
        // to prevent array out of bounds exception
        if (snake.body.get(0).getX() < 0 || snake.body.get(0).getX() >= size ||
                snake.body.get(0).getY() < 0 || snake.body.get(0).getY() >= size) {
            return false;
        }
        return cells[snake.body.get(0).getX()][snake.body.get(0).getY()].hasFruit;
    }

    public void isCollision() {
        // check if the snake has collided with itself or the walls
        for (int i = 1; i < snake.body.size(); i++) {
            if (snake.body.get(0).equals(snake.body.get(i))) {
                System.out.println("Collision with self");
            } else if (snake.body.get(0).getX() < 0 || snake.body.get(0).getX() >= size ||
                    snake.body.get(0).getY() < 0 || snake.body.get(0).getY() >= size) {
                System.out.println("Collision with wall");
            }
        }
    }
}
