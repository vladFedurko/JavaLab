package Lab_6;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FieldMouseListener extends MouseAdapter {

    private Field field;
    private Point relativePosition;
    private Component component;

    public FieldMouseListener(Field field){
        this.field = field;
    }

    @Override
    public synchronized void mousePressed(MouseEvent event) {
        if(event.getButton() == MouseEvent.BUTTON1) {
            Point pos = event.getPoint();
            component = field.getComponentFromInnerPoint(pos);
            if(component != null) {
                relativePosition = new Point((int) pos.getX() - component.getX(),
                        (int) pos.getY() - component.getY());
            }
            if(component instanceof Runnable) {
                ((BouncingBall)component).stop();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if(event.getButton() == MouseEvent.NOBUTTON) {
            if(relativePosition != null) {
                component.setX((int)(event.getPoint().getX() - relativePosition.getX()));
                component.setY((int)(event.getPoint().getY() - relativePosition.getY()));
                field.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if(event.getButton() == MouseEvent.BUTTON1) {
            if(relativePosition != null) {
                relativePosition = null;
                if(component instanceof Runnable) {
                    ((BouncingBall)component).resume((BouncingBall)component);
                }
            }
            Point pos = event.getPoint();
            field.toFocus(pos);
        }
    }

}
