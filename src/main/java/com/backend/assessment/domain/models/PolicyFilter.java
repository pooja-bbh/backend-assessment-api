package com.backend.assessment.domain.models;

import java.time.LocalDate;

public record PolicyFilter(
        PolicyStatus status,
        LineOfBusiness lineOfBusiness,
        Region region,
        LocalDate endDateFrom,
        LocalDate endDateTo,
        String searchTerm) {
}
