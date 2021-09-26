package network;

import dominoMultiplayer.classes.Domino;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author EmersonPL
 */
public class ServerGame implements Runnable{
  private Domino game;
  private int playerTurn;
  private ClientHandler[] clients;
  private DataInputStream dis;
  
  private int code;
  private String action;
  
  public ServerGame(ClientHandler[] clients, Domino game, int startPlayer) {
    this.clients = clients;
    this.game = game;
    this.playerTurn = getStartingPlayer(startPlayer);
  }
  
  public void setGame(Domino game) {
    this.game = game;
  }
  
  public void setTurn(int turn) {
    this.playerTurn = turn;
  }
  
  public int getStartingPlayer(int start) {
    for (int i = 0; i < clients.length; i++) {
      if (clients[i].getPlayerId() == start) {
        return i;
      }
    }
    
    return 0;
  }
  
  private DataInputStream getDISCurrentTurn() {
    return clients[playerTurn].dis;
  }
  
  public void run() {
    try {
      for (ClientHandler client : clients) {
        client.dos.writeInt(client.getPlayerId());
        client.dos.writeInt(playerTurn);
      }
      
      while (true) {
        dis = getDISCurrentTurn();
        code = dis.readInt();
        action = dis.readUTF();
        receiveAction(code, action);
        
        sendAction(code, action);
      }
    } catch (IOException ex) {
      System.err.println("Erro em ServerGame.run()");
    }
  }
  
  private void passTurn() {
    playerTurn = (playerTurn + 1) % clients.length;
  }
  
  private void sendAction(int code, String action) throws IOException{
    for (ClientHandler client : clients) {
      client.dos.writeInt(code);
      client.dos.writeUTF(action);
    }
  }
  
  private void receiveAction(int code, String action) {
    if (code == 0) { //pass
      passTurn();
    } else if (code == 1) { //buy Piece
      System.out.println("Comprou peça");
    } else if (code == 2) { //jogou
      System.out.println(action);
      passTurn();
    }
  } 
}
