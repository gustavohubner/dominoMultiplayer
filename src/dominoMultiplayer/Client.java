/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominoMultiplayer;

import dominoMultiplayer.classes.Domino;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JButton;

import java.util.concurrent.TimeUnit;

//TODO: Tem que resolver uns try/catch ai no meio
public class Client extends Application{

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    private int playerId;
    private int hashPlayerTurn;
    
    private Domino game;
    
    @Override
    public void start(Stage stage) throws Exception {
      /*
      Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
      Scene scene = new Scene(root);

      stage.setScene(scene);
      stage.show();
      */
      
      while (true) {
        if (hashPlayerTurn == playerId) {
          //isso aqui é só pra ler
          TimeUnit.SECONDS.sleep(5);
          
          sendAction(0, "");
        } else {
          int code = dis.readInt();
          String action = dis.readUTF();
          
          System.out.println("Cliente recebeu Code " + code + " Action: " + action);

          receiveAction(code, action);
        }
      }
    }

    private Client(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        
        playerId = dis.readInt();
        System.out.println("Connected as player " + playerId);
        
        hashPlayerTurn = dis.readInt();
        System.out.println("Turn of player: " + hashPlayerTurn);
        
        //start();
    }

    /*private void start() throws IOException, ClassNotFoundException {
        
    }
    */

    public static void main(String[] args) throws Exception {
        String ip = "localhost";
        int port = 42069;
        
        //TODO: Aqui precisa colocar pra instanciar o cliente só depois de clicar em join, etc...

        Client client = new Client( InetAddress.getByName(ip), port);
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        
        
        // TODO: Nem sei oq deveria ser aqui
        Stage stage = null;
        client.start(stage);
        
        
        //launch(args);
    }
    
    private void sendAction(int code, String action) throws IOException{
      dos.writeInt(code);
      dos.writeUTF(action);
      
      // precisa receber a resposta do server
      code = dis.readInt();
      action = dis.readUTF();
      
      receiveAction(code, action);
    }
    
    private void receiveAction(int code, String action) {
      if (code == 0) { // pass
        hashPlayerTurn = Integer.parseInt(action);
      } else if (code == 1) { // compra
        System.out.print("asjkdhaksj");
      } else if (code == 2) { // place
        // Insere a peça, recebendo ela em action
      }
    }
}
