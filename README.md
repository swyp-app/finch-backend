## 📐 아키텍처 개요

프로젝트는 **헥사고날 아키텍처(Hexagonal Architecture, Ports & Adapters)** 와 **도메인 주도 설계(DDD)** 를 기반으로 설계하었습니다.

### 핵심 설계 원칙

1. **의존성 역전 원칙(DIP)**: 내부 레이어는 외부 레이어에 의존하지 않으며, 인터페이스(Port)를 통해서만 통신
2. **도메인 중심 설계**: 비즈니스 로직은 도메인 계층에 집중되어 있으며, 외부 기술 스택과 독립적
3. **관심사의 분리**: 각 계층은 명확한 책임을 가지며, 계층 간 경계가 엄격히 관리됨

---

## 🏗️ 프로젝트 구조

```
src/main/kotlin/com/finch/
├── api/
│   └── user/                           # User 도메인 모듈
│       ├── presentation/               # 외부 계층 (Inbound Adapter)
│       ├── application/                # 애플리케이션 계층
│       ├── domain/                     # 도메인 계층 (핵심)
│       └── infrastructure/             # 외부 계층 (Outbound Adapter)
└── global/                             # 공통 설정 및 유틸리티
    ├── config/                         # 전역 설정
    ├── exception/                      # 예외 처리
    ├── filter/                         # 필터
    └── common/                         # 공통 도메인
```
---
## 🔄 의존성 흐름

```
Presentation Layer (Controller)
        ↓ (의존)
Application Layer (UseCase 인터페이스 ← Service 구현)
        ↓ (의존)
Domain Layer (Entity, Domain Service)
        ↑ (구현)
Infrastructure Layer (Adapter) ← Port 인터페이스 구현
```

### 핵심 규칙:
1. **외부 → 내부 방향으로만 의존** (단방향)
2. **내부 계층은 외부 계층을 모름** (의존성 역전)
3. **모든 외부 접근은 Port(인터페이스)를 통해서만** 가능

---

## 🎯 헥사고날 아키텍처의 장점

### 1. **테스트 용이성**
- Port를 Mock으로 대체하여 각 계층을 독립적으로 테스트 가능
- 도메인 로직을 외부 기술 스택 없이 순수 단위 테스트 가능

### 2. **기술 스택 교체 유연성**
- 데이터베이스 변경 (JPA → MongoDB): `infrastructure` 계층만 수정
- 외부 API 변경: Adapter만 교체하면 되며, 도메인 로직은 영향 없음

### 3. **비즈니스 로직 보호**
- 도메인 계층이 프레임워크와 독립적
- 비즈니스 규칙이 기술적 세부사항에 오염되지 않음

### 4. **명확한 책임 분리**
- 각 계층의 역할이 명확하여 코드 가독성 및 유지보수성 향상
- 새로운 팀원도 구조를 쉽게 파악 가능

---

## 📚 참고 자료

- [Hexagonal Architecture (Alistair Cockburn)](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design (Eric Evans)](https://www.domainlanguage.com/ddd/)
- [Clean Architecture (Robert C. Martin)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---
