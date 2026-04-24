package ui;

import dao.CategoriaDAO;
import model.Categoria;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.sql.SQLException;
import java.util.List;

public class VentanaCategorias extends JDialog {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final JList<Categoria> lista = new JList<>();
    private final DefaultListModelCategoria listModel = new DefaultListModelCategoria();

    public VentanaCategorias(Frame parent) {
        super(parent, "Gestion de categorias", true);
        configurarDialog();
        cargarCategorias();
    }

    private void configurarDialog() {
        setSize(400, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        lista.setModel(listModel);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnNuevo = new JButton("Nueva");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> accionNueva());
        btnEditar.addActionListener(e -> accionEditar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.obtenerTodas();
            listModel.clear();
            for (Categoria c : categorias) {
                listModel.addElement(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorias: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionNueva() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la categoria:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        try {
            // Comprobar antes de preguntar mas cosas si el nombre ya existe
            if (categoriaDAO.existePorNombre(nombre.trim())) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una categoria con ese nombre",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String descripcion = JOptionPane.showInputDialog(this, "Descripcion:");
            if (descripcion == null) {
                descripcion = "";
            }

            Categoria c = new Categoria(0, nombre.trim(), descripcion.trim());
            categoriaDAO.insertar(c);
            cargarCategorias();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al crear: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEditar() {
        Categoria seleccionada = lista.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = (String) JOptionPane.showInputDialog(this, "Nombre:",
                "Editar categoria", JOptionPane.PLAIN_MESSAGE, null, null, seleccionada.getNombre());
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        try {
            // Si el nombre cambia, comprobar que no choque con otro
            if (!nombre.trim().equals(seleccionada.getNombre())
                    && categoriaDAO.existePorNombre(nombre.trim())) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe otra categoria con ese nombre",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String descripcion = (String) JOptionPane.showInputDialog(this, "Descripcion:",
                    "Editar categoria", JOptionPane.PLAIN_MESSAGE, null, null, seleccionada.getDescripcion());
            if (descripcion == null) {
                return;
            }

            seleccionada.setNombre(nombre.trim());
            seleccionada.setDescripcion(descripcion.trim());
            categoriaDAO.actualizar(seleccionada);
            cargarCategorias();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminar() {
        Categoria seleccionada = lista.getSelectedValue();
        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una categoria primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "Eliminar la categoria '" + seleccionada.getNombre() + "'?\n"
                        + "Los productos asociados quedaran sin categoria.",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                categoriaDAO.eliminarConTransaccion(seleccionada.getId());
                cargarCategorias();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}