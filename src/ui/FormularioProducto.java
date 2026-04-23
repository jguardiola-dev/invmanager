package ui;

import dao.CategoriaDAO;
import dao.ProveedorDAO;
import model.Categoria;
import model.Producto;
import model.Proveedor;
import service.ProductoService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.sql.SQLException;
import java.util.List;

public class FormularioProducto extends JDialog {

    private final ProductoService productoService = new ProductoService();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    private final Producto productoEditar;
    private boolean guardado = false;

    // Componentes del formulario
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JSpinner spnStockActual = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
    private final JSpinner spnStockMinimo = new JSpinner(new SpinnerNumberModel(5, 0, 99999, 1));
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JComboBox<Proveedor> cmbProveedor = new JComboBox<>();

    public FormularioProducto(Frame parent, Producto producto) {
        super(parent, producto == null ? "Nuevo producto" : "Editar producto", true);
        this.productoEditar = producto;

        configurarDialog();
        cargarCombos();

        // Si es edicion, rellenamos los campos con los datos del producto
        if (productoEditar != null) {
            rellenarCampos();
        }
    }

    private void configurarDialog() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        // Panel central con los campos apilados verticalmente
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelCampos.add(etiquetar("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Descripcion:"));
        panelCampos.add(txtDescripcion);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Precio (EUR):"));
        panelCampos.add(txtPrecio);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Stock actual:"));
        panelCampos.add(spnStockActual);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Stock minimo:"));
        panelCampos.add(spnStockMinimo);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Categoria:"));
        panelCampos.add(cmbCategoria);
        panelCampos.add(Box.createVerticalStrut(8));

        panelCampos.add(etiquetar("Proveedor:"));
        panelCampos.add(cmbProveedor);

        // Panel inferior con los botones Guardar y Cancelar
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Metodo auxiliar para crear etiquetas alineadas a la izquierda
    private JLabel etiquetar(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void cargarCombos() {
        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodas();
            for (Categoria c : categorias) {
                cmbCategoria.addItem(c);
            }
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            for (Proveedor pr : proveedores) {
                cmbProveedor.addItem(pr);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorias/proveedores: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rellenarCampos() {
        txtNombre.setText(productoEditar.getNombre());
        txtDescripcion.setText(productoEditar.getDescripcion());
        txtPrecio.setText(String.valueOf(productoEditar.getPrecio()));
        spnStockActual.setValue(productoEditar.getStockActual());
        spnStockMinimo.setValue(productoEditar.getStockMinimo());

        // Seleccionar la categoria correcta recorriendo el combo por id
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).getId() == productoEditar.getCategoriaId()) {
                cmbCategoria.setSelectedIndex(i);
                break;
            }
        }
        // Igual con proveedor
        for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
            if (cmbProveedor.getItemAt(i).getId() == productoEditar.getProveedorId()) {
                cmbProveedor.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        try {
            // Parsear el precio cambiando coma por punto para aceptar formato espanol
            String precioTexto = txtPrecio.getText().trim().replace(",", ".");
            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio no es valido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
            Proveedor proveedor = (Proveedor) cmbProveedor.getSelectedItem();
            int categoriaId = categoria != null ? categoria.getId() : 0;
            int proveedorId = proveedor != null ? proveedor.getId() : 0;

            Producto p = new Producto();
            p.setNombre(txtNombre.getText());
            p.setDescripcion(txtDescripcion.getText());
            p.setPrecio(precio);
            p.setStockActual((Integer) spnStockActual.getValue());
            p.setStockMinimo((Integer) spnStockMinimo.getValue());
            p.setCategoriaId(categoriaId);
            p.setProveedorId(proveedorId);

            if (productoEditar == null) {
                // Alta nueva
                productoService.crearProducto(p);
            } else {
                // Edicion: mantener el id del producto original
                p.setId(productoEditar.getId());
                productoService.editarProducto(p);
            }

            guardado = true;
            dispose();

        } catch (IllegalArgumentException ex) {
            // Error de validacion del service
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validacion", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isGuardado() {
        return guardado;
    }
}