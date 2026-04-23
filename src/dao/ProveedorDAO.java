package dao;

import model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public void insertar(Proveedor p) throws SQLException {
        String sql = "INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getContacto());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getEmail());
            ps.executeUpdate();
        }
    }

    public List<Proveedor> obtenerTodos() throws SQLException {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, contacto, telefono, email FROM proveedor ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setContacto(rs.getString("contacto"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                lista.add(p);
            }
        }
        return lista;
    }

    public Proveedor obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, contacto, telefono, email FROM proveedor WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proveedor p = new Proveedor();
                    p.setId(rs.getInt("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setContacto(rs.getString("contacto"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setEmail(rs.getString("email"));
                    return p;
                }
            }
        }
        return null;
    }

    public void actualizar(Proveedor p) throws SQLException {
        String sql = "UPDATE proveedor SET nombre = ?, contacto = ?, telefono = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getContacto());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getEmail());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM proveedor WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public void eliminarConTransaccion(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Quitar el proveedor de los productos que lo tengan asignado
            String sqlUpdate = "UPDATE producto SET proveedor_id = NULL WHERE proveedor_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Borrar el proveedor
            String sqlDelete = "DELETE FROM proveedor WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}