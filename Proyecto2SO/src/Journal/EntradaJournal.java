/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Journal;

/**
 *
 * @author Peter
 */
import Procesos.TipoOperacionIO;

public class EntradaJournal {
    private int idEntrada;
    private TipoOperacionIO operacion;
    private String nombreArchivo;
    private String propietario;
    private int tamanoEnBloques;
    private int primerBloque;
    private EstadoJournal estado;

    public EntradaJournal(int idEntrada, TipoOperacionIO operacion, String nombreArchivo,
                          String propietario, int tamanoEnBloques, int primerBloque,
                          EstadoJournal estado) {
        this.idEntrada = idEntrada;
        this.operacion = operacion;
        this.nombreArchivo = nombreArchivo;
        this.propietario = propietario;
        this.tamanoEnBloques = tamanoEnBloques;
        this.primerBloque = primerBloque;
        this.estado = estado;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public TipoOperacionIO getOperacion() {
        return operacion;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getPropietario() {
        return propietario;
    }

    public int getTamanoEnBloques() {
        return tamanoEnBloques;
    }

    public int getPrimerBloque() {
        return primerBloque;
    }

    public void setPrimerBloque(int primerBloque) {
        this.primerBloque = primerBloque;
    }

    public EstadoJournal getEstado() {
        return estado;
    }

    public void setEstado(EstadoJournal estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EntradaJournal{" +
                "idEntrada=" + idEntrada +
                ", operacion=" + operacion +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", propietario='" + propietario + '\'' +
                ", tamanoEnBloques=" + tamanoEnBloques +
                ", primerBloque=" + primerBloque +
                ", estado=" + estado +
                '}';
    }
}
