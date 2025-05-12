package paqueteprincipal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Clase que representa una imagen dividida en fragmentos cuadrados.
 * Utilizada para construir un puzzle de tamaño NxN.
 */
public class Imagen {
    
    private Image imagenOriginal; // Imagen escalada base
    private Image[][] fragmentos; // Fragmentos de la imagen
    private int tamanio; // Tamaño del puzzle (NxN)

    /**
     * Carga una imagen desde el archivo y la divide en fragmentos.
     *
     * @param rutaArchivo Ruta del archivo de imagen.
     * @param tamanio     Tamaño del puzzle (número de filas/columnas).
     */
    public Imagen(String rutaArchivo, int tamanio) {
        this.tamanio = tamanio;
        this.imagenOriginal = cargarImagen(rutaArchivo);
        if (this.imagenOriginal != null) {
            this.fragmentos = dividirEnFragmentos(imagenOriginal, tamanio);
        } else {
            System.err.println("No se pudo cargar la imagen. fragmentos será null.");
            this.fragmentos = null;
        }
    }

    /**
     * Carga y escala una imagen desde la ruta indicada.
     *
     * @param ruta Ruta del archivo de imagen.
     * @return Imagen escalada o null si no se pudo cargar.
     */
    private Image cargarImagen(String ruta) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                System.err.println("No se encontró el archivo: " + ruta);
                return null;
            }
            BufferedImage imagen = ImageIO.read(archivo);
            if (imagen == null) {
                System.err.println("No se pudo leer el archivo como imagen: " + ruta);
                return null;
            }
            final int TAMANO_IMAGEN = 600;
            return imagen.getScaledInstance(TAMANO_IMAGEN, TAMANO_IMAGEN, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Divide la imagen en fragmentos cuadráticos de igual tamaño.
     *
     * @param img     Imagen original.
     * @param tamanio Número de fragmentos por fila y columna.
     * @return Matriz de imágenes con los fragmentos.
     */
    private Image[][] dividirEnFragmentos(Image img, int tamanio) {
        Image[][] fragmentos = new Image[tamanio][tamanio];

        // Asegurar que la imagen sea un BufferedImage
        if (!(img instanceof BufferedImage)) {
            BufferedImage temp = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            temp.getGraphics().drawImage(img, 0, 0, null);
            img = temp;
        }

        BufferedImage imagenBuffered = (BufferedImage) img;
        int ancho = imagenBuffered.getWidth() / tamanio;
        int alto = imagenBuffered.getHeight() / tamanio;

        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < tamanio; j++) {
                // Deja la última casilla vacía para representar el espacio libre del puzzle
                if (i == tamanio - 1 && j == tamanio - 1) {
                    fragmentos[i][j] = null;
                } else {
                    fragmentos[i][j] = imagenBuffered.getSubimage(j * ancho, i * alto, ancho, alto);
                }
            }
        }

        return fragmentos;
    }

    /**
     * Devuelve un fragmento de imagen en una posición específica.
     *
     * @param fila    Fila del fragmento.
     * @param columna Columna del fragmento.
     * @return Fragmento de imagen, o null si no está cargado.
     */
    public Image getFragmento(int fila, int columna) {
        return fragmentos != null ? fragmentos[fila][columna] : null;
    }

    /**
     * Devuelve el tamaño del puzzle (N).
     *
     * @return Número de filas o columnas de fragmentos.
     */
    public int getTamanio() {
        return tamanio;
    }
}
