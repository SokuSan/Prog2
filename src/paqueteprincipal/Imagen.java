package paqueteprincipal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

public class Imagen {
    private Image imagenOriginal;
    private Image[][] fragmentos; // Matriz 3x3 del puzzle

    public Imagen(String rutaArchivo) {
        this.imagenOriginal = cargarImagen(rutaArchivo);
        if (this.imagenOriginal != null) {
            this.fragmentos = dividirEnFragmentos(imagenOriginal);
        } else {
            System.err.println("No se pudo cargar la imagen. fragmentos será null.");
            this.fragmentos = null;
        }
    }

    private Image cargarImagen(String ruta) {
    try {
        InputStream is = getClass().getResourceAsStream(ruta);
        if (is == null) {
            System.err.println("No se encontró el recurso: " + ruta);
            return null;
        }
        return ImageIO.read(is);
    } catch (Exception e) {
        System.err.println("Error al cargar la imagen: " + e.getMessage());
        e.printStackTrace();
        return null;
        }
    }



    private Image[][] dividirEnFragmentos(Image img) {
        int filas = 3, columnas = 3;
        Image[][] fragmentos = new Image[filas][columnas];

        // Convertir a BufferedImage si no lo es
        if (!(img instanceof BufferedImage)) {
            BufferedImage temp = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = temp.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();
            img = temp;
        }

        BufferedImage imagenBuffered = (BufferedImage) img;
        int ancho = imagenBuffered.getWidth() / columnas;
        int alto = imagenBuffered.getHeight() / filas;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (i == 2 && j == 2) {
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
}
