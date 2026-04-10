package io.github.yanestyl.example.echo;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.core.JGram;

@JGramApplication
public class EchoBotApp {

    public static void main(String[] args) {
        JGram.run(EchoBotApp.class, args);
    }
}
