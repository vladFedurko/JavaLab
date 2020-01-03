package Lab_6;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private JMenu createControllerMenu() {
        JMenu menu = new JMenu("Управление");
        this.addControllerMenuItems(menu);
        return menu;
    }

    private void addControllerMenuItems(@NotNull JMenu menu) {
        JMenuItem resumeItem = this.createResumeMenuItem();
        JMenuItem stopItem = this.createStopMenuItem();
        JMenuItem obstacleItem = this.createObstacleMenuItem();
        menu.add(resumeItem);
        menu.add(stopItem);
        menu.add(obstacleItem);
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {
                resumeItem.setEnabled(field.isPaused());
                stopItem.setEnabled(!field.isPaused());
                obstacleItem.setEnabled(!field.hasObstacle());
            }
            public void menuDeselected(MenuEvent ignore) { }
            public void menuCanceled(MenuEvent ignore) {}
        });
    }

    @NotNull
    private JMenu createBallsMenu() {
        JMenu menu = new JMenu("Мячи");
        this.addBallsMenuItem(menu);
        return menu;
    }

    private void addBallsMenuItem(@NotNull JMenu menu) {
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

    @NotNull
    private JMenuItem createObstacleMenuItem() {
        JMenuItem item = new JMenuItem("Добавить препятствие");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                field.addObstacle();
            }
        });
        item.setEnabled(true);
        return item;
    }

    @NotNull
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

    @NotNull
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

    @NotNull
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

    @NotNull
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