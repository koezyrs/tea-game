package EquizGame;

import EquizGame.EquizPacket.EquizPacket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WriteThread extends Thread {
    private Socket socket = null;
    private EquizClient client = null;
    InputStream inputStream = null;
    ObjectInputStream objectInputStream = null;

    public WriteThread(Socket socket, EquizClient client) {
        this.socket = socket;
        this.client = client;
    }

    public void run() {
        while (true) {
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                EquizPacket packet = (EquizPacket) objectInputStream.readObject();
                ClientHelperResponse.handleResponse(packet);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Unable to receive server response!");
            }
        }
    }
}
