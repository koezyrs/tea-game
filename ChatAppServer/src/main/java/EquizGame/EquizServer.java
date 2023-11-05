package EquizGame;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EquizServer implements Runnable {
    ServerSocket server;
    private int corePoolSize = 5;
    private int maximumPoolSize = 10;
    private long keepAliveTime = 500;
    private TimeUnit unit = TimeUnit.SECONDS;

    private ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);

    private RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
            maximumPoolSize, keepAliveTime, unit, workQueue, handler);

    private Set<ClientHandler> clientList = new HashSet<>();
    private Set<Room> roomList = new HashSet<>();

    @Override
    public void run() {
        try {
            server = new ServerSocket(6789);
            System.out.println("Success initialize equiz server at port 6789...");
        } catch (IOException e) {
            System.out.println("Unable to start server! Maybe the port is occupied!");
            e.printStackTrace();
            return;
        }

        new ServerHandler(server, clientList).start();
        new ServerCleaner(clientList, roomList).start();

        ServerHelper.setServer(this);

        while (true) {
            try {
                Socket socket = server.accept();
                ClientHandler client = new ClientHandler(socket, this);
                System.out.println("New client from: " + socket.getInetAddress().getHostAddress());
                clientList.add(client);
                threadPoolExecutor.execute(client);
            } catch (IOException e) {
                System.out.println("Unable to accept new client!");
            }
        }
    }

    public String addRoom(String roomName, String roomPassword, int roomPlayerLimit) {
        String roomId = RandomStringUtils.randomNumeric(10);
        Room newRoom = new Room(roomId, roomName, roomPassword, roomPlayerLimit);
        roomList.add(newRoom);
        Thread thread = new Thread(newRoom);
        thread.start();
        System.out.println("Room " + roomId + " is opened!");
        return roomId;
    }

    public Room getRoom(String roomId) throws Exception {
        for (Room room : roomList) {
            if (room.roomId.equals(roomId)) {
                return room;
            }
        }
        throw new Exception("There is no room with id: " + roomId);
    }

    public Set<Room> getRoomList() {
        return roomList;
    }

    public void close() {
        try {
            server.close();
            Thread.currentThread().interrupt();
        } catch (IOException e) {

        }
    }
}
