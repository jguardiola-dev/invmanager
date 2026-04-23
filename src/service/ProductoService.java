package service;

import dao.ProductoDAO;
import model.Producto;

import java.sql.SQLException;

public class ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoService() {
        this.productoDAO = new ProductoDAO();
    }

    public void crearProducto(Producto p) throws SQLException {
        validar(p);
        productoDAO.insertar(p);
    }

    public void editarProducto(Producto p) throws SQLException {
        validar(p);
        productoDAO.actualizar(p);
    }

    // Validaciones comunes para crear y editar
    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        if (p.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que 0");
        }
        if (p.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo");
        }
        if (p.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock minimo no puede ser negativo");
        }
    }
}