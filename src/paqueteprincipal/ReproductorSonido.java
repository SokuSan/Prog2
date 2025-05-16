package paqueteprincipal;

import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;

public class ReproductorSonido {
    private static Clip clipMovimiento;
    private static Clip clipCompletado;

    /**
     * Carga los sonidos desde archivos WAV ubicados en la carpeta de recursos. 
     * Este método debe llamarse una vez al inicio del juego para preparar los clips.
     */
    public static void cargarSonidos() {
        try {
            File archivoMovimiento = new File("recursos/movimiento.wav").getAbsoluteFile();
            File archivoCompletado = new File("recursos/completado.wav").getAbsoluteFile();

            if (archivoMovimiento.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(archivoMovimiento);
                clipMovimiento = AudioSystem.getClip();
                clipMovimiento.open(audioIn);
            }

            if (archivoCompletado.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(archivoCompletado);
                clipCompletado = AudioSystem.getClip();
                clipCompletado.open(audioIn);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al cargar los sonidos: " + e.getMessage());
        }
    }

    /**
     * Reproduce el sonido de movimiento de las casillas
     */
    public static void reproducirMovimiento() {
        if (clipMovimiento != null) {
            clipMovimiento.setFramePosition(0);
            clipMovimiento.start();
        }
    }

    /**
     * Reproduce el sonido que suena al completar el rompecabezas
     */
    public static void reproducirCompletado() {
        if (clipCompletado != null) {
            clipCompletado.setFramePosition(0);
            clipCompletado.start();
        }
    }
}
