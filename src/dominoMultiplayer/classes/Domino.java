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
    private DominoTable table;

    public Domino() {
        pieces = new Stack<>();
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                pieces.push(new DominoPiece(i, j));
            }
        }
        
        for (int i = 0; i < 3; i++) {
            pieces.push(new DominoPiece());
        }

        Collections.shuffle(pieces, new Random(System.currentTimeMillis()));
        table = new DominoTable();

        table.setStartPiece(new DominoPiece());
    }

    public DominoTable getTable() {
        return table;
    }

    public DominoPiece getNextPiece() {
        return pieces.pop();
    }

    public boolean end() {
        return pieces.isEmpty();
    }

    public int remaining() {
        return pieces.size();
    }
}
