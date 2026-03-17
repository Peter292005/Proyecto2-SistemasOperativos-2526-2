/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filesystem;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Estructuras.Nodo;

public class Directorio extends NodoFS {
    private ListaEnlazada<NodoFS> hijos;

    public Directorio(String nombre, String propietario) {
        super(nombre, propietario);
        this.hijos = new ListaEnlazada<>();
    }

    public ListaEnlazada<NodoFS> getHijos() {
        return hijos;
    }

    public void agregarHijo(NodoFS hijo) {
        if (hijo != null) {
            hijo.setPadre(this);
            hijos.agregar(hijo);
        }
    }

    public boolean eliminarHijoPorNombre(String nombre) {
        Nodo<NodoFS> actual = hijos.getCabeza();
        int indice = 0;

        while (actual != null) {
            if (actual.getDato().getNombre().equals(nombre)) {
                hijos.eliminar(indice);
                return true;
            }
            actual = actual.getSiguiente();
            indice++;
        }

        return false;
    }

    public NodoFS buscarHijoPorNombre(String nombre) {
        Nodo<NodoFS> actual = hijos.getCabeza();

        while (actual != null) {
            if (actual.getDato().getNombre().equals(nombre)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }

        return null;
    }

    @Override
    public boolean esDirectorio() {
        return true;
    }

    @Override
    public String toString() {
        return "Directorio{" +
                "nombre='" + getNombre() + '\'' +
                ", propietario='" + getPropietario() + '\'' +
                ", cantidadHijos=" + hijos.tamano() +
                '}';
    }
}