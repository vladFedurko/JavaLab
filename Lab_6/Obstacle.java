package Lab_6;

import java.awt.*;

public class Obstacle {

    private int x;
    private int y;
    private int sizeX;
    private int sizeY;

    public static final int MAX_SIZE = 30;
    public static final int MIN_SIZE = 5;

    private Color color;

    public Obstacle(Field field) {
        sizeX = (int)(Math.random() * (MAX_SIZE - MIN_SIZE)) + MIN_SIZE;
        sizeY = (int)(Math.random() * (MAX_SIZE - MIN_SIZE)) + MIN_SIZE;
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        x = (int)(Math.random() * (field.getSize().getWidth() - sizeX) + sizeX / 2);
        y = (int)(Math.random() * (field.getSize().getHeight() - sizeY) + sizeY / 2);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
