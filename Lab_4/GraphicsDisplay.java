package Lab_4;

import org.jetbrains.annotations.NotNull;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class GraphicsDisplay extends JPanel {

    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean rotateGraphic = false;
    private boolean showGrid = true;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke gridStroke;

    private Font axisFont;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {9, 3, 3, 3, 6, 3, 3, 3}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }


    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }


    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }


    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }


    public void setRotateGraphic(boolean rotateGraphic) {
        this.rotateGraphic = rotateGraphic;
        repaint();
    }


    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; ++i) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }

        double scaleX;
        double scaleY;

        if (rotateGraphic) {
            scaleY = getSize().getWidth() / (maxY - minY);
            scaleX = getSize().getHeight() / (maxX - minX);
        }
        else {
            scaleX = getSize().getWidth() / (maxX - minX);
            scaleY = getSize().getHeight() / (maxY - minY);
        }

        scale = Math.min(scaleX, scaleY);

        if (scale == scaleX) {
            if (rotateGraphic) {
                double yIncrement = (getSize().getWidth() / scale - (maxY - minY)) / 2;
                maxY += yIncrement;
                minY -= yIncrement;
            }
            else
            {
                double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
                maxY += yIncrement;
                minY -= yIncrement;
            }
        }

        if (scale == scaleY) {
            if (rotateGraphic) {
                double xIncrement = (getSize().getHeight() / scale - (maxX - minX)) / 2;
                maxX += xIncrement;
                minX -= xIncrement;
            }
            else
            {
                double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
                maxX += xIncrement;
                minX -= xIncrement;
            }
        }
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (rotateGraphic) {
            AffineTransform at = canvas.getTransform();
            at.translate(0, xyToPoint(maxX, 0).getX());
            at.rotate(-Math.PI / 2);
            canvas.setTransform(at);
        }

        if (showGrid) paintGrid(canvas);

        if (showAxis) paintAxis(canvas);

        paintGraphics(canvas);

        if (showMarkers) paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }


    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.BLUE);

        GeneralPath graphics = new GeneralPath();
        {
            Point2D.Double point = xyToPoint(graphicsData[0][0], graphicsData[0][1]);
            graphics.moveTo(point.getX(), point.getY());
            for (int i = 1; i < graphicsData.length; ++i) {
                point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
                graphics.lineTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }


    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);

        double dx = 5D;
        double dy = 3D;

        for (Double[] point : graphicsData) {
            canvas.setColor(Color.RED);

            double xCenter = xyToPoint(point[0], point[1]).getX();
            double yCenter = xyToPoint(point[0], point[1]).getY();

            GeneralPath marker = new GeneralPath();

            marker.moveTo(xCenter - dx, yCenter + dy);
            marker.lineTo(xCenter - dx, yCenter - dy);
            marker.moveTo(xCenter - dx, yCenter);
            marker.lineTo(xCenter + dx, yCenter);
            marker.moveTo(xCenter + dx, yCenter + dy);
            marker.lineTo(xCenter + dx, yCenter - dy);

            marker.moveTo(xCenter + dy, yCenter - dx);
            marker.lineTo(xCenter - dy, yCenter - dx);
            marker.moveTo(xCenter, yCenter - dx);
            marker.lineTo(xCenter, yCenter + dx);
            marker.moveTo(xCenter + dy, yCenter + dx);
            marker.lineTo(xCenter - dy, yCenter + dx);

            int a = point[1].intValue();
            if (point[1] < 0)
                --a;
            a = Math.abs(a);
            while (a > 0) {
                if(a % 2 != 0) {
                    canvas.setColor(Color.BLACK);
                    break;
                }
                a /= 10;
            }

            canvas.draw(marker);
        }
    }


    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);

        FontRenderContext context = canvas.getFontRenderContext();

        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));

            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();

            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));
        }

        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));

            GeneralPath arrow = new GeneralPath();

            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();

            canvas.draw(arrow);
            canvas.fill(arrow);

            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
        }
    }


    protected void paintGrid (Graphics2D g) {
        int n = 20; //count of splitting
        double delta = Math.max(maxX - minX, maxY - minY) / n;
        double pow = Math.pow(10 ,(int)Math.round(Math.floor(Math.log10(delta))));
        delta /= pow;
        if (delta < Math.sqrt(2))
            delta = 1;
        else {
            if (delta < Math.sqrt(10))
                delta = 2;
            else
                if (delta < 5 * Math.sqrt(2))
                    delta = 5;
                else
                    delta = 10;
        }
        delta *= pow;
        double mDelta = delta / 10;

        Graphics2D canvas = g;

        canvas.setStroke(gridStroke);
        canvas.setColor(Color.GRAY);

        if (!showAxis) {
            if (minX <= 0.0 && maxX >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            }
            if (minY <= 0.0 && maxY >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            }
        }

        FontRenderContext context = canvas.getFontRenderContext();

        for (Double x = delta; x < maxX; x += delta) {
            canvas.draw(new Line2D.Double(xyToPoint(x, maxY), xyToPoint(x, minY)));

            Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
            Point2D.Double labelPos = xyToPoint(x, 0);
            canvas.drawString(x.toString(), (float) (labelPos.getX() - bounds.getWidth() / 2), (float) (labelPos.getY() + 13));
        }

        for (Double x = -delta; x > minX; x -= delta) {
            canvas.draw(new Line2D.Double(xyToPoint(x, maxY), xyToPoint(x, minY)));

            Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
            Point2D.Double labelPos = xyToPoint(x, 0);
            canvas.drawString(x.toString(), (float) (labelPos.getX() - bounds.getWidth() / 2), (float) labelPos.getY() + 13);
        }

        for (Double y = delta; y < maxY; y += delta) {
            canvas.draw(new Line2D.Double(xyToPoint(maxX, y), xyToPoint(minX, y)));

            Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
            Point2D.Double labelPos = xyToPoint(0, y);
            canvas.drawString(y.toString(), (float) (labelPos.getX() - bounds.getWidth() / 2), (float) labelPos.getY() + 13);
        }

        for (Double y = -delta; y > minY; y -= delta) {
            canvas.draw(new Line2D.Double(xyToPoint(maxX, y), xyToPoint(minX, y)));

            Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
            Point2D.Double labelPos = xyToPoint(0, y);
            canvas.drawString(y.toString(), (float) (labelPos.getX() - bounds.getWidth() / 2), (float) labelPos.getY() + 13);
        }

        Rectangle2D bounds = axisFont.getStringBounds("0.0", context);
        Point2D.Double labelPos = xyToPoint(0, 0);
        canvas.drawString("0.0", (float) (labelPos.getX() - bounds.getWidth() / 2), (float) labelPos.getY() + 13);

        if (showAxis)
            paintMarksOnAxis(g, mDelta);
    }


    protected void paintMarksOnAxis(@NotNull Graphics2D canvas, double mDelta) {

        canvas.setStroke(gridStroke);
        canvas.setColor(Color.BLACK);

        int mLength = 2;
        int bLength = 3;

        for (double x = 0; x < maxX; x += mDelta) {
            for (int i = 0; i < 4; ++i) {
                x += mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - mLength,
                        xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + mLength));
            }
            x += mDelta;
            canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - bLength,
                    xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + bLength));
            for (int i = 0; i < 4; ++i) {
                x += mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - mLength,
                        xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + mLength));
            }
        }

        for (double x = 0; x > minX; x -= mDelta) {
            for (int i = 0; i < 4; ++i)
            {
                x -= mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - mLength,
                        xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + mLength));
            }
            x -= mDelta;
            canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - bLength,
                    xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + bLength));
            for (int i = 0; i < 4; ++i)
            {
                x -= mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() - mLength,
                        xyToPoint(x, 0).getX(), xyToPoint(x, 0).getY() + mLength));
            }
        }

        for (double y = 0; y < maxY; y += mDelta) {
            for (int i = 0; i < 4; ++i)
            {
                y += mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - mLength, xyToPoint(0, y).getY(),
                        xyToPoint(0, y).getX() + mLength, xyToPoint(0, y).getY()));
            }
            y += mDelta;
            canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - bLength, xyToPoint(0, y).getY(),
                    xyToPoint(0, y).getX() + bLength, xyToPoint(0, y).getY()));
            for (int i = 0; i < 4; ++i)
            {
                y += mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - mLength, xyToPoint(0, y).getY(),
                        xyToPoint(0, y).getX() + mLength, xyToPoint(0, y).getY()));
            }
        }

        for (double y = 0; y > minY; y -= mDelta) {
            for (int i = 0; i < 4; ++i)
            {
                y -= mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - mLength, xyToPoint(0, y).getY(),
                        xyToPoint(0, y).getX() + mLength, xyToPoint(0, y).getY()));
            }
            y -= mDelta;
            canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - bLength, xyToPoint(0, y).getY(),
                    xyToPoint(0, y).getX() + bLength, xyToPoint(0, y).getY()));
            for (int i = 0; i < 4; ++i)
            {
                y -= mDelta;
                canvas.draw(new Line2D.Double(xyToPoint(0, y).getX() - mLength, xyToPoint(0, y).getY(),
                        xyToPoint(0, y).getX() + mLength, xyToPoint(0, y).getY()));
            }
        }
    }


    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }


    /*protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }*/
}