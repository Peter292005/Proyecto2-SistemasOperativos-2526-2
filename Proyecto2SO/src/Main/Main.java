/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */

import Estructuras.ColaEnlazada;
import Estructuras.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        lista.agregar("archivo1.txt");
        lista.agregar("archivo2.txt");
        lista.agregarAlInicio("root");

        System.out.println("Lista: " + lista);
        System.out.println("Elemento en índice 1: " + lista.obtener(1));
        System.out.println("Contiene archivo2.txt: " + lista.contiene("archivo2.txt"));
        System.out.println("Eliminado: " + lista.eliminar(1));
        System.out.println("Lista final: " + lista);

        ColaEnlazada<String> cola = new ColaEnlazada<>();
        cola.encolar("P1");
        cola.encolar("P2");
        cola.encolar("P3");

        System.out.println("Cola: " + cola);
        System.out.println("Frente: " + cola.frente());
        System.out.println("Desencolado: " + cola.desencolar());
        System.out.println("Cola final: " + cola);
    }
}


