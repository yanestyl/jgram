package io.github.yanestyl.example.keyboard;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.core.JGram;

@JGramApplication
public class KeyboardBotApp {

    public static void main(String[] args) {
        JGram.run(KeyboardBotApp.class, args);
    }
}
