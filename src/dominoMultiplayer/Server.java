/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.Domino;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.ServerClientHandler;
import network.ServerGame;

/**
 *
 * @author gusta
 */
public class Server {

    private static final int MAX_PLAYERS = 4;

    // gameList // lista com todos jogos atuais;
    private LinkedList<ServerClientHandler> playerList; // lista com os players em cada jogo/esperando...

    private ServerSocket server;
    private FXMLDocumentController gui;
    private boolean joined = true;
    private boolean playerQuit;

    public Server(InetAddress ipAddress) throws Exception {
        this.server = new ServerSocket(42069, 1, ipAddress);
        playerList = new LinkedList<ServerClientHandler>();
    }

    public Server(InetAddress ipAddress, FXMLDocumentController gui) throws Exception {
        this.gui = gui;
        this.server = new ServerSocket(42069, 1, ipAddress);
        playerList = new LinkedList<ServerClientHandler>();
    }

    public void listen() throws Exception {
        while (true) {
            playerQuit = false;
            Thread t2;
            while (joined || !(playerList.size() >= 2) || playerQuit) {
                playerQuit = false;
                t2 = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Socket client = server.accept();
                            String clientAddress = client.getInetAddress().getHostAddress();
                            System.out.println("\r\nNew connection from " + clientAddress);

                            ServerClientHandler c = new ServerClientHandler(client);
                            playerList.add(c);
                            for (ServerClientHandler c12 : playerList) {
                                c12.sendToClient("QUEUE {\n" + playerList.size() + "\n}");
                            }

                            joined = true;
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                joined = false;
                t2.start();

                Thread.sleep(1000);
                checkConection();
                Thread.sleep(1000);
                checkConection();
                Thread.sleep(1000);
                checkConection();
                Thread.sleep(1000);
                checkConection();
                t2.join(1000);
                t2.interrupt();
                checkConection();

                System.out.println("\r\nNum Players: " + playerList.size() + "/" + MAX_PLAYERS);
            }

            Domino game = new Domino();

            LinkedList<ServerClientHandler> clients = new LinkedList<>();

            while (!playerList.isEmpty()) {
                ServerClientHandler ch = playerList.pop();
                ch.setHash(game.addPlayer());
                clients.add(ch);
            }
            ServerGame sg = new ServerGame(clients, game);
            Thread t = new Thread(sg);
            t.start();
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                System.out.println("Address: " + args[0]);
                Server app = new Server(InetAddress.getByName("26.168.167.45"));
                System.out.println("\r\nRunning Server: "
                        + "Host=" + app.getSocketAddress().getHostAddress()
                        + " Port=" + app.getPort());
                app.listen();
            } catch (Exception ex) {
                System.out.println("Error while launching server!");
            }
        } else {
            System.out.println("Please specify a valid address!");
        }

    }

    private void checkConection() throws IOException {
        for (ServerClientHandler c1 : playerList) {
            try {
                c1.sendToClient("PING {\n}");
            } catch (IOException ex) {
                playerList.remove(c1);
                playerQuit = true;
                System.out.println("Player " + c1.getPlayerHash() + " disconnected!");
                for (ServerClientHandler c12 : playerList) {
                    c12.sendToClient("QUEUE {\n" + playerList.size() + "\n}");
                }
            }
        }
    }

    public void close() throws IOException {
        server.close();
    }
}
