package com.URLShorten.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.URLShorten.DTO.AnalysisOutputDTO;
import com.URLShorten.DTO.AnalysisOutputOverallDTO;
import com.URLShorten.Service.URLAnalysisService;

@RestController
@RequestMapping("/api/analytics")
public class AnalysisController {
    private final URLAnalysisService urlAnalysisService; 
    @Autowired
    public AnalysisController(URLAnalysisService urlAnalysisService){
        this.urlAnalysisService = urlAnalysisService;
    }
    @GetMapping("/{alias}")
    ResponseEntity<AnalysisOutputDTO> getSingleLinkAnalysis(@PathVariable String alias){
        return ResponseEntity.ok().body(urlAnalysisService.singleLinkAnalysis(alias));
    }
    @GetMapping("/topic/{topic}")
    ResponseEntity<AnalysisOutputDTO> getTopicAnalysis(@PathVariable String topic){
        return ResponseEntity.ok().body(urlAnalysisService.topicAnalysis(topic));
    }
    @GetMapping("/overall")
    ResponseEntity<AnalysisOutputOverallDTO> getOverallAnalysis(){
        return ResponseEntity.ok().body(urlAnalysisService.overallAnalysis());
    }
}
