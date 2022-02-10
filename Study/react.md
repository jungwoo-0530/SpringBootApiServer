- state - `state`는 (함수 내에 선언된 변수처럼) **컴포넌트 안에서 관리**된다.





## **Location**

 **location 객체에는 현재 페이지의 정보를 가지고 있다.** 대표적으로 location.search로 현재 url의 쿼리 스트링을 가져올 수 있다.

![img](https://blog.kakaocdn.net/dn/oU9Bn/btqBh0Y2TXE/ICzkxw2pK20f49o8owXdF1/img.png)요청 URL

![img](https://blog.kakaocdn.net/dn/b6COzX/btqBkpJUrQy/HTSMnlRGJc2kVDB8nnvyrk/img.png)console.log(this.props.location)

**· pathname** : [string] 현재 페이지의 경로명

**· search** : [string] 현재 페이지의 query string

**· hash** : [string] 현재 페이지의 hash





## **Match**

 **match 객체에는 <Route path>와 URL이 매칭된 대한 정보가 담겨져있다.** 대표적으로 match.params로 path에 설정한 파라미터값을 가져올 수 있다.

![img](https://blog.kakaocdn.net/dn/byxn34/btqBgD4x6VK/q7EWXRnRivdZ99U5PBvci0/img.png)console.log(this.props.match)

***\*·path :\**** [string] 라우터에 정의된 path

**· url :** [string] 실제 클라이언트로부터 요청된 url path

**· isExact :** [boolean] true일 경우 전체 경로가 완전히 매칭될 경우에만 요청을 수행

**· params :** [JSON object] url path로 전달된 파라미터 객체 