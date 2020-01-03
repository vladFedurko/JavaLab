package Lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Field extends JPanel {

    private ArrayList<BouncingBall> balls;

    private Obstacle obstacle;

    private boolean paused = true;

    public static final int NO_FOCUS_INDEX = -1;
    public static final int OBSTACLE_FOCUS_INDEX = -2;

    private int focusIndex = NO_FOCUS_INDEX;

    private Timer repaintTimer = new Timer(20, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        super();
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

    public void deleteBall() throws RuntimeException {
        if(focusIndex == NO_FOCUS_INDEX)
            throw new RuntimeException("Nothing to delete");
        balls.remove(focusIndex);
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
    }

    public boolean isPaused() {
        return paused;
    }
}
