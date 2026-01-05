package com.nexfing.kycMobileApi.services;

import com.nexfing.kycMobileApi.DTOs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KycService implements KycServiceI {

    private final WebClient webClient;

    @Override
    public Mono<IdCardFields> extractFields(MultipartFile file) {
        return Mono.fromCallable(() -> buildMultipartBody(file))
                .flatMap(body -> webClient.post()
                        .uri("/ocr")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(IdCardFields.class))
                .doOnError(e -> log.error("OCR extraction failed for file: {}",
                        file.getOriginalFilename(), e))
                .onErrorMap(IOException.class,
                        e -> new RuntimeException("Failed to read file for OCR", e));
    }

    @Override
    public Mono<Boolean> detectGlasses(FacialImageRequest payload) {
        return executePostRequest("detect-glasses", payload, Boolean.class)
                .doOnError(e -> log.error("Glass detection failed", e));
    }

    @Override
    public Mono<String> checkAntiSpoofing(FacialImageRequest payload) {
        return executePostRequest("check-anti-spoofing", payload, String.class)
                .doOnError(e -> log.error("Anti-spoofing check failed", e));
    }

    @Override
    public Mono<String> livenessCheck(LivenessRequest payload) {
        return executePostRequest("verify-temporal-liveness", payload, String.class)
                .doOnError(e -> log.error("Liveness check failed", e));
    }

    @Override
    public Mono<String> verifyFaces(IDVerificationRequest payload) {
        return executePostRequest("verify-id", payload, String.class)
                .doOnError(e -> log.error("Face verification failed", e));
    }

    private <T> Mono<T> executePostRequest(String uri, Object payload, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class, e -> {
                    log.error("API request to {} failed with status {}: {}",
                            uri, e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.error(e);
                });
    }

    private MultipartBodyBuilder buildMultipartBody(MultipartFile file) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        return builder;
    }
}