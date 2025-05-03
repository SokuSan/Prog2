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
    private int filas, columnas;
    
    private Timer timer;
    private int elapsedTime = 0; // segundos
    private JLabel timerLabel;

    public JuegoRompecabezas(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        
        //imagen = new Imagen("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\imagen.png");
        imagen = new Imagen("C:\\Users\\Pedro\\Desktop\\Ingenieria informatica\\Primero\\Segundo semestre\\Programacion 2\\NetBeansProjects\\TallerFinal\\src\\recursos\\imagen.png", 3);
        juego = new Puzzle(imagen);
        
        // Cargar los sonidos
        ReproductorSonido.cargarSonidos();

        // Configuración de la ventana
        setTitle("Juego de Rompecabezas");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Agregar el KeyListener para escuchar las teclas
        agregarTeclado();

        // Crear el menú sin la opción "Opciones"
        JMenuBar menuBar = new JMenuBar();
        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem iniciarItem = new JMenuItem("Inicializar");
        JMenuItem salirItem = new JMenuItem("Salir");

        // Añadir los elementos directamente al menuBar
        menuBar.add(iniciarItem);
        menuBar.add(resolverItem);
        menuBar.add(salirItem);

        setJMenuBar(menuBar);

        // Acción de los menús
        iniciarItem.addActionListener(e -> {
            PuzzleDialog.mostrar(this, medida -> {
                // Aquí inicializas el puzzle con la medida escrita
                iniciarJuego();
                System.out.println("Puzzle de " + medida + "x" + medida);
                // Llama aquí a tu método real:
                // puzzle.inicializar(medida);
            });
        });
        resolverItem.addActionListener(e -> resolverJuego());
        salirItem.addActionListener(e -> System.exit(0));

        // Panel del tablero
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(filas, columnas));
        etiquetasCasillas = new JLabel[filas][columnas];
        agregarEtiquetasTablero();

        // Mostrar el tablero vacío al principio
        add(panelTablero, BorderLayout.CENTER);
        
        // Temporizador
        timerLabel = new JLabel("Tiempo: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

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
        startTimer();
    }

    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
        startTimer();
        stopTimer();
    }

    private void moverCasilla(int fila, int columna) {
        char direccion = ' ';
        ReproductorSonido.reproducirMovimiento();
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
                stopTimer();
                // Reproducir sonido cuando se resuelve el rompecabezas
                ReproductorSonido.reproducirCompletado();
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
                ReproductorSonido.reproducirMovimiento();
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
                        ReproductorSonido.reproducirCompletado();
                        JOptionPane.showMessageDialog(JuegoRompecabezas.this, "¡Felicidades! Has resuelto el rompecabezas.", "Juego Finalizado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        this.setFocusable(true);  // Asegúrate de que el JFrame es enfocable para recibir eventos del teclado
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas(3, 3));  // Aquí puedes cambiar el tamaño de filas y columnas
    }
    
    private void startTimer() {
        if (timer != null) {
            timer.stop(); // Por si ya hay uno corriendo
        }

        elapsedTime = 0;
        updateTimerLabel();

        timer = new Timer(1000, e -> {
            elapsedTime++;
            updateTimerLabel();
        });

        timer.start();
    }
    
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText("Tiempo: " + timeString);
    }
}