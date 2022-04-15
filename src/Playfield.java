/**
 * Class representing the playfield of the game. It will contain all object that are in the game.
 */
public class Playfield {

    /**
     * The width of the playfield.
     * This value mean the number of columns, not the real pixel width.
     */
    private final int size;

    /**
     * The fruit position in the playfield.
     */
    private Position fruitPosition;

    /**
     * Snake in the playfield.
     */
    public Snake snake;

    /**
     * Constructor of the playfield.
     */
    public Playfield(int size, int life_max) {
        this.size = size;
        initSnake(life_max);
        randomFruit();
    }

    /**
     * Initialize the snake in the playfield and set the spawn position to the middle of the playfield.
     */
    private void initSnake(int life_max) {
        snake = new Snake();
        snake.life = life_max;
        int middle = size / 2;
        // Normally the snake game start with snake's head at the middle of the playfield
        // and the snake's body is at the left of the head with 3 width.
        for (int i = 1; i <= 3; i++) {
            snake.addBodyPart(new Position(middle, middle - i));
        }
    }

    /**
     * Random the fruit position in the playfield.
     */
    public void randomFruit() {
        fruitPosition = new Position((int) (Math.random() * size), (int) (Math.random() * size));
    }

    /**
     * Return the position of the fruit.
     * @return The position of the fruit.
     */
    public Position getFruitPosition() {
        return fruitPosition;
    }

    /**
     * Call the move method of the snake.
     */
    public void moveSnake() {
        snake.moveBody();
    }

    /*
     * Check that on is the snake's head position has a fruit ot not.
     */
    public Boolean isEatenFood() {
        // check if the snake's head position is the same as the fruit's position
        return snake.body.get(0).getX() == fruitPosition.getX() &&
                snake.body.get(0).getY() == fruitPosition.getY();
    }

    /**
     * Check if the snake is collided with the wall.
     * @return True if the snake is collided with the wall, false otherwise.
     */
    public Boolean isCollisionToWall() {
        // check if the snake has collided with the walls
        for (int i = 1; i < snake.length(); i++) {
            if (snake.body.get(0).getX() < 0 || snake.body.get(0).getX() >= size ||
                    snake.body.get(0).getY() < 0 || snake.body.get(0).getY() >= size) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the snake is collided with its body.
     * @return True if the snake is collided with its body, false otherwise.
     */
    public Boolean isCollisionItself() {
        // check if the snake has collided with itself
        for (int i = 1; i < snake.length(); i++) {
            if (snake.body.get(0).getX() == snake.body.get(i).getX() &&
                    snake.body.get(0).getY() == snake.body.get(i).getY()) {
                System.out.println("Collision with self");
                return true;
            }
        }
        return false;
    }
}
