package com.backend.assessment.infrastructure.persistence.repository;

import com.backend.assessment.domain.models.LineOfBusiness;
import java.math.BigDecimal;

public interface LineOfBusinessPremium {

    LineOfBusiness getLineOfBusiness();

    BigDecimal getTotalPremium();
}
