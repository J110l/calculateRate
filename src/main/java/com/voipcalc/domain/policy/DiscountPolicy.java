package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;

import java.math.BigDecimal;


public interface DiscountPolicy {

    BigDecimal apply(BigDecimal rate, CallContext context);
}