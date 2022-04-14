/**
 * The class that mock the position or Vector2D in the game.
 */
public class Position {

    /**
     * The x position.
     */
    private int x;

    /**
     * The y position.
     */
    private int y;

    /**
     * The constructor of the class.
     * @param x the x position.
     * @param y the y position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x position.
     * @return the x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x position.
     * @param x the x position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y position.
     * @return the y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y position.
     * @param y the y position.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the position as a string.
     * @return the position as a string.
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
