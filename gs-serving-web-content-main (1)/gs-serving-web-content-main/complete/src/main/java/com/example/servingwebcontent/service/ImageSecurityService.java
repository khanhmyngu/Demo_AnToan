package com.example.servingwebcontent.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
//import java.security.*;
import java.util.Base64;
import javax.imageio.ImageIO;

@Service
public class ImageSecurityService {

    private static final String SECRET = "my-secret-key";

    // Sinh chữ ký HMAC SHA256
    public String generateHmac(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
    }

    // Nhúng watermark (chữ ký + owner) vào ảnh
    public byte[] addWatermark(BufferedImage image, String text) throws Exception {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(new Color(255, 0, 0, 80)); // đỏ nhạt
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString(text, 20, image.getHeight() - 20);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}