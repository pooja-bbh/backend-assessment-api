package com.backend.assessment.api.controller;

import com.backend.assessment.api.dto.request.FlagPoliciesRequest;
import com.backend.assessment.api.dto.response.FlagResultResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicyStatsResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.api.mapper.DomainToResponseDto;
import com.backend.assessment.api.mapper.RequestDtoToDomain;
import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.domain.models.PolicyFilter;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.service.PolicyService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PolicyController.POLICIES_PATH)
public class PolicyController {

    static final String POLICIES_PATH = "/api/v1/policies";
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final PolicyService policyService;
    private final DomainToResponseDto domainToResponseDto;
    private final RequestDtoToDomain requestDtoToDomain;

    public PolicyController(
            PolicyService policyService,
            DomainToResponseDto domainToResponseDto,
            RequestDtoToDomain requestDtoToDomain) {
        this.policyService = policyService;
        this.domainToResponseDto = domainToResponseDto;
        this.requestDtoToDomain = requestDtoToDomain;
    }

    @GetMapping
    public PagedPolicyResponse getPolicies(
            @RequestParam(required = false) PolicyStatus status,
            @RequestParam(required = false) LineOfBusiness lineOfBusiness,
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo,
            @RequestParam(required = false) String q,
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        PolicyFilter filter = new PolicyFilter(status, lineOfBusiness, region, endDateFrom, endDateTo, q);
        Page<Policy> policies = policyService.getPolicies(filter, pageable);
        return domainToResponseDto.toPagedResponse(policies);
    }

    @GetMapping("/summary")
    public PolicyStatsResponse getSummary() {
        return domainToResponseDto.toStatsResponse(policyService.getSummary());
    }

    @GetMapping("/{id}")
    public PolicySummaryResponse getPolicy(@PathVariable Long id) {
        return domainToResponseDto.toPolicyResponse(policyService.getPolicyById(id));
    }

    @PatchMapping("/flag")
    public FlagResultResponse flagPolicies(@RequestBody FlagPoliciesRequest request) {
        List<Long> policyIds = requestDtoToDomain.toPolicyIds(request);
        return domainToResponseDto.toFlagResultResponse(policyService.flagPolicies(policyIds));
    }
}
