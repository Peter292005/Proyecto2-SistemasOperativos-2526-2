/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Concurrencia;

/**
 *
 * @author Peter
 */
import Estructuras.ColaEnlazada;
import Estructuras.ListaEnlazada;
import Procesos.Proceso;
import Procesos.SolicitudIO;

public class LockArchivo {
    private String nombreArchivo;
    private TipoLock tipoActual;
    private Proceso escritorActual;
    private ListaEnlazada<Proceso> lectoresActuales;
    private ColaEnlazada<SolicitudIO> colaEspera;

    public LockArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.tipoActual = null;
        this.escritorActual = null;
        this.lectoresActuales = new ListaEnlazada<>();
        this.colaEspera = new ColaEnlazada<>();
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public TipoLock getTipoActual() {
        return tipoActual;
    }

    public Proceso getEscritorActual() {
        return escritorActual;
    }

    public ListaEnlazada<Proceso> getLectoresActuales() {
        return lectoresActuales;
    }

    public ColaEnlazada<SolicitudIO> getColaEspera() {
        return colaEspera;
    }

    public boolean estaLibre() {
        return tipoActual == null;
    }

    public void asignarLectura(Proceso proceso) {
        tipoActual = TipoLock.LECTURA;
        lectoresActuales.agregar(proceso);
    }

    public void asignarEscritura(Proceso proceso) {
        tipoActual = TipoLock.ESCRITURA;
        escritorActual = proceso;
    }

    public void liberarLectura(Proceso proceso) {
        for (int i = 0; i < lectoresActuales.tamano(); i++) {
            if (lectoresActuales.obtener(i).getPid() == proceso.getPid()) {
                lectoresActuales.eliminar(i);
                break;
            }
        }

        if (lectoresActuales.estaVacia()) {
            tipoActual = null;
        }
    }

    public void liberarEscritura() {
        escritorActual = null;
        tipoActual = null;
    }

    @Override
    public String toString() {
        return "LockArchivo{" +
                "nombreArchivo='" + nombreArchivo + '\'' +
                ", tipoActual=" + tipoActual +
                ", escritorActual=" + (escritorActual != null ? escritorActual.getNombre() : "null") +
                ", lectores=" + lectoresActuales +
                ", enEspera=" + colaEspera +
                '}';
    }
}
