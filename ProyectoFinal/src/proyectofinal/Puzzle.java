package proyectofinal;

import java.awt.Image;
import java.util.*;

public class Puzzle {
    private Casilla[][] casillas;
    private int filaVacia, columnaVacia;
    private int turnos;
    private Imagen imagen;

    public Puzzle(Imagen imagen) {
        this.imagen = imagen;
        inicializar(); // coloca las piezas en orden resuelto
    }

    public void inicializar() {
        casillas = new Casilla[3][3];
        int valor = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (valor <= 8) {
                    casillas[i][j] = new Casilla(valor, i, j, imagen.getFragmento(i, j));
                    valor++;
                } else {
                    casillas[i][j] = new Casilla(0, i, j, null); // casilla vacía
                    filaVacia = i;
                    columnaVacia = j;
                }
            }
        }
        turnos = 0;
    }

    public void desordenar() {
        List<Casilla> lista = new ArrayList<>();

        // Aplanamos la matriz a una lista
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                lista.add(casillas[i][j]);
            }
        }

        // Fisher-Yates Shuffle
        Random rand = new Random();
        for (int i = lista.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Casilla temp = lista.get(i);
            lista.set(i, lista.get(j));
            lista.set(j, temp);
        }

        // Reconstruimos la matriz y actualizamos posiciones
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Casilla c = lista.get(index++);
                c.setPosicion(i, j);
                casillas[i][j] = c;

                if (c.getValor() == 0) {
                    filaVacia = i;
                    columnaVacia = j;
                }
            }
        }

        turnos = 0;
    }


    public boolean mover(char direccion) {
        int dx = 0, dy = 0;
        switch (direccion) {
            case 'w': dx = -1; break;
            case 's': dx = 1; break;
            case 'a': dy = -1; break;
            case 'd': dy = 1; break;
            default: return false;
        }

        int nuevaFila = filaVacia + dx;
        int nuevaColumna = columnaVacia + dy;

        if (nuevaFila < 0 || nuevaFila >= 3 || nuevaColumna < 0 || nuevaColumna >= 3) {
            System.out.println("Movimiento Incorrecto: Fuera del rompecabezas");
            return false;
        }

        intercambiarCasillas(filaVacia, columnaVacia, nuevaFila, nuevaColumna);
        filaVacia = nuevaFila;
        columnaVacia = nuevaColumna;
        turnos++;
        return true;
    }

    private void intercambiarCasillas(int fila1, int col1, int fila2, int col2) {
        Casilla temp = casillas[fila1][col1];
        casillas[fila1][col1] = casillas[fila2][col2];
        casillas[fila2][col2] = temp;
    }

    public boolean estaResuelto() {
        int valorEsperado = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int valor = casillas[i][j].getValor();
                if (i == 2 && j == 2) return valor == 0;
                if (valor != valorEsperado) return false;
                valorEsperado++;
            }
        }
        return true;
    }

    public void imprimirPorTexto() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int valor = casillas[i][j].getValor();
                System.out.print((valor == 0 ? " " : valor) + " ");
            }
            System.out.println();
        }
    }

    public int getTurnos() {
        return turnos;
    }
}
