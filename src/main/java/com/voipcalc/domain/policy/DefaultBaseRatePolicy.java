package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;

import java.math.BigDecimal;

public class DefaultBaseRatePolicy implements BaseRatePolicy {

    @Override
    public BigDecimal getRate(CallContext context) {

        String callee = context.getCallee();

        if (callee.startsWith("+86")) {
            return new BigDecimal("0.10");
        }

        if (callee.startsWith("+1")) {
            return new BigDecimal("0.05");
        }

        return new BigDecimal("0.50");
    }
}