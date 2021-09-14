/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer.classes;

import java.util.Random;

/**
 *
 * @author gusta
 */
public class DominoPiece {

    private int a;
    private int b;

    public DominoPiece(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public DominoPiece() {
        this.a = new Random().nextInt(7);
        this.b = new Random().nextInt(7);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

}
