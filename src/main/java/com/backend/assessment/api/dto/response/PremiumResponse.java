package com.backend.assessment.api.dto.response;

import java.math.BigDecimal;

public record PremiumResponse(BigDecimal amount, String currency) {
}
