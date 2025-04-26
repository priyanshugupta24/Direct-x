package com.URLShorten.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisOutputOverallDTO {
    private int totalClicks;
    // private List<Integer> clicksByDate;
    private String userDetails;
    int totalUrls;
}
