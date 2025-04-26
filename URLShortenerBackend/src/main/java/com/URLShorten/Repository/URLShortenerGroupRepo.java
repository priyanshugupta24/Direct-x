package com.URLShorten.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.URLShorten.Model.URLShortenGroupModel;

@RepositoryRestResource
public interface URLShortenerGroupRepo extends JpaRepository<URLShortenGroupModel,String>{
    @Query("SELECT u.customAlias FROM URLShortenGroupModel u")
    List<String> findByCustomAlias();
    List<URLShortenGroupModel> findByCustomAlias(String customAlias); 
}
