# DepositComparison-SpringBoot

> 예금, 적금 금리 비교 사이트 작성 프로젝트
Toy프로젝트로 시중 은행, 저축은행들의 
<br/>예금, 적금 금리 비교를 간단히 하기 위하여 프로젝트를 진행하였습니다.
- 누구나 예금, 적금 금리를 사이트에서 확인가능합니다.

- 매일 저녁8시에 예금, 적금 목록 동기화를 진행합니다.

[사이트](http://dognas.ipdisk.co.kr)

# Project Structure
> React(SPA) + Spring Boot(API Server)구조로 개발하였습니다.
<br>사용한 기술스택은 다음과 같습니다.

- Spring Boot (API Server)
- Spring Security (Security)
- Spring Batch (Batch)
- MySQL (RDBMS)
- JPA & QueryDSL (ORM)
- OAuth2.0 + JWT (Login)
- JUnit (Test)
- Tomcat 9.0 (WAS)

# Spring Boot (API Server)
> React(SPA)에서 요청한 데이터를 JSON형태로 Response 한다.
<br>구조는 다음과 같습니다.

- common : 공통으로 사용할 Class를 모아놓았습니다.
  - dto, exception, service
- config : project의 설정들의 모음입니다.
소셜로그인 관련 소스들도 함께 있습니다.

- domain : 각 비즈니스 로직들의 Entity, Repository, Service입니다.

- web : 호출 URL 및 REST API Controller입니다.


# Spring Security (Security)
> Security 설정을 추가하여 특정 권한 부여에 따른 API에 접근 할 수 있도록 제한.
<Br /> CORS 설정을 통해 허용된 도메인에서만 API 호출 가능.

- Allowed Origins : http://dognas.ipdisk.co.kr
- AllowedMethods : GET, POST, OPTIONS, PUT, DELETE
- AllowedCredentials : False
- CSRF : disable
- Form Login : disable

# Spring Batch (Batch)
> 매일 오후 8시에 금융감독원 Open API를 이용하여 은행, 적금, 예금목록의 동기화를 진행합니다.
