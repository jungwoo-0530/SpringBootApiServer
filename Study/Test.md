# Test



### 1. 테스트 주도 개발

​	https://yorr.tistory.com/26

- TDD
  - Test Driven Development



- BDD
  - Behaviour Driven Development



1. given - 시나리오에서 구체화
2. when - 구체적으로 기술하고자 하는 행동
3. then - 기대하는 변화/값





### 2. Mock Test

네트워크, 데이터베이스 등 제어하기 어려운 것들에 의존하고 있는 메서드를 테스트하고 검증하기 위해 가짜 객체를 만들어 사용하는 방법.

**언제 필요한지?**

- 테스트 작성을 위한 환경 구축이 어려운 경우
- 테스트가 필요한 특정 메소드 내에 타 외부 서비스 혹은 미들웨어에 의존적일 때
- 단위 테스트의 특성상 반복적이며, 자가 검증이 가능해야하기 위해 테스트 시간을 단축시키기 위해



Stub 객체 vs Mock 객체

- Stub 객체 : 상태 검증
- Mock 객체 : 행위 검증



**Mockito 프레임 워크**

- @Mock
  - mock 객체를 만들어 반환
- @InjectMocks
  - @Mock 객체를 주입할 주체 객체로 선언
- ExtendWith(MockitoExtension.class)
  - Mockito의 Mock 객체를 사용하기 위한 어노테이션으로 Test class 위에 작성



https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.testing







### 3. Repository







### 4. Service





### 5. Controller

