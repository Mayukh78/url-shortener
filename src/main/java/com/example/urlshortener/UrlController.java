package com.example.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class UrlController {

     @Autowired
     UrlDetailsRepository urlDetailsRepository;

     @GetMapping("/{code}")
     public String get(@PathVariable String code)
     {
        if(urlDetailsRepository.findByCode(code) == null)
            return "Such Code do not exist, please check again";
        else
            return urlDetailsRepository.findByCode(code).getUrl();
     }

     @PostMapping("/shorten")
     public String UrlShorten(@RequestParam(value = "url")String url){
         //check if the url already exists
         UrlDetails urlDetails = urlDetailsRepository.findByUrl(url);
         if(urlDetails != null)
             return urlDetails.getCode();

        int code_char_limit=7; //maximum character limit of shortened_code
        int radix=62;
        long maximum= (long) Math.pow(radix,code_char_limit); //generate what can be max value of combination
        long random = ThreadLocalRandom.current().nextLong(1000000,maximum);
        String code= CodeGeneration(random);
        while (urlDetailsRepository.findByCode(code) != null) {
            random = ThreadLocalRandom.current().nextLong(1000000, maximum);
            code = CodeGeneration(random);
        }
        UrlDetails obj = new UrlDetails();
        obj.setUrl(url);
        obj.setCode(code);
        urlDetailsRepository.save(obj);
        return code;
    }

    //This method converts long to Base62
    private String CodeGeneration(long number)
    {
        final String dict="0123456789"+"ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"abcdefghijklmnopqrstuvwxyz"; //maintain different digits that are possible
        StringBuilder code=new StringBuilder();

        //logic to convert to Base62
        while(number>0)
        {
            int digit= (int)(number %62);
            number=number/62;
            char c= dict.charAt(digit);
            code.append(c);
        }
        return code.reverse().toString();
    }

}
