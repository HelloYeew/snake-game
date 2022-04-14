import java.util.List;

public class Playfield {
    private final int size;

    private Position fruitPosition;

    public Snake snake;

    public Playfield(int size) {
        this.size = size;
        initSnake();
        randomFruit();
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

    /**
     * Random the fruit position in the playfield.
     */
    public void randomFruit() {
        int x = (int) (Math.random() * size);
        int y = (int) (Math.random() * size);
        fruitPosition = new Position(x, y);
        System.out.println("Fruit at " + x + "," + y);
    }

    public Position getFruitPosition() {
        return fruitPosition;
    }

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

    public Boolean isCollisionToWall() {
        // check if the snake has collided with the walls
        for (int i = 1; i < snake.body.size(); i++) {
            if (snake.body.get(0).getX() < 0 || snake.body.get(0).getX() >= size ||
                    snake.body.get(0).getY() < 0 || snake.body.get(0).getY() >= size) {
                return true;
            }
        }
        return false;
    }


    public Boolean isCollisionItself() {
        // check if the snake has collided with itself
        for (int i = 1; i < snake.body.size(); i++) {
            if (snake.body.get(0).getX() == snake.body.get(i).getX() &&
                    snake.body.get(0).getY() == snake.body.get(i).getY()) {
                System.out.println("Collision with self");
                return true;
            }
        }
        return false;
    }
}
