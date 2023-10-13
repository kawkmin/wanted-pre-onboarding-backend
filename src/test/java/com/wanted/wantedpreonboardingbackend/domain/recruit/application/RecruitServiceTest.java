package com.wanted.wantedpreonboardingbackend.domain.recruit.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dao.RecruitRepository;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest extends TestHelper {

  @InjectMocks
  private RecruitService recruitService;
  @Mock
  private RecruitRepository recruitRepository;

  private Recruit recruit;
  private Company company;

  @BeforeEach
  void setUp() {
    super.init();
    recruit = recruitTestHelper.generate();
    company = recruitTestHelper.generate().getCompany();
  }

  @Nested
  @DisplayName("채용공고 생성 테스트")
  class createRecruit {

    @Test
    @DisplayName("채용공고가 성공적으로 생성된다.")
    void 채용공고가_성공적으로_생성된다() {
      RecruitCreateReqDto reqDto = RecruitCreateReqDto.builder()
          .position(recruit.getPosition())
          .content(recruit.getContent())
          .reward(recruit.getReward())
          .skill(recruit.getSkill())
          .build();

      Recruit newRecruit = new Recruit(
          1L,
          company,
          reqDto.getPosition(),
          reqDto.getReward(),
          reqDto.getContent(),
          reqDto.getSkill());

      given(recruitRepository.save(any(Recruit.class))).willReturn(newRecruit);

      Long createdId = recruitService.createRecruit(reqDto, company);
      Assertions.assertThat(createdId).isEqualTo(1L);
    }
  }
}