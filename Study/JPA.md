# JPA 





#### 1. Auditing사용하면서 조회수 카운팅

- Jpa의 Auditing 기능을 사용하여 DB에 create 시간과 update시간을 자동으로 시간을 넣게 했다

- 그러나 단순한 조회를 하여 조회수 증가를 처음에는 더티체킹을 사용하여 `hit`를 +1해주었다.

- 여기서 hit가 업데이트 되면서 자동으로 조회수 증가떄문에 update시간이 업데이트되었다.

- 그래서 더티체킹을 사용하지 않고 

  - ```java
      @Modifying
      @Query("update Board b set b.hit = b.hit+1 where b.id = :id")
      Integer plusBoardHit(@Param("id") Long boardId);
    }`
    ```

  - 위와 같이하여 hit를 증가시켰다.

