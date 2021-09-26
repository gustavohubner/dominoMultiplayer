package network;

import dominoMultiplayer.classes.Domino;
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
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private int playerId;
  
  public ClientHandler(Socket s, int id) {
    socket = s;
    playerId = id;
    
    try {
      System.out.println("ASAAAASDSADFASDSAD");
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBB");
      oos = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCC");
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler");
    }
  }
  
  public void setGame(Domino game) {
    try {
      oos.writeObject(game);
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler setGame()");
    }
  }
  
  public void setTurn(int turn) {
    try {
      oos.writeObject(turn);
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler setGame()");
    }
  }
  
  public void run() {
    try {
      oos.writeInt(playerId);
    } catch (IOException ex) {
      System.err.println("Falha no ClientHandler run()");
    }
  }
}
