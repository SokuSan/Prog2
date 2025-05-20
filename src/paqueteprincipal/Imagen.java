// Autores: Pedro Hernandez Muñoz, Laia Moñino Peñalva


package paqueteprincipal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Clase que representa una imagen dividida en fragmentos cuadrados. Utilizada
 * para construir un puzzle de tamaño NxN.
 */
public class Imagen {

    private final Image imagenOriginal;
    private final Image[][] fragmentos;
    private final int tamanio;

    /**
     * Constructor que carga la imagen desde un InputStream.
     *
     * @param inputStream
     * @param tamanio
     */
    public Imagen(InputStream inputStream, int tamanio) {
        this.tamanio = tamanio;
        this.imagenOriginal = cargarImagenDesdeStream(inputStream);
        if (this.imagenOriginal != null) {
            this.fragmentos = dividirEnFragmentos(imagenOriginal, tamanio);
        } else {
            System.err.println("No se pudo cargar la imagen desde InputStream. fragmentos sera null.");
            this.fragmentos = null;
        }
    }

    /**
     * Carga y escala una imagen desde un InputStream.
     *
     * @param stream
     * @return
     */
    private Image cargarImagenDesdeStream(InputStream stream) {
        try {
            BufferedImage imagen = ImageIO.read(stream);
            return escalarImagen(imagen);
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen desde InputStream: " + e.getMessage());
            return null;
        }
    }

    /**
     * Escala la imagen a un tamaño fijo.
     *
     * @param imagen
     * @return
     */
    private Image escalarImagen(BufferedImage imagen) {
        if (imagen == null) {
            return null;
        }
        final int TAMANO_IMAGEN = 600;
        return imagen.getScaledInstance(TAMANO_IMAGEN, TAMANO_IMAGEN, Image.SCALE_SMOOTH);
    }

    /**
     * Divide la imagen en fragmentos cuadráticos de igual tamaño.
     *
     * @param img
     * @param tamanio
     * @return
     */
    private Image[][] dividirEnFragmentos(Image img, int tamanio) {
        Image[][] fragmentos = new Image[tamanio][tamanio];

        // Convertir a BufferedImage si es necesario
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
                if (i == tamanio - 1 && j == tamanio - 1) {
                    fragmentos[i][j] = null; // última casilla vacía
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
     * @param fila
     * @param columna
     * @return
     */
    public Image getFragmento(int fila, int columna) {
        return fragmentos != null ? fragmentos[fila][columna] : null;
    }

    /**
     * Devuelve el tamaño del puzzle (N).
     */
    public int getTamanio() {
        return tamanio;
    }
}
