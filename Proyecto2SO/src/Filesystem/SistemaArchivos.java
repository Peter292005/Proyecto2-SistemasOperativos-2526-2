/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filesystem;

/**
 *
 * @author Peter
 */
public class SistemaArchivos {
    private Directorio root;
    private Disco disco;

    public SistemaArchivos(int cantidadBloques) {
        this.root = new Directorio("root", "admin");
        this.disco = new Disco(cantidadBloques);
    }

    public Directorio getRoot() {
        return root;
    }

    public Disco getDisco() {
        return disco;
    }
}
