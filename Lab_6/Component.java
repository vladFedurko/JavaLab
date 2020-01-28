package Lab_6;

import java.awt.*;

public interface Component {
    int getX();

    int getY();

    double getSpeedX();

    double getSpeedY();

    String getName();

    void setY(int y);

    void setX(int x);

    void setXAndSpeedX(int x, long dt);

    void setYAndSpeedY(int y, long dt);
}
