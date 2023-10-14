package com.wanted.wantedpreonboardingbackend.domain.recruit.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dao.RecruitRepository;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitCreateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.dto.request.RecruitUpdateReqDto;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import java.util.Optional;
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

      given(recruitRepository.save(any(Recruit.class))).willReturn(recruit);

      Long createdId = recruitService.createRecruit(reqDto, company);
      assertThat(createdId).isEqualTo(1L);
    }
  }

  @Nested
  @DisplayName("채용공고 수정 테스트")
  class updateRecruit {

    @Test
    @DisplayName("채용공고가 특정 컬럼이 없어도, 성공적으로 수정된다.")
    void 채용공고가_특정_컬럼이_없어도_성공적으로_수정된다() {

      // 없는 컬럼 : skill
      RecruitUpdateReqDto reqDto = RecruitUpdateReqDto.builder()
          .content("업데이트 채용 내용")
          .reward(11)
          .position("업데이트 채용 포지션")
          .build();

      given(recruitRepository.findById(anyLong())).willReturn(Optional.of(recruit));

      recruitService.updateRecruit(reqDto, 1L, company);

      assertThat(recruit.getContent()).isEqualTo("업데이트 채용 내용");
      assertThat(recruit.getReward()).isEqualTo(11);
      assertThat(recruit.getPosition()).isEqualTo("업데이트 채용 포지션");

      assertThat(recruit.getSkill()).isEqualTo("테스트 사용 기술명");
    }

    @Test
    @DisplayName("채용공고 수정에 권한이 없는 회사면, 예외가 발생한다.")
    void 채용공고_수정에_권한이_없는_회사면_예외가_발생한다() {

      // 해당 채용공고에 대한 권한 없는 회사
      Company diffCompany = companyTestHelper.builder().id(2L).build();

      RecruitUpdateReqDto reqDto = RecruitUpdateReqDto.builder()
          .content("업데이트 채용 내용")
          .reward(11)
          .position("업데이트 채용 포지션")
          .build();

      given(recruitRepository.findById(anyLong())).willReturn(Optional.of(recruit));

      assertThatThrownBy(
          () -> recruitService.updateRecruit(reqDto, 1L, diffCompany)
      );
    }
  }
}