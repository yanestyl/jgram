package io.github.yanestyl.jgram.config;

import io.github.yanestyl.jgram.config.exception.JGramConfigException;
import io.github.yanestyl.jgram.config.model.*;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ConfigLoader {

    private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    private static final String[] SEARCH_PATHS = {
            "jgram.yml",
            "config/jgram.yml",
            "src/main/resources/jgram.yml"
    };

    public static JGramConfig load() {
        return load(null);
    }

    public static JGramConfig load(String customPath) {
        String content = readFile(customPath);
        content = resolveEnvVariables(content);

        JGramConfig config = parse(content);
        applyEnvOverrides(config);
        validate(config);

        return config;
    }

    // ---------------------------------------------------------------
    // File reading
    // ---------------------------------------------------------------

    private static String readFile(String customPath) {
        // 1. Явно указанный путь
        if (customPath != null) {
            try {
                String content = Files.readString(Path.of(customPath));
                log.info("Loaded config from: {}", customPath);
                return content;
            } catch (Exception e) {
                throw new JGramConfigException("Config file not found: " + customPath);
            }
        }

        // 2. Стандартные пути
        for (String path : SEARCH_PATHS) {
            try {
                String content = Files.readString(Path.of(path));
                log.info("Loaded config from: {}", path);
                return content;
            } catch (Exception ignored) {}
        }

        // 3. Classpath
        InputStream stream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("jgram.yml");

        if (stream != null) {
            try {
                String content = new String(stream.readAllBytes());
                log.info("Loaded config from classpath: jgram.yml");
                return content;
            } catch (Exception e) {
                throw new JGramConfigException("Failed to read jgram.yml from classpath");
            }
        }

        throw new JGramConfigException(
                "jgram.yml not found! Create it in the project root or resources/ directory."
        );
    }

    // ---------------------------------------------------------------
    // Env variables — ${BOT_TOKEN} → actual value
    // ---------------------------------------------------------------

    private static String resolveEnvVariables(String content) {
        Matcher matcher = ENV_PATTERN.matcher(content);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = System.getenv(varName);

            if (value == null) {
                throw new JGramConfigException(
                        "Environment variable not found: " + varName
                );
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
            log.debug("Resolved env variable: {}", varName);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    // ---------------------------------------------------------------
    // Parsing
    // ---------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private static JGramConfig parse(String content) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(content);

            if (root == null || !root.containsKey("jgram")) {
                throw new JGramConfigException("Config must have 'jgram' root key");
            }

            Map<String, Object> jgramMap = (Map<String, Object>) root.get("jgram");
            return mapToConfig(jgramMap);

        } catch (JGramConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new JGramConfigException("Failed to parse jgram.yml: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static JGramConfig mapToConfig(Map<String, Object> map) {
        JGramConfig config = new JGramConfig();

        if (map.containsKey("token")) {
            config.setToken((String) map.get("token"));
        }

        if (map.containsKey("polling")) {
            Map<String, Object> polling = (Map<String, Object>) map.get("polling");
            PollingConfig pc = new PollingConfig();
            if (polling.containsKey("timeout"))
                pc.setTimeout((Integer) polling.get("timeout"));
            if (polling.containsKey("limit"))
                pc.setLimit((Integer) polling.get("limit"));
            if (polling.containsKey("allowed-updates"))
                pc.setAllowedUpdates((java.util.List<String>) polling.get("allowed-updates"));
            config.setPolling(pc);
        }

        if (map.containsKey("session")) {
            Map<String, Object> session = (Map<String, Object>) map.get("session");
            SessionConfig sc = new SessionConfig();
            if (session.containsKey("store"))
                sc.setStore((String) session.get("store"));
            config.setSession(sc);
        }

        if (map.containsKey("database")) {
            Map<String, Object> db = (Map<String, Object>) map.get("database");
            DatabaseConfig dc = new DatabaseConfig();
            if (db.containsKey("enabled"))
                dc.setEnabled((Boolean) db.get("enabled"));
            if (db.containsKey("type"))
                dc.setType((String) db.get("type"));
            if (db.containsKey("url"))
                dc.setUrl((String) db.get("url"));
            config.setDatabase(dc);
        }

        if (map.containsKey("logging")) {
            Map<String, Object> logging = (Map<String, Object>) map.get("logging");
            LoggingConfig lc = new LoggingConfig();
            if (logging.containsKey("updates"))
                lc.setUpdates((Boolean) logging.get("updates"));
            if (logging.containsKey("handlers"))
                lc.setHandlers((Boolean) logging.get("handlers"));
            config.setLogging(lc);
        }

        if (map.containsKey("roles")) {
            Map<String, Object> roles = (Map<String, Object>) map.get("roles");
            Map<String, java.util.List<Long>> rolesMap = new HashMap<>();
            roles.forEach((role, ids) -> {
                java.util.List<Integer> intIds = (java.util.List<Integer>) ids;
                rolesMap.put(role, intIds.stream()
                        .map(Integer::longValue)
                        .toList());
            });
            config.setRoles(rolesMap);
        }

        return config;
    }

    // ---------------------------------------------------------------
    // Env overrides — BOT_TOKEN всегда побеждает
    // ---------------------------------------------------------------

    private static void applyEnvOverrides(JGramConfig config) {
        String envToken = System.getenv("BOT_TOKEN");
        if (envToken != null && !envToken.isBlank()) {
            config.setToken(envToken);
            log.info("Token overridden by BOT_TOKEN environment variable");
        }
    }

    // ---------------------------------------------------------------
    // Validation
    // ---------------------------------------------------------------

    private static void validate(JGramConfig config) {
        if (config.getToken() == null || config.getToken().isBlank()) {
            throw new JGramConfigException(
                    "Bot token is not set! Add 'token' to jgram.yml or set BOT_TOKEN env variable."
            );
        }
    }
}
