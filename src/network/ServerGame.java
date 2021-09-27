package network;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author EmersonPL
 */
public class ServerGame implements Runnable {

    private Domino game;
    private int playerTurn;
    private int lastPlayed;
    private ClientHandler[] clients;
    private DataInputStream dis;

    private String code;
    private String action;

    public ServerGame(ClientHandler[] clients, Domino game) {
        this.clients = clients;
        this.game = game;
        this.playerTurn = 0;
    }

    public void setGame(Domino game) {
        this.game = game;
    }

    public void setTurn(int turn) {
        this.playerTurn = turn;
    }

    private DataInputStream getDISCurrentTurn() {
        return clients[playerTurn].dis;
    }

    public void run() {
        System.out.println("Starting game ...");
        try {
            for (ClientHandler client : clients) {
                int hash = client.getPlayerHash();
                // Começa partida
                client.sendToClient("START {"
                        + "\n" + (clientTurn().equals(hash + ""))
                        + "\n" + game.getPlayerNumString()
                        + "\n}");
                // COMANDO INICIAL
                client.sendToClient("SETUP {"
                        + "\n" + hash
                        + "\n" + game.getPlayerHand(client.getPlayerHash())
                        + "\n}");

            }
            System.out.println("Game started!");
            while (!game.end()) {
                // jogo ...
                break;
            }
        } catch (IOException ex) {
            System.err.println("Erro em ServerGame.run()");
            System.err.println(ex);
        }
    }

    private int getPlayerHash(int turn) {
        return clients[turn].getPlayerHash();
    }

    private void passTurn() {
        playerTurn = (playerTurn + 1) % clients.length;
    }

    private String clientTurn() {
        return Integer.toString(clients[playerTurn].getPlayerHash());
    }

    private void sendAction(String code, String action) throws IOException {
        for (ClientHandler client : clients) {
            // if (client.getPlayerId() == lastPlayed) continue;
            client.dos.writeUTF(code + " " + action);
        }
    }

    private String executeCommand(String code, String action) {
        switch (code) {
            case "PASS":
                //pass
                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " Passou o turno");
                passTurn();
                action = clientTurn();
                break;
            case "BUY":
                //buy Piece
                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " Comprou peça");
                break;
            case "PLACE":
                //jogou
                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " jogou: " + action);
                passTurn();
                break;
            default:
                break;
        }

        return action;
    }
}
