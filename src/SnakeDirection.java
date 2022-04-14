/**
 * Enum for the direction of the snake.
 */
public enum SnakeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

/**
 * Utility class for the SnakeDirection enum.
 */
class SnakeDirectionUtil {
    /**
     * Returns the opposite direction of the given direction.
     * @param direction The direction to get the opposite of.
     * @return The opposite direction.
     */
    public static SnakeDirection getOppositeDirection(SnakeDirection direction) {
        return switch (direction) {
            case UP -> SnakeDirection.DOWN;
            case DOWN -> SnakeDirection.UP;
            case LEFT -> SnakeDirection.RIGHT;
            case RIGHT -> SnakeDirection.LEFT;
        };
    }
}
