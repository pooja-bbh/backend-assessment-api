package com.backend.assessment.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class MoneyTest {

    private static final Currency SGD = Currency.getInstance("SGD");

    @Test
    void constructor_whenAmountNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Money(null, SGD));
    }

    @Test
    void constructor_whenCurrencyNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Money(BigDecimal.ONE, null));
    }

    @Test
    void accessors_whenValuesProvided_exposeSameValues() {
        BigDecimal amount = new BigDecimal("250.50");
        Money money = new Money(amount, SGD);
        assertEquals(amount, money.amount());
        assertEquals(SGD, money.currency());
    }
}
