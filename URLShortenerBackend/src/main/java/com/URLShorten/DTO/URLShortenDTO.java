package com.URLShorten.DTO;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class URLShortenDTO {
    private LocalDateTime localDateTime;
    private String shortURL;
    private String message;
}
