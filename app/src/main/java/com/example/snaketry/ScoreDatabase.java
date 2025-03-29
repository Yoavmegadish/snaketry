package com.example.snaketry;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Score.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class ScoreDatabase extends RoomDatabase {
    public abstract ScoreDAO scoreDAO();

    private static volatile ScoreDatabase INSTANCE;

    public static ScoreDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ScoreDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ScoreDatabase.class,
                            "snake_scores"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
} 