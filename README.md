# [프리온보딩 백엔드 인턴십 선발과제](https://bow-hair-db3.notion.site/1850bca26fda4e0ca1410df270c03409)

## 기술 스택

#### `JDK 17` `Spring Boot 3.1.4` `MySQL`

## 목차

1. [서비스 개요](#서비스-개요)
2. [요구사항 분석](#요구사항-분석)
3. [ERD](#erd)
4. [구현과정](#요구사항-구현-과정)
5. [Api 명세](#api-명세requestresponse)

## 서비스 개요

- 본 서비스는 기업의 채용을 위한 웹 서비스 입니다.
- 회사는 채용공고를 생성하고, 이에 사용자는 지원합니다.

## 요구사항 분석

- [ ] **회사**
    - [ ] **채용공고**를 등록을 할 수 있다.
    - [ ] **채용공고**를 수정을 할 수 있다.
    - [ ] **채용공고**를 삭제를 할 수 있다.


- [ ] **사용자**
    - [ ] **채용공고** 목록을 확인할 수 있다.
    - [ ] 검색된 **채용공고** 목록을 확인할 수 있다.
    - [ ] **채용공고** 상세 페이지를 확인할 수 있다.
    - [ ] **채용공고**에 지원할 수 있다.

## ERD

![image](https://github.com/kawkmin/wanted-pre-onboarding-backend/assets/86940335/2a787849-5987-4ef6-abf2-9c7e916db1ae)

## 요구사항 구현 과정

### 0. 프로젝트 틀을 구성합니다.

관련 PR :
[#2](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/2),
[#4](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/4),
[#6](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/6),
[#9](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/9)

[#2 개발 초기 환경 세팅](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/2)

깃허브 PR 및 Issue 탬플릿을 만들어, 형식화된 틀을 사용하였습니다.

`p6spy`를 설치하여, 쿼리가 실제로 나가는 동작을 확인하여, N+1등의 문제를 미리 인지하여, 예방할 수 있습니다.
또한 포맷팅을 하여, `p6spy`의 가독성을 높였습니다.

요구사항을 분석하여, README에 정리하였습니다.

[#4 erd 기반 entity 및 dao 생성](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/4),
[#9 지원 정보 entity 구현](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/9)(refactor)

초기에 작성된 ERD를 기반으로, 요구사항에 맞는 Entity 및 DAO를 생성하였습니다.

하지만 잘못된 요구사항 분석으로, 사용자와 채용공고는 N:1 관계가 아닌, N:N임을 알게되었습니다.
이에, N:N의 중간 Entity인 `지원 정보 Entity`룰 생성하여, N:1 관계로 리팩토링 하였습니다.

[#6 전역 예외 처리 구현](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/6)

API 통신에서 예외가 발생하면, 원치않은 상태코드 및 메시지를 전송하게 됩니다. 이를 방지하여, 예외 처리를 다음과 같이 직접 구현하였습니다.

`ErrorCode`는 예외의 상태 코드 및 메시지를 쉽게 추가/변경을 할 수 있도록 도와주는 `Enum`입니다.

```java
/**
 * 오류 메시지와 상태를 쉽게 추가하기 위한 Enum
 */
@Getter
public enum ErrorCode {

  ;

  //오류 메시지
  private final String message;
  //오류 상태코드
  private final HttpStatus httpStatus;

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
```

`ErrorResponse`는 Response를 정형화한 `DTO`입니다.

```java
/**
 * 포맷팅된 에러 메시지 담을 Response DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  private final String message;

  public static ErrorResponse from(String message) {
    return new ErrorResponse(message);
  }
}
```

`BusinessException`은 앞으로 로직상 예외를 발생시켜야할 때, 사용되는 `언체크예외`입니다.

```java
/**
 * 로직에서 예외를 발생시킬 때 사용하는 언체크 예외
 */
@Getter
public class BusinessException extends RuntimeException {

  //오류 발생 부분의 값. 명확하게 없으면 Null.
  private final String invalidValue;
  //오류 필드명.
  private final String fieldName;
  //오류 상태 코드.
  private final HttpStatus httpStatus;
  //오류 메시지
  private final String message;

  public BusinessException(Object invalidValue, String fieldName, ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.invalidValue = invalidValue != null ? invalidValue.toString() : null;
    this.fieldName = fieldName;
    this.httpStatus = errorCode.getHttpStatus();
    this.message = errorCode.getMessage();
  }
}
```

`ExceptionAdvice`는 `@RestControllerAdvice`로 예외가 발생할 때, 위의 클래스들을 이용하여,
`[{오류 명칭}] : {오류필드명}: {오류시지}` 형식인 메시지와, 원하는 상태코드를 포함된 `ResponseEntity`를 반환합니다.

```java
/**
 * 전역 예외 처리
 */
@RestControllerAdvice
public class ExceptionAdvice {

  /**
   * BindException 오류가 발생할 때, Response 처리.
   *
   * @param e BindException
   * @return Response 내용
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> bindException(BindException e) {
    String errorMessage = getErrorMessage(e);

    return ResponseEntity.badRequest().body(ErrorResponse.from(errorMessage));
  }

  /**
   * BusinessException 오류가 발생할 때, Response 처리.
   *
   * @param e BusinessException
   * @return Response 내용
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> businessException(BusinessException e) {
    String errorMessage = getErrorMessage(e.getInvalidValue(), e.getFieldName(), e.getMessage());

    return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.from(errorMessage));
  }

  /**
   * BindingException의 bindingResult 분석 후, 오류 메시지 생성
   *
   * @param e BindException
   * @return 포멧팅된 오류 메시지
   */
  private static String getErrorMessage(BindException e) {
    BindingResult bindingResult = e.getBindingResult();

    return bindingResult.getFieldErrors().stream()
        .map(fieldError ->
            getErrorMessage(
                (String) fieldError.getRejectedValue(),
                fieldError.getField(),
                fieldError.getDefaultMessage()
            )
        )
        .collect(Collectors.joining(", "));
  }

  /**
   * 메시지 포멧팅
   */
  public static String getErrorMessage(String invalidValue, String errorField,
      String errorMessage) {
    return String.format("[%s] %s: %s", invalidValue, errorField, errorMessage);
  }
}
```

이로써, 예외 발생 Response를 커스텀하였습니다.

### 1. 채용공고를 등록합니다.

관련 PR :

### 2. 채용공고를 수정합니다.

관련 PR :

### 3. 채용공고를 삭제합니다.

관련 PR :

### 4-1. 채용공고 목록을 가져옵니다.

관련 PR :

### 4-2. 채용공고 검색 기능(선택 사항)

관련 PR :

### 5. 채용 상세 페이지를 가져옵니다.

관련 PR :

### 6. 사용자는 채용공고에 지원합니다.(선택 사항)

관련 PR :

## API 명세(Request/Response)

### 1. 채용공고 등록

#### Request

#### Response

### 2. 채용공고 수정

#### Request

#### Response

### 3. 채용공고 삭제

#### Request

#### Response

### 4-1. 채용공고 목록 조회

#### Request

#### Response

### 4-2. 채용공고 검색 목록 조회

#### Request

#### Response

### 5. 채용공고 상세 페이지 조회

#### Request

#### Response

### 6. 채용공고 등록

#### Request

#### Response


