package Lab_6;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Menu extends JMenuBar {

    private JMenuItem stopItem;
    private JMenuItem resumeItem;

    private Field field;

    public Menu(Field field) {
        super();
        this.field  = field;
        setAllMenu();
    }

    private void setAllMenu() {
        //this.add(createBallsMenu());
        //this.add(createControllerMenu());
    }

    /*private JMenu createControllerMenu() {
        JMenu menu = new JMenu("Мячи");
        this.addControllerMenuItems(menu);
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent menuEvent) {

            }

            @Override
            public void menuDeselected(MenuEvent menuEvent) {

            }

            @Override
            public void menuCanceled(MenuEvent menuEvent) {

            }
        });
    }*/

    /*private JMenu createBallsMenu() {
        JMenu menu = new JMenu("Мячи");

    }*/

    private void addControllerMenuItems(JMenu menu) {

    }
}
