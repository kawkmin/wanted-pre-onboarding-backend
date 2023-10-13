package com.wanted.wantedpreonboardingbackend.domain.recruit.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.wanted.wantedpreonboardingbackend.TestHelper;
import com.wanted.wantedpreonboardingbackend.domain.company.dao.CompanyRepository;
import com.wanted.wantedpreonboardingbackend.domain.company.entity.Company;
import com.wanted.wantedpreonboardingbackend.domain.recruit.entity.Recruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
}