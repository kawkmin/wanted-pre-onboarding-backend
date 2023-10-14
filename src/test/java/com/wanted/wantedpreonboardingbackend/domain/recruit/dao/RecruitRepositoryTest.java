package com.wanted.wantedpreonboardingbackend.domain.recruit.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.dao.CompanyRepository;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class RecruitRepositoryTest extends TestHelper {

  @Autowired
  private RecruitRepository recruitRepository;
  @Autowired
  private CompanyRepository companyRepository;

  private Recruit recruit;
  private Company company;

  @BeforeEach
  void setUp() {
    company = companyRepository.save(companyTestHelper.generate());
    recruit = recruitRepository.save(recruitTestHelper.builder().company(company).build());
  }

  @Nested
  @DisplayName("채용공고 저장 테스트")
  class RecruitSaveTest {

    @Test
    @DisplayName("채용공고가 정상적으로 저장이 된다.")
    void 채용공고가_정상적으로_저장이_된다() {
      Recruit findRecruit = recruitRepository.findById(recruit.getId()).orElseThrow();

      assertThat(findRecruit).isEqualTo(recruit);
    }
  }

  @Nested
  @DisplayName("채용공고 수정 테스트")
  class RecruitUpdateTest {

    @Test
    @Transactional
    @DisplayName("채용공고가 정상적으로 수정 된다")
    void 채용공고가_정상적으로_수정_된다() {

      // 수정 전 채용공고
      Recruit beforeRecruit = recruitRepository.findById(recruit.getId()).orElseThrow();

      Recruit updateRecruit = Recruit.builder()
          .company(company)
          .reward(11)
          .content("업데이트 채용 내용")
          .position("업데이트 채용 포지션")
//          .skill("업데이트 채용 스킬") patch 확인을 위함
          .build();
      // 채용공고 수정 (dirtyChecking)
      beforeRecruit.update(updateRecruit);

      // 실제 DB에 저장됐는지 확인
      Recruit afterRecruit = recruitRepository.findById(recruit.getId()).orElseThrow();

      // 업데이트 내용 O
      assertThat(afterRecruit.getReward()).isEqualTo(updateRecruit.getReward());
      assertThat(afterRecruit.getPosition()).isEqualTo(updateRecruit.getPosition());
      assertThat(afterRecruit.getContent()).isEqualTo(updateRecruit.getContent());

      // 업데이트 내용 X
      assertThat(afterRecruit.getSkill()).isEqualTo(beforeRecruit.getSkill());
    }
  }

  @Nested
  @DisplayName("채용공고 삭제 테스트")
  class RecruitDeleteTest {

    @Test
    @Transactional
    @DisplayName("채용공고가 정상적으로 삭제 된다")
    void 채용공고가_정상적으로_삭제_된다() {

      recruitRepository.delete(recruit);

      assertThatThrownBy(
          () -> recruitRepository.findById(recruit.getId()).orElseThrow()
      );
    }
  }
}