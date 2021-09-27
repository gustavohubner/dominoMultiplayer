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
  private int lastPlayed;
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
    System.out.println("Começou a rodar");
    try {
      for (ClientHandler client : clients) {
        client.dos.writeInt(client.getPlayerId());
        client.dos.writeInt(Integer.parseInt(clientTurn())); //TODO: Mudar pra não precisar ficar trocando Int -> Str -> Int
      }
      
      while (true) {
        dis = getDISCurrentTurn();
        code = dis.readInt();
        action = dis.readUTF();       
        lastPlayed = getPlayerHash(playerTurn);
        action = receiveAction(code, action);
    
        sendAction(code, action);
      }
    } catch (IOException ex) {
      System.err.println("Erro em ServerGame.run()");
      System.err.println(ex);
    }
  }
  
  private int getPlayerHash(int turn) {
    return clients[turn].getPlayerId();
  }
  
  private void passTurn() {
    playerTurn = (playerTurn + 1) % clients.length;
  }
  
  private String clientTurn() {
    return Integer.toString(clients[playerTurn].getPlayerId());
  }
  
  private void sendAction(int code, String action) throws IOException{
    for (ClientHandler client : clients) {
      // if (client.getPlayerId() == lastPlayed) continue;
      
      client.dos.writeInt(code);
      client.dos.writeUTF(action);
    }
  }
  
  private String receiveAction(int code, String action) {
    switch (code) {
      case 0:
        //pass
        System.out.println("Player: " + clients[playerTurn].getPlayerId() + " Passou o turno");
        passTurn();
        action = clientTurn();
        break;
      case 1:
        //buy Piece
        System.out.println("Player: " + clients[playerTurn].getPlayerId() + " Comprou peça");
        break;
      case 2:
        //jogou
        System.out.println("Player: " + clients[playerTurn].getPlayerId() + " jogou: " + action);
        passTurn();
        break;
      default:
        break;
    }
    
    return action;
  } 
}
