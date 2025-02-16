package com.example.snaketry;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private Point head; // מיקום ראש הנחש
    private final int gridSize = 10; // גודל הלוח
    LinkedList<Point> snake = new LinkedList<>();
    public Game() {
        this.head = new Point(1, 0); // מתחילים את הראש במיקום (0,0)
        this.snake.addFirst(this.head);
        this.snake.addLast(new Point(0,0));
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
        tailMoveTo(p.getX(),p.getY());
    }

    // הזזת הנחש ימינה
    public void moveRight() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX(), this.head.getY() + 1); // למטה = y גדל
        tailMoveTo(p.getX(),p.getY());
    }

    // הזזת הנחש למעלה
    public void moveUp() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() - 1, this.head.getY()); // שמאלה = x קטן
        tailMoveTo(p.getX(),p.getY());

    }

    // הזזת הנחש למטה
    public void moveDown() {
        Point p=new Point(this.head);
        headMoveTo(this.head.getX() + 1, this.head.getY()); // ימינה = x גדל
        tailMoveTo(p.getX(),p.getY());

    }

    public String getHeadPosition() {
        return "(" + head.getX() + ", " + head.getY() + ")";
    }

    // פונקציה להזיז את הנחש לפי קואורדינטות חדשות
    private void headMoveTo(int newX, int newY) {
        Log.d("SnakeDebug", "Moving to: (" + newX + ", " + newY + ")");
        this.head.setX((newX + gridSize) % gridSize);
        this.head.setY((newY + gridSize) % gridSize);
    }
    private void tailMoveTo(int newX, int newY) {
        Log.d("SnakeDebug", "Moving to: (" + newX + ", " + newY + ")");
        this.snake.getLast().setX((newX + gridSize) % gridSize);
        this.snake.getLast().setY((newY + gridSize) % gridSize);
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
    public void spawnApple()
    {
        Random random=new Random();
        Point apple =null;
        while(apple==null) {
           apple= new Point(ThreadLocalRandom.current().nextInt(0, 10), ThreadLocalRandom.current().nextInt(0, 10));
           if(isPointInSnake(this.snake,apple))
           {
               apple=null;
           }

        }
    }
}

