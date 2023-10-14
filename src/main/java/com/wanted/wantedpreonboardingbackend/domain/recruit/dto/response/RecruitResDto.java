package com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response;

import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
