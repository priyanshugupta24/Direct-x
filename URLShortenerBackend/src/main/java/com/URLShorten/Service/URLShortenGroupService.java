package com.URLShorten.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.URLShorten.DTO.URLShortenDTO;
import com.URLShorten.DTO.URLShortenInputDTO;
import com.URLShorten.DTO.URLShortenInputGroupDTO;
import com.URLShorten.Model.URLAnalyticsModel;
import com.URLShorten.Model.URLShortenGroupModel;
import com.URLShorten.Model.URLShortenModel;
import com.URLShorten.Repository.URLAnalysisRepo;
import com.URLShorten.Repository.URLShortenerGroupRepo;
import com.URLShorten.Repository.URLShortenerRepo;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class URLShortenGroupService {
    private URLShortenerRepo urlShortenerRepo;
    private URLShortenerGroupRepo urlShortenerGroupRepo;
    private URLAnalysisRepo urlAnalyticsRepo;
    private final String URL = "https://localhost:8080/api/shorten/";

    @Autowired
    public URLShortenGroupService(URLShortenerRepo urlShortenerRepo,URLShortenerGroupRepo urlShortenerGroupRepo,URLAnalysisRepo urlAnalyticsRepo){
        this.urlShortenerRepo = urlShortenerRepo;
        this.urlShortenerGroupRepo = urlShortenerGroupRepo;
        this.urlAnalyticsRepo = urlAnalyticsRepo;
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    public URLShortenDTO shortenURLService(URLShortenInputDTO urlShortenInputDTO){
        String longURL = urlShortenInputDTO.getLongURL();
        String customAlias = urlShortenInputDTO.getCustomAlias();
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
    public URLShortenDTO shortenURLGroupService(URLShortenInputGroupDTO urlShortenInputDTO){
        List<String> longURLList = urlShortenInputDTO.getLongURL();
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
            
            List<URLShortenGroupModel> response = new ArrayList<>();
            if(longURLList != null){
                System.out.println("YES");
                for(String url : longURLList){
                    URLShortenGroupModel urlShortenModel = new URLShortenGroupModel();
                    urlShortenModel.setCustomAlias(customAlias);
                    urlShortenModel.setTopic(topic);
                    urlShortenModel.setLongURLs(url);
                    response.add(urlShortenModel);
                }
            }
            urlShortenerGroupRepo.saveAll(response);

            return urlResponse;
        }
        else{
            URLShortenDTO urlResponse = new URLShortenDTO();
            return urlResponse;
        }
    }
    public String redirectShortenGroupURL(String customAlias,String userAgent,HttpServletRequest request){
        String ip = request.getRemoteAddr();

        
        List<URLShortenGroupModel> reponseLinks = urlShortenerGroupRepo.findByCustomAlias(customAlias);
        List<String> longURLs = new ArrayList<>();
        
        for(URLShortenGroupModel response : reponseLinks){
            String url = response.getLongURLs();
            longURLs.add(url);
        }
        URLAnalyticsModel analysisModel = saveAnalysisRecord(userAgent, customAlias, ip,reponseLinks.get(0).getTopic());
        urlAnalyticsRepo.save(analysisModel);

        Gson gson = new Gson();
        String json = gson.toJson(longURLs);

        String script = String.format(
        """
            <script>
                const urls = %s;
                for (let iterator = 0; iterator < urls.length-1; iterator++) {
                    window.open(urls[iterator], '_blank');
                }
                setTimeout(() => {
                    window.location.href = urls[urls.length-1];
                }, 1000 * urls.length-1);
            </script>
        """,json);
        return script;
    }
}
