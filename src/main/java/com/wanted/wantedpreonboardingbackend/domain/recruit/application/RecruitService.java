package com.wanted.wantedpreonboardingbackend.domain.recruit.application;


import com.wanted.wantedpreonboardingbackend.domain.company.application.CompanyService;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dao.RecruitRepository;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

  private final RecruitRepository recruitRepository;
  private final CompanyService companyService;

  /**
   * 채용공고 생성
   *
   * @param reqDto    채용공고 생성 Request Dto
   * @param companyId 관련된 회사 아이디
   * @return 생성된 채용공고 ID
   */
  @Transactional
  public Long createRecruit(RecruitCreateReqDto reqDto, Long companyId) {
    Company company = companyService.getCompanyById(companyId); //Id로 회사 가져오기

    Recruit recruit = recruitRepository.save(reqDto.toEntity(company));//생성
    return recruit.getId();
  }
}
