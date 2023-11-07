package EquizGame;

import EquizGame.EquizPacket.BaseResponse;
import EquizGame.EquizPacket.EquizPacket;
import EquizGame.EquizPacket.PacketResponse;
import EquizGame.EquizPacket.Message.MessageRequest;
import EquizGame.EquizPacket.Message.MessageResponse;
import EquizGame.EquizPacket.Room.JoinRoom.JoinRoomRequest;
import EquizGame.EquizPacket.Room.JoinRoom.JoinRoomResponse;
import EquizGame.EquizPacket.Room.OpenRoom.OpenRoomRequest;
import EquizGame.EquizPacket.Room.OpenRoom.OpenRoomResponse;
import EquizGame.EquizPacket.Room.ShowRoom.RoomWraper;
import EquizGame.EquizPacket.Room.ShowRoom.ShowRoomRequest;
import EquizGame.EquizPacket.Room.ShowRoom.ShowRoomResponse;
import EquizGame.EquizPacket.Room.StartRoom.StartRoomRequest;
import EquizGame.EquizPacket.Room.StartRoom.StartRoomResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerHelper {
    private static EquizServer server;

    public static void setServer(EquizServer serverToHelp) {
        if (server != null) {
            server.close();
        }
        server = serverToHelp;
    }

    public static EquizPacket handleRequest(EquizPacket packet, ClientHandler client) {
        EquizPacket response = null;

        switch (packet.getType()) {
            case "message_request":
                response = handleMessage((MessageRequest) packet, client);
                break;
            case "open_room_request":
                response = handleOpenRoom((OpenRoomRequest) packet, client);
                break;
            case "join_room_request":
                response = handleJoinRoom((JoinRoomRequest) packet, client);
                break;
            case "show_room_request":
                response = handleShowRoom((ShowRoomRequest) packet);
                break;
            case "start_room_request":
                response = handleStartGame((StartRoomRequest) packet, client);
                break;
            default:
                break;
        }
        return response;
    }

    private static MessageResponse handleMessage(MessageRequest message, ClientHandler client) {
        MessageResponse response;
        Room targetRoom = client.currentRoom;
        try {
            response = new MessageResponse(PacketResponse.OK, client.username, '[' + client.username + "]:"
                    + message.text);
            targetRoom.broadcast(response, client);
            targetRoom.checkAnswer(message.text, client);
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = new MessageResponse(PacketResponse.ERROR);
            return response;
        }

        // ======== Check player answer of game


    }

    private static OpenRoomResponse handleOpenRoom(OpenRoomRequest packet, ClientHandler client) {
        String roomId = server.addRoom(packet.roomName, packet.roomPassword, packet.roomPlayerLimit);
        try {
            Room room = server.getRoom(roomId);
            room.addPlayer(client);
            client.currentRoom = room;
        } catch (Exception e) {

        }
        return new OpenRoomResponse(PacketResponse.OK, "You have joined room " + roomId, roomId);
    }

    private static JoinRoomResponse handleJoinRoom(JoinRoomRequest packet, ClientHandler client) {
        String roomId = packet.roomId;
        JoinRoomResponse response;
        try {
            Room room = server.getRoom(roomId);
            room.addPlayer(client);
            client.currentRoom = room;
            response = new JoinRoomResponse(PacketResponse.OK, "You have joined room " + roomId, roomId);
        } catch (Exception e) {
            response = new JoinRoomResponse(PacketResponse.ERROR, "Unable to join room " + roomId, roomId);
        }
        return response;
    }

    private static ShowRoomResponse handleShowRoom(ShowRoomRequest packet) {
        Set<Room> roomList = server.getRoomList();
        List<RoomWraper> roomWrapers = new ArrayList<>();
        for (Room room : roomList) {
            RoomWraper roomWraper = new RoomWraper(room.roomId, room.roomName,
                    room.roomPassword, room.roomPlayerLimits);
            roomWrapers.add(roomWraper);
        }
        return new ShowRoomResponse(roomWrapers);
    }

    private static StartRoomResponse handleStartGame(StartRoomRequest packet, ClientHandler client) {
        try {
            client.currentRoom.startGame(packet.gameMode, client);
        } catch (IOException e) {

        }
        return new StartRoomResponse();
    }
}
