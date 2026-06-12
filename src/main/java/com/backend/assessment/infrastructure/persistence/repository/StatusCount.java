package com.backend.assessment.infrastructure.persistence.repository;

import com.backend.assessment.domain.models.PolicyStatus;

public interface StatusCount {

    PolicyStatus getStatus();

    long getCount();
}
