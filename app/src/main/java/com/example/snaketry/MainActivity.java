package com.example.snaketry;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private TextView pause;
    private Game game; // יצירת אובייקט של המשחק
    private TextView timerText;
    private TextView score;
    private ScoreDatabase db;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "SnakeGamePrefs";
    private static final String LAST_SCORE_KEY = "LAST_SCORE";
    private ExecutorService executorService;
    private MusicService musicService;
    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            musicService.startMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start MusicService
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Initialize database and preferences
        db = ScoreDatabase.getDatabase(this);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        executorService = Executors.newSingleThreadExecutor();

        int gameMode = getIntent().getIntExtra("GAME_MODE", GameConfig.DEFAULT_GAME_MODE);

        switch (gameMode) {
            case GameConfig.GAME_MODE_1:
                gameMode1();
                break;
            case GameConfig.GAME_MODE_2:
                gameMode2();
                break;
            case GameConfig.GAME_MODE_3:
                gameMode3();
                break;
        }
        timerText = findViewById(R.id.timerText);
        score = findViewById(R.id.score);
        LinearLayout mainLayout = findViewById(R.id.boardLayout); // לוח המשחק
        TextView pauseButton = findViewById(R.id.pausebtn);
        pauseButton.setOnClickListener(v -> {
            PauseFragment pauseFragment = new PauseFragment();
            pauseFragment.show(getSupportFragmentManager(), "pause");
            stopMoving();
        });
         // ציור הלוח בהתחלה
        drawBoard(mainLayout);
        score.setText(String.valueOf("score:"+game.getScore()));
        // חיבור כפתורי השליטה
        Button buttonUp = findViewById(R.id.buttonUp);
        Button buttonDown = findViewById(R.id.buttonDown);
        Button buttonLeft = findViewById(R.id.buttonLeft);
        Button buttonRight = findViewById(R.id.buttonRight);
        // חיבור האזנות הקלקה לכפתורים
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAndUpdate("up");
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAndUpdate("down");
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAndUpdate("left");
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveAndUpdate("right");
            }
        });
    }

    // ציור הלוח
    private void drawBoard(LinearLayout mainLayout) {
        mainLayout.removeAllViews();
        for (int i = 0; i < GameConfig.GRID_SIZE; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < GameConfig.GRID_SIZE; j++) {
                TextView cell = new TextView(this);
                Point currentPoint = new Point(i, j);

                if (game.getSnake().getFirst().equals(currentPoint)) {
                    cell.setText("\uD83D\uDC0D"); // ראש הנחש
                } else if (game.isPointInSnake(game.getSnake(),currentPoint)) {
                    cell.setText("\uD83D\uDFE2");// זנב הנחש
                }else if(game.getApple().equals(currentPoint)){
                    cell.setText("\uD83C\uDF4E");
                }
                else if(game.isPointInTraps(currentPoint)) {
                    cell.setText("\uD83D\uDD78\uFE0F");
                }
                else {
                    cell.setText(""); // תא ריק
                }

                cell.setWidth(GameConfig.CELL_WIDTH);
                cell.setHeight(GameConfig.CELL_HEIGHT);
                cell.setTextSize(GameConfig.CELL_TEXT_SIZE);
                cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                rowLayout.addView(cell);
            }
            mainLayout.addView(rowLayout);
        }
        System.out.println("Board updated");
    }


    // פונקציה להזזה ועדכון המסך
    private void moveAndUpdate(String direction) {
        // הזזת ראש הנחש בהתאם לכיוון
        switch (direction) {
            case "up":
                moveContinuously("up");

                break;
            case "down":
                moveContinuously("down");

                break;
            case "left":
                moveContinuously("left");
                break;
            case "right":
                moveContinuously("right");
                break;
        }
        // עדכון המסך אחרי הזזת הנחש
        drawBoard(findViewById(R.id.boardLayout));
    }
    private boolean isRunning = false;
    private Thread movementThread;
    private String currentDirection = "";

    // פונקציה להתחלת התנועה לכיוון מסוים
    private void moveContinuously(String direction) {
        if (direction.equals(currentDirection) && isRunning) return; // אם כבר רצים לכיוון הזה, לא צריך לשנות כלום
        if ((currentDirection.equals("left") && direction.equals("right")) ||
                (currentDirection.equals("right") && direction.equals("left")) ||
                (currentDirection.equals("up") && direction.equals("down")) ||
                (currentDirection.equals("down") && direction.equals("up"))) {
            return; // אם מנסים לפנות לכיוון הנגדי, לא לעשות כלום
        }
        stopMoving(); // לעצור את התנועה הקודמת
        currentDirection = direction;
        isRunning = true;

        movementThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(300); // השהייה של 300 מילישניות בין כל תנועה
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }

                    // הזזת הנחש בהתאם לכיוון הנוכחי
                    switch (currentDirection) {
                        case "right":
                            game.moveRight();
                            break;
                        case "left":
                            game.moveLeft();
                            break;
                        case "up":
                            game.moveUp();
                            break;
                        case "down":
                            game.moveDown();
                            break;
                    }

                    if (game.hasDuplicatePoint() || game.isPointInTraps(game.getHead())) {
                        rejection();
                    }

                    if (game.getApple().getX() == game.getHead().getX() && game.getApple().getY() == game.getHead().getY()) {
                        game.spawnApple();
                        moveContinuously(currentDirection);
                        game.growSnake();
                        game.addScore();

                        // עדכון תצוגת הניקוד על ה-UI Thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                score.setText("score:" + String.valueOf(game.getScore()));
                            }
                        });
                    }

                    // עדכון המסך על ה-UI Thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawBoard(findViewById(R.id.boardLayout));
                        }
                    });
                }
            }
        });
        movementThread.start();
    }

        // פונקציה לעצירת התנועה
    private void stopMoving() {
        isRunning = false;
        if (movementThread != null) {
            movementThread.interrupt(); // ניסיון לעצור את ה-Thread הקודם
        }
    }
    public void startCountdown() {
        // הצגת הטיימר
        timerText.setVisibility(View.VISIBLE);

        new CountDownTimer(GameConfig.COUNTDOWN_DURATION_MS, GameConfig.COUNTDOWN_INTERVAL_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                if (secondsLeft == 0) {
                    timerText.setText("Go!");
                } else {
                    timerText.setText(String.valueOf(secondsLeft));
                }
            }

            @Override
            public void onFinish() {
                resumeGame();
            }
        }.start();
    }


    public void resumeGame() {
        timerText.setVisibility(View.GONE);
        moveContinuously(currentDirection);
    }
    public void rejection()
    {
        updateBestScore();
        RejectionFragment rejectionFragment = new RejectionFragment();
        rejectionFragment.show(getSupportFragmentManager(), "rejection");
        stopMoving();

    }
    public void restart() {
        updateBestScore();
        int x= game.getTrapsSize();
         game = new Game(x);
        drawBoard(findViewById(R.id.boardLayout));
    }
    public void gameMode1()
    {
        game=new Game(0);
        drawBoard(findViewById(R.id.boardLayout));
    }
    public void gameMode2()
    {
        game=new Game(6);
        drawBoard(findViewById(R.id.boardLayout));
    }
    public void gameMode3()
    {
        game=new Game(10);
        drawBoard(findViewById(R.id.boardLayout));
    }
    private void updateBestScore() {
        int currentScore = game.getScore();
        
        // Save last score to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_SCORE_KEY, currentScore);
        editor.apply();

        // Save score to database using Room
        executorService.execute(() -> {
            Score score = new Score(currentScore);
            db.scoreDAO().insert(score);
        });
    }

    public int getLastScore() {
        return prefs.getInt(LAST_SCORE_KEY, 0);
    }

    public int getBestScore() {
        return db.scoreDAO().getBestScore();
    }

    public LiveData<List<Score>> getTopScores() {
        return db.scoreDAO().getTopScores();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}
