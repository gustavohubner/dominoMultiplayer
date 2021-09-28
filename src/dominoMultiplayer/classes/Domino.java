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
            for (int j = i; j < 3; j++) {
                pieces.push(new DominoPiece(i, j));
            }
        }
        Collections.shuffle(pieces, new Random(System.currentTimeMillis()));
        table = new DominoTable();

        System.out.println("" + players.toString());
    }

    public DominoPiece getNextPiece() {
        return pieces.pop();
    }

    public int checkEnd() {
        for (int key : players.keySet()) {
            System.out.println("CHECK WIN >>> hash: " + key + "  pieces:" + playerPieces.get(players.get(key)).size() + " win: " + playerPieces.get(players.get(key)).isEmpty());
            if (playerPieces.get(players.get(key)).isEmpty()) {
                return key;
            }
        }
        return -1;
    }

    public int remaining() {
        return pieces.size();
    }

    public int addPlayer() {
        if (players.size() < 4) {
            LinkedList<DominoPiece> hand = new LinkedList<>();
            for (int i = 0; i < 3; i++) {
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
        System.out.println("No more pieces to buy!");
        return false;
    }

    public String getPlayerNumString(int hash) {
        String str = playerPieces.size() + "";
        for (int i = 0; i < playerPieces.size(); i++) {
            if (i != players.get(hash)) {
                str += "\n" + playerPieces.get(i).size();
            }
        }
        return str;
    }

}
