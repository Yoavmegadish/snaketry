package com.example.snaketry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseGameModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_mode);

        TextView gameMode1 = findViewById(R.id.gameMode1);
        TextView gameMode2 = findViewById(R.id.gameMode2);
        TextView gameMode3 = findViewById(R.id.gameMode3);

        gameMode1.setOnClickListener(v -> startGame(1));
        gameMode2.setOnClickListener(v -> startGame(2));
        gameMode3.setOnClickListener(v -> startGame(3));
    }

    private void startGame(int mode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("GAME_MODE", mode);
        startActivity(intent);
        finish();
    }
}
