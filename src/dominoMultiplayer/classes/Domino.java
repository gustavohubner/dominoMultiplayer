/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer.classes;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author gusta
 */
public class Domino {
    private final Stack<DominoPiece> pieces;
    
    public Domino(){
        pieces = new Stack<>();
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                pieces.push(new DominoPiece(i, j));  
            }
        }
        Collections.shuffle(pieces, new Random(System.currentTimeMillis()));
    }
    
    public DominoPiece getNextPiece(){
        return pieces.pop();
    } 
    public boolean end(){
        return pieces.isEmpty();
    }
    public int remaining(){
        return pieces.size();
    }
}
