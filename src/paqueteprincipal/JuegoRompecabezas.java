package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import javax.swing.border.Border;

public class JuegoRompecabezas extends JFrame {

    private Puzzle juego;
    private JPanel panelTablero;
    private JLabel[][] etiquetasCasillas;
    private Imagen imagen;
    private int filas, columnas;

    private Timer timer;
    private int elapsedTime = 0;
    private JLabel timerLabel;
    private int turnos = 0;

    /**
     * Constructor principal que configura el juego con las filas y columnas
     * dadas.
     */
    public JuegoRompecabezas(int filas, int columnas, boolean iniciar) {
        this.filas = filas;
        this.columnas = columnas;

        // Carga la imagen y la divide según filas/columnas
        InputStream is = getClass().getResourceAsStream("/recursos/imagen.png");
        if (is == null) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            imagen = new Imagen(is, filas); // Usa un constructor que acepte InputStream
        }

        juego = new Puzzle(imagen);

        ReproductorSonido.cargarSonidos(); // Prepara sonidos del juego

        setTitle("Juego de Rompecabezas");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        agregarTeclado(); // Agrega manejo de teclado para mover piezas

        // Configura el menú superior con opciones de iniciar, resolver, ver estadísticas y salir
        JMenuBar menuBar = new JMenuBar();
        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem iniciarItem = new JMenuItem("Inicializar");
        JMenuItem estadisticasItem = new JMenuItem("Estadísticas");
        JMenuItem salirItem = new JMenuItem("Salir");

        menuBar.add(iniciarItem);
        menuBar.add(resolverItem);
        menuBar.add(estadisticasItem);
        menuBar.add(salirItem);
        setJMenuBar(menuBar);

        // Acción para reiniciar el juego con nuevo tamaño seleccionado
        iniciarItem.addActionListener(e -> {
            PuzzleDialog.mostrar(this, medida -> {
                dispose();
                new JuegoRompecabezas(medida, medida, true);
            });
        });

        resolverItem.addActionListener(e -> resolverJuego());

        estadisticasItem.addActionListener(e -> GestorRanking.mostrarRanking(JuegoRompecabezas.this));

        salirItem.addActionListener(e -> System.exit(0));

        // Configura el panel del tablero con un GridLayout
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(filas, columnas));
        etiquetasCasillas = new JLabel[filas][columnas];
        agregarEtiquetasTablero(); // Añade las etiquetas visuales al tablero

        add(panelTablero, BorderLayout.CENTER);

        // Etiqueta para mostrar el tiempo transcurrido en la parte superior
        timerLabel = new JLabel("Tiempo: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        setVisible(true);

        if (iniciar) {
            iniciarJuego(); // Desordena y comienza el temporizador
        }
    }

    /**
     * Constructor secundario que no inicia el juego automáticamente.
     */
    public JuegoRompecabezas(int filas, int columnas) {
        this(filas, columnas, false);
    }

    /**
     * Crea y agrega las etiquetas que representan las casillas del puzzle con
     * sus respectivos eventos para mover piezas al hacer click.
     */
    private void agregarEtiquetasTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JLabel etiqueta = new JLabel();
                Border bordeOriginal = BorderFactory.createLineBorder(Color.GRAY, 1);

                etiqueta.setBorder(bordeOriginal);
                etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
                etiqueta.setVerticalAlignment(SwingConstants.CENTER);
                etiqueta.setPreferredSize(new Dimension(100, 100));
                etiqueta.setOpaque(true);
                etiqueta.setBackground(Color.LIGHT_GRAY);

                final int fila = i;
                final int columna = j;

                // Evento click para mover la casilla si está al lado del espacio vacío
                etiqueta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        moverCasilla(fila, columna);
                    }

                    // Cambia el borde al pasar el mouse para efecto visual
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        etiqueta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    }

                    // Restaura el borde original cuando el mouse sale
                    @Override
                    public void mouseExited(MouseEvent e) {
                        etiqueta.setBorder(bordeOriginal);
                    }
                });

                etiquetasCasillas[i][j] = etiqueta;
                panelTablero.add(etiqueta);
            }
        }
    }

    /**
     * Inicia el juego: desordena las piezas, actualiza el tablero y comienza el
     * temporizador.
     */
    private void iniciarJuego() {
        juego.desordenar();
        actualizarTablero();
        startTimer();
    }

    /**
     * Resuelve el juego mostrando el puzzle ordenado y detiene el temporizador.
     */
    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
        startTimer();
        stopTimer();
    }

    /**
     * Intenta mover la casilla en la posición dada hacia el espacio vacío si es
     * válido. Reproduce sonidos, actualiza el tablero y verifica si el juego se
     * ha completado.
     */
    private void moverCasilla(int fila, int columna) {
        char direccion = ' ';
        ReproductorSonido.reproducirMovimiento();

        // Determina la dirección del movimiento según la posición respecto al espacio vacío
        if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() - 1) {
            direccion = 'd';
        } else if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() + 1) {
            direccion = 'a';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() - 1) {
            direccion = 's';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() + 1) {
            direccion = 'w';
        }

        // Si el movimiento es válido, actualiza el tablero y chequea si se resolvió el puzzle
        if (direccion != ' ' && juego.mover(direccion)) {
            actualizarTablero();

            if (juego.estaResuelto()) {
                stopTimer();
                ReproductorSonido.reproducirCompletado();

                // Solicita al usuario ingresar su nombre para el ranking
                NombreDialog dialogo = new NombreDialog(JuegoRompecabezas.this);
                dialogo.setVisible(true);
                String nombre = dialogo.getNombreIngresado();

                if (nombre != null) {
                    RecordTiempo nuevo = new RecordTiempo(nombre, elapsedTime);
                    GestorRanking.agregarNuevoTiempo(nuevo);
                    GestorRanking.mostrarRanking(JuegoRompecabezas.this);
                } else {
                    JOptionPane.showMessageDialog(JuegoRompecabezas.this, "No se registró el nombre en el ranking.");
                }
            }
        }
        turnos++;
    }

    /**
     * Actualiza las etiquetas del tablero mostrando los fragmentos de imagen o
     * el espacio vacío, según el estado actual del juego.
     */
    private void actualizarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int valor = juego.getCasilla(i, j).getValor();
                JLabel etiqueta = etiquetasCasillas[i][j];

                if (valor == 0) {
                    // Casilla vacía: sin icono y con fondo gris claro
                    etiqueta.setIcon(null);
                    etiqueta.setText("");
                    etiqueta.setBackground(Color.LIGHT_GRAY);
                } else {
                    // Calcula qué fragmento de imagen mostrar
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
     * Agrega un listener para detectar las teclas WASD y mover las piezas del
     * puzzle en las direcciones correspondientes.
     */
    private void agregarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char direccion = ' ';
                ReproductorSonido.reproducirMovimiento();

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

                    if (juego.estaResuelto()) {
                        stopTimer();
                        ReproductorSonido.reproducirCompletado();

                        NombreDialog dialogo = new NombreDialog(JuegoRompecabezas.this);
                        dialogo.setVisible(true);
                        String nombre = dialogo.getNombreIngresado();

                        if (nombre != null) {
                            RecordTiempo nuevo = new RecordTiempo(nombre, elapsedTime);
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
     * Método main que inicia el juego con un tablero 3x3 por defecto.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas(3, 3));
    }

    /**
     * Inicia el temporizador que cuenta el tiempo en segundos y actualiza la
     * etiqueta visual.
     */
    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        elapsedTime = 0;
        updateTimerLabel();

        // Timer que aumenta elapsedTime cada segundo y actualiza la etiqueta
        timer = new Timer(1000, e -> {
            elapsedTime++;
            updateTimerLabel();
        });

        timer.start();
    }

    /**
     * Detiene el temporizador si está activo.
     */
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    /**
     * Actualiza la etiqueta que muestra el tiempo en formato MM:SS.
     */
    private void updateTimerLabel() {
        int minutos = elapsedTime / 60;
        int segundos = elapsedTime % 60;
        timerLabel.setText(String.format("Tiempo: %02d:%02d", minutos, segundos));
    }

}
