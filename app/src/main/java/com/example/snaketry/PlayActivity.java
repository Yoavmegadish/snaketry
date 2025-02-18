package com.example.snaketry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayActivity extends AppCompatActivity {

    private Button playbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play); // קודם טוענים את ה-XML

        // אתחול ה-Button אחרי טעינת ה-XML
        playbtn = findViewById(R.id.playbtn);

        // טיפול בלחיצה
        playbtn.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, ChooseGameModeActivity.class);
            startActivity(intent);
        });
    }
}
