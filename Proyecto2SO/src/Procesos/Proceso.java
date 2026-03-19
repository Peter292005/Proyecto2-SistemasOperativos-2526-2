/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesos;

/**
 *
 * @author Peter
 */
public class Proceso {
    private int pid;
    private String nombre;
    private EstadoProceso estado;

    public Proceso(int pid, String nombre) {
        this.pid = pid;
        this.nombre = nombre;
        this.estado = EstadoProceso.NUEVO;
    }

    public int getPid() {
        return pid;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Proceso{" +
                "pid=" + pid +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }
}
