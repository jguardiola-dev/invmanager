package ui;

import dao.ProveedorDAO;
import model.Proveedor;

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

public class VentanaProveedores extends JDialog {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final JList<Proveedor> lista = new JList<>();
    private final DefaultListModelProveedor listModel = new DefaultListModelProveedor();

    public VentanaProveedores(Frame parent) {
        super(parent, "Gestion de proveedores", true);
        configurarDialog();
        cargarProveedores();
    }

    private void configurarDialog() {
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        lista.setModel(listModel);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        btnNuevo.addActionListener(e -> accionNuevo());
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

    private void cargarProveedores() {
        try {
            List<Proveedor> proveedores = proveedorDAO.obtenerTodos();
            listModel.clear();
            for (Proveedor p : proveedores) {
                listModel.addElement(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionNuevo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del proveedor:");
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }
        String contacto = JOptionPane.showInputDialog(this, "Persona de contacto:");
        if (contacto == null) contacto = "";
        String telefono = JOptionPane.showInputDialog(this, "Telefono:");
        if (telefono == null) telefono = "";
        String email = JOptionPane.showInputDialog(this, "Email:");
        if (email == null) email = "";

        try {
            Proveedor p = new Proveedor(0, nombre.trim(), contacto.trim(), telefono.trim(), email.trim());
            proveedorDAO.insertar(p);
            cargarProveedores();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al crear: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEditar() {
        Proveedor seleccionado = lista.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = (String) JOptionPane.showInputDialog(this, "Nombre:",
                "Editar proveedor", JOptionPane.PLAIN_MESSAGE, null, null, seleccionado.getNombre());
        if (nombre == null || nombre.trim().isEmpty()) return;

        String contacto = (String) JOptionPane.showInputDialog(this, "Contacto:",
                "Editar proveedor", JOptionPane.PLAIN_MESSAGE, null, null, seleccionado.getContacto());
        if (contacto == null) return;

        String telefono = (String) JOptionPane.showInputDialog(this, "Telefono:",
                "Editar proveedor", JOptionPane.PLAIN_MESSAGE, null, null, seleccionado.getTelefono());
        if (telefono == null) return;

        String email = (String) JOptionPane.showInputDialog(this, "Email:",
                "Editar proveedor", JOptionPane.PLAIN_MESSAGE, null, null, seleccionado.getEmail());
        if (email == null) return;

        try {
            seleccionado.setNombre(nombre.trim());
            seleccionado.setContacto(contacto.trim());
            seleccionado.setTelefono(telefono.trim());
            seleccionado.setEmail(email.trim());
            proveedorDAO.actualizar(seleccionado);
            cargarProveedores();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionEliminar() {
        Proveedor seleccionado = lista.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un proveedor primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "Eliminar el proveedor '" + seleccionado.getNombre() + "'?\n"
                        + "Los productos asociados quedaran sin proveedor.",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                proveedorDAO.eliminarConTransaccion(seleccionado.getId());
                cargarProveedores();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}