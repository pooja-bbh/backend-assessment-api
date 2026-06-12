package com.backend.assessment.domain.models;

import java.time.LocalDate;
import java.util.Objects;

public record Policy(
        Long id,
        String policyNumber,
        String holderName,
        Region region,
        LineOfBusiness lineOfBusiness,
        PolicyStatus status,
        Money premium,
        LocalDate startDate,
        LocalDate endDate,
        boolean flaggedForReview) {

    private static final long EXPIRING_SOON_WINDOW_DAYS = 30L;

    public Policy {
        Objects.requireNonNull(policyNumber, "policyNumber must not be null");
        Objects.requireNonNull(holderName, "holderName must not be null");
        Objects.requireNonNull(region, "region must not be null");
        Objects.requireNonNull(lineOfBusiness, "lineOfBusiness must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(premium, "premium must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
    }

    public static LocalDate expiringSoonCutoff(LocalDate referenceDate) {
        Objects.requireNonNull(referenceDate, "referenceDate must not be null");
        return referenceDate.plusDays(EXPIRING_SOON_WINDOW_DAYS);
    }

    public boolean isExpiringSoon(LocalDate referenceDate) {
        LocalDate windowEnd = expiringSoonCutoff(referenceDate);
        return !endDate.isBefore(referenceDate) && !endDate.isAfter(windowEnd);
    }
}
