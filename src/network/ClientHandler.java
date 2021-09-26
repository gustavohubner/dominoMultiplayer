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
  
  public ClientHandler(Socket s, int id) {
    socket = s;
    playerId = id;
    
    try {
      dis = new DataInputStream(socket.getInputStream());
      dos = new DataOutputStream(socket.getOutputStream());
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler");
    }
  }
  
  public void run() {
    try {
      dos.writeInt(playerId);
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler run()");
    }
  }
}
