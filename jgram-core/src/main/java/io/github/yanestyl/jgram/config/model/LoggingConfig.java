package io.github.yanestyl.jgram.config.model;

import lombok.Data;

@Data
public class LoggingConfig {
    private boolean updates = true;
    private boolean handlers = true;
}