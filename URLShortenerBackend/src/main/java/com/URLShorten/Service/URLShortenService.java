package com.URLShorten.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.URLShorten.DTO.*;
import com.URLShorten.Model.*;
import com.URLShorten.Repository.*;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class URLShortenService {
    private URLShortenerRepo urlShortenerRepo;
    private URLAnalysisRepo urlAnalyticsRepo;
    private URLShortenerGroupRepo urlShortenerGroupRepo;
    private final String URL = "https://localhost:8080/api/shorten/";
    private final int stringLength = 5;

    @Autowired
    public URLShortenService(URLShortenerRepo urlShortenerRepo,URLShortenerGroupRepo urlShortenerGroupRepo,URLAnalysisRepo urlAnalyticsRepo){
        this.urlShortenerRepo = urlShortenerRepo;
        this.urlShortenerGroupRepo = urlShortenerGroupRepo;
        this.urlAnalyticsRepo = urlAnalyticsRepo;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < stringLength) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public URLShortenDTO shortenURLService(URLShortenInputDTO urlShortenInputDTO){
        String longURL = urlShortenInputDTO.getLongURL();
        String customAlias = urlShortenInputDTO.getCustomAlias();
        List<String> customAliasList = urlShortenerRepo.findByCustomAlias();
        List<String> customAliasGroupList = urlShortenerGroupRepo.findByCustomAlias();
        if(!customAliasList.contains(customAlias) && !customAliasGroupList.contains(customAlias)){
            String topic = urlShortenInputDTO.getTopic();
            LocalDateTime localDateTime = LocalDateTime.now();
    
            if(customAlias == "")customAlias = getSaltString();
            String shortURL = URL + customAlias;
    
            URLShortenDTO urlResponse = new URLShortenDTO();
            urlResponse.setLocalDateTime(localDateTime);
            urlResponse.setShortURL(shortURL);
    
            URLShortenModel urlShortenModel = new URLShortenModel();
            urlShortenModel.setCustomAlias(customAlias);
            urlShortenModel.setShortURL(shortURL);
            urlShortenModel.setLongURL(longURL);
            urlShortenModel.setTopic(topic);
    
            urlShortenerRepo.save(urlShortenModel);
    
            return urlResponse;
        }
        else{
            URLShortenDTO urlResponse = new URLShortenDTO();
            return urlResponse;
        }
    }
    private static String getOS(String userAgent){
        String os = "";
        //=================OS=======================
        if (userAgent.toLowerCase().indexOf("windows") >= 0 ){
            os = "Windows";
        } 
        else if(userAgent.toLowerCase().indexOf("mac") >= 0){
            os = "Mac";
        } 
        else if(userAgent.toLowerCase().indexOf("x11") >= 0){
            os = "Unix";
        } 
        else if(userAgent.toLowerCase().indexOf("android") >= 0){
            os = "Android";
        } 
        else if(userAgent.toLowerCase().indexOf("iphone") >= 0){
            os = "IPhone";
        }
        else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        return os;
    }
    private String getBrowser(String userAgent){
        String  user = userAgent.toLowerCase();
        String browser = "";
        
         //===============Browser===========================
        if (user.contains("msie")){
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } 
        else if (user.contains("safari") && user.contains("version")){
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } 
        else if ( user.contains("opr") || user.contains("opera")){
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } 
        else if (user.contains("chrome")){
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        }
        else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ){
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";
                  
        } 
        else if (user.contains("firefox")){
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } 
        else if(user.contains("rv")){
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } 
        else{
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return browser;
    }
    private URLAnalyticsModel saveAnalysisRecord(String userAgent,String customAlias,String ip,String topic){
        String osName = getOS(userAgent);
        String browserName = getBrowser(userAgent);
        URLAnalyticsModel analysisModel = new URLAnalyticsModel();
        analysisModel.setCustomAlias(customAlias);
        analysisModel.setBrowserName(browserName);
        analysisModel.setIp(ip);
        analysisModel.setOsName(osName);
        analysisModel.setTopic(topic);
        LocalDateTime localDateTime = LocalDateTime.now();
        analysisModel.setTimestamp(localDateTime);
        return analysisModel;
    }
    public HttpHeaders redirectShortenURL(String customAlias,String userAgent,HttpServletRequest request){
        String ip = request.getRemoteAddr();
        HttpHeaders headers = new HttpHeaders();

        URLShortenModel responsRecord = urlShortenerRepo.findById(customAlias).get();

        headers.add("Location",responsRecord.getLongURL());
        // "Hey, this resource isn't here — go to this new location instead."
        // This is done using HTTP status 302 (Found) and the Location header.

        
        URLAnalyticsModel analysisModel = saveAnalysisRecord(userAgent, customAlias, ip,responsRecord.getTopic());
        urlAnalyticsRepo.save(analysisModel);

        return headers;
    }
}
