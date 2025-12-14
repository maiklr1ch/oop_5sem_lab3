package ua.lab3.maze.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    private final Random random = new Random();

    public Maze generate(int rows, int cols) {
        // initially all walls exist
        int[][] walls = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                walls[r][c] = Maze.TOP | Maze.RIGHT | Maze.BOTTOM | Maze.LEFT;
            }
        }

        carve(0, 0, rows, cols, walls, visited);

        // start fixed at (0,0), exit at (rows-1, cols-1)
        return new Maze(rows, cols, walls, 0, 0, rows - 1, cols - 1);
    }

    private void carve(int r, int c, int rows, int cols, int[][] walls, boolean[][] visited) {
        visited[r][c] = true;

        List<int[]> dirs = new ArrayList<>();
        // dr, dc, currentWall, neighborWall
        dirs.add(new int[]{-1, 0, Maze.TOP, Maze.BOTTOM});
        dirs.add(new int[]{0, 1, Maze.RIGHT, Maze.LEFT});
        dirs.add(new int[]{1, 0, Maze.BOTTOM, Maze.TOP});
        dirs.add(new int[]{0, -1, Maze.LEFT, Maze.RIGHT});
        Collections.shuffle(dirs, random);

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
            if (visited[nr][nc]) continue;

            // remove walls between current and neighbor
            walls[r][c] &= ~d[2];
            walls[nr][nc] &= ~d[3];

            carve(nr, nc, rows, cols, walls, visited);
        }
    }
}
