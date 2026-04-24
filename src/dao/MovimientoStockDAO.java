package dao;

import model.MovimientoStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimientoStockDAO {

    public void insertar(MovimientoStock m) throws SQLException {
        String sql = "INSERT INTO movimiento_stock (producto_id, tipo, cantidad, fecha, motivo) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, m.getProductoId());
            ps.setString(2, m.getTipo());
            ps.setInt(3, m.getCantidad());
            ps.setString(4, m.getFecha());
            ps.setString(5, m.getMotivo());
            ps.executeUpdate();
        }
    }

    public List<MovimientoStock> obtenerPorProducto(int productoId) throws SQLException {
        List<MovimientoStock> lista = new ArrayList<>();
        String sql = "SELECT id, producto_id, tipo, cantidad, fecha, motivo "
                + "FROM movimiento_stock "
                + "WHERE producto_id = ? "
                + "ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoStock m = new MovimientoStock();
                    m.setId(rs.getInt("id"));
                    m.setProductoId(rs.getInt("producto_id"));
                    m.setTipo(rs.getString("tipo"));
                    m.setCantidad(rs.getInt("cantidad"));
                    m.setFecha(rs.getString("fecha"));
                    m.setMotivo(rs.getString("motivo"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    // Suma total de entradas menos salidas para un producto
    public int calcularBalance(int productoId) throws SQLException {
        int entradas = sumarPorTipo(productoId, "ENTRADA");
        int salidas = sumarPorTipo(productoId, "SALIDA");
        return entradas - salidas;
    }

    private int sumarPorTipo(int productoId, String tipo) throws SQLException {
        String sql = "SELECT SUM(cantidad) FROM movimiento_stock WHERE producto_id = ? AND tipo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productoId);
            ps.setString(2, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}