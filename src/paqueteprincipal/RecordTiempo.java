// Autores: Pedro Hernandez Muñoz, Laia Moñino Peñalva


package paqueteprincipal;

import java.util.Collections;
import java.util.List;

public class RecordTiempo {

    private final String nombre;
    private final int segundos;

    /**
     * Constructor que inicializa un nuevo récord de tiempo.
     *
     * @param nombre
     * @param segundos
     */
    public RecordTiempo(String nombre, int segundos) {
        this.nombre = nombre;
        this.segundos = segundos;
    }

    /**
     * Devuelve el nombre del jugador que consiguió el récord.
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el tiempo registrado en segundos.
     *
     * @return
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     * Devuelve una representación en texto del récord.
     *
     * @return
     */
    @Override
    public String toString() {
        return nombre + ";" + segundos;
    }

    /**
     * Método que organiza una lista de RecordTiempo de menor a mayor tiempo.
     *
     * @param lista
     */
    public static void ordenarRanking(List<RecordTiempo> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (lista.get(j).segundos > lista.get(j + 1).segundos) {
                    Collections.swap(lista, j, j + 1);
                }
            }
        }
    }
}
