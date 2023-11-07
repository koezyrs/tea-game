package EquizGame.GameMode;

import EquizGame.ClientHandler;
import EquizGame.EquizPacket.Message.MessageResponse;
import EquizGame.EquizPacket.PacketResponse;
import EquizGame.Room;

import java.io.IOException;
import java.util.Map;

/**
 * Red tea is a game which player have to compete to guess the word faster.
 */
public class RedTea implements TeaGame {
    private final Room hostRoom;
    private volatile Timer currentRoundTimer;
    private String currentRoundWord;

    private volatile boolean isRunning = true;
    private Map<String, Integer> playerPoint;
    private int count = 0;

    static class Timer {
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        long timeMillisecond;
        boolean isStop = false;

        Timer(long timeMillisecond) {
            this.timeMillisecond = timeMillisecond;
        }

        void stop() {
            isStop = true;
        }

        void start() {
            while (currentTime - startTime <= timeMillisecond && !isStop) {
                currentTime = System.currentTimeMillis();
            }
        }

    }

    public RedTea(Room hostRoom) {
        this.hostRoom = hostRoom;
        this.playerPoint = hostRoom.playerPoint;
    }

    private void nextRound() {
        currentRoundTimer.stop();
        count++;
    }

    @Override
    public void play() throws IOException {
        while (isRunning) {
            //Send word the client
            String keyword = GameHelper.getRandomKeyword();
            MessageResponse messageResponse = new MessageResponse(PacketResponse.OK, "Server", "Guess word: " + keyword);
            hostRoom.broadcast(messageResponse, null);

            // Wait for player word
            currentRoundTimer = new Timer(30000);
            currentRoundTimer.start();

            if (count >= 3) isRunning = false;
        }
    }

    @Override
    public void checkAnswer(String word, ClientHandler client) throws IOException {
        // Check if word is valid
        if (!isValidWord(word)) {
            return;
        }

        // Add point to player.
        int currentPoint = 0;
        int highestPoint = 0;

        if (playerPoint.containsKey(client.username)) {
            currentPoint = playerPoint.get(client.username);
        }

        if (hostRoom.currentWinner != null && playerPoint.containsKey(hostRoom.currentWinner.username)) {
            highestPoint = playerPoint.get(hostRoom.currentWinner.username);
        }

        hostRoom.playerPoint.put(client.username, currentPoint + 1);

        // Check if this player is the top 1
        if (currentPoint + 1 > highestPoint) {
            hostRoom.currentWinner = client;
        }

        // Send response to client
        MessageResponse response = new MessageResponse
                (
                        PacketResponse.OK,
                        "Server",
                        "The winner of this round is " + client.username
                );
        hostRoom.broadcast(response, null);
        nextRound();
    }

    private boolean isValidWord(String word) {
        // TODO: Complete check valid word function
        return true;
    }
}
