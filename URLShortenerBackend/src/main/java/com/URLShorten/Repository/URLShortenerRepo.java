package com.URLShorten.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.URLShorten.Model.URLShortenModel;

@RepositoryRestResource
public interface URLShortenerRepo extends JpaRepository<URLShortenModel,String>{
    @Query("SELECT u.customAlias FROM URLShortenModel u")
    List<String> findByCustomAlias();
}
