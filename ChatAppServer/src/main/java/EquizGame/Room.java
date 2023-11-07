package EquizGame;

import EquizGame.EquizPacket.EquizPacket;
import EquizGame.EquizPacket.Message.MessageResponse;
import EquizGame.EquizPacket.PacketResponse;
import EquizGame.EquizPacket.Room.StartRoom.StartRoomResponse;

import java.io.IOException;
import java.util.*;

public class Room implements Runnable {
    public String roomId;
    public String roomName;
    public String roomPassword;
    public int roomPlayerLimits;
    public Set<ClientHandler> playerList = new HashSet<>();

    private volatile boolean isRunning = false;
    private final Object lock = new Object();

    private List<MessageResponse> messageHistory = new ArrayList<>();


    public Room(String roomId, String roomName, String roomPassword, int roomPlayerLimits) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.roomPlayerLimits = roomPlayerLimits;
    }

    public void addPlayer(ClientHandler client) throws IOException {
        playerList.add(client);
        System.out.println("Player " + client.socket.getInetAddress().toString() + " joined the room");

        for (MessageResponse message : messageHistory) {
            client.sendPacket(message);
        }
    }

    public void broadcast(EquizPacket packet, ClientHandler except) throws IOException {
        if (packet instanceof MessageResponse) {
            MessageResponse response = (MessageResponse) packet;
            messageHistory.add(response);
        }

        for (ClientHandler player : playerList) {
            if (player == except) continue;
            player.sendPacket(packet);
        }
    }

    public void startGame(String gameMode, ClientHandler host) throws IOException {
        synchronized (lock) {
            isRunning = true;
            StartRoomResponse response = new StartRoomResponse();
            host.sendPacket(response);
            lock.notifyAll();
        }
    }

    public void endGame() {
        isRunning = false;
    }

    @Override
    public void run() {
        List<String> random = new ArrayList<>();
        random.add("ion");
        random.add("ary");
        random.add("json");
        Random rand = new Random();
        try {
            while (true) {
                synchronized (lock) {
                    while (!isRunning) {
                        lock.wait();
                    }
                }
                int pos = rand.nextInt(random.size());
                MessageResponse messageResponse = new MessageResponse(PacketResponse.OK, "Server", "Guess word: " + random.get(pos));
                broadcast(messageResponse, null);
                Thread.sleep(5000);
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}
