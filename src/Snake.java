import java.util.ArrayList;

/**
 * The class represents the snake in the game.
 */
public class Snake {
    /**
     * List that contain all positions of the snake body.
     * The first element is the head of the snake.
     */
    public ArrayList<Position> body;

    /**
     * The direction of the snake using SnakeDirection enum.
     */
    private SnakeDirection direction;

    /**
     * Constructor of the snake.
     * Normally the snake is created with right direction.
     */
    public Snake() {
        this.direction = SnakeDirection.RIGHT;
        this.body = new ArrayList<>();
    }

    /**
     * Add a new position to the snake body.
     * @param position The position to add.
     */
    public void addBodyPart(Position position) {
        this.body.add(position);
        for (int i = 0; i < this.body.size() - 1; i++) {
            // print each body part
            System.out.println(this.body.get(i));
        }
    }

    /**
     * Get the direction of the snake.
     * @return The direction of the snake.
     */
    public SnakeDirection getDirection() {
        return direction;
    }

    /**
     * Set the direction of the snake.
     * @param direction The direction to set.
     */
    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
        System.out.println("Snake direction is now " + this.direction);
    }

    /**
     * Update the snake position.
     */
    public void moveBody() {
        // Move the body parts by determining the direction
        // and adding a new position to the front of the body
        // Remove the last position from the body
        switch (this.direction) {
            case LEFT -> this.body.add(0, new Position(this.body.get(0).getX(), this.body.get(0).getY() - 1));
            case RIGHT -> this.body.add(0, new Position(this.body.get(0).getX(), this.body.get(0).getY() + 1));
            case UP -> this.body.add(0, new Position(this.body.get(0).getX() - 1, this.body.get(0).getY()));
            case DOWN -> this.body.add(0, new Position(this.body.get(0).getX() + 1, this.body.get(0).getY()));
        }

        this.body.remove(this.body.size() - 1);
    }

    /**
     * Grow the snake by adding a new position to the end of the body.
     */
    public void grow() {
        // Add a body part to the end of the snake
        // Depending on the direction and its tail
        // TODO: This still not working properly in some cases
        switch (this.direction) {
            case LEFT -> this.body.add(new Position(this.body.get(this.body.size() - 1).getX(), this.body.get(this.body.size() - 1).getY() + 1));
            case RIGHT -> this.body.add(new Position(this.body.get(this.body.size() - 1).getX(), this.body.get(this.body.size() - 1).getY() - 1));
            case UP -> this.body.add(new Position(this.body.get(this.body.size() - 1).getX() + 1, this.body.get(this.body.size() - 1).getY()));
            case DOWN -> this.body.add(new Position(this.body.get(this.body.size() - 1).getX() - 1, this.body.get(this.body.size() - 1).getY()));
        }
    }
}
