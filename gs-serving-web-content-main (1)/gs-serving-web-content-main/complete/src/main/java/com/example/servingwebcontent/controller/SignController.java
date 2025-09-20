package com.example.servingwebcontent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Controller
public class SignController {

    private static final String SECRET_KEY = "my-secret-key";

    @GetMapping("/sign")
    public String showSignPage() {
        return "sign"; // trả về file sign.html
    }

    @PostMapping("/sign")
    public String signData(@RequestParam String data, Model model) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);

            String signature = Base64.getEncoder().encodeToString(
                    sha256_HMAC.doFinal(data.getBytes())
            );

            model.addAttribute("data", data);
            model.addAttribute("signature", signature);

        } catch (Exception e) {
            model.addAttribute("signature", "Lỗi: " + e.getMessage());
        }

        return "sign";
    }
}