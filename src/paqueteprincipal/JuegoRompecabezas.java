package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import javax.swing.border.Border;

public class JuegoRompecabezas extends JFrame {
    
    private boolean juegoTerminado = false;

    private Puzzle juego;
    private JPanel panelTablero;
    private JLabel[][] etiquetasCasillas;
    private Imagen imagen;
    private int filas, columnas;

    private Timer timer;
    private int elapsedTime = 0;
    private JLabel timerLabel;
    private int turnos = 0;

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
        JMenuItem resolverItem = new JMenuItem("Resolver");
        JMenuItem iniciarItem = new JMenuItem("Inicializar");
        JMenuItem estadisticasItem = new JMenuItem("Estadísticas");
        JMenuItem salirItem = new JMenuItem("Salir");

        menuBar.add(iniciarItem);
        menuBar.add(resolverItem);
        menuBar.add(estadisticasItem);
        menuBar.add(salirItem);
        setJMenuBar(menuBar);

        iniciarItem.addActionListener(e -> {
            PuzzleDialog.mostrar(this, medida -> {
                dispose();
                new JuegoRompecabezas(medida, medida, true);
            });
        });

        resolverItem.addActionListener(e -> resolverJuego());
        estadisticasItem.addActionListener(e -> GestorRanking.mostrarRanking(JuegoRompecabezas.this));
        salirItem.addActionListener(e -> System.exit(0));

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

    public JuegoRompecabezas(int filas, int columnas) {
        this(filas, columnas, false);
    }

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

    private void iniciarJuego() {
        juego.desordenar();
        actualizarTablero();
        empezarTiempo();
    }

    private void resolverJuego() {
        juego.inicializar();
        actualizarTablero();
        empezarTiempo();
        pararTiempo();
    }

    private void moverCasilla(int fila, int columna) {
        if (juegoTerminado) return;
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

    private void agregarTeclado() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (juegoTerminado) return;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas(3, 3));
    }

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

    private void pararTiempo() {
        if (timer != null) {
            juegoTerminado = true;
            timer.stop();
        }
    }

    private void actualizarLabelTiempo() {
        int minutos = elapsedTime / 60;
        int segundos = elapsedTime % 60;
        String tiempoFormateado = String.format("Tiempo: %02d:%02d", minutos, segundos);
        timerLabel.setText(tiempoFormateado);
    }
}
