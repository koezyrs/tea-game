package EquizGame.EquizPacket.Room.StartRoom;

import EquizGame.EquizPacket.EquizPacket;

public class StartRoomResponse implements EquizPacket {

    public StartRoomResponse() {

    }

    @Override
    public String getType() {
        return "start_room_response";
    }
}
