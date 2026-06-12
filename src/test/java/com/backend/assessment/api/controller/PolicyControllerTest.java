package com.backend.assessment.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backend.assessment.api.dto.request.FlagPoliciesRequest;
import com.backend.assessment.api.dto.response.FlagResultResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicyStatsResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.api.mapper.DomainToResponseDto;
import com.backend.assessment.api.mapper.RequestDtoToDomain;
import com.backend.assessment.domain.models.FlagResult;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Money;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyFilter;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.PolicySummary;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.service.PolicyService;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PolicyControllerTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private final PolicyService policyService = mock(PolicyService.class);
    private final DomainToResponseDto domainToResponseDto = new DomainToResponseDto(
            Clock.fixed(REFERENCE_DATE.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC")));
    private final PolicyController controller = new PolicyController(
            policyService, domainToResponseDto, new RequestDtoToDomain());

    private Policy samplePolicy() {
        Money premium = new Money(new BigDecimal("1250.00"), Currency.getInstance("SGD"));
        return new Policy(3L, "POL-SG-1", "Jane Tan", Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE,
                premium, REFERENCE_DATE.minusYears(1), REFERENCE_DATE.plusDays(10), false);
    }

    @Test
    void getPolicies_whenServiceReturnsPolicies_returnsMappedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        when(policyService.getPolicies(any(PolicyFilter.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(samplePolicy()), pageable, 1));

        PagedPolicyResponse response = controller.getPolicies(null, null, null, null, null, null, pageable);

        assertEquals(1, response.content().size());
        assertEquals("Singapore", response.content().get(0).region());
        assertEquals("Life", response.content().get(0).lineOfBusiness());
        assertEquals(1L, response.totalElements());
    }

    @Test
    void getPolicy_whenServiceReturnsPolicy_returnsMappedSummary() {
        when(policyService.getPolicyById(3L)).thenReturn(samplePolicy());

        PolicySummaryResponse summary = controller.getPolicy(3L);

        assertEquals(3L, summary.id());
        assertEquals("POL-SG-1", summary.policyNumber());
    }

    @Test
    void getSummary_whenServiceReturnsSummary_mapsToStatsResponse() {
        Map<PolicyStatus, Long> counts = new EnumMap<>(PolicyStatus.class);
        counts.put(PolicyStatus.ACTIVE, 9L);
        when(policyService.getSummary())
                .thenReturn(new PolicySummary(counts, new EnumMap<>(LineOfBusiness.class), 5L));

        PolicyStatsResponse response = controller.getSummary();

        assertEquals(9L, response.countsByStatus().get("Active"));
        assertEquals(5L, response.expiringSoonCount());
    }

    @Test
    void flagPolicies_whenServiceFlags_returnsResultResponse() {
        when(policyService.flagPolicies(List.of(1L, 999L))).thenReturn(new FlagResult(2, 1, List.of(999L)));

        FlagResultResponse response = controller.flagPolicies(new FlagPoliciesRequest(List.of(1L, 999L)));

        assertEquals(2, response.requested());
        assertEquals(1, response.updated());
        assertEquals(List.of(999L), response.missingIds());
    }
}
