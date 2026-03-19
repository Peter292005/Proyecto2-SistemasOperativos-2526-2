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

public class PlanificadorCSCAN extends PlanificadorDisco {
    private boolean haciaArriba;

    public PlanificadorCSCAN(int cabezaActual, boolean haciaArriba) {
        super(cabezaActual);
        this.haciaArriba = haciaArriba;
    }

    @Override
    public ListaEnlazada<SolicitudIO> planificar(ListaEnlazada<SolicitudIO> solicitudes) {
        ListaEnlazada<SolicitudIO> arriba = new ListaEnlazada<>();
        ListaEnlazada<SolicitudIO> abajo = new ListaEnlazada<>();

        for (int i = 0; i < solicitudes.tamano(); i++) {
            SolicitudIO s = solicitudes.obtener(i);
            if (s.getPosicionDisco() >= cabezaActual) {
                arriba.agregar(s);
            } else {
                abajo.agregar(s);
            }
        }

        ordenarAscendente(arriba);
        ordenarAscendente(abajo);
        ordenarDescendente(arriba);
        ordenarDescendente(abajo);

        ListaEnlazada<SolicitudIO> resultado = new ListaEnlazada<>();

        if (haciaArriba) {
            ordenarAscendente(arriba);
            ordenarAscendente(abajo);
            agregarTodos(resultado, arriba);
            agregarTodos(resultado, abajo);
        } else {
            ordenarDescendente(abajo);
            ordenarDescendente(arriba);
            agregarTodos(resultado, abajo);
            agregarTodos(resultado, arriba);
        }

        return resultado;
    }

    private void ordenarAscendente(ListaEnlazada<SolicitudIO> lista) {
        for (int i = 0; i < lista.tamano(); i++) {
            for (int j = 0; j < lista.tamano() - 1 - i; j++) {
                if (lista.obtener(j).getPosicionDisco() > lista.obtener(j + 1).getPosicionDisco()) {
                    intercambiar(lista, j, j + 1);
                }
            }
        }
    }

    private void ordenarDescendente(ListaEnlazada<SolicitudIO> lista) {
        for (int i = 0; i < lista.tamano(); i++) {
            for (int j = 0; j < lista.tamano() - 1 - i; j++) {
                if (lista.obtener(j).getPosicionDisco() < lista.obtener(j + 1).getPosicionDisco()) {
                    intercambiar(lista, j, j + 1);
                }
            }
        }
    }

    private void intercambiar(ListaEnlazada<SolicitudIO> lista, int i, int j) {
        SolicitudIO temp = lista.obtener(i);
        lista.set(i, lista.obtener(j));
        lista.set(j, temp);
    }

    private void agregarTodos(ListaEnlazada<SolicitudIO> destino, ListaEnlazada<SolicitudIO> origen) {
        for (int i = 0; i < origen.tamano(); i++) {
            destino.agregar(origen.obtener(i));
        }
    }
}