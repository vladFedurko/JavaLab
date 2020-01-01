package Lab_6;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {

    private Color color;
    private int radius;
    private int x;
    private int y;

    private static final int MAX_RADIUS = 30;
    private static final int MIN_RADIUS = 5;

    private int speed;
    private double speedX;
    private double speedY;

    private Field field;

    public BouncingBall() {
        radius = (int)(Math.random() * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS);
        speed = (int)(5D * MAX_RADIUS / radius);
        double angle = Math.random() * 2 * Math.PI;
        speedX = speed * Math.cos(angle);
        speedY = speed * Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        x = (int)(Math.random() * (field.getSize().getWidth() - 2 * radius) + radius);
        y = (int)(Math.random() * (field.getSize().getHeight() - 2 * radius) + radius);

        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x = field.getWidth() - radius;
                } else
                if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = field.getHeight() - radius;
                } else {
                    x += speedX;
                    y += speedY;
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException ignore) {

        }
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
