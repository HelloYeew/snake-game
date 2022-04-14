import java.util.Observable;

public class World extends Observable {
    private int tick;

    private Boolean isOver;

    private Thread thread;

    private long delayed = 200;

    public World() {
        this.tick = 0;
        this.isOver = false;
    }

    public void start() {
        this.isOver = false;
        this.thread = new Thread(() -> {
            while (!isOver) {
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

    public void continueGame() {
        this.isOver = false;
    }

    public void stop() {
        this.isOver = true;
    }

    private void tick() {
        this.tick++;
        setChanged();
        notifyObservers();
    }

    public int getTick() {
        return this.tick;
    }

    public void setDelayed(long delayed) {
        this.delayed = delayed;
    }
}
