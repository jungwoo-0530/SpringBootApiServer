React와 Spring-Boot 데이터 통신 - axios

1. binding

   1. 백엔드에서 리턴하는 객체의 변수를 react에서 아래와 같이 바인딩.

      ```java
       @GetMapping("/api/test")
        public Board home(){
      
          Board board = new Board("jungwoo", "female");
      
          return board;
        }
      
        @Data
        @AllArgsConstructor
        public static class Board{
          private String name;
          private String sex;
      
        }
      }
      ```

      ```js
      class Test extends Component {
      
          constructor(props){
              super(props);
              this.state = {
                  userName: "",
                  userSex: ""
              }
          }
          //component 시작하면 getPostIndex 함수 실행
          componentDidMount() {
              this.getMessage();
          }
          getMessage = () => {
              axios
              .get("/api/test")
              .then((response) =>{
                  this.setState({
                      userName: response.data.name,
                      userSex: response.data.sex
                  });
      
              })
          }
      
          render(){
              return (
                  <div className="container">
                      <h1>Api Test</h1>
                      <div>{this.state.userName}</div>
                      <div>{this.state.userSex}</div>
      
                  </div>
              );
          };
      
      }
      export default Test;
      
      ```



2. router

   1. router 방법

      1. `body.js`파일에서 설정

      2. 우선 js파일을 임폴트.

         ```js
         import Test from "./aa/Test";
         ```

      3. `http://localhost:3000/api/user`로 들어갈 경우 아래 코드로 들어가고 `component`의 값인 Test. 즉, 위에서 임포트한 `Test.js`로 감.

         ```js
         <Route path="/api/user" component={Test} />
         ```

         





3. 중복처리
   1. 상태코드만 보내는 방법
   2. 상태코드랑 메시지랑 같이 보내는 방법







4. axios를 사용하여 통신

```java
@PostMapping("/api/users/new")
  @ResponseBody
  public dupCheck registerMember(@RequestBody CreateMemberRequest createMemberRequest)
  {
    dupCheck result = new dupCheck("",
        memberService.dupLoginIdCheck(createMemberRequest.getLoginId()),
        memberService.dupEmailCheck(createMemberRequest.getEmail()));

    if(result.dupLoginIdCheck){
      result.setMessage("이미 사용 중인 아이디입니다.");
    }
    else if(result.dupEmailCheck) {
      result.setMessage("이미 사용 중인 이메일입니다.");
    }
    else{

      Member newMember = Member.builder().
          name(createMemberRequest.getName()).
          loginId(createMemberRequest.getLoginId()).
          email(createMemberRequest.getEmail()).
          password(createMemberRequest.getPassword()).
          telephone(createMemberRequest.getTelephone()).build();

      result.setMessage("회원가입이 완료되었습니다.");
      memberService.save(newMember);
    }

    return result;
  }

  @Data
  @AllArgsConstructor
  public static class dupCheck{

    String message;
    boolean dupLoginIdCheck;
    boolean dupEmailCheck;

  }
```

 	1. `@ResponseBody`를 사용하여 Body에 해당 객체를 담아서 전송.
 	2. `@RequestBody`를 파라미터에 사용해서 요청 body에 있는 값들을 매핑함.
      	1. HTTP 요청 처리
          - GET + 쿼리 파라미터
            - 쿼리 스트링 요청 처리 : @RequestParam, @ModelAttribute
          - POST + HTML Form
          - HTTP Body 데이터.
            - HttpEntity - HTTP header, body정보를 조회 가능, body 직접 조회, 직접 반환 가능.
            - RequestEntity, ResponseEntity - HttpEntity를 상속받은 객체
            - @RequestBody
              - HTTP 요청의 바디 정보를 편리하게 조회(json -> String/객체)
              - 생략불가능
              - JSON 요청 데이터 -> HTTP메시지 컨버터 -> 객체
            - @ResopnseBody
              - 컨트롤러의 처리 결과를 HTTP 바디에 직접 넣어서 전달 가능(String/객체 -> json)
              - 객체 -> HTTP 메시지 컨버터 -> JSON응답 데이터