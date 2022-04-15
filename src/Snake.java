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
     * The snake life.
     */
    public int life;

    /**
     * Constructor of the snake.
     * Normally the snake is created with right direction.
     */
    public Snake() {
        this.direction = SnakeDirection.RIGHT;
        this.body = new ArrayList<>();
        this.life = 0;
    }

    /**
     * Add a new position to the snake body.
     * @param position The position to add.
     */
    public void addBodyPart(Position position) {
        this.body.add(position);
        System.out.println("Added new body part at " + position);
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
     * Return the length of the snake that is the size of the body list.
     * @return The length of the snake.
     */
    public int length() {
        return this.body.size();
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
        // It must check the direction of the two last parts alignments and add the new part accordingly
        Position lastPart = this.body.get(this.body.size() - 1);
        Position secondLastPart = this.body.get(this.body.size() - 2);
        if (lastPart.getX() == secondLastPart.getX()) {
            // Add a new part to the bottom
            this.body.add(new Position(lastPart.getX(), lastPart.getY() + 1));
        } else if (lastPart.getY() == secondLastPart.getY()) {
            // Add a new part to the right
            this.body.add(new Position(lastPart.getX() + 1, lastPart.getY()));
        } else if (lastPart.getX() - secondLastPart.getX() == 1) {
            // Add a new part to the top
            this.body.add(new Position(lastPart.getX(), lastPart.getY() - 1));
        } else {
            // Add a new part to the left
            this.body.add(new Position(lastPart.getX() - 1, lastPart.getY()));
        }
    }
}
