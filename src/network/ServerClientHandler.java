package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author EmersonPL
 */
public class ServerClientHandler {

    public Socket socket;
    public DataInputStream dis;
    public DataOutputStream dos;
    private int playerHash;

    public ServerClientHandler(Socket s) {
        socket = s;

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Falha no ClientHandler");
        }
    }

    public void sendToClient(String command) throws IOException {
        dos.writeUTF(command);
    }

    public void setHash(int hash) {
        this.playerHash = hash;
    }

    public int getPlayerHash() {
        return playerHash;
    }

    //funções p/ ler o pedido do cliente, e passar p/ o serverGame
    /*public int readCode() throws IOException{
    int code = dis.readInt();
    return code;
  }
  public String readAction() throws IOException{
    String action = dis.readUTF();
    return action;
  }*/
 /*public void receiveAction(int code, String action) {
    if (code == 0) {
      dos.writeInt(code);
      dos.writeUTF(action);
    }
  }*/
}
