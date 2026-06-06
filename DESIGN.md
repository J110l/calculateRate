# VoIPCalc-Core Design

## 1. Background

VoIPCalc-Core 是跨境 VoIP 话务系统中的核心费率计算引擎。

系统职责单一：

根据通话上下文（CallContext）计算最终每分钟通话费率。

输入：

* 主叫号码（Caller）
* 被叫号码（Callee）
* 用户身份（User Type）
* 通话开始时间（Start Time）

输出：

* Final Rate（最终每分钟费率）

---

# 2. Domain Analysis

## 2.1 Core Domain

本系统的核心领域为：

**VoIP Call Rate Calculation（VoIP 通话费率计算）**

系统仅关注费率计算，不涉及：

* 用户管理
* 账单生成
* 订单系统
* 支付系统
* 数据存储

因此核心业务逻辑可以保持纯粹、无状态和可测试。

---

## 2.2 Ubiquitous Language

为了保持业务语言与代码一致，定义如下统一领域术语：

| Business Term | Domain Object   |
| ------------- | --------------- |
| 通话            | Call            |
| 通话上下文         | CallContext     |
| 用户身份          | UserType        |
| 国家            | Country         |
| 基础费率          | Base Rate       |
| 优惠规则          | Discount Policy |
| 最终费率          | Final Rate      |

---

# 3. Domain Model

## 3.1 CallContext

表示一次通话的上下文信息。

属性：

| Field     | Description |
| --------- | ----------- |
| caller    | 主叫号码        |
| callee    | 被叫号码        |
| userType  | 用户身份        |
| startTime | 通话开始时间      |

职责：

* 封装一次通话的输入数据
* 作为费率计算的唯一输入对象

不负责：

* 费率计算
* 国家解析
* 优惠计算

---

## 3.2 UserType

用户身份。

枚举值：

```text
VIP
NORMAL
```

业务规则：

* VIP 用户享受 9 折优惠
* NORMAL 用户无优惠

---

## 3.3 Country

被叫号码所属国家。

枚举值：

```text
CHINA
USA
OTHER
```

业务规则：

| Country | Base Rate |
| ------- | --------- |
| CHINA   | 0.10      |
| USA     | 0.05      |
| OTHER   | 0.50      |

---

# 4. Domain Workflow

费率计算流程如下：

```text
                CallContext
                     │
                     ▼
              RateCalculator
                     │
        ┌────────────┴────────────┐
        ▼                         ▼

 BaseRatePolicy          Discount Policies
                                   │
                     ┌─────────────┴─────────────┐
                     ▼                           ▼

            VipDiscountPolicy      NightDiscountPolicy

                     │
                     ▼

                Final Rate
```

计算过程：

1. 根据被叫号码识别国家
2. 获取基础费率
3. 应用用户身份折扣
4. 应用夜间优惠
5. 返回最终费率

---

# 5. Business Rules

## Rule 1 - Base Rate

根据被叫号码国家代码确定基础费率。

| Country Code | Rate |
| ------------ | ---- |
| +86          | 0.10 |
| +1           | 0.05 |
| Others       | 0.50 |

示例：

```text
+8613712345678

↓

China

↓

0.10
```

---

## Rule 2 - VIP Discount

VIP 用户享受 9 折优惠。

计算公式：

```text
rate × 0.9
```

示例：

```text
0.10

↓

0.09
```

---

## Rule 3 - Night Discount

夜间时段：

```text
23:00 <= time < 05:00
```

优惠规则：

```text
rate - 0.02
```

最低价格限制：

```text
rate >= 0
```

示例：

```text
0.09

↓

0.07
```

---

# 6. Example Calculation

输入：

```text
callee = +8613712345678

userType = VIP

startTime = 23:30
```

计算过程：

```text
BaseRatePolicy

China

↓

0.10

VipDiscountPolicy

↓

0.09

NightDiscountPolicy

↓

0.07
```

输出：

```text
Final Rate = 0.07
```

---

# 7. Domain Service Design

## RateCalculator

RateCalculator 是核心领域服务。

职责：

* 协调所有费率规则
* 控制规则执行顺序
* 输出最终费率

接口定义：

```java
BigDecimal calculateRate(CallContext context);
```

特点：

* 无状态（Stateless）
* 无副作用（Side Effect Free）
* 纯函数（Pure Function）

相同输入始终得到相同输出。

---

# 8. Design Principles

## Single Responsibility Principle

每条业务规则独立封装。

例如：

```text
BaseRatePolicy

VipDiscountPolicy

NightDiscountPolicy
```

每个策略只负责一种业务规则。

---

## Open Closed Principle

新增优惠规则时无需修改已有逻辑。

例如未来新增：

```text
HolidayDiscountPolicy

WeekendDiscountPolicy

NewUserDiscountPolicy
```

仅需新增 Policy 实现即可。

---

## Domain Driven Design

业务概念直接映射为领域模型。

```text
CallContext

UserType

Country

RateCalculator

Policy
```

保持代码与业务语言一致。

---

## Testability

所有业务规则均可独立测试。

例如：

```text
BaseRatePolicyTest

VipDiscountPolicyTest

NightDiscountPolicyTest

RateCalculatorTest
```

便于采用 TDD 开发模式。

---

# 9. Future Extension

未来若新增规则：

* 节假日优惠
* 周末优惠
* 套餐优惠
* 国家专项优惠

仅需新增对应 Policy。

无需修改已有核心计算逻辑。

该设计满足业务扩展需求，并保持核心领域模型稳定。
