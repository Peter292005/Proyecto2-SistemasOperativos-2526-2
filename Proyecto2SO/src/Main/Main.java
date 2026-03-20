/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */
import Concurrencia.GestorLocks;
import Procesos.Proceso;
import Procesos.SolicitudIO;
import Procesos.TipoOperacionIO;

public class Main {
    public static void main(String[] args) {
        GestorLocks gestor = new GestorLocks();

        Proceso p1 = new Proceso(1, "P1");
        Proceso p2 = new Proceso(2, "P2");
        Proceso p3 = new Proceso(3, "P3");

        SolicitudIO s1 = new SolicitudIO(1, p1, TipoOperacionIO.READ, "archivo.txt", 10);
        SolicitudIO s2 = new SolicitudIO(2, p2, TipoOperacionIO.READ, "archivo.txt", 10);
        SolicitudIO s3 = new SolicitudIO(3, p3, TipoOperacionIO.WRITE, "archivo.txt", 10);

        System.out.println("P1 solicita lectura: " + gestor.solicitarLock(s1));
        System.out.println("P2 solicita lectura: " + gestor.solicitarLock(s2));
        System.out.println("P3 solicita escritura: " + gestor.solicitarLock(s3));

        System.out.println("Estado locks:");
        System.out.println(gestor);

        System.out.println("Estados procesos:");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);

        gestor.liberarLock(s1);
        System.out.println("Después de liberar lectura de P1:");
        System.out.println(gestor);

        gestor.liberarLock(s2);
        System.out.println("Después de liberar lectura de P2:");
        System.out.println(gestor);

        System.out.println("Estado final de P3:");
        System.out.println(p3);
    }
}