package Lab_6;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FieldMouseListener extends MouseAdapter {

    private Field field;
    private Point relativePosition;
    private Component component;
    private long lastTime;

    public FieldMouseListener(Field field) {
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
            lastTime = System.currentTimeMillis();
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if(event.getButton() == MouseEvent.NOBUTTON) {
            if(relativePosition != null) {
                long curTime = System.currentTimeMillis();
                if(field.isPaused()) {
                    component.setX((int)(event.getPoint().getX() - relativePosition.getX()));
                    component.setY((int)(event.getPoint().getY() - relativePosition.getY()));
                } else
                {
                    component.setXAndSpeedX((int)(event.getPoint().getX() - relativePosition.getX()), curTime - lastTime);
                    component.setYAndSpeedY((int)(event.getPoint().getY() - relativePosition.getY()), curTime - lastTime);
                }
                lastTime = System.currentTimeMillis();
                field.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            if (relativePosition != null) {
                relativePosition = null;
                if (component instanceof Runnable) {
                    ((BouncingBall) component).resume((BouncingBall) component);
                } else {
                    ((Obstacle) component).setZeroSpeed();
                }
            }
            Point pos = event.getPoint();
            field.toFocus(pos);
        }
    }
}