package EquizGame.EquizPacket.Room.JoinRoom;

import EquizGame.EquizPacket.EquizPacket;

public class JoinRoomRequest implements EquizPacket {
    public String roomId;

    public JoinRoomRequest(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String getType() {
        return "join_room_request";
    }
}
