package com.URLShorten.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.URLShorten.DTO.AnalysisOutputDTO;
import com.URLShorten.DTO.AnalysisOutputOverallDTO;
import com.URLShorten.DTO.OutputJsonDTO;
import com.URLShorten.Model.URLAnalyticsModel;
import com.URLShorten.Repository.URLAnalysisRepo;
import com.URLShorten.Repository.URLShortenerGroupRepo;
import com.URLShorten.Repository.URLShortenerRepo;
import com.google.gson.Gson;

@Service
public class URLAnalysisService {
    private URLShortenerRepo urlShortenerRepo;
    private URLShortenerGroupRepo urlShortenerGroupRepo;
    private URLAnalysisRepo urlAnalyticsRepo;

    @Autowired
    public URLAnalysisService(URLShortenerRepo urlShortenerRepo,URLShortenerGroupRepo urlShortenerGroupRepo,URLAnalysisRepo urlAnalyticsRepo){
        this.urlShortenerRepo = urlShortenerRepo;
        this.urlShortenerGroupRepo = urlShortenerGroupRepo;
        this.urlAnalyticsRepo = urlAnalyticsRepo;
    }
    public AnalysisOutputDTO singleLinkAnalysis(String customAlias){
        AnalysisOutputDTO response = new AnalysisOutputDTO();

        List<URLAnalyticsModel> urlAnalysisList = urlAnalyticsRepo.findByCustomAlias(customAlias);
        int totalClicks = urlAnalyticsRepo.countByCustomAlias(customAlias);
        OutputJsonDTO jsonObj = new OutputJsonDTO();

        for(URLAnalyticsModel urlAnalysis : urlAnalysisList){
            jsonObj.addToBrowserName(urlAnalysis.getBrowserName());
            jsonObj.addToIp(urlAnalysis.getIp());
            jsonObj.addToOsName(urlAnalysis.getOsName());
        }

        String json = new Gson().toJson(jsonObj);
        response.setTotalClicks(totalClicks);
        response.setUserDetails(json);

        return response;
    }
    private AnalysisOutputDTO topicAnalyisGetResponse(String topic){
        AnalysisOutputDTO response = new AnalysisOutputDTO();

        List<URLAnalyticsModel> urlAnalysisList = urlAnalyticsRepo.findByTopic(topic);
        int totalClicks = urlAnalyticsRepo.countByTopic(topic);
        OutputJsonDTO jsonObj = new OutputJsonDTO();

        for(URLAnalyticsModel urlAnalysis : urlAnalysisList){
            jsonObj.addToBrowserName(urlAnalysis.getBrowserName());
            jsonObj.addToIp(urlAnalysis.getIp());
            jsonObj.addToOsName(urlAnalysis.getOsName()); 
        }

        String json = new Gson().toJson(jsonObj);
        response.setTotalClicks(totalClicks);
        response.setUserDetails(json);
        return response;
    }
    public AnalysisOutputDTO topicAnalysis(String topic){
        AnalysisOutputDTO response = topicAnalyisGetResponse(topic);
        return response;
    }
    public AnalysisOutputOverallDTO overallAnalysis(){
        AnalysisOutputOverallDTO response = new AnalysisOutputOverallDTO();
        List<URLAnalyticsModel> urlAnalysisList = urlAnalyticsRepo.findAll();
        int totalClicks = urlAnalysisList.size();
        OutputJsonDTO jsonObj = new OutputJsonDTO();

        for(URLAnalyticsModel urlAnalysis : urlAnalysisList){
            jsonObj.addToBrowserName(urlAnalysis.getBrowserName());
            jsonObj.addToIp(urlAnalysis.getIp());
            jsonObj.addToOsName(urlAnalysis.getOsName()); 
        }

        String json = new Gson().toJson(jsonObj);
        response.setTotalClicks(totalClicks);
        response.setUserDetails(json);
        response.setTotalUrls(urlShortenerGroupRepo.findAll().size() + urlShortenerRepo.findAll().size());
        return response;
    }
}