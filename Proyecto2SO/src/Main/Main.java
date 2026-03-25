/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */
import Filesystem.SistemaArchivos;
import GUI.VentanaPrincipal;
import Journal.JournalManager;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaArchivos sistema = new SistemaArchivos(1024);
            JournalManager journal = new JournalManager();

            VentanaPrincipal ventana = new VentanaPrincipal(sistema, journal);
            ventana.setVisible(true);
        });
    }
}