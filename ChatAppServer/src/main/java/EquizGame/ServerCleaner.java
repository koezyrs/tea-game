package EquizGame;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ServerCleaner extends Thread {
    private Set<ClientHandler> clientList;
    private Set<Room> roomList;

    public ServerCleaner(Set<ClientHandler> clientList, Set<Room> roomList) {
        this.clientList = clientList;
        this.roomList = roomList;
    }

    public void run() {
        while (true) {
            cleanRoomList();
            cleanClientList();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
            try {
                Runtime.getRuntime().exec("cls");
            } catch (IOException e) {

            }

            System.out.println("Current active user: " + clientList.size());
            System.out.println("Current active room: " + roomList.size());
            for (Room room : roomList) {
                System.out.println("Room id: " + room.roomId + "\n\t Current active user in room: " + room.playerList.size());
            }
        }
    }

    private void cleanRoomList() {
        for (Room room : roomList) {
            for (ClientHandler client : room.playerList) {
                if (client.socket.isClosed() || !client.socket.isConnected()) {
                    try {
                        client.socket.close();
                    } catch (Exception e) {
                    }
                    room.removePlayer(client);
                }
            }

            if (room.playerList.size() == 0) {
                roomList.remove(room);
            }
        }
    }

    private void cleanClientList() {
        for (ClientHandler client : clientList) {
            if (client.socket.isClosed() || !client.socket.isConnected()) {
                try {
                    client.socket.close();
                } catch (Exception e) {
                }
                clientList.remove(client);
            }
        }
    }


}
