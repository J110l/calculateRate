package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.model.UserType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VipDiscountPolicyTest {

    @Test
    void should_apply_90_percent_discount_for_vip() {

        VipDiscountPolicy policy = new VipDiscountPolicy();

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 12, 0)
        );

        BigDecimal rate = new BigDecimal("1.00");

        BigDecimal result = policy.apply(rate, context);

        assertEquals(
                new BigDecimal("0.90").stripTrailingZeros(),
                result.stripTrailingZeros()
        );
    }

    @Test
    void should_not_apply_discount_for_normal_user() {

        VipDiscountPolicy policy = new VipDiscountPolicy();

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.NORMAL,
                LocalDateTime.of(2026, 6, 7, 12, 0)
        );

        BigDecimal rate = new BigDecimal("1.00");

        BigDecimal result = policy.apply(rate, context);

        assertEquals(new BigDecimal("1.00"), result);
    }
}