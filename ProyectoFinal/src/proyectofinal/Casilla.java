package proyectofinal;

import java.awt.Image;

public class Casilla {
    private int valor; // de 1 a 8, o 0 si está vacía
    private int fila, columna;  // posición en la cuadrícula
    private Image fragmento; // fragmento de imagen correspondiente

    public Casilla(int valor, int fila, int columna, Image fragmento) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
        this.fragmento = fragmento;
    }

    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }

    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public Image getFragmento() { return fragmento; }
    public void setFragmento(Image fragmento) { this.fragmento = fragmento; }
}
