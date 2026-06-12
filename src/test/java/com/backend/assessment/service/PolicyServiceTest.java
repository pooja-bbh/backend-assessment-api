package com.backend.assessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backend.assessment.common.exception.ResourceNotFoundException;
import com.backend.assessment.domain.models.FlagResult;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyFilter;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.PolicySummary;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.infrastructure.persistence.entity.PolicyEntity;
import com.backend.assessment.infrastructure.persistence.mapper.EntityToDomain;
import com.backend.assessment.infrastructure.persistence.repository.LineOfBusinessPremium;
import com.backend.assessment.infrastructure.persistence.repository.PolicyRepository;
import com.backend.assessment.infrastructure.persistence.repository.StatusCount;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PolicyServiceTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private final PolicyRepository policyRepository = mock(PolicyRepository.class);
    private final PolicyService policyService = new PolicyService(policyRepository, new EntityToDomain(),
            Clock.fixed(REFERENCE_DATE.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC")));

    private PolicyEntity sampleEntity() {
        return new PolicyEntity("POL-SG-1", "Jane Tan", Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE,
                new BigDecimal("1250.00"), "SGD", LocalDate.of(2025, 1, 1), LocalDate.of(2027, 1, 1));
    }

    private PolicyFilter emptyFilter() {
        return new PolicyFilter(null, null, null, null, null, null);
    }

    @Test
    void getPolicies_whenRepositoryReturnsPage_returnsMappedDomainPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(policyRepository.search(any(), any(), any(), any(), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(sampleEntity()), pageable, 1));

        Page<Policy> result = policyService.getPolicies(emptyFilter(), pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("POL-SG-1", result.getContent().get(0).policyNumber());
        assertEquals(LineOfBusiness.LIFE, result.getContent().get(0).lineOfBusiness());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void getPolicyById_whenPolicyExists_returnsDomainPolicy() {
        when(policyRepository.findById(5L)).thenReturn(Optional.of(sampleEntity()));

        Policy policy = policyService.getPolicyById(5L);

        assertEquals("POL-SG-1", policy.policyNumber());
        assertEquals(Region.SG, policy.region());
    }

    @Test
    void getPolicyById_whenPolicyMissing_throwsResourceNotFound() {
        when(policyRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.getPolicyById(404L));
    }

    @Test
    void flagPolicies_whenSomeIdsMissing_reportsUpdatedAndMissing() {
        List<Long> ids = List.of(1L, 2L, 999L);
        when(policyRepository.findExistingIds(ids)).thenReturn(List.of(1L, 2L));
        when(policyRepository.flagForReview(ids)).thenReturn(2);

        FlagResult result = policyService.flagPolicies(ids);

        assertEquals(3, result.requested());
        assertEquals(2, result.updated());
        assertEquals(List.of(999L), result.missingIds());
    }

    @Test
    void getSummary_whenAggregatesReturned_buildsSummary() {
        StatusCount activeCount = mock(StatusCount.class);
        when(activeCount.getStatus()).thenReturn(PolicyStatus.ACTIVE);
        when(activeCount.getCount()).thenReturn(9L);
        LineOfBusinessPremium lifePremium = mock(LineOfBusinessPremium.class);
        when(lifePremium.getLineOfBusiness()).thenReturn(LineOfBusiness.LIFE);
        when(lifePremium.getTotalPremium()).thenReturn(new BigDecimal("110000.00"));
        when(policyRepository.countGroupedByStatus()).thenReturn(List.of(activeCount));
        when(policyRepository.sumPremiumGroupedByLineOfBusiness()).thenReturn(List.of(lifePremium));
        when(policyRepository.countByEndDateBetween(REFERENCE_DATE, REFERENCE_DATE.plusDays(30))).thenReturn(5L);

        PolicySummary summary = policyService.getSummary();

        assertEquals(9L, summary.countsByStatus().get(PolicyStatus.ACTIVE));
        assertEquals(new BigDecimal("110000.00"), summary.totalPremiumByLineOfBusiness().get(LineOfBusiness.LIFE));
        assertEquals(5L, summary.expiringSoonCount());
    }
}
