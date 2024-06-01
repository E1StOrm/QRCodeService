package qrcodeapi.controllers;

import com.google.zxing.WriterException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qrcodeapi.exceptions.UnsupportedLevelException;
import qrcodeapi.exceptions.UnsupportedSizeException;
import qrcodeapi.exceptions.UnsupportedTypeException;
import qrcodeapi.model.QRCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

@RestController
public class QRCodeController {

    @GetMapping(path = "/api/health")
    public ResponseEntity<String> isLive() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/api/qrcode")
    public ResponseEntity<Object> qrCode(
            @RequestParam String contents,
            @RequestParam(required = false, defaultValue = "250") int size,
            @RequestParam(required = false, defaultValue = "L") String correction,
            @RequestParam(required = false, defaultValue = "png") String type) {
        QRCode qrCode;
        try {
            qrCode = new QRCode(contents, size, correction, type);
            return ResponseEntity.ok().contentType(qrCode.getType()).body(qrCode.getImage());
        } catch (WriterException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                    Map.of("error", "Contents cannot be null or blank"));
        } catch (UnsupportedSizeException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(e.getJsonMessage());
        } catch (UnsupportedLevelException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(e.getJsonMessage());
        } catch (UnsupportedTypeException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(e.getJsonMessage());
        }
    }

    @Bean
    public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
