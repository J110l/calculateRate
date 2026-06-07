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


# Prompt3 - VipDiscountPolicy 实现（

## 🕒 时间

2026-06-07

---

## 🧩 问题背景

在实现 VoIP 费率计算引擎过程中，需要设计用户折扣规则：

* VIP 用户享受 9 折优惠
* NORMAL 用户无折扣
* 规则必须可扩展（DDD + Policy 模式）

---

## ❓ 我的问题

如何在 DDD 架构中实现 VIP 用户折扣策略？

要求：

* 避免 if-else 膨胀
* 保持领域逻辑纯净（Stateless）
* 支持未来扩展（如 Blacklist / Tier / Campaign）
* 可被 RateCalculator 编排调用

---

## 🧠 AI 设计建议

AI 建议采用 **Policy Pattern（策略模式）**：

```text
DiscountPolicy（抽象接口）
   ↓
VipDiscountPolicy
   ↓
Future: PromoDiscountPolicy / TierDiscountPolicy
```

核心思想：

> 每种折扣规则独立为一个 Policy，而不是集中在一个类中判断

---

## 🏗 最终设计决策

采用如下结构：

### 1️⃣ DiscountPolicy 接口

```java id="5b7c0e"
public interface DiscountPolicy {
    BigDecimal apply(BigDecimal rate, CallContext context);
}
```

---

### 2️⃣ VipDiscountPolicy 实现

```java id="9t2m7k"
public class VipDiscountPolicy implements DiscountPolicy {

    private static final BigDecimal VIP_RATE = new BigDecimal("0.9");

    @Override
    public BigDecimal apply(BigDecimal rate, CallContext context) {

        if (context.getUserType() == UserType.VIP) {
            return rate.multiply(VIP_RATE);
        }

        return rate;
    }
}
```

---

## 🧠 设计思考

### ✔ 为什么不直接写 if-else 在 Calculator？

避免：

* 规则集中爆炸
* 违反单一职责原则
* 后续无法扩展优惠体系

---

### ✔ 为什么用 Policy？

Policy 模式带来的好处：

* 规则解耦
* 可插拔
* 易测试
* 易组合（未来可 pipeline）

---

## 🧪 测试验证思路

测试覆盖两种情况：

* VIP → 0.9 倍
* NORMAL → 原价

---

## 🎯 结果

* VIP 折扣规则成功领域化
* DiscountPolicy 可扩展结构建立
* 为后续 NightDiscountPolicy / RateCalculator 铺路

---

## 📌 总结

该实现标志着：

> 从“过程式 if 判断” → “DDD Policy 领域建模”



# 📄 Prompt4 - TDD修复 BigDecimal 精度问题

## 🕒 时间

2026-06-07

---

## 🧩 问题现象

在执行 `VipDiscountPolicyTest` 时出现测试失败：

```
expected: <0.90> but was: <0.900>
```

---

## 🧠 AI分析

问题根因是 `BigDecimal` 的 scale（精度）差异：

* `0.90` 与 `0.900` 在数学意义上相等
* 但 `JUnit assertEquals` 会同时比较 **数值 + scale**
* 导致测试失败

---

## 🔍 修复方案（候选）

### 方案1：stripTrailingZeros（推荐次选）

```java
result.stripTrailingZeros()
```

✔ 去除末尾 0
✔ 统一精度
✔ 但仍依赖 BigDecimal 表示形式

---

### 方案2：compareTo（推荐最终方案）

```java
assertEquals(0, result.compareTo(new BigDecimal("0.90")));
```

✔ 只比较数值
✔ 忽略 scale
✔ 更符合业务语义
✔ 工业级标准写法

---

## 🎯 最终决策

采用：

> ✔ `compareTo` 方式进行断言

原因：

* 避免 BigDecimal 精度陷阱
* 更符合领域建模语义（只关心“费率值”）
* 提升测试鲁棒性

---

## 🛠 修改结果

在 `VipDiscountPolicyTest` 中统一修改断言：

```java
assertEquals(0, result.compareTo(new BigDecimal("0.90")));
```

---

## ✅ 结果

* 测试通过
* BigDecimal 精度问题解决
* 测试语义更加清晰稳定

---

## 📌 总结

该问题属于典型：

> Java BigDecimal 精度（scale）导致的测试断言陷阱问题

通过切换到 `compareTo`，避免了数值表示差异带来的误判。



# 📄 Prompt 5 - NightDiscountPolicy 夜间费率规则实现

## 🕒 时间

2026-06-07

---

## 🧩 问题背景

在 VoIP 计费系统中，需要实现夜间优惠规则：

* 时间范围：23:00 ~ 05:00（跨天）
* 夜间通话每分钟减免 0.02
* 最低费用不能低于 0

该规则需要作为独立 Policy 参与费率计算链路。

---

## ❓ 我的问题

如何在 DDD 设计中实现夜间费率规则？

要求：

* 支持跨天时间判断（23:00 → 05:00）
* 避免复杂 if-else 嵌套
* 保持 Policy 纯函数设计
* 可单元测试验证边界条件

---

## 🧠 AI 设计建议

AI 建议采用 Policy 模式实现时间规则：

```text id="kq3m9d"
NightDiscountPolicy
↓
输入：rate + CallContext
↓
输出：调整后的 rate
```

关键点：

* 使用 LocalTime 判断时间段
* 拆分跨天逻辑为两个区间：

  * 23:00 ~ 24:00
  * 00:00 ~ 05:00
* 单独封装 isNight() 方法

---

## 🏗 最终设计决策

采用如下实现方式：

### 1️⃣ 常量定义

```java id="8yq9lz"
private static final BigDecimal NIGHT_DISCOUNT = new BigDecimal("0.02");
private static final LocalTime START = LocalTime.of(23, 0);
private static final LocalTime END = LocalTime.of(5, 0);
```

---

### 2️⃣ 夜间判断逻辑

```java id="9h3q5k"
private boolean isNight(LocalTime time) {
    return !time.isBefore(START) || time.isBefore(END);
}
```

---

### 3️⃣ 核心实现逻辑

```java id="1q8w0m"
if (isNight(time)) {
    rate = rate.subtract(NIGHT_DISCOUNT);
}

if (rate.compareTo(BigDecimal.ZERO) < 0) {
    return BigDecimal.ZERO;
}
```

---

## 🧠 设计思考

### ✔ 为什么拆分 isNight？

* 提升可读性
* 避免时间逻辑散落
* 便于后续扩展（如节假日时间段）

---

### ✔ 为什么不写 if-else 直接判断？

避免：

* 复杂条件嵌套
* RateCalculator 膨胀
* 时间规则侵入业务主流程

---

### ✔ 为什么必须 clamp 到 0？

防止：

* discount 叠加导致负费率
* 计费异常数据

---

## 🧪 测试验证策略

覆盖三类情况：

* 夜间（23:00 后）
* 夜间（05:00 前）
* 白天（正常费率）

并使用：

```java id="4x9q2k"
assertEquals(0, result.compareTo(expected));
```

避免 BigDecimal scale 问题

---

## 🎯 结果

* 夜间规则成功领域化
* 时间逻辑独立封装
* Policy 体系进一步完善
* 为 RateCalculator 编排打基础

---

## 📌 总结

该实现标志着：

> ✔ 从“简单折扣规则” → “时间维度业务规则建模”



