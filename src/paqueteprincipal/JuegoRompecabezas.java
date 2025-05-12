package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

/**
 * Clase principal que representa la ventana del juego de rompecabezas.
 * Permite al usuario jugar, resolver, ver estadísticas y registrar tiempos.
 */
public class JuegoRompecabezas extends JFrame {

    // Lógica del juego
    private Puzzle juego;
    private JPanel panelTablero;
    private JLabel[][] etiquetasCasillas;
    private Imagen imagen;
    private int filas, columnas;

    // Temporizador
    private Timer timer;
    private int elapsedTime = 0; // segundos
    private JLabel timerLabel;

    /**
     * Constructor principal. Inicializa el juego con las medidas indicadas.
     * @param filas Número de filas del tablero
     * @param columnas Número de columnas del tablero
     * @param iniciar Si se debe iniciar el juego al abrir la ventana
     */
    public JuegoRompecabezas(int filas, int columnas, boolean iniciar) {
        this.filas = filas;
        this.columnas = columnas;

       // Carga la imagen para dividir en piezas
       // imagen = new Imagen("C:\\Users\\khast\\Documents\\NetBeansProjects\\ProyectoFinal\\src\\recursos\\imagen.png", filas);
        imagen = new Imagen("C:\\Users\\Pedro\\Desktop\\Ingenieria informatica\\Primero\\Segundo semestre\\Programacion 2\\NetBeansProjects\\TallerFinal\\src\\recursos\\imagen.png", filas);
        juego = new Puzzle(imagen);

        // Carga los sonidos del juego
        ReproductorSonido.cargarSonidos();

        // Configuración básica de la ventana
        setTitle("Juego de Rompecabezas");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Agrega controles por teclado
        agregarTeclado();

        // Crea y configura la barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem iniciarItem = new JMenuItem("Inicializar");
        JMenuItem estadisticasItem = new JMenuItem("Estadísticas");
        JMenuItem salirItem = new JMenuItem("Salir");

        // Añade ítems al menú
        menuBar.add(iniciarItem);
        menuBar.add(resolverItem);
        menuBar.add(estadisticasItem);
        menuBar.add(salirItem);
        setJMenuBar(menuBar);

        // Acción para reiniciar el juego
        iniciarItem.addActionListener(e -> {
            PuzzleDialog.mostrar(this, medida -> {
                dispose();
                new JuegoRompecabezas(medida, medida, true);
            });
        });
        
        // Acción para resolver automáticamente el juego
        resolverItem.addActionListener(e -> resolverJuego());

        // Muestra el ranking
        estadisticasItem.addActionListener(e -> {
            GestorRanking.mostrarRanking(JuegoRompecabezas.this);
        });
        
        // Cierra el programa
        salirItem.addActionListener(e -> System.exit(0));

        // Panel principal del tablero con layout de grilla
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(filas, columnas));
        etiquetasCasillas = new JLabel[filas][columnas];
        agregarEtiquetasTablero();

        add(panelTablero, BorderLayout.CENTER);

        // Etiqueta del temporizador
        timerLabel = new JLabel("Tiempo: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        setVisible(true);

        // Inicia el juego si se indicó
        if (iniciar) {
            iniciarJuego();
        }
    }

    /**
     * Constructor alternativo que no inicia automáticamente el juego.
     */
    public JuegoRompecabezas(int filas, int columnas) {
        this(filas, columnas, false);
    }

    /**
     * Agrega las etiquetas (JLabels) al tablero para representar las casillas.
     */
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

                // Agrega detección de clics de ratón sobre cada casilla
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

    /**
     * Inicia el juego desordenando el tablero y activando el temporizador.
     */
    private void iniciarJuego() {
        juego.desordenar();
        actualizarTablero();
        startTimer();
    }

    /**
     * Resuelve el juego, lo reinicia al estado original y detiene el temporizador.
     */
    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
        startTimer();
        stopTimer();
    }

    /**
     * Intenta mover una casilla si es adyacente a la vacía. Verifica victoria tras el movimiento.
     */
    private void moverCasilla(int fila, int columna) {
        char direccion = ' ';
        ReproductorSonido.reproducirMovimiento();

        // Determina la dirección del movimiento respecto a la casilla vacía
        if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() - 1) {
            direccion = 'd';
        } else if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() + 1) {
            direccion = 'a';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() - 1) {
            direccion = 's';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() + 1) {
            direccion = 'w';
        }

        // Si el movimiento es válido
        if (direccion != ' ' && juego.mover(direccion)) {
            actualizarTablero();
            
            // Verifica si el puzzle fue resuelto
            if (juego.estaResuelto()) {
                stopTimer();
                ReproductorSonido.reproducirCompletado();

                // Solicita nombre para registrar el tiempo en el ranking
                NombreDialog dialogo = new NombreDialog(JuegoRompecabezas.this);
                dialogo.setVisible(true);
                String nombre = dialogo.getNombreIngresado();

                if (nombre != null) {
                    RecordTiempo nuevo = new RecordTiempo(nombre, elapsedTime, LocalDate.now());
                    GestorRanking.agregarNuevoTiempo(nuevo);
                    GestorRanking.mostrarRanking(JuegoRompecabezas.this);
                } else {
                    JOptionPane.showMessageDialog(JuegoRompecabezas.this, "No se registró el nombre en el ranking.");
                }
            }
        }
    }

    /**
     * Actualiza la interfaz del tablero con las posiciones actuales del juego.
     */
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
                    int filaFragmento = (valor - 1) / columnas;
                    int colFragmento = (valor - 1) % columnas;

                    Image fragmento = imagen.getFragmento(filaFragmento, colFragmento);

                    if (fragmento != null) {
                        ImageIcon icono = new ImageIcon(fragmento.getScaledInstance(
                                etiqueta.getWidth(), etiqueta.getHeight(), Image.SCALE_SMOOTH));
                        etiqueta.setIcon(icono);
                        etiqueta.setText("");
                        etiqueta.setBackground(null);
                    } else {
                        etiqueta.setIcon(null);
                        etiqueta.setText("?");
                    }
                }
            }
        }
    }

    /**
     * Agrega control por teclado para mover las piezas con WASD.
     */
    private void agregarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char direccion = ' ';
                ReproductorSonido.reproducirMovimiento();
                
                // Detecta teclas presionadas
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        direccion = 'w';
                        break;
                    case KeyEvent.VK_S:
                        direccion = 's';
                        break;
                    case KeyEvent.VK_A:
                        direccion = 'a';
                        break;
                    case KeyEvent.VK_D:
                        direccion = 'd';
                        break;
                }

                if (direccion != ' ' && juego.mover(direccion)) {
                    actualizarTablero();
                    
                    // Verifica victoria
                    if (juego.estaResuelto()) {
                        stopTimer();
                        ReproductorSonido.reproducirCompletado();

                        NombreDialog dialogo = new NombreDialog(JuegoRompecabezas.this);
                        dialogo.setVisible(true);
                        String nombre = dialogo.getNombreIngresado();

                        if (nombre != null) {
                            RecordTiempo nuevo = new RecordTiempo(nombre, elapsedTime, LocalDate.now());
                            GestorRanking.agregarNuevoTiempo(nuevo);
                            GestorRanking.mostrarRanking(JuegoRompecabezas.this);
                        } else {
                            JOptionPane.showMessageDialog(JuegoRompecabezas.this, "No se registró el nombre en el ranking.");
                        }
                    }
                }
            }
        });
        this.setFocusable(true);
    }

    /**
     * Método principal. Crea una nueva ventana de juego de 3x3.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas(3, 3));
    }

    /**
     * Inicia el temporizador desde 0.
     */
    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        elapsedTime = 0;
        updateTimerLabel();

        timer = new Timer(1000, e -> {
            elapsedTime++;
            updateTimerLabel();
        });

        timer.start();
    }

    /**
     * Detiene el temporizador actual.
     */
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Actualiza la etiqueta del temporizador con el tiempo transcurrido.
     */
    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText("Tiempo: " + timeString);
    }
}
