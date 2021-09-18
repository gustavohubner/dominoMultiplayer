/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer.classes;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gusta
 */
public class DominoTable {

    private DominoPiece startPiece;
    private List<DominoPiece> leftSide;
    private List<DominoPiece> rightSide;

    private int pieceCount;

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

    public boolean addLeft(DominoPiece p) {
        DominoPiece top;
        if (pieceCount == 0) {
            setStartPiece(p);
            return true;
        }
        if (leftSide.size() > 0) {
            top = leftSide.get(leftSide.size() - 1);
        } else {
            top = getStartPiece();
        }

        if (p.getA() == top.getA()) {
            p.rotate();
        }
        if (p.getB() == top.getA() || p.getA() == top.getA()) {
            leftSide.add(p);
            pieceCount++;
            return true;
        }

        return false;
    }

    public boolean addRight(DominoPiece p) {
        DominoPiece top;
        if (pieceCount == 0) {
            setStartPiece(p);
            return true;
        }
        if (rightSide.size() > 0) {
            top = rightSide.get(rightSide.size() - 1);
        } else {
            top = getStartPiece();
        }

        if (p.getB() == top.getB()) {
            p.rotate();
        }
        if (p.getA() == top.getB() || p.getB() == top.getB()) {
            rightSide.add(p);
            pieceCount++;
            return true;
        }

        return false;
    }
    
    public int getPieceCount(){
        return pieceCount;
    }

}
