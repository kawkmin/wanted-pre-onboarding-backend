package com.wanted.wantedpreonboardingbackend.domain.recruit.application;


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
