package Lab_6;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class BouncingBall implements Runnable {

    private Color color;
    private int radius;
    private double x;
    private double y;

    public static final int MAX_RADIUS = 30;
    public static final int MIN_RADIUS = 5;

    private int speed;
    private double speedX;
    private double speedY;

    private Field field;

    public BouncingBall(@NotNull Field field) {
        this.field = field;
        radius = (int)(Math.random() * (MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS);
        speed = (int)(2D * MAX_RADIUS / radius);
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
                if(field.hasObstacle() && this.checkForObstacleHitting()) {

                } else {
                    if (y + speedY <= radius) {
                        speedY = -speedY;
                        y = speedY - y + 2 * radius;
                    } else if (y + speedY >= field.getHeight() - radius) {
                        speedY = -speedY;
                        y = -y - speedY + 2 * (field.getHeight() - radius);
                    } else {
                        y += speedY;
                    }
                    if (x + speedX <= radius) {
                        speedX = -speedX;
                        x = speedX - x + 2 * radius;
                    } else if (x + speedX >= field.getWidth() - radius) {
                        speedX = -speedX;
                        x = -x - speedX + 2 * (field.getWidth() - radius);
                    } else {
                        x += speedX;
                    }
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException ignore) {

        }
    }

    public void paint(@NotNull Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        this.correctCoordinates();
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }

    private void correctCoordinates() {
        if(x > (field.getSize().getWidth() - radius)) {
            x = field.getSize().getWidth() - radius;
        }
        if(y > (field.getSize().getHeight() - radius)) {
            y = field.getSize().getHeight() - radius;
        }
    }

    private boolean checkForObstacleHitting() {
        Obstacle obstacle = field.getObstacle();
        if(!processSpeedForSides(obstacle))
            return processSpeedForCorners(obstacle);
        return false;
    }

    private boolean processSpeedForSides(Obstacle obstacle) {
        boolean ans = false;
        if(x > obstacle.getX() && x < obstacle.getX2()) {
            if(y + radius <= obstacle.getY() && y + speedY + radius >= obstacle.getY()) {
                speedY = -speedY;
                y = 2 * (obstacle.getY() - radius) - y - speedY;
                x += speedX;
                ans = true;
            } else
            if(y - radius >= obstacle.getY2()
                    && y - radius + speedY <= obstacle.getY2()) {
                speedY = -speedY;
                y = 2 * (obstacle.getY2() + radius) - y - speedY;
                x += speedX;
                ans = true;
            }
        } else
        if(y > obstacle.getY() && y < obstacle.getY2()) {
            if (x + radius <= obstacle.getX() && x + speedX + radius >= obstacle.getX()) {
                speedX = -speedX;
                x = 2 * (obstacle.getX() - radius) - x - speedX;
                y += speedY;
                ans = true;
            } else if (x - radius >= obstacle.getX2()
                    && x - radius + speedX <= obstacle.getX2()) {
                speedX = -speedX;
                x = 2 * (obstacle.getX2() + radius) - x - speedX;
                y += speedY;
                ans = true;
            }
        }
        return ans;
    }

    private boolean processSpeedForCorners(Obstacle obstacle) {
        Point2D.Double center = new Point2D.Double(x, y);
        Point2D.Double corner = null;
        if((center.x < obstacle.getX() && center.y < obstacle.getY()) && (speedY > 0 || speedX > 0)
                && sqr(obstacle.getX() - center.x) + sqr(obstacle.getY() - center.y) <= sqr(radius + speed)) {
            corner = new Point2D.Double(obstacle.getX(), obstacle.getY());
        } else
        if((center.x > obstacle.getX2() && center.y < obstacle.getY()) && (speedY > 0 || speedX < 0)
                && sqr(obstacle.getX2() - center.x) + sqr(obstacle.getY() - center.y) <= sqr(radius + speed)) {
            corner = new Point2D.Double(obstacle.getX2(), obstacle.getY());
        } else
        if((center.x > obstacle.getX2() && center.y > obstacle.getY2()) && (speedY < 0 || speedX < 0)
                && sqr(obstacle.getX2() - center.x) + sqr(obstacle.getY2() - center.y) <= sqr(radius + speed)) {
            corner = new Point2D.Double(obstacle.getX2(), obstacle.getY2());
        } else
        if((center.x < obstacle.getX() && center.y > obstacle.getY2()) && (speedY < 0 || speedX > 0)
                && sqr(obstacle.getX() - center.x) + sqr(obstacle.getY2() - center.y) <= sqr(radius + speed)) {
            corner = new Point2D.Double(obstacle.getX(), obstacle.getY2());
        }
        if(corner != null)
            return calculateSpeedForCorner(center, corner);
        return false;
    }

    private boolean calculateSpeedForCorner(Point2D.Double center, Point2D.Double corner) {
        boolean ans = false;
        double b = speedX * (center.x - corner.x) + speedY * (center.y - corner.y);
        double D = sqr(b) - sqr(speed) * (sqr(center.x) + sqr(center.y) + sqr(corner.x) + sqr(corner.y) - sqr(radius)
                - 2 * center.x * corner.x - 2 * center.y * corner.y);
        if (D >= 0) {
            double t = (-b - Math.sqrt(D)) / sqr(speed);
            if (t <= 1 && t >= 0) {
                x += speedX * t;
                y += speedY * t;
                double distance = Math.sqrt(sqr(center.y - corner.y) + sqr(center.x - corner.x));
                Point2D.Double n = new Point2D.Double((center.y - corner.y) / distance,
                        (corner.x - center.x) / distance);
                double m = 2 * (speedX * n.x + speedY * n.y);
                speedX = m * n.x - speedX;
                speedY = m * n.y - speedY;
                x += speed * (1 - t);
                y += speed * (1 - t);
                ans = true;
            }
        }
        return ans;
    }

    @Contract(pure = true)
    private double sqr(double x) {
        return x * x;
    }
}
