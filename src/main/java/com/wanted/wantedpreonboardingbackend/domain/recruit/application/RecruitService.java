package com.wanted.wantedpreonboardingbackend.domain.recruit.application;


import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dao.RecruitRepository;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitUpdateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response.RecruitDetailResDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response.RecruitListResDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response.RecruitResDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.global.error.BusinessException;
import com.wanted.wantedpreonboardingbackend.global.error.ErrorCode;
import java.util.List;
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

    // 요청한 회사의 id가 채용공고에 대한 권한이 없으면 예외 발생
    checkAccessibleRecruit(company, recruit);

    // 채용공고 업데이트
    recruit.update(reqDto.toEntity(company));

    return recruitId;
  }

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
