package life;

import java.util.Random;

public class State {
    private final int size;
    private final Random rnd;
    private final boolean[][] current;
    private final boolean[][] previous;
    private int generation = 0;

    public State(int size) {
        this.size = size;
        rnd = new Random();
        current = new boolean[size][size];
        previous = new boolean[size][size];
        init();
    }

    public boolean[][] getCurrent() {
        return current;
    }

    public void evolve() {
        for (int i = 0; i < size; i++) {
            previous[i] = current[i].clone();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                current[i][j] = decideFate(i, j);
            }
        }
        generation++;
    }

    public int getAliveCount() {
        int count = 0;
        for (boolean[] row : getCurrent()) {
            for (boolean alive : row) {
                if (alive) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getGeneration() {
        return generation;
    }

    public int getSize() {
        return size;
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                current[i][j] = rnd.nextBoolean();
            }
        }
    }

    private boolean decideFate(int i, int j) {
        int neighbors = 0;

        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }
                int row = Math.floorMod(i + k, size);
                int col = Math.floorMod(j + l, size);
                if (previous[row][col]) {
                    neighbors++;
                }
            }
        }
        if (previous[i][j]) {
            return neighbors >=2 && neighbors <=3;
        } else {
            return neighbors == 3;
        }
    }

}
