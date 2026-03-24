/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Concurrencia.GestorLocks;
import Concurrencia.LockArchivo;
import Concurrencia.TipoLock;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class PanelLocksActivos extends JPanel {
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PanelLocksActivos() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        modelo = new DefaultTableModel(new Object[]{"Archivo", "Tipo", "Titular", "En espera"}, 0) {
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

                    String tipo = String.valueOf(table.getValueAt(row, 1));
                    if ("Lectura".equals(tipo)) {
                        setForeground(new Color(125, 211, 252));
                    } else if ("Escritura".equals(tipo)) {
                        setForeground(new Color(251, 146, 60));
                    } else if ("Libre".equals(tipo)) {
                        setForeground(new Color(148, 163, 184));
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

        add(scroll, BorderLayout.CENTER);
    }

    public void refrescar(GestorLocks gestorLocks) {
        modelo.setRowCount(0);

        if (gestorLocks == null || gestorLocks.getLocks() == null) {
            return;
        }

        for (int i = 0; i < gestorLocks.getLocks().tamano(); i++) {
            LockArchivo lock = gestorLocks.getLocks().obtener(i);

            String tipo = lock.getTipoActual() == null ? "Libre"
                    : (lock.getTipoActual() == TipoLock.LECTURA ? "Lectura" : "Escritura");

            String titular = "-";
            if (lock.getEscritorActual() != null) {
                titular = lock.getEscritorActual().getNombre();
            } else if (lock.getLectoresActuales() != null && !lock.getLectoresActuales().estaVacia()) {
                titular = lock.getLectoresActuales().tamano() + " lector(es)";
            }

            int enEspera = 0;
            if (lock.getColaEspera() != null) {
                enEspera = lock.getColaEspera().tamano();
            }

            modelo.addRow(new Object[]{
                    lock.getNombreArchivo(),
                    tipo,
                    titular,
                    enEspera
            });
        }
    }
}