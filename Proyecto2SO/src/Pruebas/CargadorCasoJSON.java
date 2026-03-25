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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CargadorCasoJSON {

    public CasoPrueba cargarDesdeArchivo(String rutaArchivo) throws IOException {
        String json = Files.readString(Path.of(rutaArchivo));

        CasoPrueba caso = new CasoPrueba();

        caso.setTestId(extraerString(json, "test_id"));
        caso.setInitialHead(extraerInt(json, "initial_head"));

        String direction = extraerStringOpcional(json, "direction");
        caso.setDirection(direction == null ? "UP" : direction);

        ListaEnlazada<RequestSpec> requests = extraerRequests(json);
        caso.setRequests(requests);

        ListaEnlazada<SystemFileEntry> systemFiles = extraerSystemFiles(json);
        caso.setSystemFiles(systemFiles);

        return caso;
    }

    private String extraerString(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = json.indexOf(patron);
        if (idx == -1) {
            throw new IllegalArgumentException("No se encontró la clave: " + clave);
        }

        int dosPuntos = json.indexOf(":", idx);
        int comillaInicio = json.indexOf("\"", dosPuntos + 1);
        int comillaFin = json.indexOf("\"", comillaInicio + 1);

        return json.substring(comillaInicio + 1, comillaFin).trim();
    }

    private String extraerStringOpcional(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = json.indexOf(patron);
        if (idx == -1) {
            return null;
        }

        int dosPuntos = json.indexOf(":", idx);
        int comillaInicio = json.indexOf("\"", dosPuntos + 1);
        int comillaFin = json.indexOf("\"", comillaInicio + 1);

        return json.substring(comillaInicio + 1, comillaFin).trim();
    }

    private int extraerInt(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = json.indexOf(patron);
        if (idx == -1) {
            throw new IllegalArgumentException("No se encontró la clave: " + clave);
        }

        int dosPuntos = json.indexOf(":", idx);
        int inicio = dosPuntos + 1;

        while (inicio < json.length() && Character.isWhitespace(json.charAt(inicio))) {
            inicio++;
        }

        int fin = inicio;
        while (fin < json.length() &&
                (Character.isDigit(json.charAt(fin)) || json.charAt(fin) == '-')) {
            fin++;
        }

        return Integer.parseInt(json.substring(inicio, fin).trim());
    }

    private ListaEnlazada<RequestSpec> extraerRequests(String json) {
        ListaEnlazada<RequestSpec> lista = new ListaEnlazada<>();

        int idxRequests = json.indexOf("\"requests\"");
        if (idxRequests == -1) {
            return lista;
        }

        int inicioArray = json.indexOf("[", idxRequests);
        int finArray = encontrarCierre(json, inicioArray, '[', ']');

        String bloque = json.substring(inicioArray + 1, finArray).trim();

        int pos = 0;
        while (pos < bloque.length()) {
            int inicioObj = bloque.indexOf("{", pos);
            if (inicioObj == -1) break;

            int finObj = encontrarCierre(bloque, inicioObj, '{', '}');
            String obj = bloque.substring(inicioObj + 1, finObj);

            int posicion = extraerIntDesdeObjeto(obj, "pos");
            String op = extraerStringDesdeObjeto(obj, "op");

            lista.agregar(new RequestSpec(posicion, op));

            pos = finObj + 1;
        }

        return lista;
    }

    private ListaEnlazada<SystemFileEntry> extraerSystemFiles(String json) {
        ListaEnlazada<SystemFileEntry> lista = new ListaEnlazada<>();

        int idxFiles = json.indexOf("\"system_files\"");
        if (idxFiles == -1) {
            return lista;
        }

        int inicioObj = json.indexOf("{", idxFiles);
        int finObj = encontrarCierre(json, inicioObj, '{', '}');

        String bloque = json.substring(inicioObj + 1, finObj).trim();

        int pos = 0;
        while (pos < bloque.length()) {
            int comillaClave1 = bloque.indexOf("\"", pos);
            if (comillaClave1 == -1) break;

            int comillaClave2 = bloque.indexOf("\"", comillaClave1 + 1);
            String clave = bloque.substring(comillaClave1 + 1, comillaClave2).trim();

            int dosPuntos = bloque.indexOf(":", comillaClave2);
            int inicioSubObj = bloque.indexOf("{", dosPuntos);
            int finSubObj = encontrarCierre(bloque, inicioSubObj, '{', '}');

            String obj = bloque.substring(inicioSubObj + 1, finSubObj);

            String name = extraerStringDesdeObjeto(obj, "name");
            int blocks = extraerIntDesdeObjeto(obj, "blocks");
            int startPos = Integer.parseInt(clave);

            lista.agregar(new SystemFileEntry(startPos, name, blocks));

            pos = finSubObj + 1;
        }

        return lista;
    }

    private String extraerStringDesdeObjeto(String obj, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = obj.indexOf(patron);
        if (idx == -1) {
            throw new IllegalArgumentException("No se encontró la clave: " + clave);
        }

        int dosPuntos = obj.indexOf(":", idx);
        int comillaInicio = obj.indexOf("\"", dosPuntos + 1);
        int comillaFin = obj.indexOf("\"", comillaInicio + 1);

        return obj.substring(comillaInicio + 1, comillaFin).trim();
    }

    private int extraerIntDesdeObjeto(String obj, String clave) {
        String patron = "\"" + clave + "\"";
        int idx = obj.indexOf(patron);
        if (idx == -1) {
            throw new IllegalArgumentException("No se encontró la clave: " + clave);
        }

        int dosPuntos = obj.indexOf(":", idx);
        int inicio = dosPuntos + 1;

        while (inicio < obj.length() && Character.isWhitespace(obj.charAt(inicio))) {
            inicio++;
        }

        int fin = inicio;
        while (fin < obj.length() &&
                (Character.isDigit(obj.charAt(fin)) || obj.charAt(fin) == '-')) {
            fin++;
        }

        return Integer.parseInt(obj.substring(inicio, fin).trim());
    }

    private int encontrarCierre(String texto, int inicio, char abre, char cierra) {
        int contador = 0;

        for (int i = inicio; i < texto.length(); i++) {
            char ch = texto.charAt(i);

            if (ch == abre) {
                contador++;
            } else if (ch == cierra) {
                contador--;
                if (contador == 0) {
                    return i;
                }
            }
        }

        throw new IllegalArgumentException("No se encontró cierre para " + abre);
    }
}