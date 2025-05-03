package paqueteprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PuzzleDialog extends JDialog {
    private JLabel mensajeLabel;
    private JTextField inputField;
    private JButton aceptarButton;

    public PuzzleDialog(JFrame parent, PuzzleDialogCallback callback) {
        super(parent, "Inicializar Rompecabezas", true);

        setSize(500, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel central con mensaje y campo de texto
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

        // Acción al pulsar aceptar
        aceptarButton.addActionListener(e -> {
            String texto = inputField.getText().trim();
            if (texto.matches("\\d+")) {
                int medida = Integer.parseInt(texto);
                callback.inicializarPuzzle(medida);  // llama al método del callback
                dispose(); // cierra la ventana
            }
        });

        // Cierra con la X también
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Enter activa el botón aceptar
        getRootPane().setDefaultButton(aceptarButton);

        setVisible(true);
    }

    // Interfaz de callback
    public interface PuzzleDialogCallback {
        void inicializarPuzzle(int medida);
    }

    // Método estático para mostrar el diálogo
    public static void mostrar(JFrame parent, PuzzleDialogCallback callback) {
        new PuzzleDialog(parent, callback);
    }
}
