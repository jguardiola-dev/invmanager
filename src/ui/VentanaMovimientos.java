package ui;

import dao.MovimientoStockDAO;
import dao.ProductoDAO;
import model.MovimientoStock;
import model.Producto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VentanaMovimientos extends JDialog {

    private final MovimientoStockDAO movimientoDAO = new MovimientoStockDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final Producto producto;

    private final MovimientoTableModel tableModel = new MovimientoTableModel();
    private final JTable tabla = new JTable(tableModel);

    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"ENTRADA", "SALIDA"});
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
    private final JTextField txtMotivo = new JTextField(15);
    private final JLabel lblBalance = new JLabel();

    public VentanaMovimientos(Frame parent, Producto producto) {
        super(parent, "Movimientos de " + producto.getNombre(), true);
        this.producto = producto;

        configurarDialog();
        cargarMovimientos();
    }

    private void configurarDialog() {
        setSize(650, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Panel superior: formulario para nuevo movimiento
        JPanel panelNuevo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNuevo.add(new JLabel("Tipo:"));
        panelNuevo.add(cmbTipo);
        panelNuevo.add(new JLabel("Cantidad:"));
        panelNuevo.add(spnCantidad);
        panelNuevo.add(new JLabel("Motivo:"));
        panelNuevo.add(txtMotivo);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarMovimiento());
        panelNuevo.add(btnRegistrar);

        // Panel inferior: balance + cerrar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(lblBalance);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelInferior.add(btnCerrar);

        add(panelNuevo, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarMovimientos() {
        try {
            List<MovimientoStock> movimientos = movimientoDAO.obtenerPorProducto(producto.getId());
            tableModel.setMovimientos(movimientos);

            int balance = movimientoDAO.calcularBalance(producto.getId());
            lblBalance.setText("Balance de movimientos: " + balance + "   ");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar movimientos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarMovimiento() {
        String tipo = (String) cmbTipo.getSelectedItem();
        int cantidad = (Integer) spnCantidad.getValue();
        String motivo = txtMotivo.getText().trim();

        // Si es salida, comprobar que hay suficiente stock
        if ("SALIDA".equals(tipo) && cantidad > producto.getStockActual()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock. Stock actual: " + producto.getStockActual(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Fecha en formato ISO
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        MovimientoStock m = new MovimientoStock(0, producto.getId(), tipo, cantidad, fecha, motivo);

        try {
            movimientoDAO.insertar(m);

            // Actualizar el stock del producto
            if ("ENTRADA".equals(tipo)) {
                producto.setStockActual(producto.getStockActual() + cantidad);
            } else {
                producto.setStockActual(producto.getStockActual() - cantidad);
            }
            productoDAO.actualizar(producto);

            txtMotivo.setText("");
            spnCantidad.setValue(1);
            cargarMovimientos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}