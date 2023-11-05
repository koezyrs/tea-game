package EquizGame;

import EquizGame.EquizPacket.EquizPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ReadThread extends Thread {
    private Scanner sc = new Scanner(System.in);
    private Socket socket = null;
    private EquizClient client = null;
    ObjectOutputStream objectOutputStream = null;


    public ReadThread(Socket socket, EquizClient client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        while (true) {
            try {
                String mess = sc.nextLine();
                EquizPacket request = ClientHelperRequest.handleRequest(mess);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(request);
            } catch (IOException e) {
                System.out.println("Unable to send message to server!");
                break;
            }
        }
    }
}
