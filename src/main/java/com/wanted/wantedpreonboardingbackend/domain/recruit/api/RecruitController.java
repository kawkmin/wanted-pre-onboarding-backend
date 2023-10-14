package com.wanted.wantedpreonboardingbackend.domain.recruit.api;

import com.wanted.wantedpreonboardingbackend.domain.company.application.CompanyService;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.application.RecruitService;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitUpdateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response.RecruitListResDto;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruit")
public class RecruitController {

  private final RecruitService recruitService;
  private final CompanyService companyService;

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
    // 회사 id로 가져온 회사
    Company company = companyService.getCompanyById(reqDto.getCompanyId());

    // 채용공고 생성 + 아이디 반환
    Long createdRecruitId = recruitService.createRecruit(reqDto, company);

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/recruit/" + createdRecruitId)) //생성된 채용공고 조회 url 담기
        .build();
  }

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
