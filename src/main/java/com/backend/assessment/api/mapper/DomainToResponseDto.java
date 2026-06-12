package com.backend.assessment.api.mapper;

import com.backend.assessment.api.dto.response.FlagResultResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicyStatsResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.api.dto.response.PremiumResponse;
import com.backend.assessment.domain.models.FlagResult;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.PolicySummary;
import com.backend.assessment.domain.models.Region;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class DomainToResponseDto {

    private static final Map<Region, String> REGION_DISPLAY_NAMES = buildRegionDisplayNames();
    private static final Map<PolicyStatus, String> STATUS_DISPLAY_NAMES = buildStatusDisplayNames();
    private static final Map<LineOfBusiness, String> LINE_OF_BUSINESS_DISPLAY_NAMES = buildLineOfBusinessDisplayNames();

    private final Clock clock;

    public DomainToResponseDto(Clock clock) {
        this.clock = clock;
    }

    public PagedPolicyResponse toPagedResponse(Page<Policy> page) {
        LocalDate referenceDate = LocalDate.now(clock);
        List<PolicySummaryResponse> content = page.getContent().stream()
                .map(policy -> toSummary(policy, referenceDate))
                .toList();
        return new PagedPolicyResponse(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    public PolicySummaryResponse toPolicyResponse(Policy policy) {
        return toSummary(policy, LocalDate.now(clock));
    }

    public PolicyStatsResponse toStatsResponse(PolicySummary summary) {
        Map<String, Long> countsByStatus = summary.countsByStatus().entrySet().stream()
                .collect(Collectors.toMap(entry -> STATUS_DISPLAY_NAMES.get(entry.getKey()), Map.Entry::getValue));
        Map<String, BigDecimal> premiumByLineOfBusiness = summary.totalPremiumByLineOfBusiness().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> LINE_OF_BUSINESS_DISPLAY_NAMES.get(entry.getKey()), Map.Entry::getValue));
        return new PolicyStatsResponse(countsByStatus, premiumByLineOfBusiness, summary.expiringSoonCount());
    }

    public FlagResultResponse toFlagResultResponse(FlagResult result) {
        return new FlagResultResponse(result.requested(), result.updated(), result.missingIds());
    }

    private PolicySummaryResponse toSummary(Policy policy, LocalDate referenceDate) {
        PremiumResponse premium = new PremiumResponse(
                policy.premium().amount(),
                policy.premium().currency().getCurrencyCode());
        return new PolicySummaryResponse(
                policy.id(),
                policy.policyNumber(),
                policy.holderName(),
                REGION_DISPLAY_NAMES.get(policy.region()),
                LINE_OF_BUSINESS_DISPLAY_NAMES.get(policy.lineOfBusiness()),
                STATUS_DISPLAY_NAMES.get(policy.status()),
                premium,
                policy.startDate(),
                policy.endDate(),
                policy.isExpiringSoon(referenceDate),
                policy.flaggedForReview());
    }

    private static Map<Region, String> buildRegionDisplayNames() {
        Map<Region, String> names = new EnumMap<>(Region.class);
        names.put(Region.SG, "Singapore");
        names.put(Region.HK, "Hong Kong");
        names.put(Region.AU, "Australia");
        names.put(Region.IN, "India");
        names.put(Region.JP, "Japan");
        return Collections.unmodifiableMap(names);
    }

    private static Map<PolicyStatus, String> buildStatusDisplayNames() {
        Map<PolicyStatus, String> names = new EnumMap<>(PolicyStatus.class);
        names.put(PolicyStatus.ACTIVE, "Active");
        names.put(PolicyStatus.LAPSED, "Lapsed");
        return Collections.unmodifiableMap(names);
    }

    private static Map<LineOfBusiness, String> buildLineOfBusinessDisplayNames() {
        Map<LineOfBusiness, String> names = new EnumMap<>(LineOfBusiness.class);
        names.put(LineOfBusiness.LIFE, "Life");
        names.put(LineOfBusiness.HEALTH, "Health");
        names.put(LineOfBusiness.MOTOR, "Motor");
        names.put(LineOfBusiness.PROPERTY, "Property");
        names.put(LineOfBusiness.TRAVEL, "Travel");
        names.put(LineOfBusiness.MARINE, "Marine");
        names.put(LineOfBusiness.LIABILITY, "Liability");
        return Collections.unmodifiableMap(names);
    }
}
