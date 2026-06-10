package com.backend.assessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.infrastructure.persistence.entity.PolicyEntity;
import com.backend.assessment.infrastructure.persistence.mapper.EntityToDomain;
import com.backend.assessment.infrastructure.persistence.repository.PolicyRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PolicyServiceTest {

    private final PolicyRepository policyRepository = Mockito.mock(PolicyRepository.class);
    private final PolicyService policyService = new PolicyService(policyRepository, new EntityToDomain());

    private PolicyEntity sampleEntity() {
        return new PolicyEntity("POL-SG-1", "Jane Tan", Region.SG, PolicyStatus.ACTIVE,
                new BigDecimal("1250.00"), "SGD", LocalDate.of(2025, 1, 1), LocalDate.of(2027, 1, 1));
    }

    @Test
    void getPolicies_whenRepositoryReturnsPage_returnsMappedDomainPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(policyRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(sampleEntity()), pageable, 1));

        Page<Policy> result = policyService.getPolicies(pageable);

        assertEquals(1, result.getContent().size());
        Policy policy = result.getContent().get(0);
        assertEquals("POL-SG-1", policy.policyNumber());
        assertEquals(Region.SG, policy.region());
        assertEquals("SGD", policy.premium().currency().getCurrencyCode());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void getPolicies_whenRepositoryEmpty_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(policyRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        Page<Policy> result = policyService.getPolicies(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0L, result.getTotalElements());
    }
}
