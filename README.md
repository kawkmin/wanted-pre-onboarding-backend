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

- [x] **회사**
    - [x] **채용공고**를 등록을 할 수 있다.
    - [x] **채용공고**를 수정을 할 수 있다.
    - [x] **채용공고**를 삭제를 할 수 있다.


- [ ] **사용자**
    - [x] **채용공고** 목록을 확인할 수 있다.
    - [x] 검색된 **채용공고** 목록을 확인할 수 있다.
    - [x] **채용공고** 상세 페이지를 확인할 수 있다.
    - [ ] **채용공고**에 지원할 수 있다.

## ERD

![image](https://github.com/kawkmin/wanted-pre-onboarding-backend/assets/86940335/ac7a31c6-bbad-4b1d-9c73-e7680cb0a478)

## 요구사항 구현 과정

### 0. 프로젝트 틀을 구성합니다.

관련 PR :
[#2](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/2),
[#4](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/4),
[#6](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/6),
[#9](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/9)

[[#2 개발 초기 환경 세팅]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/2)

깃허브 PR 및 Issue 탬플릿을 만들어, 형식화된 틀을 사용하였습니다.

`p6spy`를 설치하여, 쿼리가 실제로 나가는 동작을 확인하여, N+1등의 문제를 미리 인지하여, 예방할 수 있습니다.
또한 포맷팅을 하여, `p6spy`의 가독성을 높였습니다.

요구사항을 분석하여, README에 정리하였습니다.

[[#4 erd 기반 entity 및 dao 생성]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/4),
[[#9 지원 정보 entity 구현(refactor)]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/9)

초기에 작성된 ERD를 기반으로, 요구사항에 맞는 Entity 및 DAO를 생성하였습니다.

하지만 잘못된 요구사항 분석으로, 사용자와 채용공고는 N:1 관계가 아닌, N:N임을 알게되었습니다.
이에, N:N의 중간 Entity인 `지원 정보 Entity`룰 생성하여, N:1 관계로 리팩토링 하였습니다.

[[#6 전역 예외 처리 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/6)

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

관련 PR : [#10](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/10)

[[#10 채용공고 등록 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/10)

채용 관련 `Controller`에서 `ResponseBody`로 받을 때, `Bean Validation`을 이용하여, 다음과 같이 제약 조건을 걸었습니다.

```java
/**
 * 채용공고를 만들 때, Reqest Dto
 */
public class RecruitCreateReqDto {

  public static final int MIN_REWARD_PRICE = 0;

  //회사 Id (N:1)
  @NotNull(message = "회사 아이디를 입력해주세요.")
  @PositiveOrZero(message = "올바른 회사 아이디를 입력해주세요.")
  private Long companyId;

  //채용 포지션명
  @NotNull(message = "포지션을 입력해주세요.")
  private String position;

  //보상금
  @Nullable
  private Integer reward;

  //채용 내용
  @NotNull(message = "채용 내용을 입력해주세요.")
  private String content;

  //사용 기술명
  @NotNull(message = "사용 기술명을 입력해주세요.")
  private String skill;
}
```

이 때, 주입에서 오류가 발생하면, `BindException`이 일어나는데, 위에서 이미 `ExceptionAdvice`로 처리해주었습니다.

이를 이용하여, 먼저 `회사Service`에서 id로 회사 찾기 기능을 구현하였으며, 못찾을 시, `BusinessException`를 발생시킵니다.

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
  //...

  /**
   * id로 회사 조회
   *
   * @param companyId 회사 id
   * @return 조회된 회사 Entity
   */
  public Company getCompanyById(Long companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(
        () -> new BusinessException(companyId, "companyId", ErrorCode.COMPANY_NOT_FOUND)
    );
  }
}
```

이를 이용하여, 찾은 `Company`와 `ReqDto`를 통하여, `service`에서 `Recruit`를 저장합니다.

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {
  //...

  /**
   * 채용공고 생성
   *
   * @param reqDto  채용공고 생성 Request Dto
   * @param company 관계 회사
   * @return 생성된 채용공고 ID
   */
  @Transactional
  public Long createRecruit(RecruitCreateReqDto reqDto, Company company) {
    Recruit recruit = recruitRepository.save(reqDto.toEntity(company));//생성
    return recruit.getId();
  }
}
```

비지니스 로직상 자연스럽게 해당 채용공고로 이동한다고 생각하여, `Controller`에서 `201`상태코드와 `Header`의 `Location`에 `생성된 채용공고 뷰 URL`
을 담습니다.

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitController {
  //...

  /**
   * 채용공고 생성
   *
   * @param reqDto 채용공고 생성 Request Dto
   * @return 201, 생성된 채용공고 location
   */
  @PostMapping("")
  public ResponseEntity<Void> createRecruit(
      @RequestBody @Valid RecruitCreateReqDto reqDto
  ) {
    //...
    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/recruit/" + createdRecruitId)) //생성된 채용공고 조회 url 담기
        .build();
  }
}
```

### 2. 채용공고를 수정합니다.

관련 PR : [#12](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/12)

[[#12 채용공고 수정 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/12)

채용 수정 관련 Controller에서 ResponseBody로 받을 때, 사용되는 Request Dto 입니다.

```java
/**
 * 채용공고를 변경할 때, Reqest Dto
 */
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class RecruitUpdateReqDto {

  //채용 포지션명
  @Nullable
  private String position;

  //보상금
  @Nullable
  private Integer reward;

  //채용 내용
  @Nullable
  private String content;

  //사용 기술명
  @Nullable
  private String skill;

  /**
   * Entity로 변경
   *
   * @param company 회사 Entity
   * @return 채용공고 Entity
   */
  public Recruit toEntity(Company company) {
    return Recruit.builder()
        .company(company)
        .position(position)
        .reward(reward)
        .content(content)
        .skill(skill)
        .build();
  }
}

```

채용공고를 작성한 회사에서만, 수정이 가능하다고 생각했기에,`채용공고Service`에서 다음과 같이 수정을 진행합니다.

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {
  //...

  /**
   * 채용공고 수정
   *
   * @param reqDto    채용공고 수정 Request Dto
   * @param recruitId 수정할 채용공고 ID
   * @param company   관계 회사
   * @return 수정된 채용공고 ID
   */
  @Transactional
  public Long updateRecruit(RecruitUpdateReqDto reqDto, Long recruitId, Company company) {

    // id로 채용공고 조회. 없으면 예외 발생
    Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(
        () -> new BusinessException(recruitId, "recruitId", ErrorCode.RECRUIT_NOT_FOUND)
    );

    // 채용공고의 회사 id가 요청한 회사의 id와 다르면 예외 발생
    checkAccessibleRecruit(company, recruit);

    // 채용공고 업데이트
    recruit.update(reqDto.toEntity(company));

    return recruitId;
  }

  /**
   * 채용공고의 회사 id가 요청한 회사의 id와 다르면 예외를 발생시킵니다.
   *
   * @param company 회사
   * @param recruit 채용공고
   */
  private static void checkAccessibleRecruit(Company company, Recruit recruit) {
    if (!recruit.getCompany().getId().equals(company.getId())) {
      throw new BusinessException(company.getId(), "companyId", ErrorCode.RECRUIT_INACCESSIBLE);
    }
  }
}
```

`Controller`에서 `수정된 채용공고 뷰 URL` 과 `201 상태코드`를 `Response` 합니다.

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitController {
  //...

  /**
   * 채용공고 수정
   *
   * @param companyId 회사 아이디
   * @param recruitId 채용공고 아이디
   * @param reqDto    채용공고 수정 Request Dto
   * @return 201, 수정된 채용공고 location
   */
  @PatchMapping("/{companyId}/{recruitId}")
  public ResponseEntity<Void> updateRecruit(
      @PathVariable Long companyId,
      @PathVariable Long recruitId,
      @RequestBody @Valid RecruitUpdateReqDto reqDto
  ) {
    // 회사 id로 가져온 회사
    Company company = companyService.getCompanyById(companyId);

    // 채용공고 수정 + 아이디 반환
    Long updatedRecruitId = recruitService.updateRecruit(reqDto, recruitId, company);

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/recruit/" + updatedRecruitId)) //수정된 채용공고 조회 url 담기
        .build();
  }
}

```

### 3. 채용공고를 삭제합니다.

관련 PR : [#14](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/14)

[[#14 채용공고 삭제 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/14)

채용공고 수정과 비슷하게, `채용공고Service`에서 해당 채용공고에 대한 회사의 권한을 확인 후 삭제합니다.

```java

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {
  //...

  /**
   * 채용공고 삭제
   *
   * @param recruitId 삭제할 채용공고 ID
   * @param company   관계 회사
   */
  @Transactional
  public void deleteRecruit(Long recruitId, Company company) {

    // id로 채용공고 조회. 없으면 예외 발생
    Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(
        () -> new BusinessException(recruitId, "recruitId", ErrorCode.RECRUIT_NOT_FOUND)
    );

    // 요청한 회사의 id가 채용공고에 대한 권한이 없으면 예외 발생
    checkAccessibleRecruit(company, recruit);

    // 채용공고 삭제
    recruitRepository.delete(recruit);
  }

}
```

`Controller`에서 삭제 후 `상태코드 200`을 Response 합니다.

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitController {
  //...

  /**
   * 채용공고 삭제
   *
   * @param companyId 회사 아이디
   * @param recruitId 삭제할 채용공고 아이디
   * @return 200
   */
  @DeleteMapping("/{companyId}/{recruitId}")
  public ResponseEntity<Void> deleteRecruit(
      @PathVariable Long companyId,
      @PathVariable Long recruitId
  ) {
    // 회사 id로 가져온 회사
    Company company = companyService.getCompanyById(companyId);

    // 채용공고 삭제
    recruitService.deleteRecruit(recruitId, company);

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
```

### 4. 채용공고 목록을 가져옵니다. + 채용공고 검색 기능(선택 사항)

관련 PR : [#16](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/16)

[[#16 채용 공고 목록 조회 및 검색 기능 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/16)

`...?search=?`를 통해서, 채용공고의 컬럼값 중 하나라도 포함이 된다면, 목록에 포함시켜 `Response` 해야합니다.

또한, 대소문자 구분이 필요없다고 판단하였습니다. (Python과 python 등)

따라서 다음과 같은 쿼리문을 작성하였습니다.
`RecruitRepository`

```java
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
  //...

  /**
   * 검색명에 하나라도 일치
   *
   * @param search 검색명
   * @return 조건에 맞는 채용공고 리스트
   */
  @Query(
      "SELECT r " +
          "FROM Recruit r " +
          "join fetch r.company " +
          "WHERE lower(r.content) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.position) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.skill) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.name) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.region) LIKE lower(concat('%',:search,'%') )" +
          "OR lower(r.company.country) LIKE lower(concat('%',:search,'%') )"
  )
  List<Recruit> searchRecruits(@Param("search") String search);

}
```

`채용공고 service`에서 해당 쿼리문을 이용하여, `Response`형식에 맞는 `DTO`로 반환합니다.

```java
/**
 * 채용공고의 목록으로 보여줄 Response Dto
 */
@Getter
@AllArgsConstructor
public class RecruitResDto {

  //채용공고 ID
  private Long id;

  //회사 이름
  private String companyName;

  //회사 나라
  private String companyCountry;

  //회사 지역
  private String companyRegion;

  //채용 포지션명
  private String position;

  //보상금
  private Integer reward;

  //채용 내용
  private String content;

  //사용 기술명
  private String skill;


  /**
   * Entity로 Dto변경
   *
   * @param recruit 생성된 채용공고 Response Dto
   */
  public RecruitResDto(Recruit recruit) {
    this.id = recruit.getId();
    this.companyName = recruit.getCompany().getName();
    this.companyCountry = recruit.getCompany().getCountry();
    this.companyRegion = recruit.getCompany().getRegion();
    this.position = recruit.getPosition();
    this.reward = recruit.getReward();
    this.content = recruit.getContent();
    this.skill = recruit.getSkill();
  }
}

/**
 * 채용공고들을 담아 전달하는 Response Dto
 */
@Getter
@AllArgsConstructor
public class RecruitListResDto {

  private List<RecruitResDto> data;

  // 생성 반환
  public static RecruitListResDto form(List<RecruitResDto> recruits) {
    return new RecruitListResDto(recruits);
  }
}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {
  //...

  /**
   * 채용공고 목록 조회
   *
   * @param search 검색명
   * @return 채용공고 목록 Response Dto
   */
  public RecruitListResDto getRecruits(String search) {
    //검색에 맞는 채용공고들
    List<Recruit> recruits = recruitRepository.searchRecruits(search);

    // ResponseDto 형식으로 변경 후 반환
    return RecruitListResDto.form(
        recruits.stream()
            .map(RecruitResDto::new)
            .toList());
  }
}
```

`controller`에서 `200 상태코드`와 검색된 결과의 채용공고 목록들을 담아 `Response` 합니다.

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitController {
  //...

  /**
   * 채용공고 검색 및 목록 조회
   *
   * @param search 검색명
   * @return 200, 채용공고 목록
   */
  @GetMapping("")
  public ResponseEntity<RecruitListResDto> getRecruits(
      @RequestParam(required = false, defaultValue = "") String search
  ) {
    RecruitListResDto response = recruitService.getRecruits(search);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
```

### 5. 채용 상세 페이지를 가져옵니다.

관련 PR : [#20](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/20)

[[#20 채용공고 상세 정보 구현]](https://github.com/kawkmin/wanted-pre-onboarding-backend/pull/20)

상세 정보에는 해당 회사의 `다른 채용공고 ID`가 필요합니다.

따라서 `Response Dto`에서 회사의 현재 채용공고의 ID를 제외한 다른 채용공고 ID를 가져옵니다.

```java

@Getter
public class RecruitDetailResDto {

  //채용공고 ID
  private Long id;

  //회사 이름
  private String companyName;

  //회사 나라
  private String companyCountry;

  //회사 지역
  private String companyRegion;

  //회사의 다른 채용공고 id
  private List<Long> companyOtherRecruitIds;

  //채용 포지션명
  private String position;

  //보상금
  private Integer reward;

  //채용 내용
  private String content;

  //사용 기술명
  private String skill;

  /**
   * Entity로 Dto로변경
   *
   * @param recruit 생성된 채용공고 상세 정보 Response Dto
   */
  public RecruitDetailResDto(Recruit recruit) {
    // 회사의 해당 채용공고는 제외한 나머지 채용공고
    this.companyOtherRecruitIds = recruit.getCompany().getRecruits().stream()
        .map(Recruit::getId)
        .filter(recId -> !Objects.equals(recId, recruit.getId()))
        .toList();
    this.id = recruit.getId();
    this.companyName = recruit.getCompany().getName();
    this.companyCountry = recruit.getCompany().getCountry();
    this.companyRegion = recruit.getCompany().getRegion();
    this.position = recruit.getPosition();
    this.reward = recruit.getReward();
    this.content = recruit.getContent();
    this.skill = recruit.getSkill();
  }
}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {
  //...

  /**
   * 채용공고 상세 조회
   *
   * @param recruitId 채용공고 id
   * @return 채용공고 상세 정보 Response Dto
   */
  public RecruitDetailResDto getDetailRecruit(Long recruitId) {
    // id로 채용공고 조회. 없으면 예외 발생
    Recruit recruit = recruitRepository.findWithCompanyById(recruitId).orElseThrow(
        () -> new BusinessException(recruitId, "recruitId", ErrorCode.RECRUIT_NOT_FOUND)
    );

    return new RecruitDetailResDto(recruit);
  }
}
```

`controller`에서 `200 상태코드`와 채용공고의 상세 정보를 `Response` 합니다.

### 6. 사용자는 채용공고에 지원합니다.(선택 사항)

관련 PR :

### Unit Test 구현.

테스트를 할 때, 생성관련 중복되는 경우가 너무 많기에 각 도메인마다 Entity 생성을 도와주는 `{domain}TestHelper`가 존재합니다.

모든 테스트틑 이런 `testHelper`들이 선언된 `TestHelper`클래스를 상속받아, 쉽게 Entity를 생성을 할 수 있게 구현하였습니다.

`TestHelper Class`

```java
/**
 * 모든 테스트 헬퍼를 가진 부모 클래스
 */
public class TestHelper {

  //...testHelper 선언

  @PostConstruct
  public void init() {
    //...testHelper 주입
  }
}
```

`CompanyTestHelper`

```java
/**
 * 회사 테스트 헬퍼
 */
public class CompanyTestHelper {

  public Company generate() {
    return this.builder().build();
  }

  public CompanyBuilder builder() {
    return new CompanyBuilder();
  }

  public final class CompanyBuilder {

    private Long id;
    private String name;

    private String country;

    private String region;

    public CompanyBuilder() {
    }

    public CompanyBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public CompanyBuilder name(String name) {
      this.name = name;
      return this;
    }

    public CompanyBuilder country(String country) {
      this.country = country;
      return this;
    }

    public CompanyBuilder region(String region) {
      this.region = region;
      return this;
    }

    public Company build() {
      return Company.allBuilder()
          .id(id != null ? id : 1L)
          .name(name != null ? name : "테스트 회사 이름")
          .country(country != null ? country : "테스트 나라 이름")
          .region(region != null ? region : "테스트 지역 이름")
          .build();
    }
  }
}

```

`testHelper`들은 `.generate`로 쉽게 디폴트값 주입이 가능하며, `Builder` 형식으로 쉽게 커스텀이 가능합니다.

**1. DAO 계층**

속도가 빠른 `H2 DB`를 사용하여, 테스트를 하도록 합니다.

`build.gradle`

```java
dependencies{
    //...

    //테스트용 H2 DB
    testImplementation'com.h2database:h2'
    }
```

`@DataJpaTest`로 DB를 사용하여, 테스트 구현합니다.

```java

@DataJpaTest
class RecruitRepositoryTest extends TestHelper {
  //...
}
```

**2. Service 계층**

`Mock`를 사용하여, 테스트를 구현합니다.

**(주의사항 : service는 다른 service를 의존해선 안됩니다.)**

```java

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest extends TestHelper {

  @InjectMocks
  private RecruitService recruitService;
  @Mock
  private RecruitRepository recruitRepository;
  //...
}
```

**3. Controller 계층**

`Mock`와 `MockMVC`를 사용하여, 실제 Request와 Response가 있는 것 처럼 테스트 합니다.

```java

@ExtendWith(MockitoExtension.class)
class RecruitControllerTest extends TestHelper {

  private MockMvc mockMvc;
  //...
}
```

하지만 실제 동작에선 다를 가능성이 있으니, 통합 테스트를 구현하거나, `PostMan`등을 사용하여 추가적인 테스트가 필요합니다.

## API 명세(Request/Response)

### 1. 채용공고 등록

#### Request

- POST `/api/v1/recruit`

```json
{
  "companyId": 1,
  "position": "백엔드 주니어 개발자",
  "reward": 1000000,
  "content": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
  "skill": "Python"
}
```

#### Response

- 201 Created
- `Location` : `/recruit/{채용공고 ID}`

```json

```

- 400 Bad Request

```json
{
  "message": "[null] content: 채용 내용을 입력해주세요."
}
...
```

- 404 Not Found

```json
{
  "message": "[100] companyId: 해당 회사를 찾을 수 없습니다."
}
```

### 2. 채용공고 수정

#### Request

- Patch `api/v1/recurit/{회사id}/{채용공고id}`

```json
{
  "reward": 1500000,
  "content": "변경된 채용 내용",
  "skill": "Python"
}
```

#### Response

- 201 Created
- `Location` : `/recruit/{수정된 채용공고 ID}`

```json

```

- 403 Forbidden

```json
{
  "message": "[2] companyId: 해당 채용공에 대한 권한이 없습니다."
}
```

- 404 Not Found

```json
{
  "message": "[21] recruitId: 해당 채용공고를 찾을 수 없습니다."
}
```

### 3. 채용공고 삭제

#### Request

- delete `api/v1/recurit/{회사id}/{채용공고id}`

```json

```

#### Response

- 200 Ok

```json

```

- 403 Forbidden

```json
{
  "message": "[2] companyId: 해당 채용공에 대한 권한이 없습니다."
}
```

- 404 Not Found

```json
{
  "message": "[21] recruitId: 해당 채용공고를 찾을 수 없습니다."
}
```

### 4 채용공고 목록 조회

#### Request

- Get `/api/v1/recruit`

```json

```

- Get `/api/v1/recruit?search=WanteD`

```json

```

#### Response

- 200 Ok

```json
{
  "data": [
    {
      "id": 5,
      "companyName": "WANTED",
      "country": "한국",
      "region": "경기",
      "position": "PM",
      "reward": 2000000,
      "content": "원티드 PM 모집",
      "skill": "CI/CD"
    },
    ...
  ]
}
```

### 5. 채용공고 상세 페이지 조회

#### Request

- Get `/api/v1/recruit/{recruitId}`

```json

```

#### Response

- 200 Ok

```json
{
  "id": 4,
  "companyName": "KAKAO",
  "companyCountry": "한국",
  "companyRegion": "수도권",
  "companyOtherRecruitIds": [
    7,
    8,
    9,
    10
  ],
  "position": "백엔드 주니어 개발자",
  "reward": 1000000,
  "content": "카카오 개발자 모집",
  "skill": "Python"
}
```

- 404 Not Found

```json
{
  "message": "[21] recruitId: 해당 채용공고를 찾을 수 없습니다."
}
```

### 6. 채용공고 등록

#### Request

#### Response


