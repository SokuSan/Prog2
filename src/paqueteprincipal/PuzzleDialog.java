package paqueteprincipal;

import javax.swing.*;
import java.awt.*;

public class PuzzleDialog extends JDialog {
    private JLabel mensajeLabel;
    private JTextField inputField;
    private JButton aceptarButton;

    public interface PuzzleDialogCallback {
        void inicializarPuzzle(int medida);
    }

    public PuzzleDialog(JFrame parent, PuzzleDialogCallback callback) {
        super(parent, "Inicializar Rompecabezas", true);

        setSize(500, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(2, 1));
        mensajeLabel = new JLabel("¿De qué medida quieres que sea el rompecabezas? (formato NxN)", SwingConstants.CENTER);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 14));

        inputField = new JTextField();
        inputField.setHorizontalAlignment(JTextField.CENTER);

        panelCentral.add(mensajeLabel);
        panelCentral.add(inputField);
        add(panelCentral, BorderLayout.CENTER);

        aceptarButton = new JButton("Aceptar");
        add(aceptarButton, BorderLayout.SOUTH);

        aceptarButton.addActionListener(e -> {
            String texto = inputField.getText().trim();
            if (texto.matches("\\d+")) {
                int medida = Integer.parseInt(texto);
                callback.inicializarPuzzle(medida); // llama al callback
                dispose(); // cierra el diálogo
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(aceptarButton);
        setVisible(true);
    }

    public static void mostrar(JFrame parent, PuzzleDialogCallback callback) {
        new PuzzleDialog(parent, callback);
    }
}
