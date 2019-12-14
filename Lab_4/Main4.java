package Lab_4;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Main4 extends JFrame{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem rotateGraphicMenuItem;
    private JCheckBoxMenuItem showGridMenuItem;
    private JMenuItem undoChangesButton;
    private JMenuItem saveAllChangesButton;

    private GraphicsDisplay display;

    private boolean fileLoaded = false;

    public Main4() {
        super("Построение графиков функций на основе заранее подготовленных файлов");

        display = new GraphicsDisplay();

        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        setExtendedState(MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);

        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(Main4.this) == JFileChooser.APPROVE_OPTION) {
                    openGraphics(fileChooser.getSelectedFile());
                }
                undoChangesButton.setEnabled(fileLoaded);
                saveAllChangesButton.setEnabled(fileLoaded);
            }
        };
        fileMenu.add(openGraphicsAction);

        Action undoChangesAction = new AbstractAction("Отменить все изменения") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileLoaded) {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        undoChangesButton = new JMenuItem(undoChangesAction);
        undoChangesButton.setEnabled(false);
        fileMenu.add(undoChangesButton);

        Action saveAllChangesAction = new AbstractAction("Сохранить все изменения") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Double[][] data = display.getGraphicsData();
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(Main4.this) == JFileChooser.APPROVE_OPTION)
                    saveToFile(data, fileChooser.getSelectedFile());
            }
        };
        saveAllChangesButton = new JMenuItem(saveAllChangesAction);
        saveAllChangesButton.setEnabled(false);
        fileMenu.add(saveAllChangesButton);

        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);
        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);

        Action rotateGraphicAction = new AbstractAction("Повернуть график") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.setRotateGraphic(rotateGraphicMenuItem.isSelected());
            }
        };
        rotateGraphicMenuItem = new JCheckBoxMenuItem(rotateGraphicAction);
        graphicsMenu.add(rotateGraphicMenuItem);
        rotateGraphicMenuItem.setSelected(false);

        Action showGridAction = new AbstractAction("Показать сетку") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.setShowGrid(showGridMenuItem.isSelected());
            }
        };
        showGridMenuItem = new JCheckBoxMenuItem(showGridAction);
        graphicsMenu.add(showGridMenuItem);
        showGridMenuItem.setSelected(true);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            Double[][] graphicsData = new Double[in.available() / (Double.SIZE / 8) / 2][];
            int i = -1;
            while (in.available() > 0) {
                double x = in.readDouble();
                double y = in.readDouble();
                graphicsData[++i] = new Double[] {x, y};
            }

            if (graphicsData.length > 0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }

            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(Main4.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Main4.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
        }
    }


    public static void toStart(String[] args) {
        Main4 frame = new Main4();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class GraphicsMenuListener implements MenuListener {

        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            rotateGraphicMenuItem.setEnabled(fileLoaded);
            showGridMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }

    private void saveToFile (Double[][] data, File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < data.length; ++i) {
                out.writeDouble((Double)data[i][0]);
                out.writeDouble((Double)data[i][1]);
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}

