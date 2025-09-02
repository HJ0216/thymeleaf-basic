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


## 타임리프 메시지 적용
* 스프링 부트를 사용하면 스프링 부트가 `MessageSource` 를 자동으로 스프링 빈으로 등록
  * 스프링 부트 메시지 소스 설정
  * `spring.messages.basename=messages,config.i18n.messages`
  * `MessageSource`를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 `messages` 라는 이름으로 기본 등록
* 파라미터 X
  * hello=안녕
  * `<p th:text="#{hello}"></p>
* 파라미터 O
  * hello.name=안녕 {0}`
  * `<p th:text="#{hello.name(${item.itemName})}"></p>

## 국제화
스프링은 언어 선택시 기본으로 `Accept-Language` 헤더의 값을 사용


## Validation
* `Map<String, String> errors = new HashMap<>();`
  * 어떤 필드에서 오류가 발생했는지 구분하기 위해 오류가 발생한 필드명을 `key` 로 사용
* Safe Navigation Operator
  * `errors?.` 은 `errors` 가 `null` 일때 `NullPointerException` 이 발생하는 대신, `null` 을 반환
  + `th:if` 에서 `null` 은 실패로 처리
* `<input type="text" th:classappend="${errors?.containsKey('itemName')} ? 'field-error' : _" class="form-control">`
  * _` (No-Operation): 아무것도 하지 않음


## BindingResult
BindingResult bindingResult` 파라미터의 위치는 `@ModelAttribute Item item` 다음에 와야 함
  * `BindingResult` 가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동
  * `BindingResult` 가 있으면 오류 정보( `FieldError` )를 `BindingResult` 에 담아서 컨트롤러를 정상 호출
* `BindingResult` 는 검증해야 할 객체인 `target` 바로 다음에 와야하는데, 이를 통해 `BindingResult` 는 이미 본인이 검증해야 할 객체인 `target` 을 알고 있음
  * bindingResult.getObjectName(): @ModelAttribute name
  * bindingResult.getTarget(): 해당 객체
* rejectValue(), reject()
  * `field` : 오류 필드명
  * `errorCode` : 오류 코드(메시지에 등록된 코드 X, messageResolver를 위한 오류 코드)
  * `errorArgs` : 오류 메시지에서 `{0}` 을 치환하기 위한 값
  * `defaultMessage` : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
  * rejectValue(): 특정 필드에 대한 validation 에러를 추가할 때 사용
  * reject(): 객체 전체에 대한 validation 에러를 추가할 때 사용

### FieldError
```java
public FieldError(String objectName, String field, String defaultMessage) {}
```
* `objectName` : `@ModelAttribute` 이름
* `field` : 오류 필드
* `rejectedValue` : 사용자가 입력한 값(거절된 값)
  * 사용자의 입력 데이터가 컨트롤러의 `@ModelAttribute` 에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기 어려움
  * `FieldError` 는 오류 발생시 사용자 입력 값을 저장하는 기능을 제공
  * `th:field="*{price}"`
    * 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 `FieldError` 에서 보관한 값을 사용해서 값을 출력
* `bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
* `codes` : 메시지 코드
* `arguments` : 메시지에서 사용하는 인자
* `defaultMessage` : 기본 오류 메시지

### ObjectError(글로벌 오류)
```java
public ObjectError(String objectName, String defaultMessage) {}
```
* `objectName` : `@ModelAttribute` 의 이름
* `defaultMessage` : 오류 기본 메시지


## errors 메시지 파일 생성
* `application.properties`
  * `spring.messages.basename=messages,errors`
  * 생략하면 `messages.properties` 를 기본으로 인식


## DefaultMessageCodesResolver의 기본 메시지 생성 규칙
* 객체 오류
```txt
객체 오류의 경우 다음 순서로 2가지 생성
1.: code + "." + object name
2.: code

예) 오류 코드: required, object name: item
1.: required.item
2.: required
```

* 필드 오류
```txt
필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성
1.: code + "." + object name + "." + field
2.: code + "." + field
3.: code + "." + field type
4.: code

예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
1. "typeMismatch.user.age"
2. "typeMismatch.age"
3. "typeMismatch.int"
4. "typeMismatch"
```

## Validator
```java
public interface Validator {
  boolean supports(Class<?> clazz);
  void validate(Object target, Errors errors);
}
```
* `supports() {}` : 해당 검증기를 지원하는 클래스인지 확인
* `validate(Object target, Errors errors)` : 검증 대상 객체와 `BindingResult`


## WebDataBinder
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
  log.info("init binder {}", dataBinder);
  dataBinder.addValidators(itemValidator);
}
```
* `WebDataBinder` 에 검증기를 추가하면 **해당** 컨트롤러에서는 검증기를 자동으로 적용
* @Validated 적용
  * `WebDataBinder` 에 등록한 검증기를 찾아서 실행
  * 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요
    * 이때 `supports()` 사용
    * `supports(Item.class)` 호출되고, 결과가 `true` 이므로 `ItemValidator` 의 `validate()` 가 호출


## Bean Validation
* 검증 순서
  1. `@ModelAttribute` 각각의 필드에 타입 변환 시도 → 실패하면 `typeMismatch` 로 `FieldError` 추가
  2. Validator 적용
* BeanValidation 메시지 찾는 순서
  1. 생성된 메시지 코드 순서대로 `messageSource` 에서 메시지 찾기
  2. 애노테이션의 `message` 속성 사용 `@NotBlank(message = "공백! {0}")`
  3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.
* 오브젝트 오류(글로벌 오류)의
  * DTO에서 `@ScriptAssert`을 억지로 사용하는 것 보다는 Controller 메서드에서 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장

### groups
* 등록시에 검증할 기능과 수정시에 검증할 기능을 각각 그룹으로 나누어 적용
  * groups를 사용하려면 `@Validated` 를 사용
  * groups 기능을 사용할 경우, 복잡도가 올라가 실무에서는 주로 다음에 등장하는 등록용 폼 객체와 수정용 폼 객체를 분리해서 사용


## Form 전송 객체 분리
* 실무에서는 회원 등록시 회원과 관련된 데이터만 전달받는 것이 아니라, 약관 정보도 추가로 받는 등 `Item` 과 관계없는 수 많은 부가 데이터가 넘어옴
* 그래서 보통 `Item` 을 직접 전달받는 것이 아니라, 복잡한 폼의 데이터를 컨트롤러까지 전달할 별도의 객체를 만들어서 전달


## HttpMessageConverter(@RequestBody)
* `@ModelAttribute` 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용
* `@RequestBody` 는 HTTP Body의 데이터를 객체로 변환할 때 사용(주로 API JSON 요청을 다룰 때 사용)

* API: 3가지 경우
  * 성공 요청: 성공
  * 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함
    * 객체를 만들지 못하기 때문에 컨트롤러 자체가 호출되지 않고 그 전에 예외가 발생
    * Validator도 실행되지 않음
  * 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함

### @ModelAttribute vs @RequestBody
* HTTP 요청 파리미터를 처리하는 `@ModelAttribute` 는 각각의 필드 단위로 세밀하게 적용
  * 특정 필드에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리할 수 있음
* `HttpMessageConverter`는 `@ModelAttribute` 와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용
  * 따라서 메시지 컨버터의 작동이 성공해서 `ItemSaveForm` 객체를 만들어야 `@Valid` , `@Validated` 가 적용
* `@ModelAttribute`는 필드 단위로 정교하게 바인딩이 적용
  * 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있음
* `@RequestBody`는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생
  * 컨트롤러도 호출되지 않고, Validator도 적용할 수 없음


## 서블릿 예외 처리
* 종류
  * Exception (예외)
    * WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
  * response.sendError(HTTP 상태 코드, 오류 메시지)
    * 호출한다고 당장 예외가 발생하는 것은 아니지만, 서블릿 컨테이너에게 오류가 발생했다는 점을 전달할 수 있음

### 서블릿 오류 페이지 등록
* `WebServerFactoryCustomizer<ConfigurableWebServerFactory>` 구현
  * 오류 페이지는 예외를 다룰 때 해당 예외와 그 자식 타입의 오류를 함께 처리

### 예외 발생과 오류 페이지 요청 흐름
1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View

### 오류 정보 추가
WAS는 오류 페이지를 단순히 다시 요청만 하는 것이 아니라, 오류 정보를 request 의 attribute 에 추가해서 넘김  
필요하면 오류 페이지에서 이렇게 전달된 오류 정보를 사용할 수 있음
```java
private void printErrorInfo(HttpServletRequest request) {
  log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
  log.info("ERROR_EXCEPTION_TYPE: {}",
      request.getAttribute(ERROR_EXCEPTION_TYPE));
  log.info("ERROR_MESSAGE: {}",
      request.getAttribute(ERROR_MESSAGE)); //ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
  log.info("ERROR_REQUEST_URI: {}",
      request.getAttribute(ERROR_REQUEST_URI));
  log.info("ERROR_SERVLET_NAME: {}",
      request.getAttribute(ERROR_SERVLET_NAME));
  log.info("ERROR_STATUS_CODE: {}",
      request.getAttribute(ERROR_STATUS_CODE));
  log.info("dispatchType={}", request.getDispatcherType());
}
```

### DispatcherType
* REQUEST : 클라이언트 요청
* ERROR : 오류 요청

```java
// 필터를 등록할 때 어떤 DispatcherType 인 경우에 필터를 적용할 지 선택
@Bean
public FilterRegistrationBean logFilter(){
  FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
  filterRegistrationBean.setFilter(new LogFilter());
  filterRegistrationBean.setOrder(1);
  filterRegistrationBean.addUrlPatterns("/*");
  filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
  // 클라이언트 요청은 물론이고, 오류 페이지 요청에서도 필터가 호출
  // 아무것도 넣지 않으면 기본 값(DispatcherType.REQUEST) 사용

  return filterRegistrationBean;
}

// 인터셉터는 DispatcherType과 무관하게 항상 호출
// 오류 페이지 경로를 excludePathPatterns 를 사용해서 제외 가능
@Override
public void addInterceptors(InterceptorRegistry registry) {
  registry.addInterceptor(new LogInterceptor())
          .order(1)
          .addPathPatterns("/**")
          .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**");
}
```


## 뷰 선택 우선순위
BasicErrorController 의 처리 순서  
1. 뷰 템플릿
  * resources/templates/error/500.html
  * resources/templates/error/5xx.html
2. 정적 리소스( static , public )
  * resources/static/error/400.html
  * resources/static/error/404.html
  * resources/static/error/4xx.html
3. 적용 대상이 없을 때 뷰 이름( error )
  * resources/templates/error.html


### BasicErrorController
* 오류 컨트롤러에서 다음 오류 정보를 model 에 포함할지 여부 선택할 수 있음
```txt
server.error.include-exception=false : exception 포함 여부(true, false)
server.error.include-message=never : message 포함 여부(never, always, on_param)
server.error.include-stacktrace=never : trace 포함 여부
server.error.include-binding-errors=never : errors 포함 여부
```


## API 예외 처리
오류 페이지는 단순히 고객에게 오류 화면을 보여주고 끝이지만, API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 전달해야 함
* `produces = MediaType.APPLICATION_JSON_VALUE`
  * 클라이언트가 요청하는 HTTP Header의 Accept 의 값이 application/json 일 때 해당 메서드가 호출
    (클라어인트가 받고 싶은 미디어 타입이 json이면 이 컨트롤러의 메서드가 호출)

### BasicErrorController
* errorHtml() : produces = MediaType.TEXT_HTML_VALUE : 클라이언트 요청의 Accept 해더 값이text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공
* error() : 그외 경우에 호출되고 ResponseEntity 로 HTTP Body에 JSON 데이터를 반환
* 오류 발생시 /error 를 오류 페이지로 요청(스프링 부트의 기본 설정)
* 오류 정보 관련 옵션
  * server.error.include-binding-errors=always
  * server.error.include-exception=true
  * server.error.include-message=always
  * server.error.include-stacktrace=always
* BasicErrorController는 HTML 화면을 처리할 때 사용하고, API 오류 처리는 뒤에서 설명할 @ExceptionHandler 를 사용
  * 매우 세밀하고 복잡: 예를 들어서 회원과 관련된 API에서 예외가 발생할 때 응답과, 상품과 관련된 API에서 발생하는 예외에 따라 그 결과가 달라질 수 있음


## HandlerExceptionResolver
* 예외가 발생해서 서블릿을 넘어 WAS까지 예외가 전달되면 HTTP 상태코드가 500으로 처리  
  → 발생하는 예외에 따라서 400, 404 등등 다른 상태코드로 처리
* 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver를 사용
* ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이, Exception 을 처리해서 정상 흐름 처럼 변경하는 것이 목적
* 반환 값에 따른 동작 방식
  * 빈 ModelAndView: 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴
  * ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링
  * null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행
    * 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던짐
* 활용
  * 예외 상태 코드 변환
    * 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
    * 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출
      * 예를 들어서 스프링 부트가 기본으로 설정한 /error 가 호출됨
  * 뷰 템플릿 처리
    * ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
  * API 응답 처리
    * response.getWriter().println("hello"); 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는것도 가능
* 예외가 발생하면 WAS까지 예외가 던져지고, WAS에서 오류 페이지 정보를 찾아서 다시 /error 를 호출하는 과정을 ExceptionResolver에서 처리


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