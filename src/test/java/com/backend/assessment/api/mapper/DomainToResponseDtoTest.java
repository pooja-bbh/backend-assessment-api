package com.backend.assessment.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.domain.models.Money;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
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

class DomainToResponseDtoTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private final DomainToResponseDto mapper = new DomainToResponseDto(
            Clock.fixed(REFERENCE_DATE.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC")));

    private Policy policy(Region region, PolicyStatus status, LocalDate endDate) {
        Money premium = new Money(new BigDecimal("1250.00"), Currency.getInstance("SGD"));
        return new Policy("POL-SG-1", "Jane Tan", region, status, premium,
                REFERENCE_DATE.minusYears(1), endDate);
    }

    private Page<Policy> singlePage(Policy policy) {
        return new PageImpl<>(List.of(policy), PageRequest.of(0, 10), 1);
    }

    @Test
    void toPagedResponse_whenRegionActive_mapsToDisplayNames() {
        PagedPolicyResponse response = mapper.toPagedResponse(
                singlePage(policy(Region.SG, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1))));
        PolicySummaryResponse summary = response.content().get(0);
        assertEquals("Singapore", summary.region());
        assertEquals("Active", summary.status());
    }

    @Test
    void toPagedResponse_whenStatusLapsed_mapsToLapsedDisplayName() {
        PagedPolicyResponse response = mapper.toPagedResponse(
                singlePage(policy(Region.HK, PolicyStatus.LAPSED, REFERENCE_DATE.plusYears(1))));
        assertEquals("Hong Kong", response.content().get(0).region());
        assertEquals("Lapsed", response.content().get(0).status());
    }

    @Test
    void toPagedResponse_whenEndDateWithinWindow_marksExpiringSoon() {
        PagedPolicyResponse response = mapper.toPagedResponse(
                singlePage(policy(Region.SG, PolicyStatus.ACTIVE, REFERENCE_DATE.plusDays(10))));
        assertTrue(response.content().get(0).isExpiringSoon());
    }

    @Test
    void toPagedResponse_whenEndDateBeyondWindow_isNotExpiringSoon() {
        PagedPolicyResponse response = mapper.toPagedResponse(
                singlePage(policy(Region.SG, PolicyStatus.ACTIVE, REFERENCE_DATE.plusDays(60))));
        assertFalse(response.content().get(0).isExpiringSoon());
    }

    @Test
    void toPagedResponse_whenPremiumProvided_mapsAmountAndCurrencyCode() {
        PagedPolicyResponse response = mapper.toPagedResponse(
                singlePage(policy(Region.SG, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1))));
        PolicySummaryResponse summary = response.content().get(0);
        assertEquals(new BigDecimal("1250.00"), summary.premium().amount());
        assertEquals("SGD", summary.premium().currency());
    }

    @Test
    void toPagedResponse_whenPageProvided_copiesPaginationMetadata() {
        Policy policy = policy(Region.JP, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1));
        Page<Policy> page = new PageImpl<>(List.of(policy), PageRequest.of(2, 5), 42);
        PagedPolicyResponse response = mapper.toPagedResponse(page);
        assertEquals(2, response.page());
        assertEquals(5, response.size());
        assertEquals(42L, response.totalElements());
        assertEquals(9, response.totalPages());
    }
}
