package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.service.ImageSecurityService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/api")
public class ImageSecurityController {

    private final ImageSecurityService imageSecurityService;

    public ImageSecurityController(ImageSecurityService imageSecurityService) {
        this.imageSecurityService = imageSecurityService;
    }

    @PostMapping("/sign-image")
    public ResponseEntity<?> signImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("owner") String owner,
            @RequestParam("logoId") String logoId) throws Exception {

        BufferedImage img = ImageIO.read(file.getInputStream());

        // Sinh HMAC dựa trên metadata
        String data = owner + ":" + logoId;
        String signature = imageSecurityService.generateHmac(data);

        // Nhúng watermark
        byte[] signedImage = imageSecurityService.addWatermark(img, "Owner: " + owner + " | Sig: " + signature);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(signedImage);
    }
}
