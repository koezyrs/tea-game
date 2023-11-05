package EquizGame.EquizPacket.Room.ShowRoom;

import EquizGame.EquizPacket.EquizPacket;

public class ShowRoomRequest implements EquizPacket {
    @Override
    public String getType() {
        return "show_room_request";
    }
}
