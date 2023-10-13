package com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
