package Lab_6;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class BouncingBall implements Runnable, Component {

    private Color color;

    private int radius;
    private double x;
    private double y;
    private boolean stopped;

    public static final int MAX_RADIUS = 30;
    public static final int MIN_RADIUS = 5;

    private double speed;

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
                this.checkStopped(this);
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
                this.correctCoordinates();
                Thread.sleep(Main6.UPDATE_TIME);
            }
        } catch (InterruptedException ignore) {

        }
    }

    private synchronized void checkStopped(BouncingBall bouncingBall) throws InterruptedException {
        if(stopped)
            wait();
    }

    public boolean isInside(Point point) {
        if(sqr(point.getX() - (int)x) + sqr(point.getY() - (int)y) <= sqr(radius))
            return true;
        return false;
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
        if(x < 0) {
            x = radius;
        }
        if(y < 0) {
            y = radius;
        }
        if(field.hasObstacle()) {
            Obstacle obs = field.getObstacle();
            this.correctCoordinatesForSides(obs);
            Point2D.Double center = new Point2D.Double(x, y);
            if(this.isInside(new Point(obs.getX(), obs.getY()))) {
                this.correctCoordinatesForCorner(center,
                        new Point2D.Double(obs.getX(), obs.getY()), obs);
            } else
            if(this.isInside(new Point(obs.getX(), obs.getY2()))){
                this.correctCoordinatesForCorner(center,
                        new Point2D.Double(obs.getX(), obs.getY2()), obs);
            } else
            if(this.isInside(new Point(obs.getX2(), obs.getY2()))){
                this.correctCoordinatesForCorner(center,
                        new Point2D.Double(obs.getX2(), obs.getY2()), obs);
            } else
            if(this.isInside(new Point(obs.getX2(), obs.getY()))){
                this.correctCoordinatesForCorner(center,
                        new Point2D.Double(obs.getX2(), obs.getY()), obs);
            }
        }
    }

    private void correctCoordinatesForSides(Obstacle obs) {
        if(y > obs.getY() && y < obs.getY2()) {
            if(x + radius > obs.getX() && x < obs.getX()) {
                x = obs.getX() - radius;
                if(speedX > 0)
                    speedX = -speedX;
            } else
            if(x - radius < obs.getX2() && x > obs.getX2()) {
                x = obs.getX2() + radius;
                if(speedX < 0)
                    speedX = -speedX;
            } else
            if(x > obs.getX() && x < obs.getX2()) {
                //TODO
            }
        } else
        if(x > obs.getX() && x < obs.getX2()) {
            if(y + radius > obs.getY() && y < obs.getY()) {
                y = obs.getY() - radius;
                if(speedY > 0)
                    speedY = -speedY;
            } else
            if(y - radius < obs.getY2() && y > obs.getY2()) {
                y = obs.getY2() + radius;
                if(speedY < 0)
                    speedY = -speedY;
            }
        }
    }

    private void correctCoordinatesForCorner(Point2D.Double center, Point2D.Double corner, Obstacle obs) {
        double relSpeedX = speedX - obs.getSpeedX();
        double relSpeedY = speedY - obs.getSpeedY();
        Double t = this.calculateTimeForCornerHitting(center, corner, relSpeedX, relSpeedY);
        if(t != null && t < 0) {
            System.out.println(t);
            this.calculateAndApplySpeedForCornerHitting(center, corner, relSpeedX, relSpeedY, t);
            speedX = speedX + obs.getSpeedX();
            speedY = speedY + obs.getSpeedY();
            this.normalizeSpeed();
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
                y = obstacle.getY() - radius;
                if(speedY > 0)
                    speedY = -speedY;
                x += speedX;
                ans = true;
            } else
            if(y - radius >= obstacle.getY2()
                    && y - radius + speedY <= obstacle.getY2()) {
                y = obstacle.getY2() + radius;
                if(speedY < 0)
                    speedY = -speedY;
                x += speedX;
                ans = true;
            }
        } else
        if(y > obstacle.getY() && y < obstacle.getY2()) {
            if (x + radius <= obstacle.getX() && x + speedX + radius >= obstacle.getX()) {
                if(speedX > 0)
                    speedX = -speedX;
                x = obstacle.getX() - radius;
                y += speedY;
                ans = true;
            } else if (x - radius >= obstacle.getX2()
                    && x - radius + speedX <= obstacle.getX2()) {
                if(speedX < 0)
                    speedX = -speedX;
                x = obstacle.getX2() + radius;
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
        Double t = this.calculateTimeForCornerHitting(center, corner, speedX, speedY);
        if(t != null) {
            if (t <= 1 && t >= 0) {
                calculateAndApplySpeedForCornerHitting(center, corner, speedX, speedY, t);
                x += speedX * (1 - t);
                y += speedY * (1 - t);
                return true;
            }
        }
        return false;
    }

    private void calculateAndApplySpeedForCornerHitting(Point2D.Double center, Point2D.Double corner, double speedX, double speedY, double t) {
        x += speedX * t;
        y += speedY * t;
        double distance = Math.sqrt(sqr(center.y - corner.y) + sqr(center.x - corner.x));
        Point2D.Double n = new Point2D.Double((center.y - corner.y) / distance,
                (corner.x - center.x) / distance);
        double m = 2 * (speedX * n.x + speedY * n.y);
        this.speedX = m * n.x - speedX;
        this.speedY = m * n.y - speedY;
    }

    private Double calculateTimeForCornerHitting(Point2D.Double center, Point2D.Double corner, double speedX, double speedY) {
        double b = speedX * (center.x - corner.x) + speedY * (center.y - corner.y);
        double D = sqr(b) - sqr(speed) * (sqr(center.x) + sqr(center.y) + sqr(corner.x) + sqr(corner.y) - sqr(radius)
                - 2 * center.x * corner.x - 2 * center.y * corner.y);
        if (D >= 0) {
            return (-b - Math.sqrt(D)) / sqr(speed);
        }
        return null;
    }

    private void normalizeSpeed() {
        double sp = Math.sqrt(sqr(speedX) + sqr(speedY));
        speedX *= speed / sp;
        speedY *= speed / sp;
    }

    private double sqr(double x) {
        return x * x;
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setXAndSpeedX(int x, long dt) {
        speedX = Main6.UPDATE_TIME * (x - this.x) / dt;
        speed = Math.sqrt(sqr(speedY) + sqr(speedX));
        this.x = x;
    }

    @Override
    public void setYAndSpeedY(int y, long dt) {
        speedY = Main6.UPDATE_TIME * (y - this.y) / dt;
        speed = Math.sqrt(sqr(speedY) + sqr(speedX));
        this.y = y;
    }

    public void stop() {
        stopped = true;
    }

    public synchronized void resume(BouncingBall ball) {
        stopped = false;
        notify();
    }

    @Override
    public double getSpeedX() {
        return speedX;
    }

    @Override
    public double getSpeedY() {
        return speedY;
    }

    public String getName() {
        return "BouncingBall";
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
}