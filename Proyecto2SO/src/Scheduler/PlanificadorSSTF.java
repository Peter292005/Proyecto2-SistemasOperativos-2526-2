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

public class PlanificadorSSTF extends PlanificadorDisco {

    public PlanificadorSSTF(int cabezaActual) {
        super(cabezaActual);
    }

    @Override
    public ListaEnlazada<SolicitudIO> planificar(ListaEnlazada<SolicitudIO> solicitudes) {
        ListaEnlazada<SolicitudIO> pendientes = copiarLista(solicitudes);
        ListaEnlazada<SolicitudIO> resultado = new ListaEnlazada<>();

        int posicionActual = cabezaActual;

        while (!pendientes.estaVacia()) {
            int mejorIndice = 0;
            int mejorDistancia = Math.abs(pendientes.obtener(0).getPosicionDisco() - posicionActual);

            for (int i = 1; i < pendientes.tamano(); i++) {
                int distancia = Math.abs(pendientes.obtener(i).getPosicionDisco() - posicionActual);
                if (distancia < mejorDistancia) {
                    mejorDistancia = distancia;
                    mejorIndice = i;
                }
            }

            SolicitudIO elegida = pendientes.eliminar(mejorIndice);
            resultado.agregar(elegida);
            posicionActual = elegida.getPosicionDisco();
        }

        return resultado;
    }

    private ListaEnlazada<SolicitudIO> copiarLista(ListaEnlazada<SolicitudIO> original) {
        ListaEnlazada<SolicitudIO> copia = new ListaEnlazada<>();
        for (int i = 0; i < original.tamano(); i++) {
            copia.agregar(original.obtener(i));
        }
        return copia;
    }
    private void intercambiar(ListaEnlazada<SolicitudIO> lista, int i, int j) {
    SolicitudIO temp = lista.obtener(i);
    lista.set(i, lista.obtener(j));
    lista.set(j, temp);
}
}