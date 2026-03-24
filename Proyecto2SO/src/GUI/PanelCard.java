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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;

public class PanelCard extends JPanel {
    private final JPanel contenido;

    public PanelCard(String titulo) {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(TemaUI.TEXTO);
        lblTitulo.setFont(TemaUI.FUENTE_SUBTITULO);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));

        contenido = new JPanel(new BorderLayout());
        contenido.setOpaque(false);

        add(lblTitulo, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }

    public JPanel getContenido() {
        return contenido;
    }
}
