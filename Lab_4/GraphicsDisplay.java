package Lab_4;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JPanel;

@SuppressWarnings("serial")

public class GraphicsDisplay extends JPanel {

    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean rotateGraphic = false;
    private boolean showGrid = true;
    private boolean scaled = false;

    public void setDrawRect(boolean drawRect) {
        this.drawRect = drawRect;
    }

    private boolean drawRect = false;

    public void setZoom(MouseClickEvents.ZoomRect zoom) {
        this.zoom = zoom;
    }

    public boolean isRotateGraphic() {
        return rotateGraphic;
    }

    private MouseClickEvents.ZoomRect zoom;
    private int changingValue = -1;

    private int firstVisibleValue = 0;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;
    private double scaleX;
    private double scaleY;

    private BasicStroke graphicsStroke;

    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke gridStroke;
    private BasicStroke zoomRectStroke;
    private BasicStroke highlightingStroke;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    private Font axisFont;
    private Font highlightingFont;

    public GraphicsDisplay() {
        MouseClickEvents a = new MouseClickEvents(this);
        addMouseListener(a);
        addMouseMotionListener(a);
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {9, 3, 3, 3, 6, 3, 3, 3}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
        zoomRectStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {10, 10}, 0.0f);
        highlightingStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        highlightingFont = new Font("Serif", Font.PLAIN, 8);

        formatter.setMaximumFractionDigits(10);
        formatter.setGroupingUsed(false);
        formatter.setMaximumIntegerDigits(5);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        scaled = false;
        repaint();
    }

    public Double[][] getGraphicsData() {
        return graphicsData;
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
        if(!scaled) {
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
        }

        if (rotateGraphic) {
            scaleY = getSize().getWidth() / (maxY - minY);
            scaleX = getSize().getHeight() / (maxX - minX);
        }
        else {
            scaleX = getSize().getWidth() / (maxX - minX);
            scaleY = getSize().getHeight() / (maxY - minY);
        }

        if(!scaled) {
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
        }

        int l = 0, r = graphicsData.length - 1;
        while (l < r - 1)
        {
            int mid = (l + r) >> 1;
            if(graphicsData[mid][0] > minX)
            {
                r = mid;
            } else
            {
                l = mid;
            }
        }

        firstVisibleValue = l;

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

        if (drawRect) {
            canvas.setStroke(zoomRectStroke);
            canvas.setColor(Color.BLACK);
            canvas.drawRect(zoom.x1, zoom.y1, zoom.x2 - zoom.x1, zoom.y2 - zoom.y1);
        }

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
            int l = firstVisibleValue;
            int a = l == 0 ? l : l - 1;
            double max = xyToPoint(0, maxY).getY();
            double min = xyToPoint(0, minY).getY();
            boolean flag = false;
            Point2D.Double point;
            Point2D.Double prevPoint = xyToPoint(graphicsData[a][0], graphicsData[a][1]);
            graphics.moveTo(prevPoint.getX(), prevPoint.getY());
            if (a == l) ++l;
            for ( ; l  < graphicsData.length && graphicsData[l][0] < maxX; ++l) {
                point = xyToPoint(graphicsData[l][0], graphicsData[l][1]);
                if (!((prevPoint.getY() < max && point.getY() < max) || (prevPoint.getY() > min && point.getY() > min))) {
                    graphics.lineTo(point.getX(), point.getY());
                    flag = true;
                } else
                {
                    graphics.moveTo(point.getX(), point.getY());
                }
                prevPoint = new Point2D.Double(point.getX(), point.getY());
            }
            if (l < graphicsData.length) {
                point = xyToPoint(graphicsData[l][0], graphicsData[l][1]);
                graphics.lineTo(point.getX(), point.getY());
            }
            if(flag) canvas.draw(graphics);
        }
    }


    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);

        int l = firstVisibleValue;

        for ( ; l  < graphicsData.length && graphicsData[l][0] < maxX; ++l) {
            if (graphicsData[l][1] <= maxY && graphicsData[l][1] >= minY)
                drawMarker(canvas, l);
        }
    }

    protected void drawMarker(Graphics2D canvas, int i) {
        int dx = 5;
        int dy = 3;

        canvas.setColor(Color.RED);

        int xCenter = (int)xyToPoint(graphicsData[i][0], graphicsData[i][1]).getX();
        int yCenter = (int)xyToPoint(graphicsData[i][0], graphicsData[i][1]).getY();

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

        if (i == changingValue) {
            canvas.setColor(Color.BLUE);
            Stroke oldStroke = canvas.getStroke();
            canvas.setStroke(highlightingStroke);
            canvas.draw(marker);
            canvas.setStroke(oldStroke);


            FontRenderContext context = canvas.getFontRenderContext();
            canvas.setFont(highlightingFont);
            String label = "X=" + formatter.format(graphicsData[i][0]) + ", Y=" + formatter.format(graphicsData[i][1]);
            Rectangle2D bounds = axisFont.getStringBounds(label, context);
            canvas.drawString(label, (float)(xyToPoint(graphicsData[i][0], 0).getX() + 8),
                    (float)(xyToPoint(0, graphicsData[i][1]).getY()) - 5);
        }
        else {
            int a = graphicsData[i][1].intValue();
            if (graphicsData[i][1] < 0)
                --a;
            a = Math.abs(a);
            while (a > 0) {
                if (a % 2 != 0) {
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

        if (minX < 0 && maxX > 0) {
            if (maxY > 0 && minY < 0) {
                for (Double x = delta; x < maxX; x += delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, 0);
                    canvas.drawString(formatter.format(x), (float) labelPos.getX() - formatter.format(x).length() * 6 - 2,
                            (float) (labelPos.getY() + bounds.getMaxY() + 2));
                }

                for (Double x = -delta; x > minX; x -= delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, 0);
                    canvas.drawString(formatter.format(x), (float) labelPos.getX() - formatter.format(x).length() * 6,
                            (float) (labelPos.getY() + bounds.getMaxY() + 2));
                }
            }
            else {
                for (Double x = delta; x < maxX; x += delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, minY);
                    canvas.drawString(formatter.format(x), (float) (labelPos.getX()) + 1, (float) labelPos.getY() - 5);
                }

                for (Double x = -delta; x > minX; x -= delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, minY);
                    canvas.drawString(formatter.format(x), (float) (labelPos.getX()) + 1, (float) labelPos.getY() - 5);
                }
            }
        } else
        {
            if (maxY > 0 && minY < 0) {
                for (Double x = (int)(minX / delta) * delta; x < maxX; x += delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, 0);
                    canvas.drawString(formatter.format(x), (float) labelPos.getX() - formatter.format(x).length() * 6,
                            (float) (labelPos.getY() + bounds.getMaxY() + 2));
                }
            }
            else {
                for (Double x = (int)(minX / delta) * delta; x < maxX; x += delta) {
                    paintLineForGridX(x, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(x.toString(), context);
                    Point2D.Double labelPos = xyToPoint(x, minY);
                    canvas.drawString(formatter.format(x), (float) (labelPos.getX()) + 1, (float) labelPos.getY() - 5);
                }
            }
        }

        if (minY < 0 && maxY > 0) {
            if (maxX > 0 && minX < 0) {
                for (Double y = delta; y < maxY; y += delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(0, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() - formatter.format(y).length() * 6 - 3),
                            (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }

                for (Double y = -delta; y > minY; y -= delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(0, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() - formatter.format(y).length() * 6 - 3),
                            (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }
            }
            else {
                for (Double y = delta; y < maxY; y += delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(minX, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() + 2), (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }

                for (Double y = -delta; y > minY; y -= delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(minX, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() + 2), (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }
            }
        }
        else
        {
            if (maxX > 0 && minX < 0) {
                for (Double y = (int)(minY / delta) * delta; y < maxY; y += delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(0, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() - formatter.format(y).length() * 6 - 3),
                            (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }
            }
            else {
                for (Double y = (int) (minY / delta) * delta; y < maxY; y += delta) {
                    paintLineForGridY(y, canvas);

                    canvas.setColor(Color.BLACK);
                    Rectangle2D bounds = axisFont.getStringBounds(y.toString(), context);
                    Point2D.Double labelPos = xyToPoint(minX, y);
                    canvas.drawString(formatter.format(y), (float) (labelPos.getX() + 2), (float) (labelPos.getY() + bounds.getMaxY()) + 2);
                }
            }
        }

        if(maxX > 0 && maxY > 0 && minX < 0 && minY < 0) {
            Rectangle2D bounds = axisFont.getStringBounds("0", context);
            Point2D.Double labelPos = xyToPoint(0, 0);
            canvas.drawString("0", (float) (labelPos.getX() - 11), (float) (labelPos.getY() + bounds.getMaxY()) + 2);
        }


        if (showAxis)
            paintMarksOnAxis(g, mDelta);
    }

    protected void paintLineForGridX (double x, Graphics2D canvas) {
        canvas.setColor(Color.GRAY);
        canvas.draw(new Line2D.Double(xyToPoint(x, maxY), xyToPoint(x, minY)));
    }

    protected void paintLineForGridY (double y, Graphics2D canvas) {
        canvas.setColor(Color.GRAY);
        canvas.draw(new Line2D.Double(xyToPoint(maxX, y), xyToPoint(minX, y)));
    }

    protected void paintMarksOnAxis(@NotNull Graphics2D canvas, double mDelta) {

        canvas.setStroke(gridStroke);
        canvas.setColor(Color.BLACK);

        int mLength = 2;
        int bLength = 3;

        if (maxY > 0 && minY < 0)
        {

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

        }

        if (maxX > 0 && minX < 0)
        {
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
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        if(scaled)
        {
            return new Point2D.Double(deltaX * scaleX, deltaY * scaleY);
        }
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double PointToxy(Point src) {
        if(scaled)
        {
            return new Point2D.Double(src.x / scaleX + minX,maxY - src.y / scaleY);
        }
        return new Point2D.Double(src.x / scale + minX, maxY - src.y / scale);
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setScaled(boolean scaled) {
        this.scaled = scaled;
    }

    public boolean highlightThePoint (Point point) {
        if (graphicsData == null || graphicsData.length == 0) return false;
        int radius = 7;
        int distance;
        int minDistance = Integer.MAX_VALUE;
        changingValue = -1;
        Point value;
        int l = firstVisibleValue;
        for (; l < graphicsData.length; ++l) {
            value = new Point((int)xyToPoint(graphicsData[l][0], 0).x, (int)xyToPoint(0, graphicsData[l][1]).y);
            distance = (value.x - point.x) * (value.x - point.x) + (value.y - point.y) * (value.y - point.y);
            if (distance < radius * radius) {
                if (changingValue != -1) {
                    if (distance < minDistance) {
                        changingValue = l;
                        minDistance = distance;
                    }
                }
                else {
                    minDistance = distance;
                    changingValue = l;
                }
            }
        }
        return changingValue != -1;
    }

    public void setYForChangingValue(double yValue) throws ArrayIndexOutOfBoundsException {
        if (graphicsData == null || graphicsData.length == 0) return;
        graphicsData[changingValue][1] = yValue;
    }

    /*protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }*/
}