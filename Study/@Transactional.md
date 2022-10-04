# @Transactional

### 1. 트랜잭션이란?

- 데이터베이스의 상태를 **변경**하는 작업 또는 **한번에 수행되어야 하는 연산**들을 의미한다.
- <img src="https://akasai.space/static/f1a4ec04c92b0531d8e40b5cb4bb8758/01e7c/transaction-flow.png" alt="트랜잭션과 ACID | devlog.akasai" style="zoom:80%;" />
- begin, commit을 자동으로 수행해준다.
- 예외 발생 시 rollback 처리를 자동으로 수행해준다.
- 트랜잭션은 4가지 성질을 가지고 있다.
  - 원자성(Atomicity) : 한 트랜잭션 내에서 실행한 작업들은 하나의 단위로 처리(즉, 모두 성공 또는 모두 실패)
  - 일관성(Consistency) : 트랙잭션은 일관성있는 데이터베이스 상태를 유지한다.
  - 격리성(Isolation) : 동시에 실행되는 트랜잭션들이 서로 영향을 미치지 않도록 격리해야한다.
  - 영속성(Durability) : 트랜잭션을 성공적으로 마치면 결과가 항상 저장되어야 한다.



### 2. Spring에서의 트랜잭션

 스프링에서는 어노테이션 방식으로 `@Transactional`을 메소드, 클래스, 인터페이스 위에 추가 작성하여 사용하는 방식이 일반적이다. 이 방식을 **선언적 트랜잭션**이라 부르며, 적용된 범위에서는 트랜잭션 기능이 포함된 **프록시 객체가 생성**되어 자동으로 **commit 혹은 rollback**을 진행해준다.



 스프링은 또한 사용자의 편의성을 위해서 트랜잭션과 관련된 3가지 핵심 기술을 제공한다.

- 트랜잭션 동기화

- 트랜잭션 추상화

- AOP를 이용한 트랜잭션 분리



1. 트랜잭션 동기화

   JDBC를 이용하는 개발자가 직접 여러 개의 작업을 하나의 트랜잭션으로 관리하려면 Connection 객체를 공유하는 등 상당히 불필요한 작업들이 많이 생긴다. 트랜잭션 동기화는 트랜잭션을 시작하기 위한 **Connection 객체**를 특별한 저장소에 보관해두고 필요할 때 꺼내쓸 수 있도록 하는 기술.

   하지만 개발자가 JDBC가 아닌 Hibernate와 같은 기술을 쓴다면 위의 JDBC 종속적인 트랜잭션 동기화 코드들은 문제를 유발하게 된다. 대표적으로 **Hibernate**에서는 Connection이 아닌 **Session이라는 객체**를 사용하기 때문이다. 이러한 기술 종속적인 문제를 해결하기 위해 Spring은 트랜잭션 관리 부분을 추상화한 기술을 제공하고 있다.

   



### 3. @Transactional 옵션

1. isolation : 트랜잭션에서 일관성없는 데이터 허용 수준을 설정

   ```java
   @Transactional(isolation=Isolation.DEFAULT)
   public void addUser(UserDTO dto) throws Exception {
   	// 로직 구현
   }
   ```

   - DEFAULT : 기본이며, DB의 Isolation Level을 따른다.
   - READ_UNCOMMITED : Level 0이며 커밋되지 않는 데이터에 대한 읽기를 허용한다.
     - 어떤 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 B라는 아직 완료되지 않은(Uncommitted 혹은 Dirty)데이터 B를 읽을 수 있다.
     - Dirty Read 발생
   - READ_COMMITED : Level 1이며 커밋된 데이터에 대해 읽기 허용
     - 어떠한 사용자가 A라는 데이터를 B라는 데이터로 변경하는 동안 다른 사용자는 해당 데이터에 접근할 수 없다.
     - Dirty Read 방지
   - REPEATEABLE_READ : Level 2이며 동일 필드에 대해 다중 접근 시 모두 동일한 결과를 보장한다.
     - 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정이 불가능하다.
     - 선행 트랜잭션이 읽은 데이터는 트랜잭션이 종료될 때까지 후행 트랜잭션이 갱신하거나 삭제가 불가능 하기 때문에 같은 데이터를 두번 쿼리했을 때 일관성 있는 결과를 리턴한다.
     - Non-Repeatable Read 방지
   - SERIALIZABLE : Level 3이며 가장 높은 격리, 성능 저하의 우려가 있음
     - 데이터의 일관성 및 동시성을 위해 MVCC(Multi Version Concurrency Control)을 사용하지 않음.
     - 트랜잭션이 완료될 때까지 SELECT 문장이 사용하는 모든 데이터에 shared lock이 걸리므로 다른 사용자는 그 영역에 해당되는 데이터에 대한 수정 및 입력이 불가능하다.
     - Phantom Read 방지

2. 