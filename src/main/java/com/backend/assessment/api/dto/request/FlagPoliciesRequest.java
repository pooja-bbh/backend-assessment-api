package com.backend.assessment.api.dto.request;

import java.util.List;

public record FlagPoliciesRequest(List<Long> policyIds) {
}
