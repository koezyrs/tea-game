package EquizGame.EquizPacket.Message;

import EquizGame.EquizPacket.EquizPacket;

public class MessageRequest implements EquizPacket {
    public String text;


    public MessageRequest(String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return "message_request";
    }
}