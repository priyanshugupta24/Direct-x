package com.URLShorten.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "URLShortenInfo")
public class URLShortenModel {
    @Id
    private String customAlias;
    private String longURL;
    private String shortURL;
    private String topic;
}
// BCNF