package com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response;

import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

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
