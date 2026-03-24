/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Procesos.EstadoProceso;
import Procesos.Proceso;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

public class PanelProcesos extends JPanel {
    private final DefaultListModel<String> modeloListos;
    private final DefaultListModel<String> modeloCPU;
    private final DefaultListModel<String> modeloBloqueados;

    public PanelProcesos() {
        setLayout(new GridLayout(3, 1, 8, 8));
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        modeloListos = new DefaultListModel<>();
        modeloCPU = new DefaultListModel<>();
        modeloBloqueados = new DefaultListModel<>();

        add(crearLista("LISTOS", modeloListos));
        add(crearLista("EN CPU", modeloCPU));
        add(crearLista("BLOQUEADOS", modeloBloqueados));
    }

    private JPanel crearLista(String titulo, DefaultListModel<String> modelo) {
        PanelCard card = new PanelCard(titulo);

        JList<String> lista = new JList<>(modelo);
        lista.setBackground(new java.awt.Color(19, 31, 51));
        lista.setForeground(TemaUI.TEXTO);
        lista.setFont(TemaUI.FUENTE_NORMAL);
        lista.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.CARD_2);

        card.getContenido().add(scroll);
        return card;
    }

    public void refrescar(ListaEnlazada<Proceso> procesos) {
        modeloListos.clear();
        modeloCPU.clear();
        modeloBloqueados.clear();

        if (procesos == null) {
            return;
        }

        for (int i = 0; i < procesos.tamano(); i++) {
            Proceso p = procesos.obtener(i);
            String texto = p.getNombre() + " (PID " + p.getPid() + ")";

            if (p.getEstado() == EstadoProceso.LISTO) {
                modeloListos.addElement(texto);
            } else if (p.getEstado() == EstadoProceso.EJECUTANDO) {
                modeloCPU.addElement(texto);
            } else if (p.getEstado() == EstadoProceso.BLOQUEADO) {
                modeloBloqueados.addElement(texto);
            }
        }
    }
}