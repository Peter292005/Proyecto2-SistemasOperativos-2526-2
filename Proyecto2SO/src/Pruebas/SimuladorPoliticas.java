/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Procesos.SolicitudIO;
import Scheduler.PlanificadorCSCAN;
import Scheduler.PlanificadorFIFO;
import Scheduler.PlanificadorSCAN;
import Scheduler.PlanificadorSSTF;

public class SimuladorPoliticas {

    public ListaEnlazada<SolicitudIO> planificar(ContextoCasoPrueba contexto, String politica) {
        switch (politica.toUpperCase()) {
            case "FIFO":
                return new PlanificadorFIFO(contexto.getCabezaInicial())
                        .planificar(contexto.getSolicitudes());

            case "SSTF":
                return new PlanificadorSSTF(contexto.getCabezaInicial())
                        .planificar(contexto.getSolicitudes());

            case "SCAN":
                return new PlanificadorSCAN(contexto.getCabezaInicial(), contexto.isHaciaArriba())
                        .planificar(contexto.getSolicitudes());

            case "C-SCAN":
            case "CSCAN":
                return new PlanificadorCSCAN(contexto.getCabezaInicial(), contexto.isHaciaArriba())
                        .planificar(contexto.getSolicitudes());

            default:
                throw new IllegalArgumentException("Política no soportada: " + politica);
        }
    }

    public String formatearOrdenPosiciones(ListaEnlazada<SolicitudIO> orden) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < orden.tamano(); i++) {
            sb.append(orden.obtener(i).getPosicionDisco());
            if (i < orden.tamano() - 1) {
                sb.append(" -> ");
            }
        }

        return sb.toString();
    }

    public int calcularMovimientoTotal(int cabezaInicial, ListaEnlazada<SolicitudIO> orden) {
        if (orden == null || orden.estaVacia()) {
            return 0;
        }

        int movimiento = 0;
        int actual = cabezaInicial;

        for (int i = 0; i < orden.tamano(); i++) {
            int siguiente = orden.obtener(i).getPosicionDisco();
            movimiento += Math.abs(siguiente - actual);
            actual = siguiente;
        }

        return movimiento;
    }
}