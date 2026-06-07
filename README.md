````markdown
# VoIP 通话费率计算引擎（DDD + Policy 规则链实现）

## 📌 项目简介

本项目实现了一个基于 **DDD（领域驱动设计） + Policy 模式 + 规则链（Chain of Responsibility）** 的 VoIP 通话计费系统。

系统核心能力包括：

- 基础国家费率计算
- VIP 用户折扣
- 夜间时段费用减免
- 可扩展规则体系（无需修改核心计算逻辑）

目标不是“完成一个计算功能”，而是构建一个：

> ✔ 可演进  
> ✔ 可测试  
> ✔ 可扩展  
> ✔ 低耦合的计费规则引擎雏形  

---

## 🧠 架构设计理念

本项目严格遵循 DDD 思想，将系统拆分为：

### 1. 领域模型（Domain Model）
- `CallContext`
  - 封装通话上下文（主叫、被叫、用户类型、时间）
- `UserType`
  - 用户身份标识

---

### 2. 领域策略（Policy Layer）
所有业务规则被拆分为独立策略：

- `BaseRatePolicy`（基础费率）
- `VipDiscountPolicy`（VIP折扣）
- `NightDiscountPolicy`（夜间减免）

> 每个 Policy 只负责一件事，避免业务逻辑污染核心计算流程。

---

### 3. 领域服务（Domain Service）

- `RateCalculator`

职责：

- 组织规则执行顺序
- 组合 Policy
- 不关心具体业务逻辑实现

---

## 🔁 计算流程

```text
CallContext
    ↓
BaseRatePolicy（国家基础费率）
    ↓
VipDiscountPolicy（VIP 9折）
    ↓
NightDiscountPolicy（夜间减免）
    ↓
最终费用
````

---

## 🧼 代码洁癖设计细节（重点）

本项目在实现过程中，刻意强化以下“洁癖级工程约束”：

### 1. ❌ 消灭 if-else 业务膨胀

❌ 反例：

```java
if (country == CHINA) {}
if (userType == VIP) {}
if (isNight()) {}
```

✔ 改造为：

```java
List<DiscountPolicy> policies
```

每条规则独立封装。

---

### 2. 🧩 单一职责到“方法级别”

每个 Policy：

* 只处理一个规则
* 不关心其他规则
* 不做流程控制

---

### 3. 🧪 TDD 驱动设计

所有逻辑均通过测试倒推实现：

* 先写测试
* 再补实现
* 再重构结构

确保：

> ✔ 每一次设计变更都有验证约束

---

### 4. 🧱 强制 Stateless 设计

RateCalculator：

* 无状态
* 不保存计算结果
* 不缓存业务数据

保证：

> ✔ 可复用
> ✔ 可并发
> ✔ 可预测

---

### 5. 🔒 防御性数值处理

所有 BigDecimal 操作统一：

* 避免浮点误差
* 使用 compareTo
* 使用 max(0)

---

## 🤖 AI 协作方式（本项目的核心特点）

本项目不是“AI 写代码”，而是：

> **人类主导架构 + AI 执行实现 + 人类持续纠偏**

---

### 1. 架构主导（Human-driven）

我首先定义：

* DDD 分层结构
* Policy 模式拆分
* 规则链执行模型

确保 AI 不会“自由发挥成 if-else 工程”

---

### 2. 逐步约束 AI 输出

通过逐步提示 AI：

* “不要写 if-else”
* “必须拆 Policy”
* “必须可测试”
* “必须 stateless”

不断收敛输出质量

---

### 3. 测试驱动纠偏（TDD Loop）

通过 Maven test：

* 每一次失败 → 反推设计问题
* 每一个红灯 → 定位职责边界
* 每一次修复 → 强化结构一致性

形成闭环：

```text
需求 → 测试 → 实现 → 失败 → 修正设计 → 再测试
```

---

### 4. 持续重构而不是一次写完

所有设计都是演进式：

* 第一阶段：能跑
* 第二阶段：Policy 化
* 第三阶段：规则链化
* 第四阶段：准备规则引擎化

---

## 📊 当前系统能力

✔ 已实现：

* DDD 分层结构
* Policy 模式
* 规则链计算
* 单元测试覆盖
* 无状态服务设计

---

## 🚀 下一步演进方向

当前系统已具备“规则引擎雏形”，下一步可升级为：

### 1. Policy 自动注册（SPI / Spring）

替代：

```java
List.of(new VipDiscountPolicy(), new NightDiscountPolicy())
```

---

### 2. Policy 优先级排序

支持：

* @Order
* 动态执行顺序

---

### 3. 配置化计费规则

支持：

* JSON / DB 配置规则
* 无代码新增费率策略

---

### 4. 规则引擎化（高级演进）

升级为：

* Drools
* 自研 Rule Engine

---

## 🧾 总结

本项目的核心不是“计算费率”，而是：

> 用最小业务复杂度，验证一套可扩展规则引擎设计思想

通过：

* DDD 分层
* Policy 解耦
* TDD 驱动
* AI 协作约束

最终实现一个：

> ✔ 可演进的计费系统骨架，而不是一次性代码实现


