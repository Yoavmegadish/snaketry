package com.example.snaketry;
import android.view.Gravity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Game game = new Game(); // יצירת אובייקט של המשחק

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout mainLayout = findViewById(R.id.boardLayout); // לוח המשחק

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
                cell.setText(game.isHeadAt(i, j) ? "O" : "."); // לוודא שהראש מוצג
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
                game.moveUp();
                break;
            case "down":
                game.moveDown();
                break;
            case "left":
                game.moveLeft();
                break;
            case "right":
                game.moveRight();
                break;
        }
        // עדכון המסך אחרי הזזת הנחש
        drawBoard(findViewById(R.id.boardLayout));
    }
}
