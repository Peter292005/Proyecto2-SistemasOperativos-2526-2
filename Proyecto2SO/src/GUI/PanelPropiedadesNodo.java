/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Filesystem.Archivo;
import Filesystem.Directorio;
import Filesystem.NodoFS;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

public class PanelPropiedadesNodo extends JPanel {
    private final JLabel lblTipo;
    private final JLabel lblNombre;
    private final JLabel lblPropietario;
    private final JLabel lblRuta;
    private final JLabel lblTamano;
    private final JLabel lblPrimerBloque;

    public PanelPropiedadesNodo() {
        setLayout(new GridLayout(6, 1, 6, 6));
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        lblTipo = crearLabel("Tipo: -");
        lblNombre = crearLabel("Nombre: -");
        lblPropietario = crearLabel("Propietario: -");
        lblRuta = crearLabel("Ruta: -");
        lblTamano = crearLabel("Tamaño (bloques): -");
        lblPrimerBloque = crearLabel("Primer bloque: -");

        add(lblTipo);
        add(lblNombre);
        add(lblPropietario);
        add(lblRuta);
        add(lblTamano);
        add(lblPrimerBloque);
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setOpaque(true);
        lbl.setBackground(TemaUI.CARD_2);
        lbl.setForeground(TemaUI.TEXTO);
        lbl.setFont(TemaUI.FUENTE_NORMAL);
        lbl.setBorder(new EmptyBorder(8, 10, 8, 10));
        return lbl;
    }
    public void mostrarRestringido() {
    lblTipo.setText("Tipo: Restringido");
    lblNombre.setText("Nombre: Acceso denegado");
    lblPropietario.setText("Propietario: -");
    lblRuta.setText("Ruta: -");
    lblTamano.setText("Tamaño (bloques): -");
    lblPrimerBloque.setText("Primer bloque: -");
}

    public void mostrarNodo(NodoFS nodo) {
        if (nodo == null) {
            lblTipo.setText("Tipo: -");
            lblNombre.setText("Nombre: -");
            lblPropietario.setText("Propietario: -");
            lblRuta.setText("Ruta: -");
            lblTamano.setText("Tamaño (bloques): -");
            lblPrimerBloque.setText("Primer bloque: -");
            return;
        }

        if (nodo.esDirectorio()) {
            Directorio dir = (Directorio) nodo;
            String ruta = dir.getRutaCompleta();
            if (ruta.startsWith("/root")) {
                ruta = ruta.substring(5);
            }
            if (ruta.isEmpty()) {
                ruta = "/";
            }

            lblTipo.setText("Tipo: Directorio");
            lblNombre.setText("Nombre: " + (dir.getPadre() == null ? "/" : dir.getNombre()));
            lblPropietario.setText("Propietario: " + dir.getPropietario());
            lblRuta.setText("Ruta: " + ruta);
            lblTamano.setText("Hijos: " + dir.getHijos().tamano());
            lblPrimerBloque.setText("Primer bloque: N/A");
        } else {
            Archivo archivo = (Archivo) nodo;
            String ruta = archivo.getRutaCompleta();
            if (ruta.startsWith("/root")) {
                ruta = ruta.substring(5);
            }
            if (ruta.isEmpty()) {
                ruta = "/";
            }

            lblTipo.setText("Tipo: Archivo");
            lblNombre.setText("Nombre: " + archivo.getNombre());
            lblPropietario.setText("Propietario: " + archivo.getPropietario());
            lblRuta.setText("Ruta: " + ruta);
            lblTamano.setText("Tamaño (bloques): " + archivo.getTamanoEnBloques());
            lblPrimerBloque.setText("Primer bloque: " + archivo.getPrimerBloque());
        }
    }
}