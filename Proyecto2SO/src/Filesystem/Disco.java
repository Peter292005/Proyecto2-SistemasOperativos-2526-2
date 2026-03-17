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

    @Override
    public String toString() {
        return "Disco{" +
                "cantidadBloques=" + cantidadBloques +
                ", bloquesLibres=" + contarBloquesLibres() +
                '}';
    }
}
