package com.example.snaketry;

public class GameConfig {
    // Game board configuration
    public static final int GRID_SIZE = 10;
    public static final int INITIAL_SNAKE_X = 1;
    public static final int INITIAL_SNAKE_Y = 0;
    public static final int INITIAL_SNAKE_TAIL_X = 0;
    public static final int INITIAL_SNAKE_TAIL_Y = 0;
    public static final int RANDOM_RANGE = GRID_SIZE;

    // UI configuration
    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 100;
    public static final float CELL_TEXT_SIZE = 24f;
    public static final int MOVEMENT_DELAY_MS = 300;
    public static final int COUNTDOWN_DURATION_MS = 4000;
    public static final int COUNTDOWN_INTERVAL_MS = 1000;

    // Game mode configuration
    public static final int DEFAULT_GAME_MODE = 1;
    public static final int GAME_MODE_1 = 1;
    public static final int GAME_MODE_2 = 2;
    public static final int GAME_MODE_3 = 3;
} 