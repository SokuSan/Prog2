// Autores: Pedro Hernandez Muñoz, Laia Moñino Peñalva


package paqueteprincipal;

import java.util.*;

/**
 * Clase que representa el modelo lógico del rompecabezas. Gestiona la matriz de
 * casillas, el estado actual del puzzle, el número de turnos y las operaciones
 * de inicialización, mezcla y movimiento.
 */
public class Puzzle {

    private Casilla[][] casillas;
    private int filaVacia, columnaVacia;
    private int turnos;
    private Imagen imagen;
    private int tamanio;

    /**
     * Constructor del puzzle. Recibe una imagen y establece el tamaño.
     *
     * @param imagen
     */
    public Puzzle(Imagen imagen) {
        this.imagen = imagen;
        this.tamanio = imagen.getTamanio();
        inicializar();
    }

    /**
     * Inicializa el puzzle en orden correcto (estado resuelto).
     */
    public void inicializar() {
        casillas = new Casilla[tamanio][tamanio];
        int valor = 1;

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                if (i == tamanio - 1 && j == tamanio - 1) {
                    // Última casilla vacía
                    casillas[i][j] = new Casilla(0, i, j, null);
                    filaVacia = i;
                    columnaVacia = j;
                } else {
                    // Fragmento con valor creciente
                    casillas[i][j] = new Casilla(valor, i, j, imagen.getFragmento(i, j));
                    valor++;
                }
            }
        }

        turnos = 0; // Reinicia el contador de turnos
    }

    /**
     * Mezcla aleatoriamente las casillas usando un algoritmo de barajado.
     */
    public void desordenar() {
        List<Casilla> lista = new ArrayList<>();

        // Añade todas las casillas a una lista
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                lista.add(casillas[i][j]);
            }
        }

        // Mezcla aleatoriamente las casillas
        Random rand = new Random();
        for (int i = lista.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Collections.swap(lista, i, j);
        }

        // Reasigna las casillas a la matriz y actualiza posición vacía
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

        turnos = 0; // Reinicia los turnos tras mezclar
    }

    /**
     * Mueve una casilla en la dirección indicada (si es posible).
     *
     * @param direccion
     * @return
     */
    public boolean mover(char direccion) {
        int dx = 0, dy = 0;

        // Traduce la dirección a desplazamiento
        switch (direccion) {
            case 's':
                dx = -1;
                break; // Abajo
            case 'w':
                dx = 1;
                break;  // Arriba
            case 'd':
                dy = -1;
                break; // Derecha
            case 'a':
                dy = 1;
                break;  // Izquierda
            default:
                return false;   // Dirección no válida
        }

        int nuevaFila = filaVacia + dx;
        int nuevaColumna = columnaVacia + dy;

        // Verifica si la nueva posición es válida
        if (nuevaFila < 0 || nuevaFila >= tamanio || nuevaColumna < 0 || nuevaColumna >= tamanio) {
            return false;
        }

        // Intercambia la casilla vacía con la adyacente
        intercambiarCasillas(filaVacia, columnaVacia, nuevaFila, nuevaColumna);
        filaVacia = nuevaFila;
        columnaVacia = nuevaColumna;
        turnos++;

        return true;
    }

    /**
     * Intercambia dos casillas en la matriz.
     *
     * @param fila1
     * @param col1
     * @param fila2
     * @param col2
     */
    private void intercambiarCasillas(int fila1, int col1, int fila2, int col2) {
        Casilla temp = casillas[fila1][col1];
        casillas[fila1][col1] = casillas[fila2][col2];
        casillas[fila2][col2] = temp;
    }

    /**
     * Verifica si el puzzle está resuelto (orden correcto).
     *
     * @return
     */
    public boolean estaResuelto() {
        int valorEsperado = 1;

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                int valor = casillas[i][j].getValor();

                if (i == tamanio - 1 && j == tamanio - 1) {
                    // Última casilla debe ser la vacía
                    return valor == 0;
                }

                if (valor != valorEsperado) {
                    return false;
                }

                valorEsperado++;
            }
        }

        return true;
    }

    /**
     * Devuelve la casilla en una posición específica.
     *
     * @param fila
     * @param columna
     * @return
     */
    public Casilla getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    // Getters
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
