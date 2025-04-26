package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class JuegoRompecabezas extends JFrame {
    private Puzzle juego;
    private JPanel panelTablero;
    private JLabel[][] etiquetasCasillas;
    private Imagen imagen;
    private int filas = 3, columnas = 3;

    public JuegoRompecabezas() {
        imagen = new Imagen("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\imagen.png");
        juego = new Puzzle(imagen);

        // Configuración de la ventana
        setTitle("Juego de Rompecabezas");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Agregar el KeyListener para escuchar las teclas
        agregarTeclado();

        // Crear el menú sin la opción "Opciones"
        JMenuBar menuBar = new JMenuBar();
        JMenuItem iniciarItem = new JMenuItem("Iniciar");
        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem salirItem = new JMenuItem("Salir");

        // Añadir los elementos directamente al menuBar
        menuBar.add(iniciarItem);
        menuBar.add(resolverItem);
        menuBar.add(salirItem);

        setJMenuBar(menuBar);

        // Acción de los menús
        iniciarItem.addActionListener(e -> iniciarJuego());
        resolverItem.addActionListener(e -> resolverJuego());
        salirItem.addActionListener(e -> System.exit(0));

        // Panel del tablero
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(filas, columnas));
        etiquetasCasillas = new JLabel[filas][columnas];
        agregarEtiquetasTablero();

        // Mostrar el tablero vacío al principio
        add(panelTablero, BorderLayout.CENTER);

        // Mostrar la ventana
        setVisible(true);
    }

    private void agregarEtiquetasTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JLabel etiqueta = new JLabel();
                etiqueta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
                etiqueta.setVerticalAlignment(SwingConstants.CENTER);
                etiqueta.setPreferredSize(new Dimension(100, 100));
                etiqueta.setOpaque(true);
                etiqueta.setBackground(Color.LIGHT_GRAY);
                final int fila = i;
                final int columna = j;

                // Acción de clic en cada casilla
                etiqueta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        moverCasilla(fila, columna);
                    }
                });

                etiquetasCasillas[i][j] = etiqueta;
                panelTablero.add(etiqueta);
            }
        }
    }

    private void iniciarJuego() {
        juego.desordenar();
        actualizarTablero();
    }

    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
    }

    private void moverCasilla(int fila, int columna) {
        char direccion = ' ';
        reproducirSonido("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\movimiento.wav");
        // Movimiento de casilla dependiendo de su posición con el clic del ratón
        if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() - 1) {
            direccion = 'd'; // Mover hacia la derecha
        } else if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() + 1) {
            direccion = 'a'; // Mover hacia la izquierda
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() - 1) {
            direccion = 's'; // Mover hacia abajo
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() + 1) {
            direccion = 'w'; // Mover hacia arriba
        }

        if (direccion != ' ' && juego.mover(direccion)) {
            actualizarTablero();
            if (juego.estaResuelto()) {
                // Reproducir sonido cuando se resuelve el rompecabezas
                reproducirSonido("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\completado.wav");
                JOptionPane.showMessageDialog(this, "¡Felicidades! Has resuelto el rompecabezas.", "Juego Finalizado", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void actualizarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int valor = juego.getCasilla(i, j).getValor();
                JLabel etiqueta = etiquetasCasillas[i][j];

                if (valor == 0) {
                    etiqueta.setIcon(null);
                    etiqueta.setText("");
                    etiqueta.setBackground(Color.LIGHT_GRAY);
                } else {
                    // Obtener fila y columna originales a partir del valor
                    int filaFragmento = (valor - 1) / columnas;
                    int colFragmento = (valor - 1) % columnas;

                    Image fragmento = imagen.getFragmento(filaFragmento, colFragmento);

                    if (fragmento != null) {
                        ImageIcon icono = new ImageIcon(fragmento.getScaledInstance(
                            etiqueta.getWidth(), etiqueta.getHeight(), Image.SCALE_SMOOTH));
                        etiqueta.setIcon(icono);
                        etiqueta.setText(""); // No mostrar texto
                        etiqueta.setBackground(null);
                    } else {
                        etiqueta.setIcon(null);
                        etiqueta.setText("?"); // Si no tiene imagen, muestra un signo de interrogación
                    }
                }
            }
        }
    }

    // Agregar el KeyListener para el teclado
    private void agregarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char direccion = ' ';
                reproducirSonido("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\movimiento.wav");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: // Tecla W
                        direccion = 'w';
                        break;
                    case KeyEvent.VK_S: // Tecla S
                        direccion = 's';
                        break;
                    case KeyEvent.VK_A: // Tecla A
                        direccion = 'a';
                        break;
                    case KeyEvent.VK_D: // Tecla D
                        direccion = 'd';
                        break;
                }

                if (direccion != ' ' && juego.mover(direccion)) {
                    actualizarTablero();
                    if (juego.estaResuelto()) {
                        // Reproducir sonido cuando se resuelve el rompecabezas
                        reproducirSonido("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\completado.wav");
                        JOptionPane.showMessageDialog(JuegoRompecabezas.this, "¡Felicidades! Has resuelto el rompecabezas.", "Juego Finalizado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        this.setFocusable(true);  // Asegúrate de que el JFrame es enfocable para recibir eventos del teclado
    }

    // Método para reproducir sonido
    // Método para reproducir sonido corregido
private void reproducirSonido(String archivo) {
    try {
        File file = new File(archivo);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();

        // Espera a que termine de reproducirse
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                clip.close();
            }
        });

    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas());
    }
}
