/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author gusta
 */
public class ClientTCP {

    private Socket socket;
    private Scanner scanner;

    private ClientTCP(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    private void start() throws IOException {
        String input;
        while (true) {
            input = scanner.nextLine();
            PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
            out.println(input);
            out.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        String ip = "localhost";
        int port = 42069;

        ClientTCP client = new ClientTCP( InetAddress.getByName(ip), port);
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();
    }
}
