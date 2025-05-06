package paqueteprincipal;

import java.util.*;

public class Puzzle {
    private Casilla[][] casillas;
    private int filaVacia, columnaVacia;
    private int turnos;
    private Imagen imagen;
    private int tamanio;

    public Puzzle(Imagen imagen) {
        this.imagen = imagen;
        this.tamanio = imagen.getTamanio();
        inicializar();
    }

    public void inicializar() {
        casillas = new Casilla[tamanio][tamanio];
        int valor = 1;
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (i == tamanio - 1 && j == tamanio - 1) {
                    casillas[i][j] = new Casilla(0, i, j, null);
                    filaVacia = i;
                    columnaVacia = j;
                } else {
                    casillas[i][j] = new Casilla(valor, i, j, imagen.getFragmento(i, j));
                    valor++;
                }
            }
        }
        turnos = 0;
    }

    public void desordenar() {
        List<Casilla> lista = new ArrayList<>();
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                lista.add(casillas[i][j]);
            }
        }

        Random rand = new Random();
        for (int i = lista.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Collections.swap(lista, i, j);
        }

        int index = 0;
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
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
            case 's': dx = -1; break;
            case 'w': dx = 1; break;
            case 'd': dy = -1; break;
            case 'a': dy = 1; break;
            default: return false;
        }

        int nuevaFila = filaVacia + dx;
        int nuevaColumna = columnaVacia + dy;

        if (nuevaFila < 0 || nuevaFila >= tamanio || nuevaColumna < 0 || nuevaColumna >= tamanio) {
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
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                int valor = casillas[i][j].getValor();
                if (i == tamanio - 1 && j == tamanio - 1) {
                    return valor == 0;
                }
                if (valor != valorEsperado) return false;
                valorEsperado++;
            }
        }
        return true;
    }

    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public int getFilaVacia() {
        return filaVacia;
    }

    public int getColumnaVacia() {
        return columnaVacia;
    }

    public int getTurnos() {
        return turnos;
    }

    public int getTamanio() {
        return tamanio;
    }
}
