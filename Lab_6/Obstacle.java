package Lab_6;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Obstacle implements Component{

    private int x;
    private int y;
    private int sizeX;
    private int sizeY;

    public static final int MAX_SIZE = 100;
    public static final int MIN_SIZE = 30;

    private Color color;
    private Field field;

    public Obstacle(@NotNull Field field) {
        this.field = field;
        sizeX = (int)(Math.random() * (MAX_SIZE - MIN_SIZE)) + MIN_SIZE;
        sizeY = (int)(Math.random() * (MAX_SIZE - MIN_SIZE)) + MIN_SIZE;
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        x = (int)(Math.random() * (field.getSize().getWidth() - sizeX));
        y = (int)(Math.random() * (field.getSize().getHeight() - sizeY));
    }

    public void paint(@NotNull Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        this.correctCoordinates();
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, sizeX, sizeY);
        canvas.draw(rect);
        canvas.fill(rect);
    }

    private void correctCoordinates() {
        if(x > (field.getSize().getWidth() - sizeX)) {
            x = (int)(field.getSize().getWidth() - sizeX);
        }
        if(y > (field.getSize().getHeight() - sizeY)) {
            y = (int)(field.getSize().getHeight() - sizeY);
        }
        if(x < 0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }
    }

    public boolean isInside(Point point) {
        if(point.x >= x && point.y >= y && point.x <= this.getX2() && point.y <= this.getY2())
            return true;
        return false;
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

    public int getX2() {
        return x + sizeX;
    }

    public int getY2() {
        return y + sizeY;
    }
}
