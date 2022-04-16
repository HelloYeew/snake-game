import java.util.Observable;

/**
 * The class that act as the observer of the game. It is responsible for updating the game time and
 * store some game state.
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
     * Mark the state when the game want to prevent an input.
     */
    private Boolean isLockInput;

    /**
     * Mark the state when the game is in game over state.
     */
    private Boolean isGameOver;

    /**
     * Thread that use to always update the game time.
     */
    private Thread thread;

    /**
     * Delay between each update of the game time in milliseconds.
     */
    private long delayed = 100;

    /**
     * The constructor of the class.
     */
    public World() {
        this.tick = 0;
        this.isRunning = true;
        this.isLockInput = false;
        this.isGameOver = false;
    }

    /**
     * Start the thread that update the game time. This method must be called one time after the world is created.
     * Or you will see swift snake.
     */
    public void start() {
        this.isRunning = true;
        this.thread = new Thread(() -> {
            while (true) {
                if (isRunning) {
                    tick();
                    notifyObservers();
                }
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
     * Get the running state of the game.
     * @return The running state of the game.
     */
    public Boolean getRunning() {
        return isRunning;
    }

    /**
     * Set the running state of the game.
     * @param running The running state of the game that you want to set.
     */
    public void setRunning(Boolean running) {
        isRunning = running;
    }

    /**
     * Mark isRunning to true to continue the game and allow the thread to update the game time.
     */
    public void continueGame() {
        this.isRunning = true;
    }

    /**
     * Mark isRunning to false to pause the game and prevent the thread from updating the game time.
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
     * Reset the game time back to 0.
     */
    public void resetTick() {
        this.tick = 0;
    }

    /**
     * Set delay between each update of the game time.
     */
    public void setDelayed(long delayed) {
        this.delayed = delayed;
    }

    /**
     * Get the state of the lock input.
     * @return The state of the lock input.
     */
    public Boolean getLockInput() {
        return isLockInput;
    }

    /**
     * Set the state of the lock input.
     */
    public void setLockInput(Boolean lockInput) {
        isLockInput = lockInput;
    }

    /**
     * Mark isLockInput to true to prevent the input of the user.
     */
    public void lockInput() {
        isLockInput = true;
    }

    /**
     * Mark isLockInput to false to allow the input of the user.
     */
    public void unlockInput() {
        isLockInput = false;
    }

    public Boolean getGameOver() {
        return isGameOver;
    }

    public void setGameOver(Boolean gameOver) {
        isGameOver = gameOver;
    }
}
