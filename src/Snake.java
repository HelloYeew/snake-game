import java.util.ArrayList;
import java.util.List;

public class Snake {
    public ArrayList<Position> body;

    private SnakeDirection direction;

    public Snake() {
        this.direction = SnakeDirection.RIGHT;
        this.body = new ArrayList<>();
    }

    public void addBodyPart(Position position) {
        this.body.add(position);
        for (int i = 0; i < this.body.size() - 1; i++) {
            // print each body part
            System.out.println(this.body.get(i));
        }
    }

    public SnakeDirection checkTailDirection() {
        if (this.body.size() > 1) {
            if (this.body.get(body.size() - 2).getX() == this.body.get(body.size() - 1).getX() + 1) {
                return SnakeDirection.LEFT;
            } else if (this.body.get(body.size() - 2).getX() == this.body.get(body.size() - 1).getX() - 1) {
                return SnakeDirection.RIGHT;
            } else if (this.body.get(body.size() - 2).getY() == this.body.get(body.size() - 1).getY() + 1) {
                return SnakeDirection.UP;
            } else if (this.body.get(body.size() - 2).getY() == this.body.get(body.size() - 1).getY() - 1) {
                return SnakeDirection.DOWN;
            }
        } else {
            // Currently the snake is only one body part long
            // Return the opposite direction of the current direction
            return SnakeDirectionUtil.getOppositeDirection(this.direction);
        }
        return direction;
    }

    public SnakeDirection getDirection() {
        return direction;
    }


    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
        System.out.println("Snake direction is now " + this.direction);
    }

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

    public void grow() {
        // Add a body part to the end of the snake
        // Depending on the direction
        switch (this.direction) {
            case LEFT ->
                    this.body.add(new Position(this.body.get(this.body.size() - 1).getX(), this.body.get(this.body.size() - 1).getY() + 1));
            case RIGHT ->
                    this.body.add(new Position(this.body.get(this.body.size() - 1).getX(), this.body.get(this.body.size() - 1).getY() - 1));
            case UP ->
                    this.body.add(new Position(this.body.get(this.body.size() - 1).getX() + 1, this.body.get(this.body.size() - 1).getY()));
            case DOWN ->
                    this.body.add(new Position(this.body.get(this.body.size() - 1).getX() - 1, this.body.get(this.body.size() - 1).getY()));
        }
    }
}
