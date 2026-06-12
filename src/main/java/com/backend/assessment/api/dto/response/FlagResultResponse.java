package com.backend.assessment.api.dto.response;

import java.util.List;

public record FlagResultResponse(int requested, int updated, List<Long> missingIds) {
}
