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

public class ContextoCasoPrueba {
    private String testId;
    private int cabezaInicial;
    private boolean haciaArriba;
    private ListaEnlazada<SolicitudIO> solicitudes;

    public ContextoCasoPrueba(String testId, int cabezaInicial, boolean haciaArriba, ListaEnlazada<SolicitudIO> solicitudes) {
        this.testId = testId;
        this.cabezaInicial = cabezaInicial;
        this.haciaArriba = haciaArriba;
        this.solicitudes = solicitudes;
    }

    public String getTestId() {
        return testId;
    }

    public int getCabezaInicial() {
        return cabezaInicial;
    }

    public boolean isHaciaArriba() {
        return haciaArriba;
    }

    public ListaEnlazada<SolicitudIO> getSolicitudes() {
        return solicitudes;
    }
}