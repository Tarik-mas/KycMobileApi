package com.nexfing.kycMobileApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${fastapi.url}")
    private String fastApiUrl;

    @Value("${backoffice.url}")
    private String backOfficeUrl;

    @Bean
    @Primary
    public WebClient fastApiWebClient() {
        return WebClient.builder()
                .baseUrl(fastApiUrl)
                .build();
    }

    @Bean("backOfficeWebClient")
    public WebClient backOfficeWebClient() {
        return WebClient.builder()
                .baseUrl(backOfficeUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
