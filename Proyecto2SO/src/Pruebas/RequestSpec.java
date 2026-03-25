/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

/**
 *
 * @author Peter
 */
public class RequestSpec {
    private int pos;
    private String op;

    public RequestSpec() {
    }

    public RequestSpec(int pos, String op) {
        this.pos = pos;
        this.op = op;
    }

    public int getPos() {
        return pos;
    }

    public String getOp() {
        return op;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setOp(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return "RequestSpec{" +
                "pos=" + pos +
                ", op='" + op + '\'' +
                '}';
    }
}
