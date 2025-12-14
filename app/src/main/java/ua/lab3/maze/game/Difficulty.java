package ua.lab3.maze.game;

public enum Difficulty {
    EASY(10, 10),
    NORMAL(18, 18),
    HARD(26, 26);

    public final int cols;
    public final int rows;

    Difficulty(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }
}
