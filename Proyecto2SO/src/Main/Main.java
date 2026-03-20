/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

/**
 *
 * @author Peter
 */
import Filesystem.Archivo;
import Filesystem.Disco;
import Journal.EntradaJournal;
import Journal.JournalManager;
import Persistencia.PersistenciaJSON;
import Procesos.TipoOperacionIO;

public class Main {
    public static void main(String[] args) {
        try {
            Disco disco = new Disco(10);
            JournalManager journal = new JournalManager();
            PersistenciaJSON persistencia = new PersistenciaJSON();

            Archivo archivo = new Archivo("reporte.txt", "peter", 3);

            boolean asignado = disco.asignarBloquesAArchivo(archivo);
            System.out.println("Archivo asignado: " + asignado);
            System.out.println("Primer bloque: " + archivo.getPrimerBloque());
            System.out.println("Cadena: " + disco.obtenerCadenaBloques(archivo));

            EntradaJournal entrada = journal.registrarOperacionPendiente(TipoOperacionIO.CREATE, archivo);
            entrada.setPrimerBloque(archivo.getPrimerBloque());

            System.out.println("Journal antes de recuperación:");
            System.out.println(journal);

            // Simulación de fallo: NO confirmamos la operación

            journal.recuperarOperacionesPendientes(disco);

            System.out.println("Journal después de recuperación:");
            System.out.println(journal);
            System.out.println("Bloques libres después de recuperar: " + disco.contarBloquesLibres());

            persistencia.guardarEstado("estado_simulador.json", disco, journal);
            System.out.println("Estado guardado en JSON correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}