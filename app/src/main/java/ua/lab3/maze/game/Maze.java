package ua.lab3.maze.game;

public class Maze {
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 4;
    public static final int LEFT = 8;

    private final int rows;
    private final int cols;
    private final int[][] walls;

    private int playerRow;
    private int playerCol;

    private final int exitRow;
    private final int exitCol;

    public Maze(int rows, int cols, int[][] walls, int startRow, int startCol, int exitRow, int exitCol) {
        this.rows = rows;
        this.cols = cols;
        this.walls = walls;
        this.playerRow = startRow;
        this.playerCol = startCol;
        this.exitRow = exitRow;
        this.exitCol = exitCol;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int[][] getWalls() { return walls; }

    public int getPlayerRow() { return playerRow; }
    public int getPlayerCol() { return playerCol; }
    public int getExitRow() { return exitRow; }
    public int getExitCol() { return exitCol; }

    public void resetPlayer() {
        playerRow = 0;
        playerCol = 0;
    }

    public boolean isWin() {
        return playerRow == exitRow && playerCol == exitCol;
    }

    public boolean canMoveUp() {
        return (walls[playerRow][playerCol] & TOP) == 0 && playerRow > 0;
    }

    public boolean canMoveRight() {
        return (walls[playerRow][playerCol] & RIGHT) == 0 && playerCol < cols - 1;
    }

    public boolean canMoveDown() {
        return (walls[playerRow][playerCol] & BOTTOM) == 0 && playerRow < rows - 1;
    }

    public boolean canMoveLeft() {
        return (walls[playerRow][playerCol] & LEFT) == 0 && playerCol > 0;
    }

    public boolean moveUp() {
        if (!canMoveUp()) return false;
        playerRow--;
        return true;
    }

    public boolean moveRight() {
        if (!canMoveRight()) return false;
        playerCol++;
        return true;
    }

    public boolean moveDown() {
        if (!canMoveDown()) return false;
        playerRow++;
        return true;
    }

    public boolean moveLeft() {
        if (!canMoveLeft()) return false;
        playerCol--;
        return true;
    }
}
