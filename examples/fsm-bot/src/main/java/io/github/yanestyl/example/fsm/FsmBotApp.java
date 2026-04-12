package io.github.yanestyl.example.fsm;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.core.JGram;

@JGramApplication
public class FsmBotApp {

    public static void main(String[] args) {
        JGram.run(FsmBotApp.class, args);
    }
}
