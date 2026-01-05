package com.nexfing.kycMobileApi.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SubmissionRequest {
    private Applicant applicant;
    private List<Document> documents;
    private List<Biometric> biometricCheck;

    @Data
    public static class Applicant {
        private String fullName;
        private String nationalId;
        private String phoneNumber;
        private String email;
        private LocalDate dateOfBirth;
        private String address;
        private String city;
        private String occupation;
        private String employer;
        private String country;
    }

    @Data
    public static class Document {
        private String type;
        private String imageBase64;
    }

    @Data
    public static class Biometric {
            private String selfieBase64;
            private BigDecimal livenessScore;
            private BigDecimal faceMatchScore;
        }
}
