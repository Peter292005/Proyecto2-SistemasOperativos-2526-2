/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class PanelBarraCabezal extends JPanel {
    private int posicionCabezal = 0;
    private int totalBloques = 32;
    private String politica = "FIFO";

    public PanelBarraCabezal() {
        setOpaque(true);
        setBackground(TemaUI.CARD);
    }

    public void actualizar(int posicionCabezal, int totalBloques, String politica) {
        this.posicionCabezal = posicionCabezal;
        this.totalBloques = totalBloques <= 0 ? 1 : totalBloques;
        this.politica = politica;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int margenX = 30;
        int barraY = h / 2;
        int barraW = w - 2 * margenX;

        g2.setColor(new Color(34, 211, 238, 70));
        g2.fillRoundRect(margenX, barraY - 5, barraW, 10, 10, 10);

        g2.setColor(new Color(34, 211, 238));
        g2.fillRoundRect(margenX, barraY - 3, barraW, 6, 10, 10);

        int xCabezal = margenX + (int) ((barraW * 1.0 * posicionCabezal) / Math.max(1, totalBloques - 1));

        g2.setColor(new Color(250, 204, 21));
        g2.fillOval(xCabezal - 9, barraY - 9, 18, 18);
        g2.setColor(Color.WHITE);
        g2.drawOval(xCabezal - 9, barraY - 9, 18, 18);

        g2.setColor(TemaUI.TEXTO);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.drawString("0", margenX - 5, barraY + 24);
        g2.drawString(String.valueOf(totalBloques - 1), margenX + barraW - 10, barraY + 24);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.drawString("Política activa: " + politica, margenX, 20);
        g2.drawString("Cabezal actual: " + posicionCabezal, margenX + 220, 20);

        g2.dispose();
    }
}
