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
import Procesos.SolicitudIO;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class PanelSolicitudesIO extends JPanel {
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PanelSolicitudesIO() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        modelo = new DefaultTableModel(new Object[]{"ID", "PID", "Operación", "Archivo", "Posición"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(TemaUI.CARD_2);
        tabla.setForeground(TemaUI.TEXTO);
        tabla.setGridColor(TemaUI.BORDE_SUAVE);
        tabla.setRowHeight(28);
        tabla.setFont(TemaUI.FUENTE_NORMAL);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setBackground(TemaUI.CARD_3);
        tabla.getTableHeader().setForeground(TemaUI.TEXTO);
        tabla.getTableHeader().setFont(TemaUI.FUENTE_SUBTITULO);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);

                if (!isSelected) {
                    setBackground(TemaUI.CARD_2);
                    setForeground(TemaUI.TEXTO);

                    String operacion = String.valueOf(table.getValueAt(row, 2));
                    if ("CREATE".equals(operacion)) {
                        setForeground(new Color(52, 211, 153));
                    } else if ("DELETE".equals(operacion)) {
                        setForeground(new Color(248, 113, 113));
                    } else if ("WRITE".equals(operacion)) {
                        setForeground(new Color(251, 191, 36));
                    } else if ("READ".equals(operacion)) {
                        setForeground(new Color(125, 211, 252));
                    }
                }

                return this;
            }
        };

        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.CARD_2);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        setPreferredSize(new Dimension(700, 320));
    }

    public void refrescar(ListaEnlazada<SolicitudIO> solicitudes) {
        modelo.setRowCount(0);

        if (solicitudes == null) {
            return;
        }

        for (int i = 0; i < solicitudes.tamano(); i++) {
            SolicitudIO s = solicitudes.obtener(i);
            modelo.addRow(new Object[]{
                    s.getIdSolicitud(),
                    s.getProceso().getPid(),
                    s.getTipoOperacion(),
                    s.getNombreArchivo(),
                    s.getPosicionDisco()
            });
        }
    }
}