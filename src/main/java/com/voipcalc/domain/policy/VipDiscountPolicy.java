package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.model.UserType;

import java.math.BigDecimal;

public class VipDiscountPolicy implements DiscountPolicy {

    @Override
    public BigDecimal apply(BigDecimal rate, CallContext context) {

        if (context.getUserType() == UserType.VIP) {
            return rate.multiply(new BigDecimal("0.9"));
        }

        return rate;
    }

}