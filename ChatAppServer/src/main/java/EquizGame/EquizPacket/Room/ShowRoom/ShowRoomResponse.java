package EquizGame.EquizPacket.Room.ShowRoom;

import EquizGame.EquizPacket.EquizPacket;


import java.util.ArrayList;
import java.util.List;


public class ShowRoomResponse implements EquizPacket {
    public List<RoomWraper> roomList = new ArrayList<>();

    public ShowRoomResponse(List<RoomWraper> roomList) {
        this.roomList = roomList;
    }

    @Override
    public String getType() {
        return "show_room_response";
    }
}