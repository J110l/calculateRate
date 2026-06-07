package com.voipcalc.domain.service;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.model.UserType;
import com.voipcalc.domain.policy.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateCalculatorTest {

    @Test
    void should_calculate_china_vip_night_rate() {

        BaseRatePolicy baseRatePolicy = new DefaultBaseRatePolicy();

        List<DiscountPolicy> policies = List.of(
                new VipDiscountPolicy(),
                new NightDiscountPolicy()
        );

        RateCalculator calculator = new RateCalculator(baseRatePolicy, policies);

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 23, 30)
        );

        BigDecimal result = calculator.calculate(context);

        assertEquals(
                0,
                new BigDecimal("0.07").compareTo(result)
        );
    }

    @Test
    void should_apply_us_vip_rate() {

        BaseRatePolicy baseRatePolicy = new DefaultBaseRatePolicy();

        List<DiscountPolicy> policies = List.of(
                new VipDiscountPolicy(),
                new NightDiscountPolicy()
        );

        RateCalculator calculator = new RateCalculator(baseRatePolicy, policies);

        CallContext context = new CallContext(
                "10086",
                "+11234567890",
                UserType.VIP,
                LocalDateTime.of(2026, 6, 7, 14, 0)
        );

        BigDecimal result = calculator.calculate(context);

        assertEquals(
                0,
                new BigDecimal("0.045").compareTo(result)
        );
    }
}