/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer.classes;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author gusta
 */
public class DominoTable {

    private DominoPiece startPiece;
    private List<DominoPiece> leftSide;
    private List<DominoPiece> rightSide;
    
    
    public int pieceCount;

    public DominoTable() {
        rightSide = new LinkedList<>();
        leftSide = new LinkedList<>();
        pieceCount = 0;
    }

    public DominoPiece getStartPiece() {
        return startPiece;
    }

    public void setStartPiece(DominoPiece startPiece) {
        this.startPiece = startPiece;
        pieceCount++;
    }

    public List<DominoPiece> getLeftSide() {
        return leftSide;
    }

    public List<DominoPiece> getRightSide() {
        return rightSide;
    }

    public void addLeft(DominoPiece p) {
        leftSide.add(p);
        pieceCount++;
    }

    public void addRight(DominoPiece p) {
        rightSide.add(p);
        pieceCount++;
    }

}
