/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.Domino;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Queue<ClientHandler> playerList; // lista com os players em cada jogo/esperando...
    
    private ServerSocket server;
    
    public Server(String ipAddress) throws Exception {
        this.server = new ServerSocket(42069,1,InetAddress.getByName(ipAddress));
        numPlayers = 0;
        playerList = new Queue<>();
    }
    
    private void listen() throws Exception {
      while (true) {
        while (numPlayers < MAX_PLAYERS) { 
          Socket client = this.server.accept();
          String clientAddress = client.getInetAddress().getHostAddress();
          System.out.println("\r\nNew connection from " + clientAddress);

          ClientHandler c = new ClientHandler(client);
          playerList.enqueue(c);

          numPlayers++;
          System.out.println("\r\nNum Players: " + numPlayers + "/" + MAX_PLAYERS);
        }

        Domino game = new Domino();
        int playerTurn = 0;
        int i = 0;
        ClientHandler[] clients = new ClientHandler[MAX_PLAYERS];
        
        while (!playerList.isEmpty()) {
          System.out.println("dequeue");
          ClientHandler ch = playerList.dequeue();

          int hash = game.addPlayer();
          ch.setId(hash);
          
          if (i == 0) {
            playerTurn = hash;
          }
          clients[i] = ch;
          i++;
        }
        
        ServerGame sg = new ServerGame(clients, game);
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
        Server app = new Server("localhost");
        System.out.println("\r\nRunning Server: " + 
                "Host=" + app.getSocketAddress().getHostAddress() + 
                " Port=" + app.getPort());
        
        app.listen();
    }
}