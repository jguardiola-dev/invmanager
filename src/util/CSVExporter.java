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
        // Separador configurable desde config.properties (por defecto ;)
        String sep = AppConfig.getSeparadorCSV();

        // Usamos UTF-8 para que los acentos salgan bien en Excel
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fichero), StandardCharsets.UTF_8))) {

            // Cabecera
            bw.write("id" + sep + "nombre" + sep + "descripcion" + sep + "precio" + sep
                    + "stock_actual" + sep + "stock_minimo" + sep + "categoria" + sep + "proveedor");
            bw.newLine();

            // Una linea por producto
            for (Producto p : productos) {
                StringBuilder fila = new StringBuilder();
                fila.append(p.getId()).append(sep);
                fila.append(escapar(p.getNombre(), sep)).append(sep);
                fila.append(escapar(p.getDescripcion(), sep)).append(sep);
                fila.append(p.getPrecio()).append(sep);
                fila.append(p.getStockActual()).append(sep);
                fila.append(p.getStockMinimo()).append(sep);
                fila.append(escapar(p.getCategoriaNombre(), sep)).append(sep);
                fila.append(escapar(p.getProveedorNombre(), sep));

                bw.write(fila.toString());
                bw.newLine();
            }
        }
    }

    // Si el texto contiene el separador o saltos de linea, lo envolvemos en comillas
    private static String escapar(String texto, String sep) {
        if (texto == null) {
            return "";
        }
        if (texto.contains(sep) || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
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