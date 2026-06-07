# Prompt 1 - DDD 领域建模与系统设计

## 时间

2026-06-07

## 背景

阅读需求后，发现业务逻辑本身较简单：

* 根据国家确定基础费率
* VIP 用户享受 9 折优惠
* 夜间时段减免 0.02 元

虽然功能简单，但题目明确要求采用 DDD（领域驱动设计）和 XP（极限编程）方式完成，因此优先考虑如何建立清晰的领域模型和可扩展的规则体系，而不是直接编写计算逻辑。

---

## 我的问题

如何使用 DDD 思想设计一个 VoIP 通话费率计算引擎？

要求：

* 核心逻辑保持无状态
* 支持单元测试
* 避免大量 if-else
* 未来新增优惠规则时无需修改核心计算逻辑

---

## AI 建议

AI 建议识别以下领域概念：

领域对象：

* CallContext
* UserType
* Country

领域服务：

* RateCalculator

业务规则：

* BaseRatePolicy
* VipDiscountPolicy
* NightDiscountPolicy

推荐采用 Policy（策略）模式组织业务规则。

费率计算流程：

CallContext

↓

RateCalculator

↓

BaseRatePolicy

↓

VipDiscountPolicy

↓

NightDiscountPolicy

↓

Final Rate

---

## 我的分析

最初考虑直接在 RateCalculator 中实现：

```java
if (country == CHINA) {
    ...
}

if (userType == VIP) {
    ...
}

if (isNightTime()) {
    ...
}
```

这种实现能够满足当前需求，但当未来增加：

* 节假日优惠
* 周末优惠
* 套餐优惠
* 国家专项优惠

时，RateCalculator 会不断膨胀，违反单一职责原则。

因此需要将业务规则从计算器中拆离。

---

## 我的决策

采纳 Policy 模式设计。

最终确定如下领域结构：

CallContext

↓

RateCalculator

↓

BaseRatePolicy

↓

Discount Policies

├── VipDiscountPolicy

└── NightDiscountPolicy

↓

Final Rate

其中：

* CallContext 负责封装通话上下文
* BaseRatePolicy 负责国家基础费率计算
* VipDiscountPolicy 负责身份优惠
* NightDiscountPolicy 负责夜间优惠
* RateCalculator 负责规则编排

---

## 落地产出

本次设计直接形成：

* DESIGN.md 中的 Domain Model
* DESIGN.md 中的 Domain Workflow
* 后续代码目录结构设计

预计目录：

src/main/java

├── domain/model
├── domain/policy
├── domain/service

---

## 结果

确定采用：

* DDD 领域建模
* Policy 模式
* Stateless Domain Service
* TDD 开发流程

作为项目整体设计基础。

# Prompt 2 - Base Rate Policy 开发
## 时间

2026-06-07

## 背景

开始实现第一条业务规则：

根据被叫号码国家码确定基础费率。

规则：

- +86 -> 0.10
- +1 -> 0.05
- Other -> 0.50

---

## 我的问题

如何以 TDD 方式实现 Base Rate Policy？

要求：

- 使用 JUnit5
- 遵循 XP Red-Green-Refactor
- 保持领域逻辑纯净

---

## AI 建议

先编写测试：

- should_return_china_rate
- should_return_usa_rate
- should_return_default_rate

然后实现：

- BaseRatePolicy
- DefaultBaseRatePolicy

通过号码前缀匹配国家。

---

## 我的决策

采用测试优先开发。

实现步骤：

1. 编写 China Test
2. 实现最小代码通过测试
3. 补充 USA Test
4. 补充 Default Test
5. 全部测试通过

---

## 测试结果

Tests run: 3
Failures: 0
Errors: 0

---

## 开发反思

AI 曾建议直接完成全部业务逻辑。

为了体现 XP，我选择按单个业务规则逐步实现，并在每个阶段形成独立 Git Commit。


