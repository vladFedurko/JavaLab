package Lab_6;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusMenu extends Box {

    private String type;
    private Field field;
    private Component component;

    private JLabel name;

    private JTextField speedX;
    private JTextField speedY;

    public StatusMenu(Field field) {
        super(0);
        this.field = field;
        this.add(Box.createHorizontalGlue());
        name = new JLabel(this.getDefaultNameForLabel());
        this.add(name);
        this.add(Box.createHorizontalStrut(10));
        this.add(this.createSpeedBox());
        this.add(Box.createHorizontalGlue());
    }

    public void update() {
        if(field.getFocusedComponent() != null) {
            component = field.getFocusedComponent();
            speedX.setText(Double.toString(component.getSpeedX()));
            speedY.setText(Double.toString(component.getSpeedY()));
            name.setText(component.getName());
        }
    }

    private Box createSpeedBox() {
        Box box = Box.createVerticalBox();
        speedX = new JTextField("0.0", 15);
        speedX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(component != null && component instanceof BouncingBall) {
                    BouncingBall ball = (BouncingBall)component;
                    ball.setSpeedX(Double.parseDouble(speedX.getText()));
                    ball.setSpeedY(Double.parseDouble(speedY.getText()));
                }
            }
        });
        box.add(this.createLabelAndTextFieldBox("Speed X:", speedX));
        box.add(Box.createVerticalStrut(5));
        speedY = new JTextField("0.0", 15);
        box.add(this.createLabelAndTextFieldBox("Speed Y:", speedY));
        return box;
    }

    private Box createLabelAndTextFieldBox(String labelText, JTextField field) {
        Box box = Box.createHorizontalBox();
        box.add(new JLabel(labelText));
        box.add(Box.createHorizontalStrut(5));
        field.setMaximumSize(field.getPreferredSize());
        box.add(field);
        return box;
    }

    private String getDefaultNameForLabel() {
        return "---";
    }
}
