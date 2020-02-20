package com.example.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     public String UrlShorten(@RequestBody RequestDetails requestDetails){
         //check if the url already exists
         UrlDetails urlDetails = urlDetailsRepository.findByUrl(requestDetails.getUrl());
         if(urlDetails != null)
             return urlDetails.getCode();

         CodeDetails shortenedCode = new CodeDetails();
         String custom_code = requestDetails.getCustom_code();

         //check if custom code exist
         if (urlDetailsRepository.findByCode(custom_code) != null)
             return "Custom Code exists,please try another custom code";
         //if no custom code was provided
         else if(custom_code.equals("NONE")) {
             shortenedCode.codeGeneration(); //this generate a random code
             String code= shortenedCode.getCode();
             while (urlDetailsRepository.findByCode(code) != null) {
                 shortenedCode.codeGeneration();
                 code = shortenedCode.getCode();
             }
         }
         else
             shortenedCode.setCode(custom_code);

         UrlDetails obj = new UrlDetails();
         obj.setUrl(requestDetails.getUrl());
         obj.setCode(shortenedCode.getCode());
         urlDetailsRepository.save(obj);
         return shortenedCode.getCode();
    }

}
