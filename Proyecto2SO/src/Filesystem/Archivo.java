/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filesystem;

/**
 *
 * @author Peter
 */

public class Archivo extends NodoFS {
    private int tamanoEnBloques;
    private int primerBloque;
    private String color;

    public Archivo(String nombre, String propietario, int tamanoEnBloques) {
        super(nombre, propietario);
        this.tamanoEnBloques = tamanoEnBloques;
        this.primerBloque = -1;
        this.color = "SIN_COLOR";
    }

    public int getTamanoEnBloques() {
        return tamanoEnBloques;
    }

    public void setTamanoEnBloques(int tamanoEnBloques) {
        this.tamanoEnBloques = tamanoEnBloques;
    }

    public int getPrimerBloque() {
        return primerBloque;
    }

    public void setPrimerBloque(int primerBloque) {
        this.primerBloque = primerBloque;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean esDirectorio() {
        return false;
    }

    @Override
    public String toString() {
        return "Archivo{" +
                "nombre='" + getNombre() + '\'' +
                ", propietario='" + getPropietario() + '\'' +
                ", tamanoEnBloques=" + tamanoEnBloques +
                ", primerBloque=" + primerBloque +
                '}';
    }
}
