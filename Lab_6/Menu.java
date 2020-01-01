package Lab_6;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar {

    private Field field;

    public Menu(Field field) {
        super();
        this.field  = field;
        setAllMenu();
    }

    private void setAllMenu() {
        this.add(createBallsMenu());
        this.add(createControllerMenu());
    }

    private JMenu createControllerMenu() {
        JMenu menu = new JMenu("Управление");
        this.addControllerMenuItems(menu);
        return menu;
    }

    private void addControllerMenuItems(JMenu menu) {
        JMenuItem resumeItem = new JMenuItem("Продолжить");
        JMenuItem stopItem = new JMenuItem("Приостановить");
        this.addListenerForResumeMenuItem(stopItem, resumeItem);
        this.addListenerForStopMenuItem(stopItem, resumeItem);
        menu.add(resumeItem);
        menu.add(stopItem);
    }

    private JMenu createBallsMenu() {
        JMenu menu = new JMenu("Мячи");
        this.addBallsMenuItem(menu);
        return menu;
    }

    private void addBallsMenuItem(JMenu menu) {
        JMenuItem addBallItem = this.createAddBallMenuItem();
        JMenuItem deleteBallItem = this.createDeleteBallMenuItem();
        menu.add(addBallItem);
        menu.add(deleteBallItem);
    }

    private JMenuItem createDeleteBallMenuItem() {
        JMenuItem item = new JMenuItem("Удалить");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.deleteBall();
            }
        });
        return item;
    }

    private JMenuItem createAddBallMenuItem() {
        JMenuItem item = new JMenuItem("Добавить");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.addBall();
            }
        });
        item.setEnabled(true);
        return item;
    }

    private void addListenerForStopMenuItem(JMenuItem stopItem, JMenuItem resumeItem) {
        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.stop();
                stopItem.setEnabled(false);
                resumeItem.setEnabled(true);
            }
        });
        stopItem.setEnabled(false);
    }

    private void addListenerForResumeMenuItem(JMenuItem stopItem, JMenuItem resumeItem) {
        resumeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.resume();
                stopItem.setEnabled(true);
                resumeItem.setEnabled(false);
            }
        });
        resumeItem.setEnabled(true);
    }
}