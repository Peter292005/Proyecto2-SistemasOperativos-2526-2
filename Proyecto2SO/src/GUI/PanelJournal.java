/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Journal.EntradaJournal;
import Journal.EstadoJournal;
import Journal.JournalManager;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class PanelJournal extends JPanel {
    private final DefaultListModel<EntradaJournal> modelo;
    private final JList<EntradaJournal> lista;
    private final JLabel lblEstadoSistema;

    public PanelJournal() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(TemaUI.CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);
        lista.setBackground(TemaUI.CARD_2);
        lista.setForeground(TemaUI.TEXTO);
        lista.setCellRenderer(new RenderJournal());

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.CARD_2);

        lblEstadoSistema = new JLabel("Estado: Normal");
        lblEstadoSistema.setForeground(TemaUI.EXITO);
        lblEstadoSistema.setFont(TemaUI.FUENTE_SUBTITULO);

        add(scroll, BorderLayout.CENTER);
        add(lblEstadoSistema, BorderLayout.SOUTH);
    }

    public void refrescar(JournalManager journal) {
        modelo.clear();

        if (journal == null || journal.getEntradas() == null) {
            return;
        }

        for (int i = 0; i < journal.getEntradas().tamano(); i++) {
            modelo.addElement(journal.getEntradas().obtener(i));
        }
    }

    public void marcarFalloSimulado() {
        lblEstadoSistema.setText("Estado: Fallo simulado");
        lblEstadoSistema.setForeground(TemaUI.ERROR);
    }

    public void marcarSistemaNormal() {
        lblEstadoSistema.setText("Estado: Normal");
        lblEstadoSistema.setForeground(TemaUI.EXITO);
    }

    private static class RenderJournal extends JLabel implements ListCellRenderer<EntradaJournal> {
        public RenderJournal() {
            setOpaque(true);
            setBorder(new EmptyBorder(8, 10, 8, 10));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends EntradaJournal> list,
                EntradaJournal value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            String estado = value.getEstado().toString();
            setText(value.getOperacion() + " '" + value.getNombreArchivo() + "' : " + estado);

            if (isSelected) {
                setBackground(TemaUI.ACENTO);
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(18, 30, 50));
                if (value.getEstado() == EstadoJournal.PENDIENTE) {
                    setForeground(TemaUI.WARNING);
                } else {
                    setForeground(TemaUI.EXITO);
                }
            }

            return this;
        }
    }
}