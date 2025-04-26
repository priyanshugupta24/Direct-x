package com.URLShorten.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.URLShorten.Model.URLAnalyticsModel;

@RepositoryRestResource
public interface URLAnalysisRepo extends JpaRepository<URLAnalyticsModel,String>{
    List<URLAnalyticsModel> findByCustomAlias(String customAlias);
    List<URLAnalyticsModel> findByTopic(String topic);
    int countByCustomAlias(String customAlias);
    int countByTopic(String topic);
}
