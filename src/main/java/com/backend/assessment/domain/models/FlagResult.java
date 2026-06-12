package com.backend.assessment.domain.models;

import java.util.List;

public record FlagResult(int requested, int updated, List<Long> missingIds) {
}
