package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private DatabaseConnection() {
        // clase de utilidad, no se instancia
    }

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encuentra el driver de SQLite: " + e.getMessage());
        }
    }

    private static final String URL = "jdbc:sqlite:inventario.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // Activar las FK (por defecto vienen desactivadas en SQLite)
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void crearTablas() {
        String sqlCategoria = "CREATE TABLE IF NOT EXISTS categoria ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL UNIQUE,"
                + "descripcion TEXT)";

        String sqlProveedor = "CREATE TABLE IF NOT EXISTS proveedor ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "contacto TEXT,"
                + "telefono TEXT,"
                + "email TEXT)";

        String sqlProducto = "CREATE TABLE IF NOT EXISTS producto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "descripcion TEXT,"
                + "precio REAL NOT NULL CHECK (precio > 0),"
                + "stock_actual INTEGER NOT NULL CHECK (stock_actual >= 0),"
                + "stock_minimo INTEGER NOT NULL DEFAULT 0,"
                + "categoria_id INTEGER,"
                + "proveedor_id INTEGER,"
                + "FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT,"
                + "FOREIGN KEY (proveedor_id) REFERENCES proveedor(id) ON DELETE RESTRICT)";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sqlCategoria);
            st.execute(sqlProveedor);
            st.execute(sqlProducto);
        } catch (SQLException e) {
            System.out.println("Error creando tablas: " + e.getMessage());
        }
    }

    public static void insertarDatosPrueba() {
        // Si ya hay productos no insertamos nada para no duplicar
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM producto")) {

            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error comprobando datos: " + e.getMessage());
            return;
        }

        String insertCategorias = "INSERT INTO categoria (nombre, descripcion) VALUES "
                + "('Electronica', 'Dispositivos electronicos'),"
                + "('Papeleria', 'Material de oficina'),"
                + "('Alimentacion', 'Productos alimentarios')";

        String insertProveedores = "INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES "
                + "('Distribuciones Garcia S.L.', 'Ana Garcia', '961234567', 'pedidos@dgarcia.es'),"
                + "('Importaciones Mediterraneo', 'Luis Perez', '963987654', 'contacto@impmed.es')";

        String insertProductos = "INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id, proveedor_id) VALUES "
                + "('Teclado inalambrico', 'Teclado Bluetooth recargable', 29.95, 15, 5, 1, 1),"
                + "('Raton optico USB', 'Raton ergonomico', 9.50, 3, 10, 1, 1),"
                + "('Cuaderno A4 rayado', 'Cuaderno espiral 200 hojas', 4.25, 50, 20, 2, 2),"
                + "('Boligrafo azul', 'Pack 10 boligrafos', 3.00, 8, 15, 2, 2),"
                + "('Cafe molido 250g', 'Cafe natural tueste medio', 5.75, 25, 10, 3, 2)";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(insertCategorias);
            st.execute(insertProveedores);
            st.execute(insertProductos);
            System.out.println("Datos de prueba insertados.");
        } catch (SQLException e) {
            System.out.println("Error insertando datos: " + e.getMessage());
        }
    }
}