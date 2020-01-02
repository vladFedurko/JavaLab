package Lab_6;

import javax.swing.*;
import java.awt.*;

public class Main6 extends JFrame {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;

    private Field field;

    public static void toStart(String[] args) {
        Main6 frame = new Main6();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setEnabled(true);
    }

    public Main6() {
        super("Happy Balls");
        this.setSize(WIDTH, HEIGHT);
        Toolkit kit = this.getToolkit();
        this.setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);
        this.createField();
        this.createMenu();
        this.setMinimumSize(new Dimension(
                this.getJMenuBar().getWidth(),
                this.getJMenuBar().getHeight() + 2 * BouncingBall.MAX_RADIUS + 80));
    }

    private void createMenu() {
        this.setJMenuBar(new Menu(field));
    }

    private void createField() {
        field = new Field();
        this.getContentPane().add(field);
    }
}
