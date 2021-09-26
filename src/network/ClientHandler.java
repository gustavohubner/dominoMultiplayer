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
public class ClientHandler{
  private Socket socket;
  public DataInputStream dis;
  public DataOutputStream dos;
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
  
  public void setTurn(int turn) {
    this.turn = turn;
  }
  
  public int getPlayerId() {
    return playerId;
  }
  
  /*public void receiveAction(int code, String action) {
    if (code == 0) {
      dos.writeInt(code);
      dos.writeUTF(action);
    }
  }*/
}
