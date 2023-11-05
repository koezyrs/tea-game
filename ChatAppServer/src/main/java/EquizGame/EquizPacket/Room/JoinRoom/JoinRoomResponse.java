package EquizGame.EquizPacket.Room.JoinRoom;

import EquizGame.EquizPacket.EquizPacket;
import EquizGame.EquizPacket.PacketResponse;

public class JoinRoomResponse implements EquizPacket {
    public PacketResponse status;
    public String message;
    public String roomId;

    public JoinRoomResponse(PacketResponse status, String message, String roomId) {
        this.status = status;
        this.message = message;
        this.roomId = roomId;
    }

    @Override
    public String getType() {
        return "join_room_response";
    }
}
