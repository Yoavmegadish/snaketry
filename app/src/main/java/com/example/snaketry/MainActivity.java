package com.example.snaketry;
import android.content.Intent;
import android.view.Gravity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView pause;
   private Game game; // יצירת אובייקט של המשחק
    private TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int gameMode = getIntent().getIntExtra("GAME_MODE", 1); // ברירת מחדל למצב 1

        switch (gameMode) {
            case 1:
                gameMode1();
                break;
            case 2:
                gameMode2();
                break;
            case 3:
                gameMode3();
                break;
        }
        timerText = findViewById(R.id.timerText);
        LinearLayout mainLayout = findViewById(R.id.boardLayout); // לוח המשחק

        TextView pauseButton = findViewById(R.id.pausebtn);
        pauseButton.setOnClickListener(v -> {
            PauseFragment pauseFragment = new PauseFragment();
            pauseFragment.show(getSupportFragmentManager(), "pause");
            stopMoving();
        });

        // ציור הלוח בהתחלה
        drawBoard(mainLayout);

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

        for (int i = 0; i < 10; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < 10; j++) {
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
                    cell.setText("◼\uFE0F"); // תא ריק
                }

                cell.setWidth(100);
                cell.setHeight(100);
                cell.setTextSize(24f);
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


        movementThread = new Thread(() -> {
            while (isRunning) {
                try {
                    Thread.sleep(300); // השהייה של שנייה בין כל תנועה
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
                if(game.hasDuplicatePoint()||game.isPointInTraps(game.getHead()))
                {
                    rejection();
                }
                if (game.getApple().getX() == game.getHead().getX() && game.getApple().getY() == game.getHead().getY()) {
                    game.spawnApple();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    moveContinuously(currentDirection);
                    game.growSnake();
                }
                runOnUiThread(() -> drawBoard(findViewById(R.id.boardLayout))); // עדכון המסך
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
        // עושה את הטקסט של הטיימר גלוי
        timerText.setVisibility(View.VISIBLE);

        // יצירת טרד נפרד
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i >= 0; i--) {
                    try {
                        // עדכון הטקסט של הטיימר במיינתראד
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(finalI==0)
                                {
                                    timerText.setText("go!");
                                }
                                else {
                                    timerText.setText(Integer.toString(finalI));
                                }
                            }
                        });

                        // השהייה של שנייה אחת
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // אחרי שהספירה לאחור מסתיימת, אפשר להפסיק את המשחק
                // לדוגמה: להחזיר את מצב המשחק להפעלה רגילה
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resumeGame();
                    }
                });
            }
        });
        timerThread.start();
    }

    public void resumeGame() {
        timerText.setVisibility(View.GONE);
        moveContinuously(currentDirection);
    }
    public void rejection()
    {
        RejectionFragment rejectionFragment = new RejectionFragment();
        rejectionFragment.show(getSupportFragmentManager(), "rejection");
        stopMoving();

    }
    public void restart() {
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
}
