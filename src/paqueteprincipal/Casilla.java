package paqueteprincipal;

import java.awt.Image;

/**
 * Representa una casilla del rompecabezas. Cada casilla contiene un valor, su
 * posición en la cuadrícula, y un fragmento de imagen correspondiente.
 */

public class Casilla {
    private int valor; // de 1 a N^2 - 1, o 0 si está vacía
    private int fila; // Fila en la cuadricula
    private int columna;  // Columna en la cuadrícula
    private Image fragmento; // fragmento de imagen correspondiente a esta casilla

    /**
     * Constructor de la clase Casilla.
     *
     * @param valor     Valor numérico de la casilla.
     * @param fila      Fila en la que se encuentra la casilla.
     * @param columna   Columna en la que se encuentra la casilla.
     * @param fragmento Fragmento de imagen asociado a la casilla.
     */
    public Casilla(int valor, int fila, int columna, Image fragmento) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
        this.fragmento = fragmento;
    }

    //Getters y setters
    
    public int getValor() { return valor; }
    public void setValor(int valor) { this.valor = valor; }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }

    /**
     * Establece la nueva posición de la casilla en la cuadrícula.
     *
     * @param fila    Nueva fila.
     * @param columna Nueva columna.
     */
    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public Image getFragmento() { return fragmento; }
    public void setFragmento(Image fragmento) { this.fragmento = fragmento; }
}