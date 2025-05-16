package paqueteprincipal;

/**
 * Interfaz funcional que define el callback que se ejecutará
 * cuando el usuario introduzca una medida válida del rompecabezas.
 */
public interface PuzzleDialogCallback {

    /**
     * Método a implementar para inicializar el rompecabezas con la medida indicada.
     */
    void inicializarPuzzle(int medida);
}
