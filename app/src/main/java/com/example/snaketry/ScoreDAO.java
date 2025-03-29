package com.example.snaketry;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScoreDAO {
    @Insert
    void insert(Score score);

    @Update
    void update(Score score);

    @Query("SELECT * FROM scores")
    List<Score> getAllScores();

    @Query("SELECT MAX(score) FROM scores")
    int getBestScore();

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    LiveData<List<Score>> getTopScores();

    @Query("SELECT * FROM scores ORDER BY date DESC LIMIT 1")
    Score getLastScore();
}