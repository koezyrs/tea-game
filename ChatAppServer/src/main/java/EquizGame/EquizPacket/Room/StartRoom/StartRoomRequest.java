package EquizGame.EquizPacket.Room.StartRoom;

import EquizGame.EquizPacket.EquizPacket;

public class StartRoomRequest implements EquizPacket {
    public String gameMode;

    public StartRoomRequest(String gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public String getType() {
        return "start_room_request";
    }
}
