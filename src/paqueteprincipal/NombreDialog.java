package paqueteprincipal;

import javax.swing.*;
import java.awt.*;

public class NombreDialog extends JDialog {
    private JTextField nombreField;
    private String nombreIngresado;

    public NombreDialog(Frame parent) {
        super(parent, "Introduce tus iniciales", true);
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JLabel label = new JLabel("Introduce tus iniciales (3 letras):");
        nombreField = new JTextField();
        nombreField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= 3 && str.matches("[a-zA-Z]+")) {
                    super.insertString(offs, str.toUpperCase(), a);
                }
            }
        });

        JButton aceptar = new JButton("Aceptar");
        aceptar.addActionListener(e -> {
            String texto = nombreField.getText().trim();
            if (texto.length() == 3) {
                nombreIngresado = texto;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Debes introducir exactamente 3 letras.");
            }
        });

        JPanel panelCentral = new JPanel(new GridLayout(2, 1));
        panelCentral.add(label);
        panelCentral.add(nombreField);

        add(panelCentral, BorderLayout.CENTER);
        add(aceptar, BorderLayout.SOUTH);
    }

    public String getNombreIngresado() {
        return nombreIngresado;
    }
}
