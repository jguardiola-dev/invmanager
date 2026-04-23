package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {

    private AppConfig() {
        // clase de utilidad, no se instancia
    }

    private static final Properties PROPS = new Properties();
    private static boolean cargado = false;

    private static void cargar() {
        if (cargado) {
            return;
        }
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            PROPS.load(fis);
        } catch (IOException e) {
            // Si no existe el fichero, usamos valores por defecto
            System.out.println("No se encontro config.properties, se usan valores por defecto");
        }
        cargado = true;
    }

    public static String getSeparadorCSV() {
        cargar();
        return PROPS.getProperty("csv.separador", ";");
    }

    public static int getStockMinimoDefecto() {
        cargar();
        try {
            return Integer.parseInt(PROPS.getProperty("stock.minimo.defecto", "5"));
        } catch (NumberFormatException e) {
            return 5;
        }
    }
}