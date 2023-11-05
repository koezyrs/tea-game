package EquizGame;

import EquizGame.EquizPacket.EquizPacket;
import EquizGame.EquizPacket.Message.MessageRequest;
import EquizGame.EquizPacket.Room.JoinRoom.JoinRoomRequest;
import EquizGame.EquizPacket.Room.OpenRoom.OpenRoomRequest;
import EquizGame.EquizPacket.Room.ShowRoom.ShowRoomRequest;
import EquizGame.EquizPacket.Room.StartRoom.StartRoomRequest;

public class ClientHelperRequest {
    public static EquizPacket handleRequest(String request) {
        if (request.startsWith("/show_room")) {
            return createShowRoomRequest(request);
        }

        if (request.startsWith("/join")) {
            return createJoinRoomRequest(request);
        }

        if (request.startsWith("/open")) {
            return createOpenRoomRequest(request);
        }

        if (request.startsWith("/start_game")) {
            return createStartGameRequest(request);
        }

        return createMessageRequest(request);
    }

    private static JoinRoomRequest createJoinRoomRequest(String request) {
        String[] res = request.split(" ");
        if (res.length != 2) {
            System.out.println("Invalid join room request!");
            return null;
        }
        return new JoinRoomRequest(res[1]);
    }

    private static OpenRoomRequest createOpenRoomRequest(String request) {
        String[] res = request.split(" ");
        if (res.length != 4) {
            System.out.println("Invalid open room request!");
            return null;
        }
        return new OpenRoomRequest(res[1], res[2], Integer.parseInt(res[3]));
    }

    private static MessageRequest createMessageRequest(String message) {
        return new MessageRequest(message);
    }

    private static ShowRoomRequest createShowRoomRequest(String request) {
        return new ShowRoomRequest();
    }

    private static StartRoomRequest createStartGameRequest(String resquest) {
        return new StartRoomRequest("red_tea");
    }
}
