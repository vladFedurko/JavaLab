package Lab_6;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
        JMenuItem resumeItem = this.createResumeMenuItem();
        JMenuItem stopItem = this.createStopMenuItem();
        menu.add(resumeItem);
        menu.add(stopItem);
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {
                resumeItem.setEnabled(field.isPaused());
                stopItem.setEnabled(!field.isPaused());
            }
            public void menuDeselected(MenuEvent ignore) { }
            public void menuCanceled(MenuEvent ignore) {}
        });
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
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {
                deleteBallItem.setEnabled(field.hasFocus());
            }
            public void menuDeselected(MenuEvent ignore) {}
            public void menuCanceled(MenuEvent ignore) {}
        });
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

    private JMenuItem createStopMenuItem() {
        JMenuItem stopItem = new JMenuItem("Приостановить");
        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.pause();
            }
        });
        stopItem.setEnabled(false);
        return stopItem;
    }

    private JMenuItem createResumeMenuItem() {
        JMenuItem resumeItem = new JMenuItem("Возобновить");
        resumeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.resume();
            }
        });
        resumeItem.setEnabled(true);
        return resumeItem;
    }
}