package paqueteprincipal;

import java.awt.Component;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Clase utilitaria que gestiona el ranking de mejores tiempos de juego.
 * Guarda y carga los datos desde un archivo de texto.
 */
public class GestorRanking {
    
    private static final String ARCHIVO = "ranking.txt"; //archivo donde se guarda el ranking
    private static final int MAX_RECORDS = 10; //Numero maximo de registros en el ranking

    /**
     * Agrega un nuevo tiempo al ranking, lo ordena y guarda.
     *
     * @param nuevo Nuevo registro de tiempo a agregar.
     */
    public static void agregarNuevoTiempo(RecordTiempo nuevo) {
        List<RecordTiempo> ranking = leerRanking();
        ranking.add(nuevo);
        Collections.sort(ranking);
        
        // Limita el ranking a los mejores N tiempos
        if (ranking.size() > MAX_RECORDS) {
            ranking = ranking.subList(0, MAX_RECORDS);
        }
        guardarRanking(ranking);
    }

    /**
     * Muestra el ranking actual en un cuadro de diálogo.
     *
     * @param parent Componente padre para el cuadro de diálogo.
     */
    public static void mostrarRanking(Component parent) {
        List<RecordTiempo> ranking = leerRanking();
        StringBuilder mensaje = new StringBuilder("Top 10 Mejores Tiempos:\n\n");
        for (int i = 0; i < ranking.size(); i++) {
            mensaje.append(String.format("%2d. %s\n", i + 1, ranking.get(i)));
        }
        JOptionPane.showMessageDialog(parent, mensaje.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Lee el ranking desde el archivo y lo devuelve como una lista de RecordTiempo.
     *
     * @return Lista de registros leída del archivo.
     */
    private static List<RecordTiempo> leerRanking() {
        List<RecordTiempo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 3) {
                    String nombre = partes[0];
                    int segundos = Integer.parseInt(partes[1]);
                    LocalDate fecha = LocalDate.parse(partes[2]);
                    
                    lista.add(new RecordTiempo(nombre, segundos, fecha));
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe aún, se devuelve una lista vacía
        }
        return lista;
    }

    /**
     * Guarda la lista de registros en el archivo de ranking.
     *
     * @param lista Lista de registros a guardar.
     */
    private static void guardarRanking(List<RecordTiempo> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (RecordTiempo record : lista) {
                pw.println(record.getNombre() + ";" + record.getSegundos() + ";" + record.getFecha());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
