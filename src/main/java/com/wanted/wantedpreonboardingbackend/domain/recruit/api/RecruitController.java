package com.wanted.wantedpreonboardingbackend.domain.recruit.api;

import com.wanted.wantedpreonboardingbackend.domain.company.application.CompanyService;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.application.RecruitService;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
