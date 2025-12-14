package ua.lab3.maze.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ua.lab3.maze.game.Maze;

public class MazeView extends View {
    private final Paint wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint exitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Maze maze;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        wallPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setStrokeWidth(6f);

        playerPaint.setStyle(Paint.Style.FILL);
        exitPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (maze == null) return;

        bgPaint.setColor(0xFFF7F7F7);
        wallPaint.setColor(0xFF111111);
        playerPaint.setColor(0xFF1565C0);
        exitPaint.setColor(0xFF2E7D32);

        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        int rows = maze.getRows();
        int cols = maze.getCols();

        float cellSize = Math.min(getWidth() / (float) cols, getHeight() / (float) rows);
        float offsetX = (getWidth() - cols * cellSize) / 2f;
        float offsetY = (getHeight() - rows * cellSize) / 2f;

        int[][] w = maze.getWalls();

        // walls
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float left = offsetX + c * cellSize;
                float top = offsetY + r * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;

                int mask = w[r][c];

                if ((mask & Maze.TOP) != 0) canvas.drawLine(left, top, right, top, wallPaint);
                if ((mask & Maze.RIGHT) != 0) canvas.drawLine(right, top, right, bottom, wallPaint);
                if ((mask & Maze.BOTTOM) != 0) canvas.drawLine(left, bottom, right, bottom, wallPaint);
                if ((mask & Maze.LEFT) != 0) canvas.drawLine(left, top, left, bottom, wallPaint);
            }
        }

        // exit
        float exLeft = offsetX + maze.getExitCol() * cellSize;
        float exTop = offsetY + maze.getExitRow() * cellSize;
        canvas.drawRect(exLeft + cellSize * 0.2f, exTop + cellSize * 0.2f,
                exLeft + cellSize * 0.8f, exTop + cellSize * 0.8f, exitPaint);

        // player
        float px = offsetX + (maze.getPlayerCol() + 0.5f) * cellSize;
        float py = offsetY + (maze.getPlayerRow() + 0.5f) * cellSize;
        canvas.drawCircle(px, py, cellSize * 0.28f, playerPaint);
    }
}
