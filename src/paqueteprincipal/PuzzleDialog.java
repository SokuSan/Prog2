package paqueteprincipal;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa un cuadro de diálogo para pedir al usuario
 * la medida del rompecabezas en formato NxN.
 */
public class PuzzleDialog extends JDialog {

    private JLabel mensajeLabel;
    private JTextField inputField;
    private JButton aceptarButton;

    /**
     * Constructor del dialog que solicita al usuario una medida válida.
     */
    public PuzzleDialog(JFrame parent, PuzzleDialogCallback callback) {
        super(parent, "Inicializar Rompecabezas", true);

        setSize(500, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel central con mensaje y campo de entrada
        JPanel panelCentral = new JPanel(new GridLayout(2, 1));
        mensajeLabel = new JLabel("¿De qué medida quieres que sea el rompecabezas? (formato NxN)", SwingConstants.CENTER);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.CENTER);

        panelCentral.add(mensajeLabel);
        panelCentral.add(inputField);
        add(panelCentral, BorderLayout.CENTER);

        // Botón aceptar
        aceptarButton = new JButton("Aceptar");
        add(aceptarButton, BorderLayout.SOUTH);

        // Acción del botón "Aceptar"
        aceptarButton.addActionListener(e -> {
            String texto = inputField.getText().trim();
            if (texto.matches("\\d+")) {
                int medida = Integer.parseInt(texto);
                if (medida > 1) {
                    callback.inicializarPuzzle(medida);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "La medida debe ser mayor que 1.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debes introducir un número entero válido.");
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(aceptarButton);
        setVisible(true);
    }

    /**
     * Método para mostrar el dialog desde fuera.
     */
    public static void mostrar(JFrame parent, PuzzleDialogCallback callback) {
        new PuzzleDialog(parent, callback);
    }
}
