package Lab_6;

import javax.swing.*;
import java.awt.*;

public class Main6 extends JFrame {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;

    public static final int UPDATE_TIME = 10;

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
        Field field = new Field();
        this.getContentPane().add(field, BorderLayout.CENTER);
        this.createMenu(field);
        StatusMenu statusMenu = this.createStatusMenu(field);
        field.setStatusMenu(statusMenu);
        this.setMinimumSize(new Dimension(
                this.getJMenuBar().getWidth(),
                this.getJMenuBar().getHeight() + 2 * BouncingBall.MAX_RADIUS + 80));
    }

    private void createMenu(Field field) {
        this.setJMenuBar(new Menu(field));
    }

    private StatusMenu createStatusMenu(Field field) {
        StatusMenu menu = new StatusMenu(field);
        this.getContentPane().add(menu, BorderLayout.NORTH);
        return menu;
    }
}
