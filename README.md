## text vs utext
* Escape
  * 웹 브라우저는 `<` 를 HTML 테그의 시작으로 인식
  * 따라서 `<` 를 태그의 시작이 아니라 문자로 표현할 수 있는 방법이 HTML 엔티티
  * HTML에서 사용하는 특수 문자를 HTML 엔티티로 변경하는 것: 이스케이프(escape)
  * `th:text` , `[[...]]` 는 **기본적으로 이스케이프(escape)를 제공**
  * `th:utext` , `[(...)]` 는 **언이스케이프(unescape)를 제공**

> 실제 서비스를 개발하다 보면 escape를 사용하지 않아서 HTML이 정상 렌더링 되지 않는 수 많은 문제가 발생  
> escape를 기본으로 하고, 꼭 필요한 때만 unescape를 사용


## SpringEL
* Spring 프레임워크에서 제공하는 **표현식 언어(Expression Language)**
* 런타임에 객체 그래프를 탐색하고, 메서드 호출, 프로퍼티 접근, 산술/논리 연산 등을 수행할 수 있음
```html
<ul>Object
  <li>${user.name}: <span th:text="${user.name}"></span></li>
  <li>${user['name']}: <span th:text="${user['name']}"></span></li>
  <li>${user.getName()}: <span th:text="${user.getName()}"></span></li>
</ul>
```
* 지역변수 선언
```html
<div th:with="first=${list[0]}">
  <p>처음 사람의 이름은 <span th:text="${first.name}"></span></p>
</div>
```


## 편의 객체
```java
@GetMapping("/basic-objects")
public String basicObjects(
    HttpSession session
) {
  session.setAttribute("data", "Hello, Session");
  return "basic/basic-objects";
}

@Component("helloBean")
static class HelloBean {

  public String hello(String data) {
    return "Hello, " + data;
  }
}
```
```html
<!--http://localhost:8080/basic/basic-objects?data=HelloParam-->
<ul>
  <li>Request Parameter = <span th:text="${param.data}"></span></li>
  <li>session = <span th:text="${session.data}"></span></li>
  <li>spring bean = <span th:text="${@helloBean.hello('Spring!')}"></span></li>
</ul>
```


## 유틸리티 객체
* `#message` : 메시지, 국제화 처리
* `#uris` : URI 이스케이프 지원
* `#dates` : `java.util.Date` 서식 지원
* `#calendars` : `java.util.Calendar` 서식 지원
* `#temporals` : 자바8 날짜 서식 지원
* `#numbers` : 숫자 서식 지원
* `#strings` : 문자 관련 편의 기능
* `#objects` : 객체 관련 기능 제공
* `#bools` : boolean 관련 기능 제공
* `#arrays` : 배열 관련 기능 제공
* `#lists` , `#sets` , `#maps` : 컬렉션 관련 기능 제공
* `#ids` : 아이디 처리 관련 기능 제공

[유틸리티 객체 예시](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expression-utility-objects)


## URL 링크
```html
<ul>
  <li><a th:href="@{/hello}">basic url</a></li>
  <li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">/hello?param1=data1&param2=data2</a></li>
  <li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">/hello/data1/data2</a></li>
  <li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">/hello/data1?param2=data2</a></li>
</ul>
```


## Literals
* 소스 코드상에 고정된 값
* 타임리프에서 문자 리터럴은 항상 `'` (작은 따옴표)로 감싸야 함
  * 공백 없이 쭉 이어진다면 하나의 의미있는 토큰으로 인지해서 다음과 같이 작은 따옴표를 생략할 수 있음
```html
<span th:text="hello world!"></span> <!--중간에 공백이 있어서 하나의 의미있는 토큰으로도 인식되지 않음-->
<span th:text="'hello world!'"></span>

<li>'hello' + ' world!' = <span th:text="'hello' + ' world!'"></span></li>
<li>'hello world!' = <span th:text="'hello world!'"></span></li>
<li>'hello ' + ${data} = <span th:text="'hello ' + ${data}"></span></li>
<li>리터럴 대체 |hello ${data}| = <span th:text="|hello ${data}|"></span></li>
```


## 연산
* 비교연산: HTML 엔티티를 사용해야 하는 부분을 주의
* Elvis 연산자**: 조건식의 편의 버전
  * `<li>${nullData}?: '데이터가 없습니다.' = <span th:text="${nullData}?: '데이터가 없습니다.'"></span></li>`
* No-Operation**: `_` 인 경우 마치 타임리프가 실행되지 않는 것 처럼 동작
* `<li>${nullData}?: _ = <span th:text="${nullData}?: _">데이터가 없습니다.</span></li>`


## 속성 값 설정
* `th:*` 로 속성을 적용하면 기존 속성을 대체하거나, 기존 속성이 없으면 새로 만듦
* `th:attrappend` : 속성 값의 뒤에 값을 추가
* `th:attrprepend` : 속성 값의 앞에 값을 추가
* `th:classappend` : class 속성에 추가
  * attrappend, attrprepend는 띄어쓰기를 직접 처리해야하는 불편함이 있어서 주로 classappend 사용
* 타임리프의 `th:checked` 는 값이 `false` 인 경우 `checked` 속성 자체를 제거
  * HTML에서는 `<input type="checkbox" name="active" checked="false" />`의 경우에 checked 속성이 있기 때문에 checked 처리


## 반복
```html
<table border="1">
  <tr>
    <th>count</th>
    <th>username</th>
    <th>age</th>
    <th>etc</th>
  </tr>
  <tr th:each="user, userStat : ${users}">
    <td th:text="${userStat.count}">username</td>
    <td th:text="${user.username}">username</td>
    <td th:text="${user.age}">0</td>
    <td>
      index = <span th:text="${userStat.index}"></span>
      count = <span th:text="${userStat.count}"></span>
      size = <span th:text="${userStat.size}"></span>
      even? = <span th:text="${userStat.even}"></span>
      odd? = <span th:text="${userStat.odd}"></span>
      first? = <span th:text="${userStat.first}"></span>
      last? = <span th:text="${userStat.last}"></span>
      current = <span th:text="${userStat.current}"></span>
    </td>
  </tr>
</table>
```
* 두번째 파라미터는 생략 가능한데, 생략하면 지정한 변수명(`user`) + `Stat


## 조건부 평가
* **if, unless**: 타임리프는 해당 조건이 맞지 않으면 태그 자체를 렌더링하지 않음
* **switch**: `*` 은 만족하는 조건이 없을 때 사용하는 디폴트


## 주석
1. 표준 HTML 주석  
자바스크립트의 표준 HTML 주석은 타임리프가 렌더링 하지 않고, 그대로 남겨둠
2. 타임리프 파서 주석  
타임리프 파서 주석은 타임리프의 진짜 주석, 렌더링에서 주석 부분을 제거


## 블록
```html
<th:block th:each="user : ${users}">
  <div>
    사용자 이름1 <span th:text="${user.username}"></span>
    사용자 나이1 <span th:text="${user.age}"></span>
  </div>
  <div>
    요약 <span th:text="${user.username} + ' / ' + ${user.age}"></span>
  </div>
</th:block>
```
* `th:each`로는 해결할 수 없는 `<div>`를 2개씩 반복하는 등의 상황에서 사용
* <th:block>` 은 렌더링시 제거


## 자바스크립트 인라인
* 텍스트 렌더링
  * `var username = [[${user.username}]];`
    * 인라인 사용 전 `var username = userA;`
    * 인라인 사용 후 `var username = "userA";`(인라인 사용 후 렌더링 결과를 보면 문자 타입인 경우 `"` 를 포함)
* 자바스크립트 내추럴 템플릿
  * `var username2 = /*[[${user.username}]]*/ "test username";`
    * 인라인 사용 전 `var username2 = /*userA*/ "test username";`
    * 인라인 사용 후 `var username2 = "userA";`(인라인 사용 후 결과를 보면 주석 부분이 제거되고, 기대한 "userA"가 정확하게 적용)
* 객체
  * 타임리프의 자바스크립트 인라인 기능을 사용하면 객체를 JSON으로 자동으로 변환
  * `var user = [[${user}]];`
    * 인라인 사용 전 `var user = BasicController.User(username=userA, age=10);`(toString() 호출한 값)
    * 인라인 사용 후 `var user = {"username":"userA","age":10};`(객체를 JSON으로 변환)
* each
```html
<script th:inline="javascript">
  [# th:each="user, stat : ${users}"]
  var user[[${stat.count}]] = [[${user}]];
  [/]

  // var user1 = {"username":"userA","age":10};
  // var user2 = {"username":"userB","age":20};
  // var user3 = {"username":"userC","age":30};
</script>
```


## 템플릿 조각
`template/fragment/footer :: copy` : `template/fragment/footer.html` 템플릿에 있는
`th:fragment="copy"` 라는 부분을 템플릿 조각으로 가져와서 사용한다는 의미
* `th:insert` 를 사용하면 현재 태그( `div` ) 내부에 추가
* `th:replace` 를 사용하면 현재 태그( `div` )를 대체
* `파라미터`
  * `<div th:replace="~{template/fragment/footer :: copyParam ('데이터1', '데이터2')}"></div>`


## 템플릿 레이아웃
코드 조각을 레이아웃에 넘겨서 사용하는 방법
```html
<head th:replace="template/layout/base :: common_header(~{::title},~{::link})">
  <title>메인 타이틀</title>
  <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}">
  <link rel="stylesheet" th:href="@{/themes/smoothness/jquery-ui.css}">
</head>
```
* `common_header(~{::title},~{::link})`
  * `::title`: 현재 페이지의 title 태그들을 전달
  * `::link`: 현재 페이지의 link 태그들을 전달



## 입력 폼 처리
* `th:object="${item}"` : `<form>` 에서 사용할 객체를 지정
  * 선택 변수 식( `*{...}` )을 적용할 수 있음
  * `th:field` 는 `id` , `name` , `value` 속성을 모두 자동으로 만들어줌
```html
<form action="item.html" th:action th:object="${item}" method="post">
  <div>
    <label for="itemName">상품명</label>
    <input type="text" id="itemName" th:field="*{itemName}" class="form-control" placeholder="이름을 입력하세요">
  </div>
</form>
```


## 체크 박스 단일
* 체크 박스를 체크하면 HTML Form에서 `open=on` 이라는 값이 넘어감 → 스프링은 `on` 이라는 문자를 `true` 타입으로 변환
* HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 `open` 이라는 필드 자체가 서버로 전송되지 않음
  * 히든 필드를 하나 만들어서, `_open` 처럼 기존 체크 박스 이름 앞에 언더스코어( `_` )를 붙여서 전송하면 체크를 해제했다고 인식(히든 필드는 항상 전송)
* 타임리프의 `th:field` 를 사용하면, 값이 `true` 인 경우 checked` 속성을 자동으로 추가


## 체크 박스 다중
* `@ModelAttribute`
  * 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리할 수 있음
```java
@ModelAttribute("regions")
public Map<String, String> regions() {
  Map<String, String> regions = new LinkedHashMap<>();

  regions.put("SEOUL", "서울");
  regions.put("BUSAN", "부산");
  regions.put("JEJU", "제주");
  
  return regions;
}
```

* `th:for="${#ids.prev('regions')}"`
  * Thymeleaf가 자동 생성한 이전 input의 ID를 가져오는 유틸리티
```txt
#ids 유틸리티:

${#ids.seq('필드명')}: 다음 순번 ID 생성
${#ids.prev('필드명')}: 이전에 생성된 ID 반환
${#ids.next('필드명')}: 다음 ID 미리 보기
```
  * 타임리프는 체크박스를 `each` 루프 안에서 반복해서 만들 때 임의로 `1` , `2` , `3` 숫자를 뒤에 붙여줌
```html
<div>
  <div>등록 지역</div>
  <div th:each="region : ${regions}" class="form-check form-check-inline">
    <input type="checkbox" th:field="*{regions}" th:value="${region.key}" class="form-check-input">
    <label th:for="${#ids.prev('regions')}" th:text="${region.value}" class="form-check-label">서울</label>
  </div>
</div>
```
* `th:field="*{regions}"`
  * `name="regions"` 자동 설정
  * `id="regions1, regions2, ..."` 자동 생성
  * 체크 상태 관리
    * Text Input의 경우, th:field가 name, id, value 모두 설정
    * 체크박스의 경우, Value 설정은 안하고, Name 설정, ID 설정, 바인딩된 컬렉션에 해당 값이 있으면 checked 추가
* `th:value="${region.key}"`
  * 각 체크박스의 실제 value 값 설정
  * th:field의 기본 value를 `override`
* `_regions` 는 웹 브라우저에서 체크를 하나도 하지 않았을 때, 클라이언트가 서버에 아무런 데이터를 보내지 않는 것을 방지
  * `_regions` 조차 보내지 않으면 결과는 `null`


## 라디오 버튼
* ItemType.values()` 를 사용하면 해당 ENUM의 모든 정보를 배열로 반환
* 체크박스와 달리 선택하지 않으면 아무 값도 넘어가지 않음


## 멀티 모듈 생성
1. Root Project 생성

2. Root Project 내 구조 정리
```txt
📁 root/
├── 📁 .git                ✅
├── 📁 gradle              ✅  
├── 📁 subProject1/        ✅
│   ├── 📁 src/            ✅
│   └── 📄 build.gradle    ✅ 해당 프로젝트만의 설정
├── 📁 subProject2/        ✅
│   ├── 📁 src/            ✅
│   └── 📄 build.gradle    ✅ 해당 프로젝트만의 설정
├── 📄 .gitattributes      ✅ Root에 1개
├── 📄 .gitignore          ✅ Root에 1개
├── 📄 build.gradle        ✅ 공통 속성 선언
├── 📄 gradlew             ✅ Root에 1개
├── 📄 gradlew.bat         ✅ Root에 1개
└── 📄 settings.gradle     ✅ Root에 1개
```

3. 프로젝트 구조 선언
```bash
# settings.gradle

rootProject.name = 'root'

include 'subProject1'
include 'subProject2'
```

4. 빌드 테스트
```bash
gradlew projects # 전체 빌드
gradlew :subProject1:bootRun # 개별 프로젝트 빌드
```


### 참고자료
[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2)