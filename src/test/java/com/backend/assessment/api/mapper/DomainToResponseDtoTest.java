package com.backend.assessment.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backend.assessment.api.dto.response.FlagResultResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicyStatsResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.domain.models.FlagResult;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Money;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.PolicySummary;
import com.backend.assessment.domain.models.Region;
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

class DomainToResponseDtoTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private final DomainToResponseDto mapper = new DomainToResponseDto(
            Clock.fixed(REFERENCE_DATE.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneId.of("UTC")));

    private Policy policy(Region region, LineOfBusiness lineOfBusiness, PolicyStatus status, LocalDate endDate) {
        Money premium = new Money(new BigDecimal("1250.00"), Currency.getInstance("SGD"));
        return new Policy(7L, "POL-SG-1", "Jane Tan", region, lineOfBusiness, status, premium,
                REFERENCE_DATE.minusYears(1), endDate, true);
    }

    private Page<Policy> singlePage(Policy policy) {
        return new PageImpl<>(List.of(policy), PageRequest.of(0, 10), 1);
    }

    @Test
    void toPagedResponse_whenRegionLobStatusProvided_mapsToDisplayNames() {
        PolicySummaryResponse summary = mapper.toPagedResponse(
                singlePage(policy(Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1))))
                .content().get(0);
        assertEquals("Singapore", summary.region());
        assertEquals("Life", summary.lineOfBusiness());
        assertEquals("Active", summary.status());
    }

    @Test
    void toPagedResponse_whenStatusLapsedAndMarine_mapsDisplayNames() {
        PolicySummaryResponse summary = mapper.toPagedResponse(
                singlePage(policy(Region.AU, LineOfBusiness.MARINE, PolicyStatus.LAPSED, REFERENCE_DATE.plusYears(1))))
                .content().get(0);
        assertEquals("Australia", summary.region());
        assertEquals("Marine", summary.lineOfBusiness());
        assertEquals("Lapsed", summary.status());
    }

    @Test
    void toPagedResponse_whenPolicyHasIdAndFlag_copiesThem() {
        PolicySummaryResponse summary = mapper.toPagedResponse(
                singlePage(policy(Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1))))
                .content().get(0);
        assertEquals(7L, summary.id());
        assertTrue(summary.flaggedForReview());
    }

    @Test
    void toPagedResponse_whenEndDateWithinWindow_marksExpiringSoon() {
        PolicySummaryResponse summary = mapper.toPagedResponse(
                singlePage(policy(Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE, REFERENCE_DATE.plusDays(10))))
                .content().get(0);
        assertTrue(summary.isExpiringSoon());
    }

    @Test
    void toPagedResponse_whenEndDateBeyondWindow_isNotExpiringSoon() {
        PolicySummaryResponse summary = mapper.toPagedResponse(
                singlePage(policy(Region.SG, LineOfBusiness.LIFE, PolicyStatus.ACTIVE, REFERENCE_DATE.plusDays(60))))
                .content().get(0);
        assertFalse(summary.isExpiringSoon());
    }

    @Test
    void toPagedResponse_whenPageProvided_copiesPaginationMetadata() {
        Policy policy = policy(Region.JP, LineOfBusiness.TRAVEL, PolicyStatus.ACTIVE, REFERENCE_DATE.plusYears(1));
        Page<Policy> page = new PageImpl<>(List.of(policy), PageRequest.of(2, 5), 42);
        PagedPolicyResponse response = mapper.toPagedResponse(page);
        assertEquals(2, response.page());
        assertEquals(5, response.size());
        assertEquals(42L, response.totalElements());
        assertEquals(9, response.totalPages());
    }

    @Test
    void toStatsResponse_whenSummaryProvided_mapsKeysToDisplayNames() {
        Map<PolicyStatus, Long> counts = new EnumMap<>(PolicyStatus.class);
        counts.put(PolicyStatus.ACTIVE, 9L);
        counts.put(PolicyStatus.LAPSED, 3L);
        Map<LineOfBusiness, BigDecimal> premiums = new EnumMap<>(LineOfBusiness.class);
        premiums.put(LineOfBusiness.LIFE, new BigDecimal("110000.00"));
        PolicyStatsResponse response = mapper.toStatsResponse(new PolicySummary(counts, premiums, 5L));
        assertEquals(9L, response.countsByStatus().get("Active"));
        assertEquals(3L, response.countsByStatus().get("Lapsed"));
        assertEquals(new BigDecimal("110000.00"), response.totalPremiumByLineOfBusiness().get("Life"));
        assertEquals(5L, response.expiringSoonCount());
    }

    @Test
    void toFlagResultResponse_whenResultProvided_copiesFields() {
        FlagResultResponse response = mapper.toFlagResultResponse(new FlagResult(3, 2, List.of(999L)));
        assertEquals(3, response.requested());
        assertEquals(2, response.updated());
        assertEquals(List.of(999L), response.missingIds());
    }

    @Test
    void toPolicyResponse_whenPolicyProvided_mapsSingleSummary() {
        PolicySummaryResponse summary = mapper.toPolicyResponse(
                policy(Region.HK, LineOfBusiness.HEALTH, PolicyStatus.ACTIVE, REFERENCE_DATE.plusDays(10)));
        assertEquals("Hong Kong", summary.region());
        assertEquals("Health", summary.lineOfBusiness());
        assertTrue(summary.isExpiringSoon());
    }
}
