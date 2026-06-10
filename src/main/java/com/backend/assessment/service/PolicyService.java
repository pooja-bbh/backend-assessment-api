package com.backend.assessment.service;

import com.backend.assessment.common.logging.CorrelationId;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.infrastructure.persistence.mapper.EntityToDomain;
import com.backend.assessment.infrastructure.persistence.repository.PolicyRepository;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyService.class);

    private final PolicyRepository policyRepository;
    private final EntityToDomain entityToDomain;

    public PolicyService(PolicyRepository policyRepository, EntityToDomain entityToDomain) {
        this.policyRepository = policyRepository;
        this.entityToDomain = entityToDomain;
    }

    public Page<Policy> getPolicies(Pageable pageable) {
        long startNanos = System.nanoTime();
        Page<Policy> policies = policyRepository.findAll(pageable).map(entityToDomain::toDomain);
        long durationMs = Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
        LOGGER.info("getPolicies completed. correlationId={} pageSize={} totalElements={} durationMs={}",
                MDC.get(CorrelationId.MDC_KEY), policies.getSize(), policies.getTotalElements(), durationMs);
        return policies;
    }
}
