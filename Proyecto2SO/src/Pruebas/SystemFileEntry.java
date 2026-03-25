/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

/**
 *
 * @author Peter
 */

public class SystemFileEntry {
    private int startPos;
    private String name;
    private int blocks;

    public SystemFileEntry() {
    }

    public SystemFileEntry(int startPos, String name, int blocks) {
        this.startPos = startPos;
        this.name = name;
        this.blocks = blocks;
    }

    public int getStartPos() {
        return startPos;
    }

    public String getName() {
        return name;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "SystemFileEntry{" +
                "startPos=" + startPos +
                ", name='" + name + '\'' +
                ", blocks=" + blocks +
                '}';
    }
}
