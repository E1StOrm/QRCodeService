package qrcodeapi.exceptions;

import java.util.Map;

public class UnsupportedLevelException extends RuntimeException {
    private String level;

    public UnsupportedLevelException(String message, String level) {
        super(message);
    }

    public String getLevel() {
        return level;
    }

    public Map<String, String> getJsonMessage() {
        return Map.of("error", getMessage());
    }
}
