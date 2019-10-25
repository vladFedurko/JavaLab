package Lab_3;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return new Double(Math.ceil((to - from) / step)).intValue()+1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        Double result = coefficients[0];
        switch (col) {
            case 0:
                return x;
            case 1:
                for (int i = 1; i < coefficients.length; ++i) {
                    result = result * x + coefficients[i];
                }
                return result;
            case 2:
                result = 0D;
                for (int i = 0; i < coefficients.length; ++i) {
                    result += coefficients[i] * Math.pow(x, coefficients.length - i - 1);
                }
                return result;
            default:
                for (int i = 1; i < coefficients.length; ++i) {
                    result = result * x + coefficients[i];
                }
                for (int i = 0; i < coefficients.length; ++i) {
                    result -= coefficients[i] * Math.pow(x, coefficients.length - i - 1);
                }
                return result;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена по горнеру";
            case 2:
                return "Значение многочлена возведением в степень";
            default:
                return "Сравнение методов (разность)";
        }
    }

    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}