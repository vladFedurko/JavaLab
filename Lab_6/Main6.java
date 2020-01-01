package Lab_6;

import javax.swing.*;

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
        this.createField();
        this.createMenu();
    }

    private void createMenu() {
        this.setJMenuBar(new Menu(field));
    }

    private void createField() {
        field = new Field();
        this.getContentPane().add(field);
    }
}
