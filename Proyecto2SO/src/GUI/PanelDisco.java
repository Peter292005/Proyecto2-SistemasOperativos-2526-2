/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Filesystem.BloqueDisco;
import Filesystem.Disco;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

public class PanelDisco extends JPanel {
    private Disco disco;
    private int bloqueHover = -1;

    private static final int COLUMNAS = 10;
    private static final int MARGEN_X = 18;
    private static final int MARGEN_Y = 20;
    private static final int ANCHO = 54;
    private static final int ALTO = 38;
    private static final int GAP = 10;

    public PanelDisco(Disco disco) {
        this.disco = disco;
        setBackground(TemaUI.CARD_2);
        ToolTipManager.sharedInstance().registerComponent(this);
        enableEvents(java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public void setDisco(Disco disco) {
        this.disco = disco;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (disco == null) {
            return new Dimension(820, 500);
        }

        int filas = (int) Math.ceil(disco.getCantidadBloques() / (double) COLUMNAS);

        int anchoTotal = MARGEN_X * 2 + COLUMNAS * ANCHO + (COLUMNAS - 1) * GAP;
        int altoTotal = MARGEN_Y * 2 + filas * ALTO + (filas - 1) * GAP;

        return new Dimension(anchoTotal + 20, altoTotal + 20);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        if (disco == null) {
            return null;
        }

        for (int i = 0; i < disco.getCantidadBloques(); i++) {
            int fila = i / COLUMNAS;
            int col = i % COLUMNAS;

            int x = MARGEN_X + col * (ANCHO + GAP);
            int y = MARGEN_Y + fila * (ALTO + GAP);

            if (event.getX() >= x && event.getX() <= x + ANCHO &&
                event.getY() >= y && event.getY() <= y + ALTO) {
                BloqueDisco b = disco.obtenerBloque(i);
                String archivo = b.getNombreArchivo() == null ? "Libre" : b.getNombreArchivo();
                return "Bloque " + i + " | Archivo: " + archivo + " | Siguiente: " + b.getSiguienteBloque();
            }
        }

        return null;
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        bloqueHover = obtenerBloqueEnPunto(e.getX(), e.getY());
        repaint();
    }

    private int obtenerBloqueEnPunto(int px, int py) {
        if (disco == null) {
            return -1;
        }

        for (int i = 0; i < disco.getCantidadBloques(); i++) {
            int fila = i / COLUMNAS;
            int col = i % COLUMNAS;

            int x = MARGEN_X + col * (ANCHO + GAP);
            int y = MARGEN_Y + fila * (ALTO + GAP);

            if (px >= x && px <= x + ANCHO && py >= y && py <= y + ALTO) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (disco == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        for (int i = 0; i < disco.getCantidadBloques(); i++) {
            BloqueDisco bloque = disco.obtenerBloque(i);

            int fila = i / COLUMNAS;
            int col = i % COLUMNAS;

            int x = MARGEN_X + col * (ANCHO + GAP);
            int y = MARGEN_Y + fila * (ALTO + GAP);

            Color colorBloque = bloque.estaLibre()
                    ? TemaUI.BLOQUE_LIBRE
                    : colorPorArchivo(bloque.getNombreArchivo());

            g2.setColor(new Color(0, 0, 0, 55));
            g2.fillRoundRect(x + 3, y + 4, ANCHO, ALTO, 16, 16);

            g2.setColor(colorBloque);
            g2.fillRoundRect(x, y, ANCHO, ALTO, 16, 16);

            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(x, y, ANCHO, ALTO, 16, 16);

            if (i == bloqueHover) {
                g2.setColor(TemaUI.WARNING);
                g2.drawRoundRect(x - 2, y - 2, ANCHO + 4, ALTO + 4, 18, 18);
                g2.drawRoundRect(x - 1, y - 1, ANCHO + 2, ALTO + 2, 17, 17);
            }

            g2.setColor(Color.WHITE);
            String texto = String.valueOf(i);
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (ANCHO - fm.stringWidth(texto)) / 2;
            int ty = y + ((ALTO - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(texto, tx, ty);
        }

        g2.dispose();
    }

    private Color colorPorArchivo(String nombreArchivo) {
        if (nombreArchivo == null) {
            return TemaUI.BLOQUE_LIBRE;
        }

        int hash = Math.abs(nombreArchivo.hashCode());
        Color[] paleta = new Color[]{
                new Color(239, 68, 68),
                new Color(249, 115, 22),
                new Color(245, 158, 11),
                new Color(34, 197, 94),
                new Color(20, 184, 166),
                new Color(59, 130, 246),
                new Color(99, 102, 241),
                new Color(168, 85, 247),
                new Color(236, 72, 153)
        };

        return paleta[hash % paleta.length];
    }
}