/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */

import Estructuras.ListaEnlazada;
import Procesos.Proceso;
import Procesos.SolicitudIO;
import Procesos.TipoOperacionIO;
import Scheduler.PlanificadorCSCAN;
import Scheduler.PlanificadorFIFO;
import Scheduler.PlanificadorSCAN;
import Scheduler.PlanificadorSSTF;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada<SolicitudIO> solicitudes = new ListaEnlazada<>();

        Proceso p1 = new Proceso(1, "P1");
        Proceso p2 = new Proceso(2, "P2");

        solicitudes.agregar(new SolicitudIO(1, p1, TipoOperacionIO.READ, "a.txt", 95));
        solicitudes.agregar(new SolicitudIO(2, p2, TipoOperacionIO.WRITE, "b.txt", 180));
        solicitudes.agregar(new SolicitudIO(3, p1, TipoOperacionIO.READ, "c.txt", 34));
        solicitudes.agregar(new SolicitudIO(4, p2, TipoOperacionIO.DELETE, "d.txt", 119));
        solicitudes.agregar(new SolicitudIO(5, p1, TipoOperacionIO.CREATE, "e.txt", 11));
        solicitudes.agregar(new SolicitudIO(6, p2, TipoOperacionIO.READ, "f.txt", 123));
        solicitudes.agregar(new SolicitudIO(7, p1, TipoOperacionIO.WRITE, "g.txt", 62));
        solicitudes.agregar(new SolicitudIO(8, p2, TipoOperacionIO.READ, "h.txt", 64));

        System.out.println("FIFO:");
        System.out.println(new PlanificadorFIFO(50).planificar(solicitudes));

        System.out.println("SSTF:");
        System.out.println(new PlanificadorSSTF(50).planificar(solicitudes));

        System.out.println("SCAN:");
        System.out.println(new PlanificadorSCAN(50, true).planificar(solicitudes));

        System.out.println("C-SCAN:");
        System.out.println(new PlanificadorCSCAN(50, true).planificar(solicitudes));
    }
}