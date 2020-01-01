package Lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Field extends JPanel {

    private ArrayList<BouncingBall> balls;

    private boolean paused = true;

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

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        notifyAll();
    }

    public void addBall() {
        //TODO
    }

    public void deleteBall() throws RuntimeException {
        //TODO
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
