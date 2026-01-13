package com.nexfing.kycMobileApi.Controllers;

import com.nexfing.kycMobileApi.DTOs.*;
import com.nexfing.kycMobileApi.services.KycServiceI;
import com.nexfing.kycMobileApi.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KycController {

    private final KycServiceI kycService;
    private final SubmissionService submissionService;

    @PostMapping("/ocr")
    public Mono<IdCardFields> extractFields(@RequestParam("image") MultipartFile file) {
        return kycService.extractFields(file);
        //fff
    }

    @PostMapping("/detect-glasses")
    public Mono<Boolean> detectGlasses(@RequestBody FacialImageRequest payload) {
        return kycService.detectGlasses(payload);
    }

    @PostMapping("/check-anti-spoofing")
    public Mono<String> checkAntiSpoofing(@RequestBody FacialImageRequest payload) {
        return kycService.checkAntiSpoofing(payload);
    }

    @PostMapping("/verify-temporal-liveness")
    public Mono<String> livenessCheck(@RequestBody LivenessRequest payload) {
        return kycService.livenessCheck(payload);
    }

    @PostMapping("/verify-id")
    public Mono<String> verify(@RequestBody IDVerificationRequest payload) {
        return kycService.verifyFaces(payload);
    }

    @PostMapping("/submit/{userId}")
    public Mono<String> submitMobile(@PathVariable Integer userId, @RequestBody SubmissionRequest payload) {
        return submissionService.submit(userId, payload);
    }
}
