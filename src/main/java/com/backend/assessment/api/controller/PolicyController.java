package com.backend.assessment.api.controller;

import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.mapper.DomainToResponseDto;
import com.backend.assessment.domain.models.Policy;
import com.backend.assessment.service.PolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PolicyController.POLICIES_PATH)
public class PolicyController {

    static final String POLICIES_PATH = "/api/policies";
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final PolicyService policyService;
    private final DomainToResponseDto domainToResponseDto;

    public PolicyController(PolicyService policyService, DomainToResponseDto domainToResponseDto) {
        this.policyService = policyService;
        this.domainToResponseDto = domainToResponseDto;
    }

    @GetMapping
    public PagedPolicyResponse getPolicies(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<Policy> policies = policyService.getPolicies(pageable);
        return domainToResponseDto.toPagedResponse(policies);
    }
}
