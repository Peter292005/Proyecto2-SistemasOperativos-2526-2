/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras;

/**
 *
 * @author Peter
 */

public class ColaEnlazada<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;

    public ColaEnlazada() {
        this.frente = null;
        this.fin = null;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public Nodo<T> getFrente() {
        return frente;
    }

    public Nodo<T> getFin() {
        return fin;
    }
}
