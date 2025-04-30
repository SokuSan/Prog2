package paqueteprincipal;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class ReproductorSonido {
    // Pre-cargar sonidos en el inicio del juego
    private static Clip clipMovimiento;
    private static Clip clipCompletado;

    public static void cargarSonidos() {
        try {
            // Ruta relativa de los sonidos
            URL sonidoMovimientoURL = ReproductorSonido.class.getResource("/recursos/movimiento.wav");
            URL sonidoCompletadoURL = ReproductorSonido.class.getResource("/recursos/completado.wav");
            
            if (sonidoMovimientoURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(sonidoMovimientoURL);
                clipMovimiento = AudioSystem.getClip();
                clipMovimiento.open(audioIn);
            }
            
            if (sonidoCompletadoURL != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(sonidoCompletadoURL);
                clipCompletado = AudioSystem.getClip();
                clipCompletado.open(audioIn);
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al cargar los sonidos: " + e.getMessage());
        }
    }

    public static void reproducirMovimiento() {
        if (clipMovimiento != null && !clipMovimiento.isRunning()) {
            clipMovimiento.setFramePosition(0); // Reiniciar desde el principio
            clipMovimiento.start();
        }
    }

    public static void reproducirCompletado() {
        if (clipCompletado != null && !clipCompletado.isRunning()) {
            clipCompletado.setFramePosition(0); // Reiniciar desde el principio
            clipCompletado.start();
        }
    }
}
