package ui;

import model.Producto;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class StockBajoRenderer extends DefaultTableCellRenderer {

    // Fondo rojo suave para las filas con stock bajo
    private static final Color ROJO_SUAVE = new Color(255, 200, 200);

    private final ProductoTableModel tableModel;

    public StockBajoRenderer(ProductoTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Si la fila esta seleccionada respetamos el color de seleccion
        if (!isSelected) {
            Producto p = tableModel.getProductoEnFila(row);
            if (p.getStockActual() < p.getStockMinimo()) {
                c.setBackground(ROJO_SUAVE);
            } else {
                c.setBackground(Color.WHITE);
            }
        }

        return c;
    }
}