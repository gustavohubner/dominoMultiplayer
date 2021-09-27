/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.Domino;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import network.ClientHandler;
import network.ServerGame;
import sun.misc.Queue;

/**
 *
 * @author gusta
 */
public class Server {

    private static final int MAX_PLAYERS = 2;
    private int numPlayers;
    // gameList // lista com todos jogos atuais;
    private LinkedList<ClientHandler> playerList; // lista com os players em cada jogo/esperando...

    private ServerSocket server;
    private FXMLDocumentController gui;

    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(42069, 1, ipAddress);
        numPlayers = 0;
        playerList = new LinkedList<ClientHandler>();
    }

    public Server(InetAddress ipAddress, FXMLDocumentController gui) throws Exception {
        this.gui = gui;
        this.server = new ServerSocket(42069, 1, ipAddress);
        playerList = new LinkedList<ClientHandler>();
    }

    private void listen() throws Exception {
        while (true) {
            while (playerList.size() < MAX_PLAYERS) {
                Socket client = this.server.accept();
                String clientAddress = client.getInetAddress().getHostAddress();
                System.out.println("\r\nNew connection from " + clientAddress);

                ClientHandler c = new ClientHandler(client);
                playerList.add(c);

                for (ClientHandler c1 : playerList) {
                    if (c1.socket.isClosed()) {
                        System.err.println("Connection lost!");
                        playerList.remove(c1);
                    }
                }

                for (ClientHandler c1 : playerList) {
                    c1.sendToClient("QUEUE {\n" + playerList.size() + "\n}");
                }

                System.out.println("\r\nNum Players: " + playerList.size() + "/" + MAX_PLAYERS);
            }

            Domino game = new Domino();
            int i = 0;
            ClientHandler[] clients = new ClientHandler[MAX_PLAYERS];

            while (!playerList.isEmpty()) {
                ClientHandler ch = playerList.pop();
                ch.setHash(game.addPlayer());
                clients[i] = ch;
                i++;
            }

            ServerGame sg = new ServerGame(clients, game);
            Thread t = new Thread(sg);
            t.start();

            numPlayers = 0;
        }

    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        Server app = new Server(InetAddress.getByName("localhost"));
        System.out.println("\r\nRunning Server: "
                + "Host=" + app.getSocketAddress().getHostAddress()
                + " Port=" + app.getPort());

        app.listen();
    }
}
