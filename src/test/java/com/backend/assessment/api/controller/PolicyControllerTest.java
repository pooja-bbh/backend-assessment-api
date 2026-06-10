package com.backend.assessment.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.mapper.DomainToResponseDto;
import com.backend.assessment.domain.models.Money;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.service.PolicyService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PolicyControllerTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private final DomainToResponseDto mapper = new DomainToResponseDto(
            Clock.fixed(REFERENCE_DATE.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC")));

    private Policy samplePolicy() {
        Money premium = new Money(new BigDecimal("1250.00"), Currency.getInstance("SGD"));
        return new Policy("POL-SG-1", "Jane Tan", Region.SG, PolicyStatus.ACTIVE,
                premium, REFERENCE_DATE.minusYears(1), REFERENCE_DATE.plusDays(10));
    }

    private PolicyService stubServiceReturning(Page<Policy> page) {
        return new PolicyService(null, null) {
            @Override
            public Page<Policy> getPolicies(Pageable pageable) {
                return page;
            }
        };
    }

    @Test
    void getPolicies_whenServiceReturnsPolicies_returnsMappedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Policy> page = new PageImpl<>(List.of(samplePolicy()), pageable, 1);
        PolicyController controller = new PolicyController(stubServiceReturning(page), mapper);

        PagedPolicyResponse response = controller.getPolicies(pageable);

        assertEquals(1, response.content().size());
        assertEquals("POL-SG-1", response.content().get(0).policyNumber());
        assertEquals("Singapore", response.content().get(0).region());
        assertEquals("Active", response.content().get(0).status());
        assertEquals(1L, response.totalElements());
    }

    @Test
    void getPolicies_whenServiceReturnsEmptyPage_returnsEmptyContent() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Policy> page = new PageImpl<>(List.of(), pageable, 0);
        PolicyController controller = new PolicyController(stubServiceReturning(page), mapper);

        PagedPolicyResponse response = controller.getPolicies(pageable);

        assertTrue(response.content().isEmpty());
        assertEquals(0L, response.totalElements());
    }
}
