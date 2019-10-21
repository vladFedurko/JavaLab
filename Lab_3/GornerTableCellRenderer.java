package Lab_3;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    private String needle = null;

    private boolean simpleFlag = false;

    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);
        //panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);
        if ((Double)value > 0)
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        else
            if ((Double)value < 0)
                panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            else
                panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        if ((col == 1 || col == 2) && needle != null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        } else {
            if ((col == 1 || col == 2) && simpleFlag && ((Double)value > 1)) {
                long simple = Math.round((Double)value);
                panel.setBackground(Color.WHITE);
                if (Math.abs(simple - (Double)value) > 0.1)
                    return panel;
                else {
                    if (simple != 2 && (simple % 2) == 0)
                        return panel;
                    for (int i = 3; i < simple; i += 2)
                    {
                        if ((simple % i) == 0)
                            return panel;
                    }
                    panel.setBackground(Color.YELLOW);
                }
            } else {
                panel.setBackground(Color.WHITE);
            }
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public void setSimpleFlag(boolean flag) {
        this.simpleFlag = flag;
    }
}