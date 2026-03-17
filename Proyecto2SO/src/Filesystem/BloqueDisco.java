/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filesystem;

/**
 *
 * @author Peter
 */

public class BloqueDisco {
    private int indice;
    private boolean libre;
    private String nombreArchivo;
    private int siguienteBloque;

    public BloqueDisco(int indice) {
        this.indice = indice;
        this.libre = true;
        this.nombreArchivo = null;
        this.siguienteBloque = -1;
    }

    public int getIndice() {
        return indice;
    }

    public boolean estaLibre() {
        return libre;
    }

    public void ocupar(String nombreArchivo, int siguienteBloque) {
        this.libre = false;
        this.nombreArchivo = nombreArchivo;
        this.siguienteBloque = siguienteBloque;
    }

    public void liberar() {
        this.libre = true;
        this.nombreArchivo = null;
        this.siguienteBloque = -1;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public int getSiguienteBloque() {
        return siguienteBloque;
    }

    public void setSiguienteBloque(int siguienteBloque) {
        this.siguienteBloque = siguienteBloque;
    }

    @Override
    public String toString() {
        return "BloqueDisco{" +
                "indice=" + indice +
                ", libre=" + libre +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", siguienteBloque=" + siguienteBloque +
                '}';
    }
}
