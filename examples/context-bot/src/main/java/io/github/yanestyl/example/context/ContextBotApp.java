package io.github.yanestyl.example.context;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.core.JGram;

@JGramApplication
public class ContextBotApp {

    public static void main(String[] args) {
        JGram.run(ContextBotApp.class, args);
    }
}
