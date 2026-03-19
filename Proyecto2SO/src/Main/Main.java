/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */


import Filesystem.Archivo;
import Filesystem.Disco;

public class Main {
    public static void main(String[] args) {
        Disco disco = new Disco(10);

        Archivo archivo1 = new Archivo("tarea.txt", "peter", 3);
        Archivo archivo2 = new Archivo("foto.png", "peter", 2);

        boolean asignado1 = disco.asignarBloquesAArchivo(archivo1);
        boolean asignado2 = disco.asignarBloquesAArchivo(archivo2);

        System.out.println("Archivo 1 asignado: " + asignado1);
        System.out.println("Primer bloque archivo1: " + archivo1.getPrimerBloque());
        System.out.println("Cadena archivo1: " + disco.obtenerCadenaBloques(archivo1));

        System.out.println("Archivo 2 asignado: " + asignado2);
        System.out.println("Primer bloque archivo2: " + archivo2.getPrimerBloque());
        System.out.println("Cadena archivo2: " + disco.obtenerCadenaBloques(archivo2));

        System.out.println("Bloques libres antes de liberar: " + disco.contarBloquesLibres());

        disco.liberarBloquesDeArchivo(archivo1);

        System.out.println("Archivo 1 liberado.");
        System.out.println("Primer bloque archivo1: " + archivo1.getPrimerBloque());
        System.out.println("Cadena archivo1 despues de liberar: " + disco.obtenerCadenaBloques(archivo1));
        System.out.println("Bloques libres despues de liberar: " + disco.contarBloquesLibres());
    }
}