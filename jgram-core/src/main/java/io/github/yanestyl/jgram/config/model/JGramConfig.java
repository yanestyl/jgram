package io.github.yanestyl.jgram.config.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class JGramConfig {

    private String token;
    private PollingConfig polling   = new PollingConfig();
    private SessionConfig session   = new SessionConfig();
    private DatabaseConfig database = new DatabaseConfig();
    private LoggingConfig logging   = new LoggingConfig();

    // ADMIN: [123456789, 987654321]
    private Map<String, List<Long>> roles = new HashMap<>();
}