package paqueteprincipal;

import java.awt.Component;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class GestorRanking {

    private static final String ARCHIVO = "src/recursos/ranking.txt"; // archivo donde se guarda el ranking
    private static final int MAX_RECORDS = 10; // Número máximo de registros en el ranking

    /**
     * Agrega un nuevo tiempo al ranking, lo ordena y guarda.
     */
    public static void agregarNuevoTiempo(RecordTiempo nuevo) {
        List<RecordTiempo> ranking = leerRanking();
        ranking.add(nuevo);

        RecordTiempo.ordenarRanking(ranking);

        if (ranking.size() > MAX_RECORDS) {
            ranking = new ArrayList<>(ranking.subList(0, MAX_RECORDS));
        }

        guardarRanking(ranking);
    }

    /**
     * Muestra el ranking actual en un cuadro de diálogo.
     */
    public static void mostrarRanking(Component parent) {
        List<RecordTiempo> ranking = leerRanking();
        StringBuilder mensaje = new StringBuilder("Top 10 Mejores Tiempos:\n\n");
        for (int i = 0; i < ranking.size(); i++) {
            RecordTiempo r = ranking.get(i);
            mensaje.append(String.format("%2d. %s - Tiempo: %d seg\n", i + 1, r.getNombre(), r.getSegundos()));
        }
        JOptionPane.showMessageDialog(parent, mensaje.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Lee el ranking desde el archivo y lo devuelve como una lista de
     * RecordTiempo.
     */
    private static List<RecordTiempo> leerRanking() {
        List<RecordTiempo> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            System.out.println("El archivo no existe, se devolverá lista vacía.");
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    String nombre = partes[0];
                    int segundos = Integer.parseInt(partes[1]);
                    System.out.println("Leído: " + nombre + " " + segundos);
                    lista.add(new RecordTiempo(nombre, segundos));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo, se devolverá la lista vacía");
        }
        return lista;
    }

    /**
     * Guarda la lista de registros en el archivo de ranking.
     */
    private static void guardarRanking(List<RecordTiempo> lista) {
        try {
            File archivo = new File(ARCHIVO);
            if (!archivo.getParentFile().exists()) {
                archivo.getParentFile().mkdirs();
            }

            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                for (RecordTiempo record : lista) {
                    pw.println(record.getNombre() + ";" + record.getSegundos());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
