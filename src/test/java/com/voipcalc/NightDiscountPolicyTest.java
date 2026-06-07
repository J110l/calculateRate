package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.model.UserType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NightDiscountPolicyTest {

    @Test
    void should_apply_night_discount_after_23() {

        NightDiscountPolicy policy = new NightDiscountPolicy();

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 23, 30)
        );

        BigDecimal result = policy.apply(new BigDecimal("1.00"), context);

        assertEquals(0, result.compareTo(new BigDecimal("0.98")));
    }

    @Test
    void should_apply_night_discount_before_05() {

        NightDiscountPolicy policy = new NightDiscountPolicy();

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 4, 30)
        );

        BigDecimal result = policy.apply(new BigDecimal("1.00"), context);

        assertEquals(0, result.compareTo(new BigDecimal("0.98")));
    }

    @Test
    void should_not_apply_discount_in_daytime() {

        NightDiscountPolicy policy = new NightDiscountPolicy();

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 12, 0)
        );

        BigDecimal result = policy.apply(new BigDecimal("1.00"), context);

        assertEquals(0, result.compareTo(new BigDecimal("1.00")));
    }
}