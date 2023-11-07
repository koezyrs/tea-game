package EquizGame.GameMode;

import EquizGame.ClientHandler;

import java.io.IOException;

public interface TeaGame {
    void play() throws IOException;

    void checkAnswer(String word, ClientHandler client) throws IOException;
}
