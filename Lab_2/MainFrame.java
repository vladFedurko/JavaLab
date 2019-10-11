package Lab_2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MainFrame extends JFrame{

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;

    private JTextField textFieldResult;

    private ButtonGroup radioButtons = new ButtonGroup();

    private ButtonGroup radioMemButtons = new ButtonGroup();

    private Box hboxFormulaType = Box.createHorizontalBox();
    private int formulaId = 1;

    private Box vboxVarNumber = Box.createVerticalBox();

    private Double[] mem = new Double[3];

    private int varNumber;
    private JLabel MemXLabel = new JLabel("X = 0.0");
    private JLabel MemYLabel = new JLabel("Y = 0.0");
    private JLabel MemZLabel = new JLabel("Z = 0.0");

    private Image f1 = Toolkit.getDefaultToolkit().getImage("f1.bmp");
    private Image f2 = Toolkit.getDefaultToolkit().getImage("f2.bmp");

    private Box hImageBox = Box.createHorizontalBox();

    public Double calculate1(Double x, Double y, Double z) {
        return Math.pow(Math.log((1 + x) * (1 + x)) + Math.cos(Math.PI * z * z * z), Math.sin(y)) + Math.pow(Math.exp(x * x) + Math.cos(Math.exp(z)) + Math.sqrt(1 / y), 1 / x);
    }

    public Double calculate2(Double x, Double y, Double z) {
        return Math.pow(Math.cos(Math.PI * x * x * x) + Math.log((1 + y) * (1 + y)), 1D / 4) * (Math.exp(z * z) + Math.sqrt(1 / x) + Math.cos(Math.exp(y)));
    }

    private void addRadioButton(String buttonName, final int formulaId) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.formulaId = formulaId;
                //imagePane.updateUI();
                if (formulaId == 1)
                    getGraphics().drawImage(f1,0, 0 ,null);
                else
                    getGraphics().drawImage(f2,0, 0 ,null);
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);
    }

    private void addRadioButtonForVars(String buttonName, final int varNumber) {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                MainFrame.this.varNumber = varNumber;
                //imagePane.updateUI();
            }
        });
        button.setAlignmentX(0.5f);
        radioMemButtons.add(button);
        vboxVarNumber.add(button);
    }

    public MainFrame() {
        super("Вычисление формулы");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        hboxFormulaType.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(
                radioButtons.getElements().nextElement().getModel(), true);
        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(
                BorderFactory.createLineBorder(Color.YELLOW));
        JLabel labelForX = new JLabel("X:");
        textFieldX = new JTextField("0", 10);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y:");
        textFieldY = new JTextField("0", 10);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z:");
        textFieldZ = new JTextField("0", 10);
        textFieldZ.setMaximumSize(textFieldY.getPreferredSize());
        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.setBorder(
                BorderFactory.createLineBorder(Color.RED));
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalStrut(50));
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalStrut(50));
        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(10));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalGlue());
        JLabel labelForResult = new JLabel("Результат:");
        //labelResult = new JLabel("0");
        textFieldResult = new JTextField("0", 10);
        textFieldResult.setMaximumSize(
                textFieldResult.getPreferredSize());
        //textFieldResult.setEnabled(false);
        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    textFieldResult.setText(result.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldResult.setText("0");
            }
        });
        mem[0] = 0D;
        mem[1] = 0D;
        mem[2] = 0D;
        vboxVarNumber.add(Box.createVerticalStrut(10));
        addRadioButtonForVars("X", 1);
        vboxVarNumber.add(Box.createVerticalStrut(10));
        addRadioButtonForVars("Y", 2);
        vboxVarNumber.add(Box.createVerticalStrut(10));
        addRadioButtonForVars("Z", 3);
        vboxVarNumber.add(Box.createVerticalStrut(10));

        JButton MC = new JButton("MC");
        MC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mem[varNumber - 1] = 0D;
                switch(varNumber)
                {
                    case 1:
                        MemXLabel.setText("X = 0.0");
                    case 2:
                        MemYLabel.setText("Y = 0.0");
                    case 3:
                        MemZLabel.setText("Z = 0.0");
                }
            }
        });
        JButton MPlus = new JButton("M+");
        MPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    if (formulaId == 1)
                        mem[varNumber - 1] += calculate1(x, y, z);
                    else
                        mem[varNumber - 1] += calculate2(x, y, z);
                    textFieldResult.setText(mem[varNumber - 1].toString());
                    switch (varNumber)
                    {
                        case 1:
                            MemXLabel.setText("X = " + mem[0].toString());
                        case 2:
                            MemYLabel.setText("Y = " + mem[1].toString());
                        case 3:
                            MemZLabel.setText("Z = " + mem[2].toString());
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        Box hboxMemButtons = Box.createHorizontalBox();
        hboxMemButtons.add(Box.createHorizontalGlue());
        hboxMemButtons.add(MC);
        hboxMemButtons.add(Box.createHorizontalStrut(50));
        hboxMemButtons.add(MPlus);
        hboxMemButtons.add(Box.createHorizontalGlue());
        hboxMemButtons.setBorder(
                BorderFactory.createLineBorder(Color.CYAN)
        );
        radioMemButtons.setSelected(
                radioMemButtons.getElements().nextElement().getModel(), true);
        varNumber = 1;
        Box vboxLabel = Box.createVerticalBox();
        MemXLabel.setAlignmentX(0.5f);
        vboxLabel.add(MemXLabel);
        vboxLabel.add(Box.createVerticalStrut(18));
        MemYLabel.setAlignmentX(0.5f);
        vboxLabel.add(MemYLabel);
        vboxLabel.add(Box.createVerticalStrut(18));
        MemZLabel.setAlignmentX(0.5f);
        vboxLabel.add(MemZLabel);

        Box hboxChoose = Box.createHorizontalBox();
        hboxChoose.add(Box.createHorizontalGlue());
        hboxChoose.add(vboxVarNumber);
        hboxChoose.add(vboxLabel);
        hboxChoose.add(Box.createHorizontalGlue());

        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setBorder(
                BorderFactory.createLineBorder(Color.GREEN));
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);
        contentBox.add(hboxVariables);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);
        contentBox.add(hboxChoose);
        contentBox.add(hboxMemButtons);
        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);
    }
    public static void toStart(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}