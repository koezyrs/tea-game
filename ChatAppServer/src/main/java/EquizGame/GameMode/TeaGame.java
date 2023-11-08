package EquizGame.GameMode;

import EquizGame.ClientHandler;

import java.io.IOException;

public abstract class TeaGame {
    public abstract void play() throws IOException, InterruptedException;

    public abstract void checkAnswer(String word, ClientHandler client) throws IOException;
}
