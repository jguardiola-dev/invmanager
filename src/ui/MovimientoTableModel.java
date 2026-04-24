package ui;

import model.MovimientoStock;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MovimientoTableModel extends AbstractTableModel {

    private final String[] columnas = {"Fecha", "Tipo", "Cantidad", "Motivo"};
    private List<MovimientoStock> movimientos = new ArrayList<>();

    public void setMovimientos(List<MovimientoStock> movimientos) {
        this.movimientos = movimientos;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return movimientos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int fila, int col) {
        MovimientoStock m = movimientos.get(fila);
        switch (col) {
            case 0: return m.getFecha();
            case 1: return m.getTipo();
            case 2: return m.getCantidad();
            case 3: return m.getMotivo();
            default: return "";
        }
    }
}