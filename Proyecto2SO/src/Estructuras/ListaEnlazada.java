/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Estructuras;

/**
 *
 * @author Peter
 */

public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private int tamano;

    public ListaEnlazada() {
        this.cabeza = null;
        this.tamano = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return tamano;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }

        tamano++;
    }

    public void agregarAlInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamano++;
    }

    public T obtener(int indice) {
        validarIndice(indice);

        Nodo<T> actual = cabeza;
        int contador = 0;

        while (contador < indice) {
            actual = actual.getSiguiente();
            contador++;
        }

        return actual.getDato();
    }

    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;

        while (actual != null) {
            if ((dato == null && actual.getDato() == null) ||
                (dato != null && dato.equals(actual.getDato()))) {
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public T eliminarPrimero() {
        if (estaVacia()) {
            return null;
        }

        T dato = cabeza.getDato();
        cabeza = cabeza.getSiguiente();
        tamano--;
        return dato;
    }

    public T eliminar(int indice) {
        validarIndice(indice);

        if (indice == 0) {
            return eliminarPrimero();
        }

        Nodo<T> actual = cabeza;
        int contador = 0;

        while (contador < indice - 1) {
            actual = actual.getSiguiente();
            contador++;
        }

        Nodo<T> eliminado = actual.getSiguiente();
        actual.setSiguiente(eliminado.getSiguiente());
        tamano--;

        return eliminado.getDato();
    }

    public void limpiar() {
        cabeza = null;
        tamano = 0;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Nodo<T> actual = cabeza;
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
    public void set(int indice, T dato) {
    validarIndice(indice);

    Nodo<T> actual = cabeza;
    int contador = 0;

    while (contador < indice) {
        actual = actual.getSiguiente();
        contador++;
    }

    actual.setDato(dato);
}
    
}
