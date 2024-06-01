package qrcodeapi.exceptions;

import org.springframework.http.MediaType;

import java.util.Map;

public class UnsupportedTypeException extends RuntimeException {
    private MediaType type;

    public UnsupportedTypeException(String message, MediaType type) {
        super(message);
        this.type = type;
    }

    public MediaType getType() {
        return type;
    }

    public Map<String, String> getJsonMessage() {
        return Map.of("error", getMessage());
    }
}
