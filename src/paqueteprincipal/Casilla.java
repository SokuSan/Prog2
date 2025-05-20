// Autores: Pedro Hernandez Muñoz, Laia Moñino Peñalva


package paqueteprincipal;

import java.awt.Image;

/**
 * Representa una casilla del rompecabezas. Cada casilla contiene un valor, su
 * posición en la cuadrícula, y un fragmento de imagen correspondiente.
 */
public class Casilla {

    private int valor;
    private int fila;
    private int columna;
    private Image fragmento;

    /**
     * Constructor de la clase Casilla.
     *
     * @param valor
     * @param fila
     * @param columna
     * @param fragmento
     */
    public Casilla(int valor, int fila, int columna, Image fragmento) {
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
        this.fragmento = fragmento;
    }

    //Getters y setters
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Image getFragmento() {
        return fragmento;
    }

    public void setFragmento(Image fragmento) {
        this.fragmento = fragmento;
    }

    /**
     * Establece la nueva posición de la casilla en la cuadrícula.
     *
     * @param fila
     * @param columna
     */
    public void setPosicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

}
