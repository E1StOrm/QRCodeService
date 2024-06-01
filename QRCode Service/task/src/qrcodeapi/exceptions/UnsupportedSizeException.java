package qrcodeapi.exceptions;

import java.util.Map;

public class UnsupportedSizeException extends RuntimeException {
    private final int size;

    public UnsupportedSizeException(String message, int size) {
        super(message);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Map<String, String> getJsonMessage() {
        return Map.of("error", getMessage());
    }
}
