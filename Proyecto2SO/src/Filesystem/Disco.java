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

public class Disco {
    private int cantidadBloques;
    private ListaEnlazada<BloqueDisco> bloques;

    public Disco(int cantidadBloques) {
        this.cantidadBloques = cantidadBloques;
        this.bloques = new ListaEnlazada<>();

        for (int i = 0; i < cantidadBloques; i++) {
            bloques.agregar(new BloqueDisco(i));
        }
    }

    public int getCantidadBloques() {
        return cantidadBloques;
    }

    public ListaEnlazada<BloqueDisco> getBloques() {
        return bloques;
    }

    public BloqueDisco obtenerBloque(int indice) {
        return bloques.obtener(indice);
    }

    public int contarBloquesLibres() {
        int libres = 0;

        for (int i = 0; i < bloques.tamano(); i++) {
            if (bloques.obtener(i).estaLibre()) {
                libres++;
            }
        }

        return libres;
    }

    public boolean hayEspacioSuficiente(int cantidadNecesaria) {
        return contarBloquesLibres() >= cantidadNecesaria;
    }

    public ListaEnlazada<Integer> buscarBloquesLibres(int cantidadNecesaria) {
        ListaEnlazada<Integer> indicesLibres = new ListaEnlazada<>();

        for (int i = 0; i < bloques.tamano() && indicesLibres.tamano() < cantidadNecesaria; i++) {
            if (bloques.obtener(i).estaLibre()) {
                indicesLibres.agregar(i);
            }
        }

        return indicesLibres;
    }

    public boolean asignarBloquesAArchivo(Archivo archivo) {
        int cantidadNecesaria = archivo.getTamanoEnBloques();

        if (!hayEspacioSuficiente(cantidadNecesaria)) {
            return false;
        }

        ListaEnlazada<Integer> indices = buscarBloquesLibres(cantidadNecesaria);

        for (int i = 0; i < indices.tamano(); i++) {
            int indiceActual = indices.obtener(i);
            int siguiente = (i < indices.tamano() - 1) ? indices.obtener(i + 1) : -1;

            BloqueDisco bloque = obtenerBloque(indiceActual);
            bloque.ocupar(archivo.getNombre(), siguiente);
        }

        archivo.setPrimerBloque(indices.obtener(0));
        return true;
    }

    public void liberarBloquesDeArchivo(Archivo archivo) {
        int actual = archivo.getPrimerBloque();

        while (actual != -1) {
            BloqueDisco bloque = obtenerBloque(actual);
            int siguiente = bloque.getSiguienteBloque();
            bloque.liberar();
            actual = siguiente;
        }

        archivo.setPrimerBloque(-1);
    }

    public ListaEnlazada<Integer> obtenerCadenaBloques(Archivo archivo) {
        ListaEnlazada<Integer> cadena = new ListaEnlazada<>();
        int actual = archivo.getPrimerBloque();

        while (actual != -1) {
            cadena.agregar(actual);
            actual = obtenerBloque(actual).getSiguienteBloque();
        }

        return cadena;
    }

    @Override
    public String toString() {
        return "Disco{" +
                "cantidadBloques=" + cantidadBloques +
                ", bloquesLibres=" + contarBloquesLibres() +
                '}';
    }
}