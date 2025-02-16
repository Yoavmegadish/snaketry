package com.example.snaketry;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Point head; // מיקום ראש הנחש
    private final int gridSize = 50; // גודל הלוח
    private LinkedList<Point> snake = new LinkedList<>();
    private Point apple;

    public Game() {
        this.head = new Point(1, 0); // מתחילים את הראש במיקום (0,0)
        this.snake.addFirst(this.head);
        this.snake.addLast(new Point(0,0));
        spawnApple();
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
        //tailMoveTo(p.getX(),p.getY());
    }

    // הזזת הנחש ימינה
    public void moveRight() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX(), this.head.getY() + 1); // למטה = y גדל
       // tailMoveTo(p.getX(),p.getY());
    }

    // הזזת הנחש למעלה
    public void moveUp() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() - 1, this.head.getY()); // שמאלה = x קטן
       // tailMoveTo(p.getX(),p.getY());

    }

    // הזזת הנחש למטה
    public void moveDown() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() + 1, this.head.getY()); // ימינה = x גדל
       // tailMoveTo(p.getX(),p.getY());

    }

    public String getHeadPosition() {
        return "(" + head.getX() + ", " + head.getY() + ")";
    }

    // פונקציה להזיז את הנחש לפי קואורדינטות חדשות
    private void headMoveTo(int newX, int newY) {
        Log.d("SnakeDebug", "Moving to: (" + newX + ", " + newY + ")");
        this.snake.removeLast();
        Point p=new Point((newX + gridSize) % gridSize,(newY + gridSize) % gridSize);
        this.head=new Point(p);
        snake.addFirst(p);

    }
    /*private void tailMoveTo(int newX, int newY) {
        Log.d("SnakeDebug", "Moving to: (" + newX + ", " + newY + ")");
        this.snake.getLast().setX((newX + gridSize) % gridSize);
        this.snake.getLast().setY((newY + gridSize) % gridSize);
    }*/

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
            int x = random.nextInt(10);  // מיקום רנדומלי על ציר ה-X (מ-0 עד 9)
            int y = random.nextInt(10);  // מיקום רנדומלי על ציר ה-Y (מ-0 עד 9)

            // יצירת אובייקט נקודה חדש עבור התפוח
            this.apple = new Point(x, y);

            // אם התפוח נמצא באותו מיקום כמו גוף הנחש, נייצר מיקום חדש
            if (isPointInSnake(this.snake, this.apple)) {
                this.apple = null;  // ביטול המיקום הנוכחי אם הוא חופף עם הנחש
            }
        }
    }
}

