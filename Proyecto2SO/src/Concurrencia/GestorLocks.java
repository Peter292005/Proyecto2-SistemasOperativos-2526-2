/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Concurrencia;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Procesos.EstadoProceso;
import Procesos.Proceso;
import Procesos.SolicitudIO;
import Procesos.TipoOperacionIO;

public class GestorLocks {
    private ListaEnlazada<LockArchivo> locks;

    public GestorLocks() {
        this.locks = new ListaEnlazada<>();
    }

    public ListaEnlazada<LockArchivo> getLocks() {
        return locks;
    }

    public boolean solicitarLock(SolicitudIO solicitud) {
        LockArchivo lock = obtenerOCrearLock(solicitud.getNombreArchivo());
        TipoLock tipoSolicitado = inferirTipoLock(solicitud);

        if (puedeOtorgarse(lock, solicitud.getProceso(), tipoSolicitado)) {
            otorgarLock(lock, solicitud.getProceso(), tipoSolicitado);
            solicitud.getProceso().setEstado(EstadoProceso.LISTO);
            return true;
        } else {
            lock.getColaEspera().encolar(solicitud);
            solicitud.getProceso().setEstado(EstadoProceso.BLOQUEADO);
            return false;
        }
    }

    public void liberarLock(SolicitudIO solicitud) {
        LockArchivo lock = buscarLock(solicitud.getNombreArchivo());

        if (lock == null) {
            return;
        }

        TipoLock tipo = inferirTipoLock(solicitud);

        if (tipo == TipoLock.LECTURA) {
            lock.liberarLectura(solicitud.getProceso());
        } else {
            if (lock.getEscritorActual() != null &&
                lock.getEscritorActual().getPid() == solicitud.getProceso().getPid()) {
                lock.liberarEscritura();
            }
        }

        procesarColaEspera(lock);
    }

    private LockArchivo obtenerOCrearLock(String nombreArchivo) {
        LockArchivo existente = buscarLock(nombreArchivo);
        if (existente != null) {
            return existente;
        }

        LockArchivo nuevo = new LockArchivo(nombreArchivo);
        locks.agregar(nuevo);
        return nuevo;
    }

    private LockArchivo buscarLock(String nombreArchivo) {
        for (int i = 0; i < locks.tamano(); i++) {
            LockArchivo lock = locks.obtener(i);
            if (lock.getNombreArchivo().equals(nombreArchivo)) {
                return lock;
            }
        }
        return null;
    }

    private TipoLock inferirTipoLock(SolicitudIO solicitud) {
        if (solicitud.getTipoOperacion() == TipoOperacionIO.READ) {
            return TipoLock.LECTURA;
        }
        return TipoLock.ESCRITURA;
    }

    private boolean puedeOtorgarse(LockArchivo lock, Proceso proceso, TipoLock tipoSolicitado) {
        if (lock.estaLibre()) {
            return true;
        }

        if (tipoSolicitado == TipoLock.LECTURA && lock.getTipoActual() == TipoLock.LECTURA) {
            return true;
        }

        return false;
    }

    private void otorgarLock(LockArchivo lock, Proceso proceso, TipoLock tipoSolicitado) {
        if (tipoSolicitado == TipoLock.LECTURA) {
            lock.asignarLectura(proceso);
        } else {
            lock.asignarEscritura(proceso);
        }
    }

    private void procesarColaEspera(LockArchivo lock) {
        boolean progreso = true;

        while (progreso && !lock.getColaEspera().estaVacia()) {
            progreso = false;

            SolicitudIO siguiente = lock.getColaEspera().frente();
            TipoLock tipo = inferirTipoLock(siguiente);

            if (puedeOtorgarse(lock, siguiente.getProceso(), tipo)) {
                lock.getColaEspera().desencolar();
                otorgarLock(lock, siguiente.getProceso(), tipo);
                siguiente.getProceso().setEstado(EstadoProceso.LISTO);
                progreso = true;
            }
        }
    }

    @Override
    public String toString() {
        return locks.toString();
    }
}
