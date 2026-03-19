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

public class PlanificadorFIFO extends PlanificadorDisco {

    public PlanificadorFIFO(int cabezaActual) {
        super(cabezaActual);
    }

    @Override
    public ListaEnlazada<SolicitudIO> planificar(ListaEnlazada<SolicitudIO> solicitudes) {
        ListaEnlazada<SolicitudIO> resultado = new ListaEnlazada<>();

        for (int i = 0; i < solicitudes.tamano(); i++) {
            resultado.agregar(solicitudes.obtener(i));
        }

        return resultado;
    }
}
