package EquizGame;

import EquizGame.EquizPacket.EquizPacket;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

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
                try {
                    FileWriter writer = new FileWriter(new File("log.txt"), true);
                    writer.write(LocalDateTime.now() + " - WriteThread: Unable to receive server response!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
