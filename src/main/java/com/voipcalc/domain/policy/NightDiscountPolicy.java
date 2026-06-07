package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;

import java.math.BigDecimal;
import java.time.LocalTime;

public class NightDiscountPolicy implements DiscountPolicy {

    private static final BigDecimal NIGHT_DISCOUNT = new BigDecimal("0.02");

    private static final LocalTime START = LocalTime.of(23, 0);
    private static final LocalTime END = LocalTime.of(5, 0);

    @Override
    public BigDecimal apply(BigDecimal rate, CallContext context) {

        LocalTime time = context.getStartTime().toLocalTime();

        if (isNight(time)) {
            rate = rate.subtract(NIGHT_DISCOUNT);
        }

        // 防止负数
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return rate;
    }

    private boolean isNight(LocalTime time) {

        // 23:00 - 24:00
        boolean afterNightStart = !time.isBefore(START);

        // 00:00 - 05:00
        boolean beforeNightEnd = time.isBefore(END);

        return afterNightStart || beforeNightEnd;
    }
}