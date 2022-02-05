# SpringBootApiServer
This project is migration to React &amp; Spring boot. ( original backend is Node.js(Express))





- React
- Spring-Boot
- Jpa

- MariaDB
- Docker





1. MariaDB와 스프링 부트 연동





2. React와 Spring-Boot 연동

   1. CORS - Proxy

      1. Proxy

         - 원래는 브라우저->프론트서버, 브라우저 -> 백엔드 서버 통신
         - 그러나 Proxy를 사용함으로써 브라우저->프론트서버->백엔드 서버 통신
         - 즉, 백엔드서버가 할 일을 프론트서버가 해주는 것.
         - 그러기에 백엔드 서버의 ip주소나 port번호가 바뀌더라도 프론트인 react에서 한 라인으로 변경가능.

      2. Proxy 설정 두가지방법(프론트엔드-react)

         1. setupProxy.js

         2. package.json

            ```json
            "proxy": "http://localhost:8080/",
            ```

            

