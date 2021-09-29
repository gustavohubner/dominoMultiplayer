package dominoserver;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
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
    private LinkedList<ServerClientHandler> clients;
    private boolean gameover = false;
    private int passes = 0;

    public ServerGame(LinkedList<ServerClientHandler> clients, Domino game) {
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
        return clients.get(playerTurn).dis;
    }

    public void run() {
        System.out.println("Starting game ...");
        try {
            for (ServerClientHandler client : clients) {
                System.out.println("network.ServerGame.run() " + clients.size());
                int hash = client.getPlayerHash();
                System.out.println("network.ServerGame.run() " + clients.size() + " " + hash);
                // Começa partida
                client.sendToClient("START {"
                        + "\n" + (clientTurn() == hash)
                        + "\n}");

                // COMANDO INICIAL
                client.sendToClient("SETUP {"
                        + "\n" + hash
                        + "\n" + game.getPlayerHand(client.getPlayerHash())
                        + "\n}");
            }
            System.out.println("Game started!");
            updateGame();
            while (!gameover) {
                // Comandos vindo dos clientes
                for (ServerClientHandler client : clients) {
                    int winnerHash = game.checkEnd();
                    if (winnerHash != -1) {
                        closeGame(winnerHash);
                        return;
                    }
                    if (client.getPlayerHash() == clientTurn()) {
                        boolean done;
                        do {
                            String clientCommand = client.dis.readUTF();
                            System.out.println("Client " + client.getPlayerHash() + ": " + clientCommand);

                            done = processCommand(clientCommand, client);

                            if (passes >= clients.size() * 2) {
                                closeGame(-1);
                                return;
                            }
                        } while (!done);

                        passTurn();
                        updateGame();
                    }
                    winnerHash = game.checkEnd();
                    if (winnerHash != -1) {
                        closeGame(winnerHash);
                        return;
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("Erro em ServerGame.run()");
            System.err.println(ex);
        }
    }

    private int getPlayerHash(int turn) {
        return clients.get(turn).getPlayerHash();
    }

    private void passTurn() {
        playerTurn = (playerTurn + 1) % clients.size();
    }

    private int clientTurn() {
        return clients.get(playerTurn).getPlayerHash();
    }

    // retorna true se acaba rodada do jogador
    private boolean processCommand(String command, ServerClientHandler cl) {
        Scanner scanner = new Scanner(command);
        String line = getNextLine(scanner);
        boolean myTurn = false;

        if (line.equals("PASS {")) {
            passes++;
            int hash = Integer.parseInt(getNextLine(scanner));
            return true;
        }

        if (line.equals("BUY {")) {
            int hash = Integer.parseInt(getNextLine(scanner));
            boolean sucess = game.buyPiece(hash);
            updatePlayerHand(cl, sucess);
            passes = 0;
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
                } else {
                    passes = 0;
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
                        + "\n" + game.getPlayerNumString(cl.getPlayerHash())
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

    private void closeGame(int winnerHash) {
        try {
            endGame(winnerHash);
            for (ServerClientHandler cl2 : clients) {
                cl2.socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
