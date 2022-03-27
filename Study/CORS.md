# CORS



### CORS란?

- Cross-Origin Resource Sharing(교차 출처 리소스 공유)
- 브라우저에서 실행 중인 스크립트에서 시작되는 cross-origin HTTP 요청을 제한하는 브라우저 보안 기능
- cross-origin 종류
  - 다른 도메인 (ex: `example.com`에서 `test.com`으로)
  - 다른 하위 도메인 (ex: `example.com`에서 `store.example.com`으로)
  - 다른 포트 (ex: `example.com`에서 `example.com:81`로)
  - 다른 프로토콜 (ex: `https://example.com`에서 `http://example.com`으로)



**#XMLHttpRequest(XHR)**

- XHR 객체는 **서버와 상호작용**하기 위해 사용
- 전체 페이지의 **새로고침없이도** **URL로부터 데이터**를 받아올 수 있음
- `AJAX`프로그래밍에 주로 사용됨
- **XML**를 받아오는 것만이 아니라 **모든 데이터**를 받아올 수 있음.
- https://developer.mozilla.org/ko/docs/Web/API/XMLHttpRequest



### 1. Preflight

- 서버에서 어떤 메서드와 어떤 header를 허용하는지 확인하는 과정을 Preflight에서 수행
- **서버의 응답으로 어떤 `origin`과 어떤 `method`를 허용하는지 브라우저에게 알려주는 역할.**
- 확인하는 과정
  - 특정 HTTP Method로 요청을 **보내기 전**(프론트(브라우저)에서 서버)
  - 해당 서버로 **OPTIONS를 미리 보냄**
  - 서버로 부터 온 응답을 확인 한 후, HTTP Method가 지원하면 실제 요청이 이뤄지게 되는 것

- 그렇다면 매 요청마다 Preflight 요청을 전송하는가?
  - 서버 설정을 통하여 preflight 결과를 캐시를 일정 기간 동안 저장한다.
  - 이 캐시 정보가 살아있는 동안 `cross-origin`요청에 대해서 preflight를 생략
  - `maxAge`







### 2. Spring boot CORS 허용 방법

Spring boot에서 CORS 허용 방법으로는 대표적으로 두 가지가 존재.

- `WebConfig` 설정

  - ```java
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
    
        /*
            http://localhost:8080 에서 들어오는 모든 요청 CORS 허용
        */
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080")
              			.maxAge(3600);//3600초 동안 preflight결과를 캐시에 저장
        }
    }
    ```

    





- `@CrossOrigin`

  - Controller 단에서 `@CrossOrigin` 어노테이션을 사용하여 CORS 허용하는 방법

  - ```java
    @RestController
    public class ApiController {
    
        /*
            @CrossOrigin(origins = “허용주소:포트”)
            모든 origin 허용은 @CrossOrigin(origins="*") 설정
        */
            
        // http://localhost:8080 에서 들어오는 요청만 CORS 허용
        @CrossOrigin(origins = "http://localhost:8080", maxAge=3600)
        @PostMapping("/")
        public String postSuccess() {
            return "REST API 호출 성공~!!";
        }
    }
    ```

    