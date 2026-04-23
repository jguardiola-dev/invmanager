package ui;

import model.Producto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductoTableModel extends AbstractTableModel {

    private final String[] columnas = {"ID", "Nombre", "Precio", "Stock", "Stock min", "Categoria", "Proveedor"};
    private List<Producto> productos = new ArrayList<>();

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        fireTableDataChanged();
    }

    public Producto getProductoEnFila(int fila) {
        return productos.get(fila);
    }

    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto p = productos.get(fila);
        switch (col) {
            case 0: return p.getId();
            case 1: return p.getNombre();
            case 2: return p.getPrecio();
            case 3: return p.getStockActual();
            case 4: return p.getStockMinimo();
            case 5: return p.getCategoriaNombre();
            case 6: return p.getProveedorNombre();
            default: return "";
        }
    }
}