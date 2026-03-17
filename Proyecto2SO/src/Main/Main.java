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
import Filesystem.Directorio;
import Filesystem.Disco;

public class Main {
    public static void main(String[] args) {
        Directorio root = new Directorio("root", "admin");
        Directorio documentos = new Directorio("documentos", "peter");
        Archivo archivo1 = new Archivo("tarea.txt", "peter", 3);
        Archivo archivo2 = new Archivo("notas.doc", "peter", 2);

        root.agregarHijo(documentos);
        documentos.agregarHijo(archivo1);
        documentos.agregarHijo(archivo2);

        System.out.println(root);
        System.out.println(documentos);
        System.out.println(archivo1);
        System.out.println("Ruta de archivo1: " + archivo1.getRutaCompleta());

        Disco disco = new Disco(20);
        System.out.println(disco);
        System.out.println("Bloque 0: " + disco.obtenerBloque(0));
        System.out.println("Bloques libres: " + disco.contarBloquesLibres());
    }
}

