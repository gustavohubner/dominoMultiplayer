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
        while (true) {
            try {
                if (!socket.isClosed()) {
                    String command = dis.readUTF();

                    processComand(command);
                } else {
                    return;
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        //TODO: Aqui precisa colocar pra instanciar o cliente só depois de clicar em join, etc...
        Client client = new Client(InetAddress.getByName(ip), port);
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());

        client.run();

        //launch(args);
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

        }

        if (line.equals("QUEUE {")) {
            int queueSize = Integer.parseInt(getNextLine(scanner));

            if (gui != null) {
                gui.setQueue(queueSize);
            }
        }

        if (line.equals("START {")) {
            myTurn = Boolean.parseBoolean(getNextLine(scanner));
            // TODO: tambem é mandado o num de player e pecas de cada um
//             int playernum = Integer.parseInt(getNextLine(scanner));
            gui.startGame();
            gui.setMyTurn(myTurn);
        }

        if (line.equals("UPDATE {")) {
            System.out.println("UPDATE RECEIVED!");
            String start, left, right, hand;
            start = getNextLine(scanner);
            left = getNextLine(scanner);
            right = getNextLine(scanner);
            hand = getNextLine(scanner);

            // ...
            boolean turn = Boolean.parseBoolean(getNextLine(scanner));

            gui.setTableString(start, left, right);
            gui.setHandString(hand);
            gui.setMyTurn(turn);
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

    void buyPiece() {
        try {
            sendToServer("BUY {"
                    + "\n" + hash
                    + "\n}");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void pass() {
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

}
