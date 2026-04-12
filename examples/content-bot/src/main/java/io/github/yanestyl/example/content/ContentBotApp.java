package io.github.yanestyl.example.content;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.core.JGram;

@JGramApplication
public class ContentBotApp {

    public static void main(String[] args) {
        JGram.run(ContentBotApp.class, args);
    }
}
