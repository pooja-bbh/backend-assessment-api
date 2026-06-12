package com.backend.assessment.infrastructure.persistence.mapper;

import com.backend.assessment.domain.models.Money;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.infrastructure.persistence.entity.PolicyEntity;
import java.util.Currency;
import org.springframework.stereotype.Component;

@Component
public class EntityToDomain {

    public Policy toDomain(PolicyEntity entity) {
        Money premium = new Money(
                entity.getPremiumAmount(),
                Currency.getInstance(entity.getPremiumCurrency()));
        return new Policy(
                entity.getId(),
                entity.getPolicyNumber(),
                entity.getHolderName(),
                entity.getRegion(),
                entity.getLineOfBusiness(),
                entity.getStatus(),
                premium,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isFlaggedForReview());
    }
}
