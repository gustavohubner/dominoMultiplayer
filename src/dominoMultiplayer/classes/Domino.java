/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer.classes;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author gusta
 */
public class Domino {

    private Stack<DominoPiece> pieces;
    private DominoTable table;

    private HashMap<Integer, Integer> players;
    private List<List<DominoPiece>> playerPieces;

    public Domino() {
        pieces = new Stack<>();
        players = new HashMap<>();
        playerPieces = new LinkedList<>();

        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                pieces.push(new DominoPiece(i, j));
            }
        }
        Collections.shuffle(pieces, new Random(System.currentTimeMillis()));
        table = new DominoTable();
        
//        table.setStartPiece(pieces.pop());

        System.out.println("" + players.toString());
    }

    public DominoPiece getNextPiece() {
        return pieces.pop();
    }

    public boolean end() {
        //return pieces.isEmpty();
        return false;
    }

    public int remaining() {
        return pieces.size();
    }

    public int addPlayer() {
        if (players.size() < 4) {
            LinkedList<DominoPiece> hand = new LinkedList<>();
            for (int i = 0; i < 7; i++) {
                hand.add(pieces.pop());
            }
            playerPieces.add(hand);
            int hash = new Random().nextInt() & Integer.MAX_VALUE;
            players.put(hash, playerPieces.size() - 1);

            return hash;
        }
        return -1;
    }

    public DominoPiece getStartPiece() {
        return table.getStartPiece();
    }

    public List<DominoPiece> getLeftSide() {
        return table.getLeftSide();
    }

    public List<DominoPiece> getRightSide() {
        return table.getRightSide();
    }

    public boolean addPiece(int side, int hash, int pieceIndex) {
        int index = players.get(hash);
        if (players.get(hash) != null) {
            List<DominoPiece> hand = playerPieces.get(index);
            DominoPiece p = hand.get(pieceIndex);
            boolean response;
            if (side == 0) {
                response = table.addLeft(p);
            } else {
                response = table.addRight(p);
            }

            if (response) {
                hand.remove(p);
                return true;
            }
        }
        return false;
    }

    public LinkedList<DominoPiece> getPlayerHand(int hash) {
        if (players.get(hash) != null) {
            int index = players.get(hash);
            return (LinkedList<DominoPiece>) playerPieces.get(index);
        }
        return null;
    }

    public boolean buyPiece(int hash) {
        if (players.get(hash) != null) {
            int index = players.get(hash);
            if (pieces.size() > 0) {
                playerPieces.get(index).add(pieces.pop());
                return true;
            }

        }
        System.out.println("ACOBOU AS PEÇA!!!)");
        return false;
    }

    public String getPlayerNumString() {
        String str = playerPieces.size()+"";
        for (List<DominoPiece> player : playerPieces){
            str += "\n"+player.size();
        }
        return str;
    }

}
