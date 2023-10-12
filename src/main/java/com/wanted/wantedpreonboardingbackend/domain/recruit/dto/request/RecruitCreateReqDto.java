package com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채용공고를 만들 때, Reqest Dto
 */
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
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
