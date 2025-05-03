package paqueteprincipal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Imagen {
    private Image imagenOriginal;
    private Image[][] fragmentos; // Matriz NxN del puzzle
    private int tamanio;

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

            // Reescalar la imagen a un tamaño estándar para consistencia (opcional)
            int TAMANO_IMAGEN = 600; // 600x600 píxeles
            Image imagenEscalada = imagen.getScaledInstance(TAMANO_IMAGEN, TAMANO_IMAGEN, Image.SCALE_SMOOTH);
            return imagenEscalada;
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Image[][] dividirEnFragmentos(Image img, int tamanio) {
        Image[][] fragmentos = new Image[tamanio][tamanio];

        // Convertir a BufferedImage si no lo es
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
                    fragmentos[i][j] = null; // Última casilla vacía
                } else {
                    fragmentos[i][j] = imagenBuffered.getSubimage(j * ancho, i * alto, ancho, alto);
                }
            }
        }

        return fragmentos;
    }

    public Image getFragmento(int fila, int columna) {
        return fragmentos != null ? fragmentos[fila][columna] : null;
    }

    public int getTamanio() {
        return tamanio;
    }
}