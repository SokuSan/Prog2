package proyectofinal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Imagen {
    private Image imagenOriginal;
    private Image[][] fragmentos; // 3x3, el último puede ser null (vacío)

    public Imagen(String rutaArchivo) {
        this.imagenOriginal = cargarImagen(rutaArchivo);
        this.fragmentos = dividirEnFragmentos(imagenOriginal);
    }

    private Image cargarImagen(String ruta) {
    try {
        BufferedImage imagen = ImageIO.read(new File(ruta));
        return imagen;
    } catch (IOException e) {
        System.err.println("Error al cargar la imagen: " + e.getMessage());
        return null;
    }
}


    private Image[][] dividirEnFragmentos(Image img) {
        int filas = 3, columnas = 3;
        Image[][] fragmentos = new Image[filas][columnas];

        if (!(img instanceof BufferedImage)) {
            // Convertir a BufferedImage si no lo es
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
                    fragmentos[i][j] = null; // último fragmento es la casilla vacía
                } else {
                    fragmentos[i][j] = imagenBuffered.getSubimage(j * ancho, i * alto, ancho, alto);
                }
            }
        }

        return fragmentos;
    }


    public Image getFragmento(int fila, int columna) {
        return fragmentos[fila][columna];
    }
}
