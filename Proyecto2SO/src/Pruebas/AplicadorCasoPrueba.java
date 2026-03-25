/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

/**
 *
 * @author Peter
 */
import Estructuras.ListaEnlazada;
import Filesystem.Archivo;
import Filesystem.Directorio;
import Filesystem.NodoFS;
import Filesystem.SistemaArchivos;
import Procesos.EstadoProceso;
import Procesos.Proceso;
import Procesos.SolicitudIO;
import Procesos.TipoOperacionIO;

public class AplicadorCasoPrueba {
    private final SistemaArchivos sistema;
    private int siguientePid = 1;
    private int siguienteSolicitudId = 1;

    public AplicadorCasoPrueba(SistemaArchivos sistema) {
        this.sistema = sistema;
    }

    public ContextoCasoPrueba aplicar(CasoPrueba caso) {
        validarCapacidadCaso(caso);

        limpiarSistema();
        sistema.getDisco().limpiarDisco();

        Directorio systemFilesDir = new Directorio("system_files", "admin");
        sistema.getRoot().agregarHijo(systemFilesDir);

        for (int i = 0; i < caso.getSystemFiles().tamano(); i++) {
            SystemFileEntry entry = caso.getSystemFiles().obtener(i);

            boolean ok = sistema.getDisco().precargarArchivoEnBloques(
                    entry.getName(),
                    entry.getStartPos(),
                    entry.getBlocks()
            );

            if (!ok) {
                throw new IllegalStateException(
                        "No se pudo precargar '" + entry.getName() +
                        "' en bloque " + entry.getStartPos() +
                        " con longitud " + entry.getBlocks()
                );
            }

            Archivo archivo = new Archivo(entry.getName(), "admin", entry.getBlocks());
            archivo.setPrimerBloque(entry.getStartPos());
            systemFilesDir.agregarHijo(archivo);
        }

        ListaEnlazada<SolicitudIO> solicitudes = new ListaEnlazada<>();

        for (int i = 0; i < caso.getRequests().tamano(); i++) {
            RequestSpec request = caso.getRequests().obtener(i);

            Proceso proceso = new Proceso(siguientePid++, "REQ_" + (i + 1));
            proceso.setEstado(EstadoProceso.LISTO);

            TipoOperacionIO tipo = mapearOperacion(request.getOp());
            String nombreArchivo = resolverNombreArchivoPorPosicion(caso.getSystemFiles(), request.getPos());

            solicitudes.agregar(new SolicitudIO(
                    siguienteSolicitudId++,
                    proceso,
                    tipo,
                    nombreArchivo,
                    request.getPos()
            ));
        }

        boolean haciaArriba = !"DOWN".equalsIgnoreCase(caso.getDirection());
        return new ContextoCasoPrueba(caso.getTestId(), caso.getInitialHead(), haciaArriba, solicitudes);
    }

    private void validarCapacidadCaso(CasoPrueba caso) {
        int maxNecesario = 0;

        for (int i = 0; i < caso.getSystemFiles().tamano(); i++) {
            SystemFileEntry entry = caso.getSystemFiles().obtener(i);
            int fin = entry.getStartPos() + entry.getBlocks();
            if (fin > maxNecesario) {
                maxNecesario = fin;
            }
        }

        for (int i = 0; i < caso.getRequests().tamano(); i++) {
            RequestSpec req = caso.getRequests().obtener(i);
            int pos = req.getPos() + 1;
            if (pos > maxNecesario) {
                maxNecesario = pos;
            }
        }

        if (sistema.getDisco().getCantidadBloques() < maxNecesario) {
            throw new IllegalStateException(
                    "El disco actual tiene " + sistema.getDisco().getCantidadBloques() +
                    " bloques, pero el caso necesita al menos " + maxNecesario + "."
            );
        }
    }

    private void limpiarSistema() {
        vaciarDirectorio(sistema.getRoot());
    }

    private void vaciarDirectorio(Directorio dir) {
        while (dir.getHijos().tamano() > 0) {
            NodoFS hijo = dir.getHijos().obtener(0);

            if (hijo.esDirectorio()) {
                vaciarDirectorio((Directorio) hijo);
            }

            dir.eliminarHijoPorNombre(hijo.getNombre());
        }
    }

    private TipoOperacionIO mapearOperacion(String op) {
        String valor = op == null ? "" : op.trim().toUpperCase();

        switch (valor) {
            case "READ":
                return TipoOperacionIO.READ;
            case "UPDATE":
            case "WRITE":
                return TipoOperacionIO.WRITE;
            case "DELETE":
                return TipoOperacionIO.DELETE;
            case "CREATE":
                return TipoOperacionIO.CREATE;
            default:
                throw new IllegalArgumentException("Operación no soportada en JSON: " + op);
        }
    }

    private String resolverNombreArchivoPorPosicion(ListaEnlazada<SystemFileEntry> systemFiles, int pos) {
        for (int i = 0; i < systemFiles.tamano(); i++) {
            SystemFileEntry entry = systemFiles.obtener(i);

            int inicio = entry.getStartPos();
            int fin = inicio + entry.getBlocks() - 1;

            if (pos >= inicio && pos <= fin) {
                return entry.getName();
            }
        }

        return "bloque_" + pos;
    }
}