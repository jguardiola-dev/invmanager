package ui;

import dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        // Preparamos la base de datos al arrancar
        DatabaseConnection.crearTablas();
        DatabaseConnection.insertarDatosPrueba();

        // Listamos los productos para comprobar que la conexion funciona
        String sql = "SELECT p.id, p.nombre, p.precio, p.stock_actual, "
                + "c.nombre AS categoria, pr.nombre AS proveedor "
                + "FROM producto p "
                + "LEFT JOIN categoria c ON p.categoria_id = c.id "
                + "LEFT JOIN proveedor pr ON p.proveedor_id = pr.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("=== Productos en inventario ===");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock_actual");
                String categoria = rs.getString("categoria");
                String proveedor = rs.getString("proveedor");

                System.out.println(id + " | " + nombre + " | " + precio + " EUR | stock: " + stock
                        + " | " + categoria + " | " + proveedor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
    }
}