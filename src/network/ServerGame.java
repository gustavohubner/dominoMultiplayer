package network;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EmersonPL
 */
public class ServerGame implements Runnable {

    private Domino game;
    private int playerTurn;
    private int lastPlayed;
    private ServerClientHandler[] clients;
    private DataInputStream dis;

    public ServerGame(ServerClientHandler[] clients, Domino game) {
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
            for (ServerClientHandler client : clients) {
                int hash = client.getPlayerHash();
                // Começa partida
                client.sendToClient("START {"
                        + "\n" + (clientTurn() == hash)
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
                // Comandos vindo dos clientes
                for (ServerClientHandler client : clients) {
                    if (client.getPlayerHash() == clientTurn()) {
                        boolean done;
                        do {
                            String clientCommand = client.dis.readUTF();
                            System.err.println("Client " + client.getPlayerHash() + ": " + clientCommand);

                            done = processCommand(clientCommand);
                        } while (!done);

                        if (done) {
                            updateGame();
                        }
                        break;
                    }
                }

                // update os players
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

    private int clientTurn() {
        return clients[playerTurn].getPlayerHash();
    }

    private void sendAction(String code, String action) throws IOException {
        for (ServerClientHandler client : clients) {
            // if (client.getPlayerId() == lastPlayed) continue;
            client.dos.writeUTF(code + " " + action);
        }
    }

    // retorna true se acaba rodada do jogador
    private boolean processCommand(String command) {
        Scanner scanner = new Scanner(command);
        String line = getNextLine(scanner);
        boolean myTurn = false;

        if (line.equals("PASS {")) {
            int hash = Integer.parseInt(getNextLine(scanner));
            passTurn();
            return true;
        }
//        switch (code) {
//            case "PASS":
//                //pass
//                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " Passou o turno");
//                passTurn();
//                break;
//            case "BUY":
//                //buy Piece
//                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " Comprou peça");
//                break;
//            case "PLACE":
//                //jogou
//                System.out.println("Player: " + clients[playerTurn].getPlayerHash() + " jogou: " + action);
//                passTurn();
//                break;
//            default:
//                break;
//        }
//
//        return action;
        return false;
    }

    private String getNextLine(Scanner scanner) {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    private void updateGame() {
        for (ServerClientHandler cl : clients) {
            try {
                String command = "UPDATE {"
                        + "\n" + game.getStartPiece()
                        + "\n" + game.getLeftSide()
                        + "\n" + game.getRightSide()
                        + "\n" + game.getPlayerHand(cl.getPlayerHash())
                        + "\n" + (clientTurn() == cl.getPlayerHash())
                        + "\n" // mais coisas ...
                        + "\n}";
                System.out.println("Update to " + cl.getPlayerHash() +"\n"+command);
                cl.sendToClient(command);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
