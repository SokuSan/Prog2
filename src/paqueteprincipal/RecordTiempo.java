package paqueteprincipal;

public class RecordTiempo implements Comparable<RecordTiempo> {
    private String nombre;
    private int segundos;
    

    /**
     * Constructor que inicializa un nuevo récord de tiempo.
     */
    public RecordTiempo(String nombre, int segundos) {
        this.nombre = nombre;
        this.segundos = segundos;
    }

    /**
     * Devuelve el nombre del jugador que consiguió el récord.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el tiempo registrado en segundos.
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     * Compara este objeto RecordTiempo con otro RecordTiempo para ordenarlos de menor a mayor tiempo.
     */
    @Override
    public int compareTo(RecordTiempo otro) {
        return Integer.compare(this.segundos, otro.segundos);
    }

    /**
     * Devuelve una representación en texto del récord,
     * con el tiempo formateado en minutos y segundos.
     */
    @Override
    public String toString() {
        return nombre + ";" + segundos;
    }
}
