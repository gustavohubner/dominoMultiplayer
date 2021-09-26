package network;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author EmersonPL
 */
public class ClientHandler implements Runnable{
  private Socket socket;
  private DataInputStream dis;
  private DataOutputStream dos;
  private int playerId;
  private Domino game;
  private int turn;
  
  public ClientHandler(Socket s) {
    socket = s;
    
    try {
      dis = new DataInputStream(socket.getInputStream());
      dos = new DataOutputStream(socket.getOutputStream());
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler");
    }
  }
  
  public void setId(int hash) {
    this.playerId = hash;
  }
  
  public void setGame(Domino game) {
    this.game = game;
  }
  
  public void setTurn(int turn) {
    this.turn = turn;
  }
  
  public void run() {
    try {
      dos.writeInt(playerId);
      dos.writeInt(turn);
      
      
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler run()");
    }
  }
}
