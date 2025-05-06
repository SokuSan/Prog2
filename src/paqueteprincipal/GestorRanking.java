package paqueteprincipal;

import java.awt.Component;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class GestorRanking {
    private static final String ARCHIVO = "ranking.txt";
    private static final int MAX_RECORDS = 10;

    public static void agregarNuevoTiempo(RecordTiempo nuevo) {
        List<RecordTiempo> ranking = leerRanking();
        ranking.add(nuevo);
        Collections.sort(ranking);
        if (ranking.size() > MAX_RECORDS) {
            ranking = ranking.subList(0, MAX_RECORDS);
        }
        guardarRanking(ranking);
    }

    public static void mostrarRanking(Component parent) {
        List<RecordTiempo> ranking = leerRanking();
        StringBuilder mensaje = new StringBuilder("Top 10 Mejores Tiempos:\n\n");
        for (int i = 0; i < ranking.size(); i++) {
            mensaje.append(String.format("%2d. %s\n", i + 1, ranking.get(i)));
        }
        JOptionPane.showMessageDialog(parent, mensaje.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

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
            // Archivo no existe todavía
        }
        return lista;
    }

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
