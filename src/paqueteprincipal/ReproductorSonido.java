package paqueteprincipal;

import java.io.BufferedInputStream;
import javax.sound.sampled.*;
import java.io.InputStream;

public class ReproductorSonido {
    private static Clip clipMovimiento;
    private static Clip clipCompletado;

    /**
     * Carga los sonidos desde archivos WAV ubicados en la carpeta de recursos.
     * Este método debe llamarse una vez al inicio del juego para preparar los clips.
     */
    public static void cargarSonidos() {
        try {
            InputStream isMovimiento = ReproductorSonido.class.getResourceAsStream("/recursos/movimiento.wav");
            InputStream isCompletado = ReproductorSonido.class.getResourceAsStream("/recursos/completado.wav");

            if (isMovimiento != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(isMovimiento));
                clipMovimiento = AudioSystem.getClip();
                clipMovimiento.open(audioIn);
            }

            if (isCompletado != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(isCompletado));
                clipCompletado = AudioSystem.getClip();
                clipCompletado.open(audioIn);
            }

        } catch (Exception e) {
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
