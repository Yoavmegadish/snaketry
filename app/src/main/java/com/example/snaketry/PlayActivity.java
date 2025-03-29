package com.example.snaketry;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayActivity extends AppCompatActivity {

    private Button playbtn;
    private ScoreDatabase db;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "SnakeGamePrefs";
    private static final String LAST_SCORE_KEY = "LAST_SCORE";
    private MusicService musicService;
    private boolean isBound = false;
    private Switch musicSwitch;
    private boolean isMusicPlaying = true;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            musicSwitch.setChecked(isMusicPlaying);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);

        // Initialize database and preferences
        db = ScoreDatabase.getDatabase(this);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // אתחול ה-Button אחרי טעינת ה-XML
        playbtn = findViewById(R.id.playbtn);
        TextView bestScoreText;
        TextView lastScoreText;
        
        // טיפול בלחיצה
        playbtn.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, ChooseGameModeActivity.class);
            startActivity(intent);
        });
        
        bestScoreText = findViewById(R.id.bestScore); // חפש את תצוגת השיא
        lastScoreText = findViewById(R.id.lastScore); // חפש את תצוגת הסקור האחרון

        // טען את השיא והסקור האחרון והצג אותם
        int bestScore = getBestScore();
        int lastScore = getLastScore();
        bestScoreText.setText("Best Score: " + bestScore);
        lastScoreText.setText("Last Score: " + lastScore);

        // Bind to MusicService
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        // Initialize music switch
        musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isMusicPlaying = isChecked;
            if (isBound) {
                if (isChecked) {
                    musicService.startMusic();
                } else {
                    musicService.pauseMusic();
                }
            }
        });
    }

    private int getBestScore() {
        return db.scoreDAO().getBestScore();
    }

    private int getLastScore() {
        return prefs.getInt(LAST_SCORE_KEY, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
}

