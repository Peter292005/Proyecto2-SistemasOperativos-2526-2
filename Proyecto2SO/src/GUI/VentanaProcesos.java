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
import Procesos.Proceso;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class VentanaProcesos extends JFrame {
    private final PanelProcesos panelProcesos;

    public VentanaProcesos(ListaEnlazada<Proceso> procesos) {
        setTitle("Vista completa - Cola de Procesos");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(TemaUI.FONDO_APP);
        setLayout(new BorderLayout(10, 10));

        panelProcesos = new PanelProcesos();
        panelProcesos.refrescar(procesos, null);

        add(panelProcesos, BorderLayout.CENTER);
    }

    public void refrescar(ListaEnlazada<Proceso> procesos, Proceso procesoActivo) {
        panelProcesos.refrescar(procesos, procesoActivo);
        panelProcesos.revalidate();
        panelProcesos.repaint();
        revalidate();
        repaint();
    }
}