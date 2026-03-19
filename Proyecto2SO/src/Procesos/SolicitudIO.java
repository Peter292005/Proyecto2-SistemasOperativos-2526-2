/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Procesos;

/**
 *
 * @author Peter
 */
public class SolicitudIO {
    private int idSolicitud;
    private Proceso proceso;
    private TipoOperacionIO tipoOperacion;
    private String nombreArchivo;
    private int posicionDisco;

    public SolicitudIO(int idSolicitud, Proceso proceso, TipoOperacionIO tipoOperacion,
                       String nombreArchivo, int posicionDisco) {
        this.idSolicitud = idSolicitud;
        this.proceso = proceso;
        this.tipoOperacion = tipoOperacion;
        this.nombreArchivo = nombreArchivo;
        this.posicionDisco = posicionDisco;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public TipoOperacionIO getTipoOperacion() {
        return tipoOperacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public int getPosicionDisco() {
        return posicionDisco;
    }

    @Override
    public String toString() {
        return "SolicitudIO{" +
                "idSolicitud=" + idSolicitud +
                ", proceso=" + proceso.getNombre() +
                ", tipoOperacion=" + tipoOperacion +
                ", archivo='" + nombreArchivo + '\'' +
                ", posicionDisco=" + posicionDisco +
                '}';
    }
}