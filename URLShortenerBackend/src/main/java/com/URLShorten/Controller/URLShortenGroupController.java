package com.URLShorten.Controller;

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
import com.URLShorten.DTO.URLShortenInputGroupDTO;
import com.URLShorten.Service.URLShortenGroupService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/shorten/grouplink")
public class URLShortenGroupController {
    private final URLShortenGroupService urlShortenGroupService;

    @Autowired
    public URLShortenGroupController(URLShortenGroupService urlShortenGroupService){
        this.urlShortenGroupService = urlShortenGroupService;
    }
    @GetMapping("/{alias}")
    public ResponseEntity<String> openTab(@PathVariable String alias,@RequestHeader("User-Agent") String osDetails,HttpServletRequest ip) {
        return ResponseEntity.ok().header("Content-Type", "text/html").body(urlShortenGroupService.redirectShortenGroupURL(alias,osDetails,ip));
    }
    @PostMapping("/")
    public ResponseEntity<URLShortenDTO> shortenLongURLGroup(@RequestBody URLShortenInputGroupDTO urlShortenInputGroupDTO){
        URLShortenDTO response = urlShortenGroupService.shortenURLGroupService(urlShortenInputGroupDTO);
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
