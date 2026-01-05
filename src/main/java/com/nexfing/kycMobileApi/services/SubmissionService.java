package com.nexfing.kycMobileApi.services;

import com.nexfing.kycMobileApi.DTOs.SubmissionRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class SubmissionService{

    private final WebClient backOfficeWebClient;

    public SubmissionService(@Qualifier("backOfficeWebClient") WebClient backOfficeWebClient) {
        this.backOfficeWebClient = backOfficeWebClient;
    }

    public Mono<String> submit(Integer userId, SubmissionRequest payload) {

        return backOfficeWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/mobile/submit/{userId}")
                        .build(userId))
                .bodyValue(payload)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new IllegalArgumentException("Client error: " + body))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new IllegalStateException("BackOffice error: " + body))
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))
                .retryWhen(
                        Retry.fixedDelay(2, Duration.ofSeconds(1))
                                .filter(ex -> ex instanceof WebClientRequestException)
                );
    }
}
