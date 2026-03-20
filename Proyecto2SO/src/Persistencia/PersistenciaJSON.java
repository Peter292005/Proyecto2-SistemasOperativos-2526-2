/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author Peter
 */
import Filesystem.BloqueDisco;
import Filesystem.Disco;
import Journal.EntradaJournal;
import Journal.JournalManager;

import java.io.FileWriter;
import java.io.IOException;

public class PersistenciaJSON {

    public void guardarEstado(String rutaArchivo, Disco disco, JournalManager journal) throws IOException {
        FileWriter writer = new FileWriter(rutaArchivo);

        writer.write("{\n");

        writer.write("  \"disco\": {\n");
        writer.write("    \"cantidadBloques\": " + disco.getCantidadBloques() + ",\n");
        writer.write("    \"bloques\": [\n");

        for (int i = 0; i < disco.getBloques().tamano(); i++) {
            BloqueDisco bloque = disco.getBloques().obtener(i);

            writer.write("      {\n");
            writer.write("        \"indice\": " + bloque.getIndice() + ",\n");
            writer.write("        \"libre\": " + bloque.estaLibre() + ",\n");
            writer.write("        \"nombreArchivo\": " + valorJSON(bloque.getNombreArchivo()) + ",\n");
            writer.write("        \"siguienteBloque\": " + bloque.getSiguienteBloque() + "\n");
            writer.write("      }");

            if (i < disco.getBloques().tamano() - 1) {
                writer.write(",");
            }
            writer.write("\n");
        }

        writer.write("    ]\n");
        writer.write("  },\n");

        writer.write("  \"journal\": [\n");

        for (int i = 0; i < journal.getEntradas().tamano(); i++) {
            EntradaJournal entrada = journal.getEntradas().obtener(i);

            writer.write("    {\n");
            writer.write("      \"idEntrada\": " + entrada.getIdEntrada() + ",\n");
            writer.write("      \"operacion\": \"" + entrada.getOperacion() + "\",\n");
            writer.write("      \"nombreArchivo\": \"" + entrada.getNombreArchivo() + "\",\n");
            writer.write("      \"propietario\": \"" + entrada.getPropietario() + "\",\n");
            writer.write("      \"tamanoEnBloques\": " + entrada.getTamanoEnBloques() + ",\n");
            writer.write("      \"primerBloque\": " + entrada.getPrimerBloque() + ",\n");
            writer.write("      \"estado\": \"" + entrada.getEstado() + "\"\n");
            writer.write("    }");

            if (i < journal.getEntradas().tamano() - 1) {
                writer.write(",");
            }
            writer.write("\n");
        }

        writer.write("  ]\n");
        writer.write("}\n");

        writer.close();
    }

    private String valorJSON(String valor) {
        return valor == null ? "null" : "\"" + valor + "\"";
    }
}