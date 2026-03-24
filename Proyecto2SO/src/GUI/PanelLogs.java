/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class PanelLogs extends JPanel {
    private final JTextArea areaLogs;
    private final JButton btnLimpiar;

    public PanelLogs() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        areaLogs = new JTextArea();
        areaLogs.setEditable(false);
        areaLogs.setFont(TemaUI.FUENTE_LOG);
        areaLogs.setBackground(TemaUI.LOG_FONDO);
        areaLogs.setForeground(new Color(196, 255, 214));
        areaLogs.setCaretColor(TemaUI.TEXTO);
        areaLogs.setBorder(new EmptyBorder(10, 10, 10, 10));
        areaLogs.setLineWrap(true);
        areaLogs.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(areaLogs);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.LOG_FONDO);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBackground(TemaUI.CARD_2);
        btnLimpiar.setForeground(TemaUI.TEXTO);
        btnLimpiar.setFont(TemaUI.FUENTE_BOTON);
        btnLimpiar.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        btnLimpiar.addActionListener(e -> limpiar());

        add(scroll, BorderLayout.CENTER);
        add(btnLimpiar, BorderLayout.EAST);
    }

    public void agregarLog(String mensaje) {
        areaLogs.append(mensaje + "\n");
        areaLogs.setCaretPosition(areaLogs.getDocument().getLength());
    }

    public void limpiar() {
        areaLogs.setText("");
    }
}