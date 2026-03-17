/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Filesystem;

/**
 *
 * @author Peter
 */

public abstract class NodoFS {
    private String nombre;
    private String propietario;
    private Directorio padre;

    public NodoFS(String nombre, String propietario) {
        this.nombre = nombre;
        this.propietario = propietario;
        this.padre = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public Directorio getPadre() {
        return padre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }

    public String getRutaCompleta() {
        if (padre == null) {
            return "/" + nombre;
        }
        return padre.getRutaCompleta() + "/" + nombre;
    }

    public abstract boolean esDirectorio();
}