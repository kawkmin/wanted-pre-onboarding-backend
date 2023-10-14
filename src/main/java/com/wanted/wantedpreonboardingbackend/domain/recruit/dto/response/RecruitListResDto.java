package com.wanted.wantedpreonboardingbackend.domain.recruit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 채용공고들을 담아 전달하는 Response Dto
 */
@Getter
@AllArgsConstructor
public class RecruitListResDto {

  private List<RecruitResDto> data;

  // 생성 반환
  public static RecruitListResDto form(List<RecruitResDto> recruits) {
    return new RecruitListResDto(recruits);
  }
}
