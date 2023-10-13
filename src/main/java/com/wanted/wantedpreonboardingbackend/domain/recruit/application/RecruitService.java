package com.wanted.wantedpreonboardingbackend.domain.recruit.application;


import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dao.RecruitRepository;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitUpdateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.global.error.BusinessException;
import com.wanted.wantedpreonboardingbackend.global.error.ErrorCode;
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
