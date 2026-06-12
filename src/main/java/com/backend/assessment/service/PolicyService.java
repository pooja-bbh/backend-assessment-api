package com.backend.assessment.service;

import com.backend.assessment.common.exception.ResourceNotFoundException;
import com.backend.assessment.common.logging.CorrelationId;
import com.backend.assessment.domain.models.FlagResult;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyFilter;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.PolicySummary;
import com.backend.assessment.infrastructure.persistence.mapper.EntityToDomain;
import com.backend.assessment.infrastructure.persistence.repository.LineOfBusinessPremium;
import com.backend.assessment.infrastructure.persistence.repository.PolicyRepository;
import com.backend.assessment.infrastructure.persistence.repository.StatusCount;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolicyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyService.class);
    private static final String POLICY_NOT_FOUND_MESSAGE = "Policy not found: %s";

    private final PolicyRepository policyRepository;
    private final EntityToDomain entityToDomain;
    private final Clock clock;

    public PolicyService(PolicyRepository policyRepository, EntityToDomain entityToDomain, Clock clock) {
        this.policyRepository = policyRepository;
        this.entityToDomain = entityToDomain;
        this.clock = clock;
    }

    public Page<Policy> getPolicies(PolicyFilter filter, Pageable pageable) {
        long startNanos = System.nanoTime();
        Page<Policy> policies = policyRepository.search(
                filter.status(), filter.lineOfBusiness(), filter.region(),
                filter.endDateFrom(), filter.endDateTo(), filter.searchTerm(), pageable)
                .map(entityToDomain::toDomain);
        long durationMs = Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
        LOGGER.info("getPolicies completed. correlationId={} pageSize={} totalElements={} durationMs={}",
                MDC.get(CorrelationId.MDC_KEY), policies.getSize(), policies.getTotalElements(), durationMs);
        return policies;
    }

    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id)
                .map(entityToDomain::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(POLICY_NOT_FOUND_MESSAGE, id)));
    }

    @Transactional
    public FlagResult flagPolicies(List<Long> policyIds) {
        long startNanos = System.nanoTime();
        List<Long> existingIds = policyRepository.findExistingIds(policyIds);
        int updated = policyRepository.flagForReview(policyIds);
        List<Long> missingIds = policyIds.stream()
                .filter(id -> !existingIds.contains(id))
                .toList();
        long durationMs = Duration.ofNanos(System.nanoTime() - startNanos).toMillis();
        LOGGER.info("flagPolicies completed. correlationId={} requested={} updated={} durationMs={}",
                MDC.get(CorrelationId.MDC_KEY), policyIds.size(), updated, durationMs);
        return new FlagResult(policyIds.size(), updated, missingIds);
    }

    public PolicySummary getSummary() {
        Map<PolicyStatus, Long> countsByStatus = policyRepository.countGroupedByStatus().stream()
                .collect(Collectors.toMap(StatusCount::getStatus, StatusCount::getCount));
        Map<LineOfBusiness, BigDecimal> premiumByLineOfBusiness = policyRepository.sumPremiumGroupedByLineOfBusiness()
                .stream()
                .collect(Collectors.toMap(LineOfBusinessPremium::getLineOfBusiness, LineOfBusinessPremium::getTotalPremium));
        LocalDate today = LocalDate.now(clock);
        long expiringSoonCount = policyRepository.countByEndDateBetween(today, Policy.expiringSoonCutoff(today));
        return new PolicySummary(countsByStatus, premiumByLineOfBusiness, expiringSoonCount);
    }
}
