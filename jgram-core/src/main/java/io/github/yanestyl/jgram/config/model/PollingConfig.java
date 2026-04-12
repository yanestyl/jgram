package io.github.yanestyl.jgram.config.model;

import lombok.Data;

import java.util.List;

@Data
public class PollingConfig {
    private int timeout = 30;
    private int limit = 100;
    private List<String> allowedUpdates = List.of(
            "message", "callback_query"
    );
}