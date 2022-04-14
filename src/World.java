import java.util.Observable;

/**
 * The class that act as the observer of the game. Ir is responsible for updating the game time.
 */
public class World extends Observable {

    /**
     * The time of the game.
     */
    private int tick;

    /**
     * Boolean that indicates if the game is running. It can use to pause the game too.
     */
    private Boolean isRunning;

    /**
     * Thread that use to always update the game time.
     */
    private Thread thread;

    /**
     * Delay between each update of the game time in milliseconds.
     */
    private long delayed = 200;

    /**
     * The constructor of the class.
     */
    public World() {
        this.tick = 0;
        this.isRunning = true;
    }

    /**
     * Start the thread that update the game time. This method must be called one time after the world is created.
     * Or you will see swift snake.
     */
    public void start() {
        this.isRunning = true;
        this.thread = new Thread(() -> {
            while (isRunning) {
                tick();
                try {
                    Thread.sleep(delayed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.thread.start();
    }

    /**
     * Mark isRunning to false to pause the game and prevent the thread from updating the game time.
     */
    public void continueGame() {
        this.isRunning = true;
    }

    /**
     * Mark isOver to true to stop the thread from updating the game time.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Update the game time.
     */
    private void tick() {
        this.tick++;
        setChanged();
        notifyObservers();
    }

    /**
     * Get the game time.
     * @return The game time.
     */
    public int getTick() {
        return this.tick;
    }

    /**
     * Set delay between each update of the game time.
     */
    public void setDelayed(long delayed) {
        this.delayed = delayed;
    }
}
