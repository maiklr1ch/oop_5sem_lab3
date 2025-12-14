package ua.lab3.maze;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ua.lab3.maze.game.Difficulty;
import ua.lab3.maze.game.Maze;
import ua.lab3.maze.game.MazeGenerator;
import ua.lab3.maze.ui.MazeView;
import ua.lab3.maze.ui.SwipeController;

public class MainActivity extends AppCompatActivity implements SwipeController.OnSwipeListener {

    private static final String PREFS = "maze_prefs";

    private MazeView mazeView;
    private Spinner difficultySpinner;
    private TextView timerText;
    private TextView bestText;
    private Button newGameBtn;
    private Button resetBestBtn;

    private final MazeGenerator generator = new MazeGenerator();
    private Maze maze;

    private long startTimeMs = 0L;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable timerTick = new Runnable() {
        @Override public void run() {
            updateTimer();
            handler.postDelayed(this, 100);
        }
    };

    private SharedPreferences prefs;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        mazeView = findViewById(R.id.mazeView);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        timerText = findViewById(R.id.timerText);
        bestText = findViewById(R.id.bestText);
        newGameBtn = findViewById(R.id.newGameBtn);
        resetBestBtn = findViewById(R.id.resetBestBtn);

        setupDifficultySpinner();

        gestureDetector = new GestureDetector(this, new SwipeController(this));
        mazeView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        newGameBtn.setOnClickListener(v -> startNewGame());
        resetBestBtn.setOnClickListener(v -> resetBestForCurrentDifficulty());

        startNewGame();
    }

    private void setupDifficultySpinner() {
        String[] items = new String[]{"EASY (10x10)", "NORMAL (18x18)", "HARD (26x26)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        difficultySpinner.setAdapter(adapter);

        difficultySpinner.setSelection(0);
        difficultySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateBestText(getSelectedDifficulty());
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private Difficulty getSelectedDifficulty() {
        int pos = difficultySpinner.getSelectedItemPosition();
        if (pos == 1) return Difficulty.NORMAL;
        if (pos == 2) return Difficulty.HARD;
        return Difficulty.EASY;
    }

    private void startNewGame() {
        Difficulty d = getSelectedDifficulty();

        maze = generator.generate(d.rows, d.cols);
        mazeView.setMaze(maze);

        startTimeMs = System.currentTimeMillis();
        handler.removeCallbacks(timerTick);
        handler.post(timerTick);

        updateBestText(d);
        updateTimer();
    }

    private void updateTimer() {
        long now = System.currentTimeMillis();
        long elapsed = Math.max(0, now - startTimeMs);

        long totalSeconds = elapsed / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long tenths = (elapsed % 1000) / 100;

        timerText.setText(String.format("Time: %02d:%02d.%d", minutes, seconds, tenths));
    }

    private String bestKey(Difficulty d) {
        return "best_" + d.name();
    }

    private void updateBestText(Difficulty d) {
        long best = prefs.getLong(bestKey(d), -1);
        if (best <= 0) {
            bestText.setText("Best: --");
            return;
        }
        long totalSeconds = best / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long tenths = (best % 1000) / 100;
        bestText.setText(String.format("Best: %02d:%02d.%d", minutes, seconds, tenths));
    }

    private void trySaveBest(Difficulty d, long elapsedMs) {
        long best = prefs.getLong(bestKey(d), -1);
        if (best <= 0 || elapsedMs < best) {
            prefs.edit().putLong(bestKey(d), elapsedMs).apply();
        }
        updateBestText(d);
    }

    private void resetBestForCurrentDifficulty() {
        Difficulty d = getSelectedDifficulty();
        prefs.edit().remove(bestKey(d)).apply();
        updateBestText(d);
        Toast.makeText(this, "Best time reset for " + d.name(), Toast.LENGTH_SHORT).show();
    }

    private void onMoveAttempt(boolean moved) {
        if (maze == null) return;

        if (moved) {
            mazeView.invalidate();
            if (maze.isWin()) {
                handler.removeCallbacks(timerTick);
                long elapsed = System.currentTimeMillis() - startTimeMs;
                Difficulty d = getSelectedDifficulty();
                trySaveBest(d, elapsed);
                Toast.makeText(this, "You escaped! New best is saved if improved.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSwipeUp() {
        if (maze == null) return;
        onMoveAttempt(maze.moveUp());
    }

    @Override
    public void onSwipeRight() {
        if (maze == null) return;
        onMoveAttempt(maze.moveRight());
    }

    @Override
    public void onSwipeDown() {
        if (maze == null) return;
        onMoveAttempt(maze.moveDown());
    }

    @Override
    public void onSwipeLeft() {
        if (maze == null) return;
        onMoveAttempt(maze.moveLeft());
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timerTick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (maze != null && !maze.isWin()) {
            handler.post(timerTick);
        }
    }
}
