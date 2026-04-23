package dao;

import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id, proveedor_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getStockMinimo());
            ps.setInt(6, p.getCategoriaId());
            ps.setInt(7, p.getProveedorId());
            ps.executeUpdate();
        }
    }

    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock_actual, p.stock_minimo, "
                + "p.categoria_id, p.proveedor_id, "
                + "c.nombre AS categoria_nombre, pr.nombre AS proveedor_nombre "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id "
                + "ORDER BY p.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        }
        return lista;
    }

    public Producto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock_actual, p.stock_minimo, "
                + "p.categoria_id, p.proveedor_id, "
                + "c.nombre AS categoria_nombre, pr.nombre AS proveedor_nombre "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id "
                + "WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        }
        return null;
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, "
                + "stock_actual = ?, stock_minimo = ?, categoria_id = ?, proveedor_id = ? "
                + "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStockActual());
            ps.setInt(5, p.getStockMinimo());
            ps.setInt(6, p.getCategoriaId());
            ps.setInt(7, p.getProveedorId());
            ps.setInt(8, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Metodo auxiliar para no repetir el mismo codigo de mapeo en obtenerTodos y obtenerPorId
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStockActual(rs.getInt("stock_actual"));
        p.setStockMinimo(rs.getInt("stock_minimo"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setProveedorId(rs.getInt("proveedor_id"));
        p.setCategoriaNombre(rs.getString("categoria_nombre"));
        p.setProveedorNombre(rs.getString("proveedor_nombre"));
        return p;
    }
    public List<Producto> buscarPorNombre(String texto) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock_actual, p.stock_minimo, "
                + "p.categoria_id, p.proveedor_id, "
                + "c.nombre AS categoria_nombre, pr.nombre AS proveedor_nombre "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id "
                + "WHERE p.nombre LIKE ? "
                + "ORDER BY p.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        }
        return lista;
    }

    public List<Producto> filtrar(Integer categoriaId, Integer proveedorId, Integer stockMin, Integer stockMax) throws SQLException {
        List<Producto> lista = new ArrayList<>();

        // Construimos la query paso a paso segun los filtros que vengan informados
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock_actual, p.stock_minimo, "
                        + "p.categoria_id, p.proveedor_id, "
                        + "c.nombre AS categoria_nombre, pr.nombre AS proveedor_nombre "
                        + "FROM producto p "
                        + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                        + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id "
                        + "WHERE 1=1 ");

        List<Object> parametros = new ArrayList<>();

        if (categoriaId != null) {
            sql.append("AND p.categoria_id = ? ");
            parametros.add(categoriaId);
        }
        if (proveedorId != null) {
            sql.append("AND p.proveedor_id = ? ");
            parametros.add(proveedorId);
        }
        if (stockMin != null) {
            sql.append("AND p.stock_actual >= ? ");
            parametros.add(stockMin);
        }
        if (stockMax != null) {
            sql.append("AND p.stock_actual <= ? ");
            parametros.add(stockMax);
        }

        sql.append("ORDER BY p.nombre");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        }
        return lista;
    }

    public List<Producto> obtenerStockBajo() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock_actual, p.stock_minimo, "
                + "p.categoria_id, p.proveedor_id, "
                + "c.nombre AS categoria_nombre, pr.nombre AS proveedor_nombre "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id "
                + "WHERE p.stock_actual < p.stock_minimo "
                + "ORDER BY p.nombre";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        }
        return lista;
    }

}