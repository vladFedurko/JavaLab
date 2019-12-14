package Lab_4;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Stack;
import java.awt.Cursor;


public class MouseClickEvents extends MouseAdapter {

    private boolean pressedButton1 = false;
    private boolean valueChanging = false;
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
            if (!valueChanging) {
                startPressedMButt1 = mouseEvent.getPoint();
                component.setDrawRect(true);
            }
            else {
                component.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            if (!valueChanging) {
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

                Point2D.Double a = (Point2D.Double) component.PointToxy(startPressedMButt1).clone();
                Point2D.Double b = component.PointToxy(mouseEvent.getPoint());
                if (component.isRotateGraphic()) {
                    Point a1 = (Point) startPressedMButt1.clone();
                    Point b1 = mouseEvent.getPoint();
                    Integer temp = a1.x;
                    a1.x = a1.y;
                    a1.y = temp;
                    temp = b1.x;
                    b1.x = b1.y;
                    b1.y = temp;
                    a1.x = (int) component.xyToPoint(component.getMaxX(), 0).getX() - a1.x;
                    b1.x = (int) component.xyToPoint(component.getMaxX(), 0).getX() - b1.x;
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
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point b;
        if (component.isRotateGraphic()) {
            b = mouseEvent.getPoint();
            Integer temp = b.x;
            b.x = b.y;
            b.y = temp;
            b.x = (int)component.xyToPoint(component.getMaxX(), 0).getX() - b.x;
        }
        else {
            b = mouseEvent.getPoint();
        }
        if (component.highlightThePoint(b)) {
            valueChanging = true;
            component.repaint();
        }
        else {
            if (valueChanging)
                component.repaint();
            valueChanging = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (pressedButton1)
        {
            if (!valueChanging) {
                component.setCursor(Cursor.getPredefinedCursor(CursorView.getCursorType(startPressedMButt1, mouseEvent.getPoint())));
                ZoomRect z;
                if (component.isRotateGraphic()) {
                    Point a = (Point) startPressedMButt1.clone();  // Вот из-за этого у меня ничего не получалось
                    Point b = mouseEvent.getPoint();
                    Integer temp = a.x;
                    a.x = a.y;
                    a.y = temp;
                    temp = b.x;
                    b.x = b.y;
                    b.y = temp;
                    a.x = (int) component.xyToPoint(component.getMaxX(), 0).getX() - a.x;
                    b.x = (int) component.xyToPoint(component.getMaxX(), 0).getX() - b.x;
                    z = createRect(a, b);
                } else {
                    z = createRect(startPressedMButt1, mouseEvent.getPoint());
                }
                component.setZoom(z);
                component.repaint();
            }
            else {
                Point b = mouseEvent.getPoint();
                if (component.isRotateGraphic()) {
                    Integer temp = b.x;
                    b.x = b.y;
                    b.y = temp;
                }
                component.setYForChangingValue(component.PointToxy(b).y);
                component.repaint();
            }
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

class CursorView {

    protected static int getCursorType (Point startPoint, Point currentPoint) {
        double height = startPoint.y - currentPoint.y;
        double width = startPoint.x - currentPoint.x;
        if (width == 0 && height == 0) {
            return Cursor.DEFAULT_CURSOR;
        }
        double angle = Math.acos(width / Math.sqrt(width * width + height * height));
        int type;
        double piDivideEight = Math.PI / 8;
        if (angle < piDivideEight) {
            type = Cursor.E_RESIZE_CURSOR;
        }
        else if (angle < 3 * piDivideEight) {
            if (height > 0)
                type = Cursor.NW_RESIZE_CURSOR;
            else
                type = Cursor.NE_RESIZE_CURSOR;
        }
        else if (angle < 5 * piDivideEight) {
            type = Cursor.N_RESIZE_CURSOR;
        }
        else if (angle < 7 * piDivideEight) {
            if (height > 0)
                type = Cursor.NE_RESIZE_CURSOR;
            else
                type = Cursor.NW_RESIZE_CURSOR;
        }
        else {
            type = Cursor.W_RESIZE_CURSOR;
        }
        return type;
    }

    protected static int getDefaultType () {
        return Cursor.DEFAULT_CURSOR;
    }
}
