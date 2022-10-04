# 가비지 컬렉션(GC)

### 1. 가비지 컬렉션?

JVM의 Heap 영역에서 사용하지 않는 객체를 삭제하는 프로세스



### 2. JVM 메모리 구조

1. 메소드 영역
   - 클래스 멤버 변수의 이름, 데이터 타입, 접근 제어자 정보같은 필드 정보와 메소드의 이름, 리턴 타입, 파라미터, 접근 제어자 정보같은 메소드 정보, Type정보(Interface인지 class인지), Runtime Constant Pool(문자 상수, 타입, 필드에 대한 레퍼런스가 저장됨), static 변수, final class 변수등이 생성되는 영역.
2. 힙 영역
   - **new 키워드로 생성된 객체와 배열**이 저장되는 영역.
   - **String constant pool** : **문자열 리터럴을 저장**하는 공간 (String str = **“abc”** 에서 “abc” 부분)
   - 메소드 영역에 로드된 클래스만 생성이 가능하고 **Garbage Collector가 참조되지 않는 메모리를 확인하고 제거**하는 영역.
3. 스택 영역
   - 지역 변수, 파라미터, 리턴 값, 연산에 사용되는 임시 값등이 생성되는 영역.
4. PC Register
   - Thread(쓰레드)가 생성될 때마다 생성되는 영역으로 Program Counter 즉, 현재 쓰레드가 실행되는 부분의 주소와 명령을 저장하고 있는 영역. (*CPU의 레지스터와 다름)
5. Native method stack
   - 자바 외 언어로 작성된 네이티브 코드를 위한 메모리 영역.



### 3. GC의 수거대상

GC Roots로 부터 참조를 탐색했을 때, Unreachable한 Object

GC Roots

- stack 영역의 데이터들
- method 영역의 static 데이터들
- JNI에 의해 생성된 객체들

