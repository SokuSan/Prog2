package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JuegoRompecabezas extends JFrame {
    private Puzzle juego;
    private JPanel panelTablero;
    private JLabel[][] etiquetasCasillas;
    private Imagen imagen;
    private int filas = 3, columnas = 3;

    public JuegoRompecabezas() {
        imagen = new Imagen("/paqueteprincipal/imagen.png");
        juego = new Puzzle(imagen);

        // Configuración de la ventana
        setTitle("Juego de Rompecabezas");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        // Movimiento de casilla dependiendo de su posición
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
                    etiqueta.setText("?");
                }
            }
        }
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JuegoRompecabezas());
    }
}
