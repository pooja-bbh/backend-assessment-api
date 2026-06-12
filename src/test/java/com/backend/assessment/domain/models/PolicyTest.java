package com.backend.assessment.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class PolicyTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 6, 10);

    private Policy policyEndingOn(LocalDate endDate) {
        Money premium = new Money(new BigDecimal("100.00"), Currency.getInstance("SGD"));
        return new Policy(1L, "POL-SG-1", "Jane Tan", Region.SG, LineOfBusiness.LIFE,
                PolicyStatus.ACTIVE, premium, REFERENCE_DATE.minusYears(1), endDate, false);
    }

    @Test
    void isExpiringSoon_whenEndDateWithinWindow_returnsTrue() {
        assertTrue(policyEndingOn(REFERENCE_DATE.plusDays(10)).isExpiringSoon(REFERENCE_DATE));
    }

    @Test
    void isExpiringSoon_whenEndDateOnWindowBoundary_returnsTrue() {
        assertTrue(policyEndingOn(REFERENCE_DATE.plusDays(30)).isExpiringSoon(REFERENCE_DATE));
    }

    @Test
    void isExpiringSoon_whenEndDateAfterWindow_returnsFalse() {
        assertFalse(policyEndingOn(REFERENCE_DATE.plusDays(31)).isExpiringSoon(REFERENCE_DATE));
    }

    @Test
    void isExpiringSoon_whenPolicyAlreadyEnded_returnsFalse() {
        assertFalse(policyEndingOn(REFERENCE_DATE.minusDays(1)).isExpiringSoon(REFERENCE_DATE));
    }

    @Test
    void isExpiringSoon_whenReferenceDateNull_throwsNullPointerException() {
        Policy policy = policyEndingOn(REFERENCE_DATE.plusDays(5));
        assertThrows(NullPointerException.class, () -> policy.isExpiringSoon(null));
    }

    @Test
    void expiringSoonCutoff_whenGivenDate_returnsThirtyDaysLater() {
        assertEquals(REFERENCE_DATE.plusDays(30), Policy.expiringSoonCutoff(REFERENCE_DATE));
    }
}
