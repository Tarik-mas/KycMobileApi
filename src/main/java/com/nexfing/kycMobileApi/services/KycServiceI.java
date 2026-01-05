package com.nexfing.kycMobileApi.services;

import com.nexfing.kycMobileApi.DTOs.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface KycServiceI {
    Mono<IdCardFields> extractFields(MultipartFile file);
    Mono<Boolean> detectGlasses(FacialImageRequest payload);
    Mono<String> checkAntiSpoofing(FacialImageRequest payload);
    Mono<String> livenessCheck(LivenessRequest payload);
    Mono<String> verifyFaces(IDVerificationRequest payload);
}