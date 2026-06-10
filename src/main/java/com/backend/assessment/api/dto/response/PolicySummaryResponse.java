package com.backend.assessment.api.dto.response;

import java.time.LocalDate;

public record PolicySummaryResponse(
        String policyNumber,
        String holderName,
        String region,
        String status,
        PremiumResponse premium,
        LocalDate startDate,
        LocalDate endDate,
        boolean isExpiringSoon) {
}
