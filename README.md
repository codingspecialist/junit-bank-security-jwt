# 입출금이체 프로그램

## 준비 github 자료
- https://github.com/codingspecialist/JUNIT5-Security-Lecture
- https://github.com/codingspecialist/junit-bank-security-jwt

## 진행된 테스트

- UserRepository findByUsername() 테스트 - 통과
- UserService 회원가입 테스트 - 통과
- UserApiController 회원가입 테스트 - 통과
- CustomValidationAdvice 생성 및 CustomValidationException 및 ReqDto 유효성 검사 - 통과
- CustomApiExceptionAdvice 생성 및 CustomApiException - 통과 (회원가입 유저네임 중복 테스트)
- Jwt Create, Verify 테스트 - 통과
- JwtAuthentication(로그인) 테스트 - 통과
- JwtAuthorization(인가) 테스트 - 통과 (ROLE_ 안붙여도 된다 이제!! - 문서에 나옴)
https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
- Account 계좌등록 (완)
- User 패스워드 수정 (완)
- Account 삭제 (ADMIN, CUSTOMER) (완)
- CustomForbiddenException 생성 (완)
- 계좌입금, 출금, 이체 (컨트롤러 테스트) (완)
- 계좌입금, 출금, 이체 (서비스 테스트) (완)
- Account 목록보기 (완)
- Transaction 목록보기 (완)

## 포스트맨 공유
- https://www.postman.com/winter-firefly-4359/workspace/bank-security-junit/collection/6436049-f674b2a8-7400-4a6e-be46-162ff42727e2?action=share&creator=6436049

## 화면설계
[ux-design](./class-note/ux-design.pdf)

## 테이블설계
[table-design](./class-note/table-design.pdf)

## 유효성검사
> [regex](./class-note/regex/regex.pdf)
> [validation](./class-note/regex/validation.png)

## 기능정리
- 회원가입 (완)
- 회원패스워드변경 (완)
- 로그인 (완)
- 계좌등록 (완)
- 계좌삭제 (완)
- 본인계좌목록보기 (완)
> (User select, Account select)
- 입금하기 (완)
- 출금하기 (완)
- 이체하기 (완)
- 입출금목록보기 동적쿼리 (완)
> (Transaction select, or query, withdrawAccount left join fetch,  depositAccount left join fetch)
- 본인계좌상세보기 (완)
> (Account select, user join fetch, (입출금목록보기))   
- vs (보너스 강의 - 새로운 브랜치에서 양방향 매핑 테스트하기) (미완)
> (Account select, user join fetch, (양방향매핑 + 컬렉션 concat/sort + batchsize 100 in query))

## 기술스택
- Springboot 2.7.6
- JPA (JPQL)
- JWT
- Security
- MariaDB (프로덕션)
- H2 (테스트/개발)
- AOP (Validation)
- Junit5

## 테이블 스키마
- users
- account
- transaction

```sql
create database metadb;
use metadb;

create table user_tb (
    id bigint auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not null,
    fullname varchar(255) not null,
    email varchar(255) not null,
    role varchar(255) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
create table account_tb (
    id bigint auto_increment,
    number bigint not null unique,
    balance bigint not null,
    password varchar(255) not null,
    user_id bigint,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
create index idx_account_number on account_tb (number);

create table transaction_tb (
    id bigint auto_increment,
    amount bigint not null,
    gubun varchar(255) not null, -- WITHDRAW, DEPOSIT, TRANSFER
    withdraw_account_balance bigint,
    deposit_account_balance bigint,
    deposit_account_id bigint,
    withdraw_account_id bigint,
    created_at timestamp not null,
    updated_at timestamp not null,
    primary key (id)
);
```

## 개발 더미 데이터 (통합 or 레포)
```java
public void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스,"));
        User love = userRepository.save(newUser("love", "러브"));
        User admin = userRepository.save(newUser("admin", "관리자"));
        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));
        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(ssarAccount1, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
}
```
## 테스트 더미 데이터 (서비스)
```java
User ssar = newMockUser(1L, "ssar", "쌀");
User cos = newMockUser(2L, "cos", "코스,");
User love = newMockUser(3L, "love", "러브");
Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
Account cosAccount = newMockAccount(2L, 2222L, 1000L, cos);
Account loveAccount = newMockAccount(3L, 3333L, 1000L, love);
Transaction withdrawTransaction1 = newMockWithdrawTransaction(1L, ssarAccount);
Transaction depositTransaction1 = newMockDepositTransaction(2L, ssarAccount);
Transaction transferTransaction1 = newMockTransferTransaction(3L, ssarAccount, cosAccount);
Transaction transferTransaction2 = newMockTransferTransaction(4L, ssarAccount, loveAccount);
Transaction transferTransaction3 = newMockTransferTransaction(5L, cosAccount, ssarAccount);
List<Transaction> transactions = Arrays.asList(withdrawTransaction1, depositTransaction1,
                transferTransaction1,
                transferTransaction2, transferTransaction3);
```

## 통합테스트 기본 어노테이션 세팅
```java
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
```

## 레포지토리 기본 어노테이션 세팅
```java
@ActiveProfiles("test")
@DataJpaTest
```
> 만약에 QueryDSL 빈이 필요하다면 추가하기 @Import(QueryDSLConfig.class)

## 서비스 기본 어노테이션 세팅
```java
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
```

## Junit 테스트시에 주의할 점
> Lombok 어노테이션 사용을 하지 않는다. Lombok이 compileOnly이기 때문에 runtime시에 작동 안한다.

## 통합테스트 MockUser 주입하는 법
> @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)

> default로 TestExecutionListener.beforeTestMethod로 설정되어 있음 @BeforeAll, @BeforeEach 실행전에 WithUserDetails가 실행되어서, DB에 User가 생기기전에 실행됨

## Security와 JWT를 함께 사용시 주의할 점
> JWT 인증, 인가 테스트를 따로 한다.

> 통합 테스트에서 인증체크는 세션값을 확인하면 된다.

## Security 설정 최신
```txt
AuthenticationManager 의존이 무한으로 의존하는 이슈가 있었다.
그래서 시큐리티 설정은 이제 @Configuration 클래스안에 @Bean으로 설정한다.
그리고 필터 설정은 전부 내부 클래스를 만들어서 AuthenticationManager를 주입받아서 필터를 설정한다.
```

## 서비스 테스트시에 주의할 점
> stub 실행시점은 service 메서드 동작시점이기 때문에, read일 때는 stub이 한개만 있어도 되지만, write일 때는 stub을 단계별로 만들고 깊은 복사를 해야 한다.

> DB에 영속화된 값이 아닌 더미데이터로 테스트하는 것이기 때문에 양방향 매핑시에는 양쪽으로 객체를 동기화 시켜줘야 한다.

## 서비스 테스트시에 참고할 어노테이션
```java
/*
 * Mock -> 진짜 객체를 추상화된 가짜 객체로 만들어서 Mockito환경에 주입함.
 * InjectMocks -> Mock된 가짜 객체를 진짜 객체 UserService를 만들어서 주입함
 * MockBean -> Mock객체들을 스프링 ApplicationContext에 주입함. (IoC컨테이너 주입)
 * Spy -> 진짜 객체를 만들어서 Mockito환경에 주입함.
 * SpyBean -> Spay객체들을 스프링 ApplicationContext에 주입함. (IoC컨테이너 주입)
 */
```
