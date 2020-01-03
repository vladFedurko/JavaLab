package Lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class Field extends JPanel {

    private ArrayList<BouncingBall> balls;

    private Obstacle obstacle;

    private boolean paused = true;

    public static final int NO_FOCUS_INDEX = -1;
    public static final int OBSTACLE_FOCUS_INDEX = -2;

    private int focusIndex = NO_FOCUS_INDEX;

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        super();
        FieldMouseListener mouseListener = new FieldMouseListener(this);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        balls = new ArrayList<>(15);
        this.setBackground(Color.WHITE);
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if(paused) {
            wait();
        }
    }

    public synchronized void pause() {
        paused = true;
        repaintTimer.stop();
        repaint();
    }

    public synchronized void resume() {
        paused = false;
        repaintTimer.start();
        notifyAll();
    }

    public void addBall() {
        BouncingBall ball = new BouncingBall(this);
        balls.add(ball);
        repaint();
    }

    public void deleteComponent() throws RuntimeException {
        if(focusIndex == NO_FOCUS_INDEX)
            throw new RuntimeException("Nothing to delete");
        if(focusIndex == OBSTACLE_FOCUS_INDEX) {
            obstacle = null;
        }
        else {
            balls.remove(focusIndex);
        }
        focusIndex = NO_FOCUS_INDEX;
        repaint();
    }

    public void addObstacle() {
        obstacle = new Obstacle(this);
        repaint();
    }

    public void toFocus(Point pos) {
        for(int i = balls.size() - 1; i >= 0; --i) {
            if(balls.get(i).isInside(pos)) {
                focusIndex = i;
                return;
            }
        }
        if(this.hasObstacle() && obstacle.isInside(pos))
            focusIndex = OBSTACLE_FOCUS_INDEX;
        else
            focusIndex = NO_FOCUS_INDEX;
    }

    public Component getComponentFromInnerPoint(Point pos) {
        for(int i = balls.size() - 1; i >= 0; --i) {
            if(balls.get(i).isInside(pos)) {
                return balls.get(i);
            }
        }
        if(this.hasObstacle() && obstacle.isInside(pos))
            return obstacle;
        return null;
    }

    public boolean hasObstacle() {
        return obstacle != null;
    }

    public boolean hasFocus() {
        return focusIndex != NO_FOCUS_INDEX;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)(g);
        for(BouncingBall ball : balls) {
            ball.paint(canvas);
        }
        if(this.hasObstacle()) {
            obstacle.paint(canvas);
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
