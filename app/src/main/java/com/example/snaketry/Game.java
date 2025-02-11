package com.example.snaketry;

public class Game {
    private Point head; // מיקום ראש הנחש
    private final int gridSize = 10; // גודל הלוח

    public Game() {
        this.head = new Point(0, 0); // מתחילים את הראש במיקום (0,0)
    }

    // בודק אם ראש הנחש נמצא במקום מסוים
    public boolean isHeadAt(int x, int y) {
        return head.getX() == x && head.getY() == y;
    }

    // הזזת הנחש שמאלה
    public void moveLeft() {
        moveTo(head.getX() - 1, head.getY());
    }

    // הזזת הנחש ימינה
    public void moveRight() {
        moveTo(head.getX() + 1, head.getY());
    }

    // הזזת הנחש למעלה
    public void moveUp() {
        moveTo(head.getX(), head.getY() - 1);
    }

    // הזזת הנחש למטה
    public void moveDown() {
        moveTo(head.getX(), head.getY() + 1);
    }

    // פונקציה להזיז את הנחש לפי קואורדינטות חדשות
    private void moveTo(int newX, int newY) {
        // עדכון המיקום החדש של הראש
        head.setX((newX + gridSize) % gridSize);
        head.setY((newY + gridSize) % gridSize);
    }

    // הדפסת המיקום של ראש הנחש
    public void printHeadPosition() {
        System.out.println("Head: (" + head.getX() + ", " + head.getY() + ")");
    }
    }

