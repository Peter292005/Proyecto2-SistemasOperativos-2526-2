/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Procesos.SolicitudIO;

public abstract class PlanificadorDisco {
    protected int cabezaActual;

    public PlanificadorDisco(int cabezaActual) {
        this.cabezaActual = cabezaActual;
    }

    public int getCabezaActual() {
        return cabezaActual;
    }

    public void setCabezaActual(int cabezaActual) {
        this.cabezaActual = cabezaActual;
    }

    public abstract ListaEnlazada<SolicitudIO> planificar(ListaEnlazada<SolicitudIO> solicitudes);
}