package com.URLShorten.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.URLShorten.DTO.URLShortenDTO;
import com.URLShorten.DTO.URLShortenInputDTO;
import com.URLShorten.Service.URLShortenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/shorten")
public class URLShortenController {
    private final URLShortenService urlShortenService;

    @Autowired
    public URLShortenController(URLShortenService urlShortenService){
        this.urlShortenService = urlShortenService;
    }

    @GetMapping("/{alias}")
    ResponseEntity<Void> getShortenURL(@PathVariable String alias,@RequestHeader("User-Agent") String osDetails,HttpServletRequest ip){
        return new ResponseEntity<>(urlShortenService.redirectShortenURL(alias,osDetails,ip), HttpStatus.FOUND);
    }
    @PostMapping("/")
    ResponseEntity<URLShortenDTO> shortenLongURL(@RequestBody URLShortenInputDTO urlShortenInputDTO){
        URLShortenDTO response = urlShortenService.shortenURLService(urlShortenInputDTO);
        int httpStatusCode;
        if(response.getShortURL() == null){
            response.setMessage("This Custom Alias Already Exists..");
            httpStatusCode = 404;
        }
        else{ 
            response.setMessage("Success");
            httpStatusCode = 201;
        }
        return ResponseEntity.status(httpStatusCode).body(response);
    }
}