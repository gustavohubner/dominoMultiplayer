/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {

    Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private int hash;
    private String hand;

    FXMLDocumentController gui;

    @Override
    public void run() {
        try {
            while (true) {
                String command = dis.readUTF();
                System.out.println("Server Command: " + command);
                processComand(command);
            }
        } catch (Exception ex) {
            System.err.println("Comnection Error!");
        }

    }

    Client(InetAddress serverAddress, int serverPort, FXMLDocumentController gui) {
        try {
            this.socket = new Socket(serverAddress, serverPort);
            this.gui = gui;

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            gui.connectionSucess();
        } catch (IOException ex) {
            gui.connectionError(serverAddress.toString());
        }
    }

    Client(InetAddress serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        String ip = "localhost";
        int port = 42069;

        Client client = new Client(InetAddress.getByName(ip), port);
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());

        client.run();
    }

    private void processComand(String command) {
        Scanner scanner = new Scanner(command);
        String line = getNextLine(scanner);
        boolean myTurn = false;

        if (line.equals("SETUP {")) {
            hash = Integer.parseInt(getNextLine(scanner));
            hand = getNextLine(scanner);

            if (gui != null) {
                gui.setHash(hash);
                gui.setHandString(hand);
            }
            gui.drawGrid();
        }

        if (line.equals("QUEUE {")) {
            int queueSize = Integer.parseInt(getNextLine(scanner));

            if (gui != null) {
                gui.setQueue(queueSize);
            }
        }

        if (line.equals("START {")) {
            myTurn = Boolean.parseBoolean(getNextLine(scanner));
            gui.startGame();
            gui.setMyTurn(myTurn);
        }

        if (line.equals("UPDATE {")) {
            System.out.println("UPDATE RECEIVED!");
            String start, left, right, handStr;
            start = getNextLine(scanner);
            left = getNextLine(scanner);
            right = getNextLine(scanner);
            handStr = getNextLine(scanner);
            boolean turn = Boolean.parseBoolean(getNextLine(scanner));

            String players = "Other Players: ";

            int numPlayer = Integer.parseInt(getNextLine(scanner));
            for (int i = 0; i < numPlayer - 1; i++) {
                int x = Integer.parseInt(getNextLine(scanner));
                players += "\nPlayer " + (i + 1) + ": " + x + (x > 1 ? " Domino pieces" : " Domino piece");
            }
            gui.setPlayersStatus(players);
            gui.setTableString(start, left, right);
            gui.setHandString(handStr);
            gui.setMyTurn(turn);
            gui.drawGrid();
        }

        if (line.equals("UPDATEHAND {")) {
            String newHand;
            boolean sucess;
            sucess = Boolean.parseBoolean(getNextLine(scanner));
            System.out.println("UPDATEHAND RECEIVED: " + (sucess ? "Success" : "Fail"));
            if (sucess) {
                newHand = getNextLine(scanner);
                gui.setHandString(newHand);
            } else {
                gui.showAlert("Attention", "No more domino pieces to buy!");
            }
            gui.drawGrid();
        }
        if (line.equals("GAMEOVER {")) {
            gui.setMyTurn(false);
            boolean winnerHash = Boolean.parseBoolean(getNextLine(scanner));
            System.out.println("win " + winnerHash);
            if (winnerHash) {
                gui.gameover("You won!");
            } else {
                gui.gameover("You lost!");
            }
            scanner.close();
            return;
        }

        scanner.close();
    }

    private String getNextLine(Scanner scanner) {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception ex) {
            System.out.println("dominoMultiplayer.Client.close() Error");
        }
    }

    public void buyPiece() {
        try {
            sendToServer("BUY {"
                    + "\n" + hash
                    + "\n}");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pass() {
        try {
            sendToServer("PASS {"
                    + "\n" + hash
                    + "\n}");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendToServer(String command) throws IOException {
        dos.writeUTF(command);
    }

    void addPiece(int selecIndex, int selecSide) {
        try {
            sendToServer("ADD {"
                    + "\n" + hash
                    + "\n" + selecIndex
                    + "\n" + selecSide
                    + "\n}");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
