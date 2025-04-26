package com.URLShorten.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URLShortenInputDTO {
    private String longURL;
    private String customAlias;
    private String topic;
}
