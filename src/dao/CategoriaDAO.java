package dao;

import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void insertar(Categoria c) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
        }
    }

    public List<Categoria> obtenerTodas() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categoria ORDER BY nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        }
        return lista;
    }

    public Categoria obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion FROM categoria WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    return c;
                }
            }
        }
        return null;
    }

    public void actualizar(Categoria c) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";

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

            // Quitar la categoria de los productos que la tengan asignada
            String sqlUpdate = "UPDATE producto SET categoria_id = NULL WHERE categoria_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            // Borrar la categoria
            String sqlDelete = "DELETE FROM categoria WHERE id = ?";
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