package qrcodeapi.model;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import qrcodeapi.exceptions.UnsupportedLevelException;
import qrcodeapi.exceptions.UnsupportedSizeException;
import qrcodeapi.exceptions.UnsupportedTypeException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class QRCode {
    private static final int MIN_SIZE = 150;
    private static final int MAX_SIZE = 350;
    private QRCodeWriter writer = new QRCodeWriter();
    private String contents;
    private int size;
    private ErrorCorrectionLevel level;
    private MediaType type;

    public QRCode(String contents, int size, String level, MediaType type) throws WriterException, UnsupportedSizeException, UnsupportedLevelException, UnsupportedTypeException {
        if (contents == null || contents.trim().isEmpty()) {
            throw new WriterException("Contents cannot be null or blank");
        } else {
            this.contents = contents;
        }

        if (size >= MIN_SIZE && size <= MAX_SIZE) {
            this.size = size;
        } else {
            throw new UnsupportedSizeException("Image size must be between 150 and 350 pixels", size);
        }

        if ("L".equalsIgnoreCase(level)) {
            this.level = ErrorCorrectionLevel.L;
        } else if ("M".equalsIgnoreCase(level)) {
            this.level = ErrorCorrectionLevel.M;
        } else if ("Q".equalsIgnoreCase(level)) {
            this.level = ErrorCorrectionLevel.Q;
        } else if ("H".equalsIgnoreCase(level)) {
            this.level = ErrorCorrectionLevel.H;
        } else {
            throw new UnsupportedLevelException("Permitted error correction levels are L, M, Q, H", level);
        }

        if (type == MediaType.IMAGE_GIF || type == MediaType.IMAGE_JPEG || type == MediaType.IMAGE_PNG) {
            this.type = type;
        } else {
            throw new UnsupportedTypeException("Only png, jpeg and gif image types are supported", type);
        }
    }

    public QRCode(String contents, int size, String level, String type) throws WriterException, UnsupportedSizeException, UnsupportedLevelException, UnsupportedTypeException {
        this(contents, size, level,
                type != null && type.equalsIgnoreCase("gif")
                ? MediaType.IMAGE_GIF
                : type != null && type.equalsIgnoreCase("png")
                    ? MediaType.IMAGE_PNG
                    : type != null && type.equalsIgnoreCase("jpeg")
                        ? MediaType.IMAGE_JPEG
                        : MediaType.ALL);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public BufferedImage getImage() throws WriterException {
        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, level);
        BitMatrix bitMatrix = writer.encode(this.contents, BarcodeFormat.QR_CODE, size, size, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
