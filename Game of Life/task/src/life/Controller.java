package life;

public class Controller {

    private State state;
    private final GameOfLife gui;
    private int size = 10;
    volatile private boolean paused = false;
    private final int stepDuration = 500;

    public Controller(GameOfLife gui) {
        this.gui = gui;
        this.state = new State(size);
    }

    public void newGame() {
        gui.startUpdate(state);
        playGame();
    }

    public synchronized void startStop() {
        paused = !paused;
        if (!paused) {
            notify();
        }
    }

    public synchronized void resetGame() {
        state = new State(size);
    }


    public void setSize(int size) {
        this.size = size;
        resetGame();
    }

    private void playGame() {
        while (true) {
            try {
                Thread.sleep(stepDuration);
                synchronized (this) {
                    while(paused) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            state.evolve();
            gui.startUpdate(state);
            if (state.getGeneration() > 200) {
                break;
            }
        }
    }
}
