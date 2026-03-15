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
    private int tamano;

    public ColaEnlazada() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamano() {
        return tamano;
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            fin = nuevo;
        }

        tamano++;
    }

    public T desencolar() {
        if (estaVacia()) {
            return null;
        }

        T dato = frente.getDato();
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null;
        }

        tamano--;
        return dato;
    }

    public T frente() {
        return estaVacia() ? null : frente.getDato();
    }

    public void limpiar() {
        frente = null;
        fin = null;
        tamano = 0;
    }

    public Nodo<T> getFrenteNodo() {
        return frente;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }

        sb.append("]");
        return sb.toString();
    }
}
