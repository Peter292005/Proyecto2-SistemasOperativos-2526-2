/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Journal;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Filesystem.Archivo;
import Filesystem.Disco;
import Procesos.TipoOperacionIO;

public class JournalManager {
    private ListaEnlazada<EntradaJournal> entradas;
    private int siguienteId;

    public JournalManager() {
        this.entradas = new ListaEnlazada<>();
        this.siguienteId = 1;
    }

    public ListaEnlazada<EntradaJournal> getEntradas() {
        return entradas;
    }

    public EntradaJournal registrarOperacionPendiente(TipoOperacionIO operacion, Archivo archivo) {
        EntradaJournal entrada = new EntradaJournal(
                siguienteId++,
                operacion,
                archivo.getNombre(),
                archivo.getPropietario(),
                archivo.getTamanoEnBloques(),
                archivo.getPrimerBloque(),
                EstadoJournal.PENDIENTE
        );

        entradas.agregar(entrada);
        return entrada;
    }

    public void confirmarOperacion(int idEntrada) {
        EntradaJournal entrada = buscarPorId(idEntrada);
        if (entrada != null) {
            entrada.setEstado(EstadoJournal.CONFIRMADA);
        }
    }

    public EntradaJournal buscarPorId(int idEntrada) {
        for (int i = 0; i < entradas.tamano(); i++) {
            EntradaJournal entrada = entradas.obtener(i);
            if (entrada.getIdEntrada() == idEntrada) {
                return entrada;
            }
        }
        return null;
    }

    public ListaEnlazada<EntradaJournal> obtenerPendientes() {
        ListaEnlazada<EntradaJournal> pendientes = new ListaEnlazada<>();

        for (int i = 0; i < entradas.tamano(); i++) {
            EntradaJournal entrada = entradas.obtener(i);
            if (entrada.getEstado() == EstadoJournal.PENDIENTE) {
                pendientes.agregar(entrada);
            }
        }

        return pendientes;
    }

    public void recuperarOperacionesPendientes(Disco disco) {
        for (int i = 0; i < entradas.tamano(); i++) {
            EntradaJournal entrada = entradas.obtener(i);

            if (entrada.getEstado() == EstadoJournal.PENDIENTE) {
                if (entrada.getOperacion() == TipoOperacionIO.CREATE) {
                    deshacerCreate(disco, entrada);
                } else if (entrada.getOperacion() == TipoOperacionIO.DELETE) {
                    // Para esta primera versión, el undo real de DELETE puede quedar pendiente
                    // o registrarse como operación manual de recuperación.
                }

                entrada.setEstado(EstadoJournal.CONFIRMADA);
            }
        }
    }

    private void deshacerCreate(Disco disco, EntradaJournal entrada) {
        int actual = entrada.getPrimerBloque();

        while (actual != -1) {
            int siguiente = disco.obtenerBloque(actual).getSiguienteBloque();
            disco.obtenerBloque(actual).liberar();
            actual = siguiente;
        }
    }

    @Override
    public String toString() {
        return entradas.toString();
    }
  
}