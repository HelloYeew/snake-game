public enum SnakeDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class SnakeDirectionUtil {
    public static SnakeDirection getOppositeDirection(SnakeDirection direction) {
        switch (direction) {
            case UP:
                return SnakeDirection.DOWN;
            case DOWN:
                return SnakeDirection.UP;
            case LEFT:
                return SnakeDirection.RIGHT;
            case RIGHT:
                return SnakeDirection.LEFT;
            default:
                return null;
        }
    }
}
