package ui;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import model.Categoria;
import model.Proveedor;
import util.CSVExporter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;
    private final ProveedorDAO proveedorDAO;
    private final ProductoTableModel tableModel;
    private final JTable tabla;

    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnEditar = new JButton("Editar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnExportar = new JButton("Exportar CSV");
    private final JTextField txtBuscar = new JTextField(20);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JComboBox<Proveedor> cmbProveedor = new JComboBox<>();

    // Centinelas para el "Todas" / "Todos" en los combos de filtro
    private static final Categoria CATEGORIA_TODAS = new Categoria(0, "Todas", "");
    private static final Proveedor PROVEEDOR_TODOS = new Proveedor(0, "Todos", "", "", "");

    public VentanaPrincipal() {
        super("InvManager - Gestion de inventario");

        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
        proveedorDAO = new ProveedorDAO();
        tableModel = new ProductoTableModel();
        tabla = new JTable(tableModel);

        configurarVentana();
        configurarBotones();
        cargarCombos();
        cargarProductos();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(null);

        // Panel con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);

        // Panel con busqueda y filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.add(new JLabel("Buscar:"));
        panelFiltros.add(txtBuscar);
        panelFiltros.add(new JLabel("Categoria:"));
        panelFiltros.add(cmbCategoria);
        panelFiltros.add(new JLabel("Proveedor:"));
        panelFiltros.add(cmbProveedor);

        // Panel superior agrupado en vertical
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.add(panelBotones);
        panelSuperior.add(panelFiltros);

        JScrollPane scroll = new JScrollPane(tabla);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void configurarBotones() {
        btnNuevo.addActionListener(e -> accionNuevo());
        btnEditar.addActionListener(e -> accionEditar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnExportar.addActionListener(e -> accionExportar());

        // Cualquier cambio en busqueda o filtros refresca la tabla
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        cmbCategoria.addActionListener(e -> aplicarFiltros());
        cmbProveedor.addActionListener(e -> aplicarFiltros());
    }

    private void cargarCombos() {
        try {
            // Primer item: "Todas/Todos" como sentinela
            cmbCategoria.addItem(CATEGORIA_TODAS);
            for (Categoria c : categoriaDAO.obtenerTodas()) {
                cmbCategoria.addItem(c);
            }

            cmbProveedor.addItem(PROVEEDOR_TODOS);
            for (Proveedor pr : proveedorDAO.obtenerTodos()) {
                cmbProveedor.addItem(pr);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar filtros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionNuevo() {
        FormularioProducto form = new FormularioProducto(this, null);
        form.setVisible(true);
        if (form.isGuardado()) {
            aplicarFiltros();
        }
    }

    private void accionEditar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        FormularioProducto form = new FormularioProducto(this, tableModel.getProductoEnFila(fila));
        form.setVisible(true);
        if (form.isGuardado()) {
            aplicarFiltros();
        }
    }

    private void accionEliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "Seguro que quieres eliminar el producto?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                productoDAO.eliminar(tableModel.getProductoEnFila(fila).getId());
                aplicarFiltros();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void accionExportar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar inventario como CSV");
        chooser.setSelectedFile(new File("inventario.csv"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fichero = chooser.getSelectedFile();
        if (!fichero.getName().toLowerCase().endsWith(".csv")) {
            fichero = new File(fichero.getAbsolutePath() + ".csv");
        }

        try {
            CSVExporter.exportar(productoDAO.obtenerTodos(), fichero);
            JOptionPane.showMessageDialog(this,
                    "Inventario exportado correctamente en:\n" + fichero.getAbsolutePath(),
                    "Exportacion completada", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltros() {
        try {
            String textoBusqueda = txtBuscar.getText().trim();
            Categoria categoriaFiltro = (Categoria) cmbCategoria.getSelectedItem();
            Proveedor proveedorFiltro = (Proveedor) cmbProveedor.getSelectedItem();

            // Si id=0 es el centinela "Todas/Todos", el filtro no aplica
            Integer categoriaId = (categoriaFiltro != null && categoriaFiltro.getId() != 0) ? categoriaFiltro.getId() : null;
            Integer proveedorId = (proveedorFiltro != null && proveedorFiltro.getId() != 0) ? proveedorFiltro.getId() : null;

            List<model.Producto> resultado;

            // Si el usuario esta buscando por nombre, usamos buscarPorNombre
            if (!textoBusqueda.isEmpty()) {
                resultado = productoDAO.buscarPorNombre(textoBusqueda);
                // Y filtramos manualmente los combos sobre el resultado
                if (categoriaId != null) {
                    resultado.removeIf(p -> p.getCategoriaId() != categoriaId);
                }
                if (proveedorId != null) {
                    resultado.removeIf(p -> p.getProveedorId() != proveedorId);
                }
            } else {
                // Si no hay busqueda, usamos filtrar directamente en BD
                resultado = productoDAO.filtrar(categoriaId, proveedorId, null, null);
            }

            tableModel.setProductos(resultado);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar filtros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarProductos() {
        try {
            tableModel.setProductos(productoDAO.obtenerTodos());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}