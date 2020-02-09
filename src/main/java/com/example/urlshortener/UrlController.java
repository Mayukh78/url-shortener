package com.example.urlshortener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class UrlController {

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/shorten")
    public String UrlShorten(@RequestParam(value = "url")String url){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            String code = number.toString(36);
            code = code.substring(0,8);
            return code;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
