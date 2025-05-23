package com.example.snaketry;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Point head; // מיקום ראש הנחש
    private LinkedList<Point> snake = new LinkedList<>();
    private Point apple;
    private int trapsSize;
    private Point []traps;
    private int score;
    
    public Game(int trapsSize) {
        this.head = new Point(GameConfig.INITIAL_SNAKE_X, GameConfig.INITIAL_SNAKE_Y); // מתחילים את הראש במיקום המוגדר
        this.snake.addFirst(this.head);
        this.snake.addLast(new Point(GameConfig.INITIAL_SNAKE_TAIL_X, GameConfig.INITIAL_SNAKE_TAIL_Y));
        spawnApple();
        this.trapsSize = trapsSize;
        this.traps = new Point[this.trapsSize];
        Random random = new Random();

        for (int i = 0; i < this.trapsSize; i++) {
            int x = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-X
            int y = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-Y
            Point p = new Point(x, y);

            // אם המיקום חופף לנחש או תפוח, מחפשים מיקום חדש
            while (isPointInSnake(this.snake, p) || p.equals(this.apple)) {
                x = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-X
                y = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-Y
                p = new Point(x, y);
            }

            this.traps[i] = p;  // מיקום המלכודת
        }
        this.score = 0;
    }
    public void addScore()
    {
     this.score=1+this.score;
    }
    public int getScore()
    {
        return this.score;
    }
    public int getTrapsSize()
    {
        return this.trapsSize;
    }
    public void setTrapsSize(int trapsSize)
    {
        this.trapsSize=trapsSize;
    }
    public Point getHead()
    {
        return this.head;
    }
    public LinkedList<Point> getSnake() {
        return this.snake;
    }

    // בודק אם ראש הנחש נמצא במקום מסוים
    public boolean isHeadAt(int x, int y) {
        return head.getX() == x && head.getY() == y;
    }

    // הזזת הנחש שמאלה
    // הזזת הנחש שמאלה
    public void moveLeft() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX(), this.head.getY() - 1); // למעלה = y קטן
    }

    // הזזת הנחש ימינה
    public void moveRight() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX(), this.head.getY() + 1); // למטה = y גדל
    }

    // הזזת הנחש למעלה
    public void moveUp() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() - 1, this.head.getY()); // שמאלה = x קטן

    }

    // הזזת הנחש למטה
    public void moveDown() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() + 1, this.head.getY()); // ימינה = x גדל

    }

    public String getHeadPosition() {
        return "(" + head.getX() + ", " + head.getY() + ")";
    }

    // פונקציה להזיז את הנחש לפי קואורדינטות חדשות
    private void headMoveTo(int newX, int newY) {
        Log.d("SnakeDebug", "Moving to: (" + newX + ", " + newY + ")");
        this.snake.removeLast();
        Point p = new Point((newX + GameConfig.GRID_SIZE) % GameConfig.GRID_SIZE,
                          (newY + GameConfig.GRID_SIZE) % GameConfig.GRID_SIZE);
        this.head = new Point(p);
        snake.addFirst(p);
    }
    public void growSnake()
    {
        Point p=new Point(this.snake.getLast());
        this.snake.addLast(p);
    }


    // הדפסת המיקום של ראש הנחש
    public void printHeadPosition() {
        System.out.println("Head: (" + head.getX() + ", " + head.getY() + ")");
    }
    public boolean isPointInSnake(LinkedList<Point> snake, Point target) {
        Iterator<Point> iterator = snake.iterator(); // יצירת איטרטור לרשימה
        while (iterator.hasNext()) {
            if (iterator.next().equals(target)) {  // בדיקה אם הנקודה קיימת
                return true;
            }
        }
        return false;
    }
    public Point getApple()
    {
        Point p=new Point(this.apple);
        return p;
    }
    public void deleteApple()
    {
        this.apple=null;
    }
    public void spawnApple() {
        // מאפס את התפוח הקודם
        this.deleteApple();
        Random random = new Random();
        // לולאת ניסיון לייצר מיקום רנדומלי שלא חופף עם הנחש
        while (this.apple == null) {
            // יצירת מיקום רנדומלי לתפוח
            int x = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-X
            int y = random.nextInt(GameConfig.RANDOM_RANGE);  // מיקום רנדומלי על ציר ה-Y

            // יצירת אובייקט נקודה חדש עבור התפוח
            this.apple = new Point(x, y);

            // אם התפוח נמצא באותו מיקום כמו גוף הנחש, נייצר מיקום חדש
            if (isPointInSnake(this.snake, this.apple)) {
                this.apple = null;  // ביטול המיקום הנוכחי אם הוא חופף עם הנחש
            }
        }
    }
    public boolean hasDuplicatePoint() {
        for (int i = 0; i < this.snake.size(); i++) {
            Point current = this.snake.get(i);
            Iterator<Point> iterator = this.snake.listIterator(i + 1);

            while (iterator.hasNext()) {
                if (current.equals(iterator.next())) {
                    return true; // נמצא כפילות
                }
            }
        }
        return false; // אין כפילויות
    }
    public boolean isPointInTraps(Point point)
    {
        for(int i=0;i<this.traps.length;i++)
        {
            if(point.equals(this.traps[i]))
                return true;
        }
        return false;
    }
}

