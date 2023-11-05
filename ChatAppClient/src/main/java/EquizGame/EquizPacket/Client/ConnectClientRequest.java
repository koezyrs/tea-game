package EquizGame.EquizPacket.Client;

import EquizGame.EquizPacket.EquizPacket;

public class ConnectClientRequest implements EquizPacket {
    public String username;

    public ConnectClientRequest(String username) {
        this.username = username;
    }

    @Override
    public String getType() {
        return "connect_client_request";
    }
}
