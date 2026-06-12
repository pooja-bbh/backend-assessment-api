package com.backend.assessment.api.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record PolicyStatsResponse(
        Map<String, Long> countsByStatus,
        Map<String, BigDecimal> totalPremiumByLineOfBusiness,
        long expiringSoonCount) {
}
