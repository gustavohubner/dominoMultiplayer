package network;

import dominoMultiplayer.classes.Domino;
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
  
  public ServerGame(ClientHandler[] clients, Domino game, int startPlayer) {
    this.clients = clients;
    this.game = game;
    this.playerTurn = startPlayer;
  }
  
  public void setGame(Domino game) {
    this.game = game;
  }
  
  public void setTurn(int turn) {
    this.playerTurn = turn;
  }
  
  
  public void run() {
    try {
      for (ClientHandler client : clients) {
        client.dos.writeInt(client.getPlayerId());
        client.dos.writeInt(playerTurn);
      }
    } catch (IOException ex) {
      System.err.println("Erro em ServerGame.run()");
    }
  }
}
