package com.backend.assessment.api.dto.response;

import java.time.LocalDate;

public record PolicySummaryResponse(
        Long id,
        String policyNumber,
        String holderName,
        String region,
        String lineOfBusiness,
        String status,
        PremiumResponse premium,
        LocalDate startDate,
        LocalDate endDate,
        boolean isExpiringSoon,
        boolean flaggedForReview) {
}
