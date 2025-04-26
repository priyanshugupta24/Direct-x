package com.URLShorten.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URLShortenInputGroupDTO {
    private String customAlias;
    private List<String> longURL;
    private String topic;
}
