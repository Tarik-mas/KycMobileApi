package com.nexfing.kycMobileApi.DTOs;

import lombok.Builder;

@Builder
public record IdCardFields(
        String firstName,
        String lastName,
        String dateOfBirth,
        String documentNumber,
        String expiryDate,
        String placeOfBirth
) {}

