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
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

public class PanelProcesos extends JPanel {
    private final DefaultListModel<String> modeloListos;
    private final DefaultListModel<String> modeloCPU;
    private final DefaultListModel<String> modeloBloqueados;
    private final DefaultListModel<String> modeloTerminados;

    private final JList<String> listaListos;
    private final JList<String> listaCPU;
    private final JList<String> listaBloqueados;
    private final JList<String> listaTerminados;

    public PanelProcesos() {
        setLayout(new GridLayout(4, 1, 10, 10));
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        modeloListos = new DefaultListModel<>();
        modeloCPU = new DefaultListModel<>();
        modeloBloqueados = new DefaultListModel<>();
        modeloTerminados = new DefaultListModel<>();

        listaListos = crearLista(modeloListos);
        listaCPU = crearLista(modeloCPU);
        listaBloqueados = crearLista(modeloBloqueados);
        listaTerminados = crearLista(modeloTerminados);

        add(crearSeccion("LISTOS", listaListos, new Color(59, 130, 246)));
        add(crearSeccion("EN CPU", listaCPU, new Color(34, 197, 94)));
        add(crearSeccion("BLOQUEADOS", listaBloqueados, new Color(239, 68, 68)));
        add(crearSeccion("TERMINADOS", listaTerminados, new Color(168, 85, 247)));
    }

    private JList<String> crearLista(DefaultListModel<String> modelo) {
        JList<String> lista = new JList<>(modelo);
        lista.setBackground(TemaUI.CARD_2);
        lista.setForeground(TemaUI.TEXTO);
        lista.setFont(new Font("Consolas", Font.PLAIN, 13));
        lista.setCellRenderer(new RenderProceso());
        return lista;
    }

    private JPanel crearSeccion(String titulo, JList<String> lista, Color colorTitulo) {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setOpaque(true);
        panel.setBackground(TemaUI.CARD_2);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(colorTitulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.CARD_2);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void refrescar(ListaEnlazada<Proceso> procesos) {
        refrescar(procesos, null);
    }

    public void refrescar(ListaEnlazada<Proceso> procesos, Proceso procesoActivo) {
        modeloListos.clear();
        modeloCPU.clear();
        modeloBloqueados.clear();
        modeloTerminados.clear();

        if (procesos == null) {
            ponerGuiones();
            return;
        }

        Integer pidActivo = procesoActivo != null ? procesoActivo.getPid() : null;

        for (int i = 0; i < procesos.tamano(); i++) {
            Proceso p = procesos.obtener(i);
            String texto = formatearProceso(p);

            if (pidActivo != null && p.getPid() == pidActivo) {
                modeloCPU.addElement(texto);
                continue;
            }

            EstadoProceso estado = p.getEstado();

            if (estado == EstadoProceso.LISTO) {
                modeloListos.addElement(texto);
            } else if (estado == EstadoProceso.EJECUTANDO) {
                modeloCPU.addElement(texto);
            } else if (estado == EstadoProceso.BLOQUEADO) {
                modeloBloqueados.addElement(texto);
            } else if (estado == EstadoProceso.TERMINADO) {
                modeloTerminados.addElement(texto);
            } else {
                modeloListos.addElement(texto);
            }
        }

        ponerGuionesSiHaceFalta();
    }

    private void ponerGuiones() {
        modeloListos.addElement("—");
        modeloCPU.addElement("—");
        modeloBloqueados.addElement("—");
        modeloTerminados.addElement("—");
    }

    private void ponerGuionesSiHaceFalta() {
        if (modeloListos.isEmpty()) modeloListos.addElement("—");
        if (modeloCPU.isEmpty()) modeloCPU.addElement("—");
        if (modeloBloqueados.isEmpty()) modeloBloqueados.addElement("—");
        if (modeloTerminados.isEmpty()) modeloTerminados.addElement("—");
    }

    private String formatearProceso(Proceso p) {
        return "PID " + p.getPid() + "  |  " + p.getNombre();
    }

    private static class RenderProceso extends JLabel implements ListCellRenderer<String> {
        public RenderProceso() {
            setOpaque(true);
            setBorder(new EmptyBorder(6, 8, 6, 8));
            setFont(new Font("Consolas", Font.PLAIN, 13));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends String> list,
                String value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            setText(value);

            if (isSelected) {
                setBackground(TemaUI.ACENTO);
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(13, 27, 46));
                setForeground(TemaUI.TEXTO);
            }

            return this;
        }
    }
}