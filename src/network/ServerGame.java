package network;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.IOException;
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
    private ServerClientHandler[] clients;
    private boolean gameover = false;

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
            while (!gameover) {
                // Comandos vindo dos clientes
                for (ServerClientHandler client : clients) {
                    if (client.getPlayerHash() == clientTurn()) {
                        boolean done;
                        do {
                            String clientCommand = client.dis.readUTF();
                            System.out.println("Client " + client.getPlayerHash() + ": " + clientCommand);

                            done = processCommand(clientCommand, client);
                        } while (!done);

                        passTurn();
                        updateGame();
                    }
                    int winnerHash = game.checkEnd();
                    if (winnerHash != -1) {
                        endGame(winnerHash);
                        for (ServerClientHandler cl2 : clients) {
                            cl2.socket.close();
                        }

                        Thread.currentThread().interrupt();
                    }
                }
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

    // retorna true se acaba rodada do jogador
    private boolean processCommand(String command, ServerClientHandler cl) {
        Scanner scanner = new Scanner(command);
        String line = getNextLine(scanner);
        boolean myTurn = false;

        if (line.equals("PASS {")) {
            int hash = Integer.parseInt(getNextLine(scanner));
            return true;
        }

        if (line.equals("BUY {")) {
            int hash = Integer.parseInt(getNextLine(scanner));
            boolean sucess = game.buyPiece(hash);
            updatePlayerHand(cl, sucess);
            return false;
        }

        if (line.equals("ADD {")) {
            try {
                int hash = Integer.parseInt(getNextLine(scanner));
                int index = Integer.parseInt(getNextLine(scanner));
                int side = Integer.parseInt(getNextLine(scanner));

                boolean result = game.addPiece(side, hash, index);

                if (!result) {
                    cl.sendToClient("ADDFAIL {\n}");
                }

                return result;
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
                        + "\n}";
                System.out.println("Update to " + cl.getPlayerHash() + ":\n" + command);
                cl.sendToClient(command);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updatePlayerHand(ServerClientHandler cl, boolean sucess) {
        try {
            String command = "UPDATEHAND {"
                    + "\n" + sucess
                    + "\n" + (sucess ? game.getPlayerHand(cl.getPlayerHash()) + "\n}" : "\n}");
            System.out.println("UpdateHand to " + cl.getPlayerHash() + ":\n" + command);
            cl.sendToClient(command);
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void endGame(int winnerHash) {
        for (ServerClientHandler cl : clients) {
            try {
                String command = "GAMEOVER {"
                        + "\n" + (cl.getPlayerHash() == winnerHash)
                        + "\n}";
                System.out.println("Update to " + cl.getPlayerHash() + ":\n" + command);
                cl.sendToClient(command);
            } catch (IOException ex) {
                Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        gameover = true;
    }
}
