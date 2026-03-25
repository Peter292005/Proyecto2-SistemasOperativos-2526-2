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

public class CasoPrueba {
    private String testId;
    private int initialHead;
    private String direction;
    private ListaEnlazada<RequestSpec> requests;
    private ListaEnlazada<SystemFileEntry> systemFiles;

    public CasoPrueba() {
        this.requests = new ListaEnlazada<>();
        this.systemFiles = new ListaEnlazada<>();
        this.direction = "UP";
    }

    public String getTestId() {
        return testId;
    }

    public int getInitialHead() {
        return initialHead;
    }

    public String getDirection() {
        return direction == null ? "UP" : direction;
    }

    public ListaEnlazada<RequestSpec> getRequests() {
        return requests;
    }

    public ListaEnlazada<SystemFileEntry> getSystemFiles() {
        return systemFiles;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setInitialHead(int initialHead) {
        this.initialHead = initialHead;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setRequests(ListaEnlazada<RequestSpec> requests) {
        this.requests = requests;
    }

    public void setSystemFiles(ListaEnlazada<SystemFileEntry> systemFiles) {
        this.systemFiles = systemFiles;
    }
}
