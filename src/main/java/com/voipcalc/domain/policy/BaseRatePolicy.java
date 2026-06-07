package com.voipcalc.domain.policy;

import com.voipcalc.domain.model.CallContext;

import java.math.BigDecimal;

public interface BaseRatePolicy {

    BigDecimal getRate(CallContext context);
}