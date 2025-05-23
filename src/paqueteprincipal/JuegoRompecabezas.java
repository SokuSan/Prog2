// Autores: Pedro Hernandez Muñoz, Laia Moñino Peñalva
package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import javax.swing.border.Border;

public class JuegoRompecabezas extends JFrame {

    private boolean juegoTerminado = true;

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
     * dadas
     *
     * @param filas
     * @param columnas
     * @param iniciar
     */
    public JuegoRompecabezas(int filas, int columnas, boolean iniciar) {

        this.filas = filas;
        this.columnas = columnas;

        InputStream is = getClass().getResourceAsStream("/recursos/imagen.png");
        if (is == null) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            imagen = new Imagen(is, filas);
        }

        juego = new Puzzle(imagen);

        ReproductorSonido.cargarSonidos();

        setTitle("Juego de Rompecabezas");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        agregarTeclado();

        JMenuBar menuBar = new JMenuBar();
        JMenu jocMenu = new JMenu("Joc");

        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem iniciarItem = new JMenuItem("Inicializar");
        JMenuItem estadisticasItem = new JMenuItem("Estadísticas");
        JMenuItem salirItem = new JMenuItem("Salir");

        jocMenu.add(iniciarItem);
        jocMenu.add(resolverItem);
        jocMenu.add(estadisticasItem);
        jocMenu.add(salirItem);

        menuBar.add(jocMenu);
        setJMenuBar(menuBar);

        iniciarItem.addActionListener(e -> {
            PuzzleDialog.mostrar(this, medida -> {
                setVisible(false);
                new JuegoRompecabezas(medida, medida, true);
            });
        });

        resolverItem.addActionListener(e -> {
            resolverJuego();
        });

        estadisticasItem.addActionListener(e -> {
            GestorRanking.mostrarRanking(JuegoRompecabezas.this);
        });

        salirItem.addActionListener(e -> {
            System.exit(0);
        });

        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(filas, columnas));
        etiquetasCasillas = new JLabel[filas][columnas];
        agregarEtiquetasTablero();
        add(panelTablero, BorderLayout.CENTER);

        timerLabel = new JLabel("Tiempo: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        setVisible(true);

        if (iniciar) {
            iniciarJuego();
        }
    }

    /**
     * Constructor secundario que no inicia el juego automáticamente
     *
     * @param filas
     * @param columnas
     */
    public JuegoRompecabezas(int filas, int columnas) {
        this(filas, columnas, false);
    }

    /**
     * Crea y agrega las etiquetas que representan las casillas del puzzle con
     * sus respectivos eventos para mover piezas al hacer click
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

                etiqueta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        moverCasilla(fila, columna);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        etiqueta.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                    }

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
     * temporizador
     */
    private void iniciarJuego() {
        juego.desordenar();
        actualizarTablero();
        empezarTiempo();
    }

    /**
     * Resuelve el juego mostrando el puzzle ordenado y detiene el temporizador
     */
    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
        empezarTiempo();
        pararTiempo();
    }

    /**
     * Intenta mover la casilla en la posición dada hacia el espacio vacío si es
     * válido. Reproduce sonidos, actualiza el tablero y verifica si el juego se
     * ha completado
     *
     * @param fila
     * @param columna
     */
    private void moverCasilla(int fila, int columna) {
        if (juegoTerminado) {
            return;
        }
        char direccion = ' ';
        ReproductorSonido.reproducirMovimiento();

        if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() - 1) {
            direccion = 'd';
        } else if (fila == juego.getFilaVacia() && columna == juego.getColumnaVacia() + 1) {
            direccion = 'a';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() - 1) {
            direccion = 's';
        } else if (columna == juego.getColumnaVacia() && fila == juego.getFilaVacia() + 1) {
            direccion = 'w';
        }

        if (direccion != ' ' && juego.mover(direccion)) {
            actualizarTablero();

            if (juego.estaResuelto()) {
                pararTiempo();
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
        turnos++;
    }

    /**
     * Actualiza las etiquetas del tablero mostrando los fragmentos de imagen o
     * el espacio vacío, según el estado actual del juego
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
     * Agrega un listener para detectar las teclas WASD y mover las piezas del
     * puzzle en las direcciones correspondientes
     */
    private void agregarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (juegoTerminado) {
                    return;
                }
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
                        pararTiempo();
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
     * Método main que inicia el juego con un tablero 3x3 por defecto
     *
     * @param args
     */
    public static void main(String[] args) {
         new JuegoRompecabezas(3, 3);
    }

    /**
     * Inicia el temporizador que cuenta el tiempo en segundos y actualiza la
     * etiqueta visual
     */
    private void empezarTiempo() {
        if (timer != null) {
            timer.stop();
        }
        juegoTerminado = false;
        elapsedTime = 0;
        actualizarLabelTiempo();

        timer = new Timer(1000, e -> {
            elapsedTime++;
            actualizarLabelTiempo();
        });

        timer.start();
    }

    /**
     * Detiene el temporizador si está activo
     */
    private void pararTiempo() {
        if (timer != null) {
            juegoTerminado = true;
            timer.stop();
        }
    }

    /**
     * Actualiza la etiqueta que muestra el tiempo en formato MM:SS
     */
    private void actualizarLabelTiempo() {
        int minutos = elapsedTime / 60;
        int segundos = elapsedTime % 60;
        String tiempoFormateado = String.format("Tiempo: %02d:%02d", minutos, segundos);
        timerLabel.setText(tiempoFormateado);
    }
}
