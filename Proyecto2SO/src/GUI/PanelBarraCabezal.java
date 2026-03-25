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
    private int totalBloques = 256;
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

        int cardX = 12;
        int cardY = 10;
        int cardW = w - 24;
        int cardH = h - 20;

        g2.setColor(new Color(8, 18, 36));
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 18, 18);

        g2.setColor(new Color(255, 255, 255, 25));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 18, 18);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.setColor(new Color(230, 238, 248));
        g2.drawString("Política activa: " + politica, 28, 34);
        g2.drawString("Cabezal actual: " + posicionCabezal, 260, 34);

        int barraX = 28;
        int barraY = h / 2 + 8;
        int barraW = w - 56;
        int barraH = 12;

        g2.setColor(new Color(15, 30, 52));
        g2.fillRoundRect(barraX, barraY, barraW, barraH, 14, 14);

        g2.setColor(new Color(34, 211, 238, 70));
        g2.fillRoundRect(barraX, barraY, barraW, barraH, 14, 14);

        g2.setColor(new Color(34, 211, 238));
        g2.fillRoundRect(barraX, barraY + 3, barraW, 6, 14, 14);

        int xCabezal = barraX + (int) ((barraW * 1.0 * posicionCabezal) / Math.max(1, totalBloques - 1));

        g2.setColor(new Color(250, 204, 21, 80));
        g2.fillOval(xCabezal - 14, barraY - 10, 28, 28);

        g2.setColor(new Color(250, 204, 21));
        g2.fillOval(xCabezal - 9, barraY - 5, 18, 18);

        g2.setColor(Color.WHITE);
        g2.drawOval(xCabezal - 9, barraY - 5, 18, 18);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(new Color(230, 238, 248));
        g2.drawString("0", barraX - 2, barraY + 28);

        String ultimo = String.valueOf(totalBloques - 1);
        g2.drawString(ultimo, barraX + barraW - 18, barraY + 28);

        g2.dispose();
    }
}