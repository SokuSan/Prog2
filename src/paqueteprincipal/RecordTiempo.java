package paqueteprincipal;

import java.time.LocalDate;

public class RecordTiempo implements Comparable<RecordTiempo> {
    private String nombre;
    private int segundos;
    private LocalDate fecha;

    public RecordTiempo(String nombre, int segundos, LocalDate fecha) {
        this.nombre = nombre;
        this.segundos = segundos;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSegundos() {
        return segundos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public int compareTo(RecordTiempo otro) {
        return Integer.compare(this.segundos, otro.segundos);
    }

    @Override
    public String toString() {
        int minutos = segundos / 60;
        int seg = segundos % 60;
        return String.format("%s - %02d:%02d - %s", nombre, minutos, seg, fecha);
    }
}
