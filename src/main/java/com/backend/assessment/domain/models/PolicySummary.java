package com.backend.assessment.domain.models;

import java.math.BigDecimal;
import java.util.Map;

public record PolicySummary(
        Map<PolicyStatus, Long> countsByStatus,
        Map<LineOfBusiness, BigDecimal> totalPremiumByLineOfBusiness,
        long expiringSoonCount) {
}
