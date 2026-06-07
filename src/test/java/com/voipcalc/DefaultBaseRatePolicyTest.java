package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.model.UserType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultBaseRatePolicyTest {

    @Test
    void should_return_china_rate() {

        CallContext context = new CallContext(
                "10086",
                "+8613712345678",
                UserType.NORMAL,
                LocalDateTime.now()
        );

        DefaultBaseRatePolicy policy =
                new DefaultBaseRatePolicy();

        BigDecimal rate =
                policy.getRate(context);

        assertEquals(
                new BigDecimal("0.10"),
                rate
        );
    }
    @Test
    void should_return_usa_rate() {

        CallContext context = new CallContext(
                "10086",
                "+14155551234",
                UserType.NORMAL,
                LocalDateTime.now()
        );

        DefaultBaseRatePolicy policy =
                new DefaultBaseRatePolicy();

        BigDecimal rate =
                policy.getRate(context);

        assertEquals(
                new BigDecimal("0.05"),
                rate
        );
    }
    @Test
    void should_return_default_rate() {

        CallContext context = new CallContext(
                "10086",
                "+442012345678",
                UserType.NORMAL,
                LocalDateTime.now()
        );

        DefaultBaseRatePolicy policy =
                new DefaultBaseRatePolicy();

        BigDecimal rate =
                policy.getRate(context);

        assertEquals(
                new BigDecimal("0.50"),
                rate
        );
    }
}