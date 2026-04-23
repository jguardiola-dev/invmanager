package util;

import model.Producto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVExporter {

    private CSVExporter() {
        // clase de utilidad, no se instancia
    }

    public static void exportar(List<Producto> productos, File fichero) throws IOException {
        // Usamos UTF-8 para que los acentos salgan bien en Excel
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fichero), StandardCharsets.UTF_8))) {

            // Cabecera
            bw.write("id;nombre;descripcion;precio;stock_actual;stock_minimo;categoria;proveedor");
            bw.newLine();

            // Una linea por producto
            for (Producto p : productos) {
                StringBuilder fila = new StringBuilder();
                fila.append(p.getId()).append(";");
                fila.append(escapar(p.getNombre())).append(";");
                fila.append(escapar(p.getDescripcion())).append(";");
                fila.append(p.getPrecio()).append(";");
                fila.append(p.getStockActual()).append(";");
                fila.append(p.getStockMinimo()).append(";");
                fila.append(escapar(p.getCategoriaNombre())).append(";");
                fila.append(escapar(p.getProveedorNombre()));

                bw.write(fila.toString());
                bw.newLine();
            }
        }
    }

    // Si el texto contiene el separador o saltos de linea, lo envolvemos en comillas
    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        if (texto.contains(";") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}