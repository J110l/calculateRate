package com.voipcalc.domain.service;

import com.voipcalc.domain.model.CallContext;
import com.voipcalc.domain.policy.BaseRatePolicy;
import com.voipcalc.domain.policy.DiscountPolicy;
import com.voipcalc.domain.policy.NightDiscountPolicy;

import java.math.BigDecimal;
import java.util.List;

public class RateCalculator {

    private final BaseRatePolicy baseRatePolicy;
    private final List<DiscountPolicy> discountPolicies;

    public RateCalculator(BaseRatePolicy baseRatePolicy,
                          List<DiscountPolicy> discountPolicies) {
        this.baseRatePolicy = baseRatePolicy;
        this.discountPolicies = discountPolicies;
    }

    public BigDecimal calculate(CallContext context) {

        // 1. 基础费率
        BigDecimal rate = baseRatePolicy.getRate(context);
        // 👇 加在这里（非常重要）
        System.out.println("BASE = " + rate);

        // 2. 逐个应用折扣策略（关键：扩展点）
        for (DiscountPolicy policy : discountPolicies) {
            rate = policy.apply(rate, context);

             // 👇 每一步都打印
            System.out.println(policy.getClass().getSimpleName() + " => " + rate);
        }

        // 3. 保证非负
        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return rate;
    }
}