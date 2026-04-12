package io.github.yanestyl.jgram.config.model;

import lombok.Data;

@Data
public class DatabaseConfig {
    private boolean enabled = false;
    private String type = "h2";
    private String url = "./jgram.db";
}