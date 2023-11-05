package EquizGame;

import EquizGame.EquizPacket.EquizPacket;
import EquizGame.EquizPacket.Message.MessageResponse;
import EquizGame.EquizPacket.PacketResponse;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Room implements Runnable {
    public String roomId;
    public String roomName;
    public String roomPassword;
    public int roomPlayerLimits;

    private volatile boolean isRunning = false;
    private CountDownLatch startSignal = new CountDownLatch(1);

    public Set<ClientHandler> playerList = new HashSet<>();

    public Room(String roomId, String roomName, String roomPassword, int roomPlayerLimits) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPassword = roomPassword;
        this.roomPlayerLimits = roomPlayerLimits;
    }

    public void addPlayer(ClientHandler client) {
        playerList.add(client);
        System.out.println("Player " + client.socket.getInetAddress().toString() + " join the room");
    }

    public void removePlayer(ClientHandler client) {
        playerList.remove(client);
    }

    public void broadcast(EquizPacket packet, ClientHandler except) {
        for (ClientHandler player : playerList) {
            if (player == except) continue;
            player.sendPacket(packet);
        }
    }

    public void startGame(String gameMode) {
        isRunning = true;
        startSignal.countDown(); // Release the latch to allow the thread to continue
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
                if (!isRunning) {
                    startSignal = new CountDownLatch(1);
                    startSignal.await();
                }
                int pos = rand.nextInt(random.size());
                MessageResponse messageResponse = new MessageResponse(PacketResponse.OK, "Server", "Guess word: " + random.get(pos));
                broadcast(messageResponse, null);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
