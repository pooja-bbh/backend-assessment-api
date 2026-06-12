package com.backend.assessment.api.mapper;

import com.backend.assessment.api.dto.request.FlagPoliciesRequest;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RequestDtoToDomain {

    public List<Long> toPolicyIds(FlagPoliciesRequest request) {
        return request.policyIds();
    }
}
