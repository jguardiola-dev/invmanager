package ui;

import dao.DatabaseConnection;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.crearTablas();
        DatabaseConnection.insertarDatosPrueba();

        // Swing debe arrancar en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}