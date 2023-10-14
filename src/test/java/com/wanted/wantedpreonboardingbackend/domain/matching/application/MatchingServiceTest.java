package com.wanted.wantedpreonboardingbackend.domain.matching.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.matching.dao.MatchingRepository;
import com.wanted.wantedpreonboardingbackend.domain.matching.entity.Matching;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import com.wanted.wantedpreonboardingbackend.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest extends TestHelper {

  @InjectMocks
  private MatchingService matchingService;

  @Mock
  private MatchingRepository matchingRepository;

  private Matching matching;
  private User user;
  private Recruit recruit;

  @BeforeEach
  void setUp() {
    super.init();
    recruit = recruitTestHelper.generate();

    user = User.builder()
        .id(1L)
        .name("테스트 유저 이름")
        .build();

    matching = Matching.allBuilder()
        .id(1L)
        .recruit(recruit)
        .user(user)
        .build();
  }

  @Nested
  @DisplayName("매칭 생성 테스트")
  class CreateMatching {

    @Test
    @DisplayName("매칭이 정상적으로 생성된다.")
    void 매칭이_정상적으로_생성된다() {
      given(matchingRepository.save(any())).willReturn(matching);

      Long createdMatchingId = matchingService.CreateMatching(user, recruit);
      assertThat(createdMatchingId).isEqualTo(1L);
    }

    @Test
    @DisplayName("지원자는 중복으로 지원할 수 없다.")
    void 지원자는_중복으로_지원할_수_없다() {

      Recruit dupUserRecruit = Recruit.allBuilder()
          .company(companyTestHelper.generate())
          .skill("skill")
          .reward(1)
          .content("content")
          .position("쵳ㅋ")
          .build();

      assertThatThrownBy(
          () -> matchingService.CreateMatching(user, dupUserRecruit)
      );
    }
  }
}