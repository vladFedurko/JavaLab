package Lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Stack;


public class MouseClickEvents extends MouseAdapter {

    private boolean pressedButton1 = false;
    private Stack<PrevZoom> prevScaling = new Stack<PrevZoom>();
    private Point startPressedMButt1;
    private GraphicsDisplay component;

    public MouseClickEvents(GraphicsDisplay comp)
    {
        component = comp;
    }

    static class PrevZoom
    {
        public double x1, y1, x2, y2;
    }

    static class ZoomRect
    {
        public int x1, y1, x2, y2;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            if(!prevScaling.empty()) {
                PrevZoom a = prevScaling.pop();
                component.setMinX(a.x1);
                component.setMaxX(a.x2);
                component.setMinY(a.y1);
                component.setMaxY(a.y2);
                component.setScaled(!prevScaling.empty());
                component.repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1)
        {
            pressedButton1 = true;
            startPressedMButt1 = mouseEvent.getPoint();
            component.setDrawRect(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1)
        {
            pressedButton1 = false;
            component.setDrawRect(false);
            if (startPressedMButt1.x == mouseEvent.getPoint().x || startPressedMButt1.y == mouseEvent.getPoint().y) {
                component.repaint();
                return;
            }
            {
                Point2D.Double a = new Point2D.Double(component.getMaxX(), component.getMaxY());
                Point2D.Double b = new Point2D.Double(component.getMinX(), component.getMinY());
                PrevZoom z = createRect(a, b);
                prevScaling.push(z);
            }

            Point2D.Double a = (Point2D.Double)component.PointToxy(startPressedMButt1).clone();
            Point2D.Double b = component.PointToxy(mouseEvent.getPoint());
            if (component.isRotateGraphic()) {
                Point a1 = (Point)startPressedMButt1.clone();
                Point b1 = mouseEvent.getPoint();
                Integer temp = a1.x;
                a1.x = a1.y;
                a1.y = temp;
                temp = b1.x;
                b1.x = b1.y;
                b1.y = temp;
                a1.x = (int)component.xyToPoint(component.getMaxX(), 0).getX() - a1.x;
                b1.x = (int)component.xyToPoint(component.getMaxX(), 0).getX() - b1.x;
                a = component.PointToxy(a1);
                b = component.PointToxy(b1);
            }
            component.setMaxX(Math.max(a.x, b.x));
            component.setMaxY(Math.max(a.y, b.y));
            component.setMinX(Math.min(a.x, b.x));
            component.setMinY(Math.min(a.y, b.y));

            component.setScaled(true);
            component.repaint();
        }
    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (pressedButton1)
        {
            ZoomRect z;
            if (component.isRotateGraphic())
            {
                Point a = (Point)startPressedMButt1.clone();  // Вот из-за этого у меня ничего не получалось
                Point b = mouseEvent.getPoint();
                Integer temp = a.x;
                a.x = a.y;
                a.y = temp;
                temp = b.x;
                b.x = b.y;
                b.y = temp;
                a.x = (int)component.xyToPoint(component.getMaxX(), 0).getX() - a.x;
                b.x = (int)component.xyToPoint(component.getMaxX(), 0).getX() - b.x;
                z = createRect(a, b);
            }
            else {
                z = createRect(startPressedMButt1, mouseEvent.getPoint());
            }
            component.setZoom(z);
            component.repaint();
        }
    }


    private ZoomRect createRect (Point start, Point end)
    {
        ZoomRect z = new ZoomRect();
        if (end.x > start.x)
        {
            z.x1 = start.x;
            z.x2 = end.x;
        } else
        {
            z.x2 = start.x;
            z.x1 = end.x;
        }

        if (end.y > start.y)
        {
            z.y1 = start.y;
            z.y2 = end.y;
        } else
        {
            z.y2 = start.y;
            z.y1 = end.y;
        }
        return z;
    }

    private PrevZoom createRect (Point2D.Double start, Point2D.Double end)
    {
        PrevZoom z = new PrevZoom();
        if (end.x > start.x)
        {
            z.x1 = start.x;
            z.x2 = end.x;
        } else
        {
            z.x2 = start.x;
            z.x1 = end.x;
        }

        if (end.y > start.y)
        {
            z.y1 = start.y;
            z.y2 = end.y;
        } else
        {
            z.y2 = start.y;
            z.y1 = end.y;
        }
        return z;
    }
}
